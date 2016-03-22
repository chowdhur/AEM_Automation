package com.aem.genericutilities;

import static com.aem.keyword.driver.TestSuiteDriver.glb_Properties_objectRepository;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.util.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aem.application.Application_Constants;
import com.aem.constants.DriverConstants;
import com.aem.keyword.driver.TestSuiteDriver;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.parser.PdfTextExtractor;

/**
 * 
 * @author mkarthik
 * 
 * This class should have all the commonly used functions developed using selenium web driver
 * 
 * please add description for all the methods defined and declared in this class
 *
 */
public class CommonFunctions implements Application_Constants
{
	public static WebDriver glb_Webdriver_driver=null;
	public static Logger glb_Logger_commonlogs=null;
	public static int glb_Int_captureScreenshotCount =0;
	public static String failMsg = null;
	public static String pdfSavePath = null;
	
	/**
	 * @author mkarthik
	 * date: October 2nd
	 * date of review: 
	 * parameters : It is a zero parameterized constructor
	 * Description: This constructor is used to initialize the logs
	 * 
	 */
	public CommonFunctions()
	{
		glb_Logger_commonlogs=CommonLogging.getLogObj(CommonFunctions.class);
	}
	/**
	 * @author mkarthik
	 * date: October 2nd
	 * date of review:
	 * Description: This method will is used to open the webapplication using the given url.
	 * Only if the response code is 200 the webapplication is opened else exception is thrown
	 * @param url : url of the application
	 * @return This method returns a boolean value
	 * @throws CommonFunctionsExceptions
	 */
	
	public boolean openApp(String object,String url) throws CommonFunctionsExceptions
	{
		boolean m_bln_openApp_Status = false;
		int int_response_code = 0;
		try
		{
			Assert.assertNotNull(url);
			if(url!=null)
			{
				
				int_response_code=sendGet("",url);
				Assert.assertEquals(int_response_code, 200,"The response code is not 200. Hence halting the execution");
				if(int_response_code==200)
				{
					glb_Webdriver_driver.get(url);
					waitForPageToLoad();
					m_bln_openApp_Status=true;
					glb_Logger_commonlogs.info("WebApplication opened..");
				}
				else
				{
					Exceptions m_exceptions=Exceptions.INVALID_URL_EXCEPTION;
					if(m_exceptions==Exceptions.INVALID_URL_EXCEPTION)
					{
						throw new CommonFunctionsExceptions(int_response_code, url);
					}
				}
			}
			else
			{
				Exceptions m_exceptions=Exceptions.NULL_URL_EXCEPTION;
				if(m_exceptions==Exceptions.NULL_URL_EXCEPTION)
				{
					throw new CommonFunctionsExceptions(url);
				}
			}
		}
		
		catch(Exception e)
		{
			glb_Logger_commonlogs.error(e.getMessage());
		}
		return m_bln_openApp_Status;
	}
	/**
	 * @author mkarthik
	 * date: October 2nd
	 * date of review: 
	 * Description: This method is used to verify links.
	 * @param urls : link to be verified
	 * @return m_responseCode : status code of the link that has been currently hit
	 * @throws Exception
	 */
	private int sendGet(String object,String urls) throws Exception 
	{
		String m_url = urls; 
		URL m_obj = new URL(m_url);
        HttpURLConnection m_con = (HttpURLConnection) m_obj.openConnection();
	    // optional default is GET
		m_con.setRequestMethod("GET");
		//add request header
	    //con.setRequestProperty("User-Agent", USER_AGENT);
		int m_responseCode = m_con.getResponseCode();
		m_con.disconnect();
		return m_responseCode;
	}
	/**
	 * @author mkarthik
	 * date: October 2nd
	 * date of review: 
	 * Description: This method is used to close the browser which is currently opened.
	 * @return m_bln_close_state : this method returns the status once the browser is closed
	 * @throws CommonFunctionsExceptions
	 * @return
	 */
	public static boolean closeBrowser(String object,String testdata) throws CommonFunctionsExceptions
	{
		boolean m_bln_close_state=false;
		try
		{
			if(glb_Webdriver_driver!=null)
			{
				glb_Webdriver_driver.close();
				m_bln_close_state=true;
				glb_Logger_commonlogs.info("Closed browser...");
			}
			else
			{
				Exceptions m_exceptions=Exceptions.NULL_WEBDRIVER_EXCEPTION;
				if(m_exceptions==Exceptions.NULL_WEBDRIVER_EXCEPTION)
				{
					throw new CommonFunctionsExceptions();
				}
			}
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.error(e.getMessage());
		}
		return m_bln_close_state;
	}
	/**
	 * @author mkarthik
	 * date: October 2nd
	 * date of review: 
	 * Description: This method is used to perform drag and drop operation
	 * @param source : locator for source
	 * @param destination : locator for destination
	 * @return m_bln_dragAndDrop_status
	 */
	public boolean dragAndDrop(String v_str_object,String v_testdata)
	{
		boolean m_bln_dragAndDrop_status=false;
		
		WebElement m_WebElement_Source = null;
		WebElement m_WebElement_Destination = null;
		String [] locator=null;
		String m_str_source=null;
		String m_str_destination=null;
		String locate=null;
		
		Assert.assertNotNull(v_str_object,"The object passes is null");
		locate= glb_Properties_objectRepository.getProperty(v_str_object);
		Assert.assertNotNull(locate, "The object passes is null");
		locator=splitLocators(locate);
		
		int m_int_size=locator.length;
		
		if(m_int_size==2)
		{
			m_str_source=locator[0];
			m_str_destination=locator[1];
		}
		else
		{
			glb_Logger_commonlogs.info("In dragAndDrop()");
			glb_Logger_commonlogs.info("Expected only 2 elements. But number of elements found: "+m_int_size);
		}
		try
		{
			Assert.assertNotNull(m_str_source, "The source element is null. Hence halting the execution...");
			Assert.assertNotNull(m_str_destination, "The destination element is null. Hence halting the execution...");
			
			if((m_str_source!=null)&&(m_str_destination!=null))
			{
				m_WebElement_Source=locateElement(m_str_source);
				m_WebElement_Destination=locateElement(m_str_destination);
				
				Assert.assertNotNull(m_WebElement_Source, "The source element is not located. Hence halting the execution..");
				Assert.assertNotNull(m_WebElement_Destination, "The destination element is not located. Hence halting the execution..");
				
				if((m_WebElement_Source!=null)&&(m_WebElement_Destination!=null))
				{
					
					Actions m_Actions_action=new Actions(glb_Webdriver_driver);
					m_Actions_action.dragAndDrop(m_WebElement_Source, m_WebElement_Destination).build().perform();
					m_bln_dragAndDrop_status=true;
					glb_Logger_commonlogs.info("Drag and Drop operation completed...");
				}
				else
				{
					Exceptions m_Exceptions=Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
					if(m_Exceptions==Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION)
					{
						if(m_WebElement_Source==null)
						{
							throw new CommonFunctionsExceptions(m_Exceptions,m_str_source);
						}
						else if(m_WebElement_Destination==null)
						{
							throw new CommonFunctionsExceptions(m_Exceptions,m_str_destination);
						}
					}
				}
			}
			else
			{
				Exceptions m_exceptions=Exceptions.SOURCE_DESTINATION_VALUES_NULL_EXCEPTION;
				if(m_exceptions==Exceptions.SOURCE_DESTINATION_VALUES_NULL_EXCEPTION)
				{
					throw new CommonFunctionsExceptions(m_str_source,m_str_destination);
				}
			}
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.error(e.getMessage());
		}
		return m_bln_dragAndDrop_status;
	}
		/**
	 * @author Rajdeep
	 * date: December 6th
	 * date of review: 
	 * Description: Select drop down based on option visible text
	 * 
	 */
	public static void selectByVisibleText(String v_str_object, String v_str_data){
		
		WebElement m_WebElement_Select_Object = null;
		failMsg = "No element located.";
		
		try
		{		Assert.assertNotNull(v_str_object, "Null Select object passed.");
					Assert.assertNotNull(v_str_data, "Null select visible text passed.");
			List<WebElement> elements = getListOfWebElementObjects(v_str_object);
			
			if(elements.size() > 1){
				glb_Logger_commonlogs.error("More than one element passed to select. Selecting the first element.");
				System.out.println("More than one element passed to select. Selecting the first element.");
			}
			m_WebElement_Select_Object = elements.get(0);
			
			if(m_WebElement_Select_Object == null){
				System.out.println(failMsg);
				glb_Logger_commonlogs.error(failMsg);
				actionOnFailure(failMsg);
			}
			else{
				Select dropDown = new Select(m_WebElement_Select_Object);
				dropDown.selectByVisibleText(v_str_data);
			}
			
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.info("In selectByVisibleText()");
			glb_Logger_commonlogs.error(e.getMessage());
			System.out.println(failMsg);
			glb_Logger_commonlogs.error(failMsg);
			actionOnFailure(failMsg);
		}
		
	}
		/**
	 * @author Rajdeep
	 * date: December 6th
	 * date of review: 
	 * Description: Select drop down based on option value
	 */
	public static void selectByValue(String v_str_object, String v_str_data){
		
		WebElement m_WebElement_Select_Object = null;
		failMsg = "No element located.";
		
		try
		{
			Assert.assertNotNull(v_str_object, "Null Select object passed.");
			Assert.assertNotNull(v_str_data, "Null select visible text passed.");
			List<WebElement> elements = getListOfWebElementObjects(v_str_object);
			
			if(elements.size() > 1){
				glb_Logger_commonlogs.error("More than one element passed to select. Selecting the first element.");
				System.out.println("More than one element passed to select. Selecting the first element.");
			}
			m_WebElement_Select_Object = elements.get(0);
			
			if(m_WebElement_Select_Object == null){
				System.out.println(failMsg);
				glb_Logger_commonlogs.error(failMsg);
				actionOnFailure(failMsg);
			}
			else{
				Select dropDown = new Select(m_WebElement_Select_Object);
				dropDown.selectByValue(v_str_data);
				System.out.println("Selected by value- " + v_str_data);
				glb_Logger_commonlogs.info("Selected by value- " + v_str_data);
			}
			
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.info("In selectByValue()");
			glb_Logger_commonlogs.error(e.getMessage());
			System.out.println(failMsg);
			glb_Logger_commonlogs.error(failMsg);
			actionOnFailure(failMsg);
		}
		
	}
	public void dragAndDropFromOneFrameToAnother(String v_str_object,String v_testdata) throws CommonFunctionsExceptions, InterruptedException
	{
		boolean m_bln_dragAndDrop_status=false;
		WebElement m_WebElement_Source = null;
		WebElement m_WebElement_Destination = null;
		String [] locator=null;
		String m_str_source=null;
		String m_str_destination=null;
		String locate=null;
		
		Assert.assertNotNull(v_str_object,"The object passes is null");
		locate= glb_Properties_objectRepository.getProperty(v_str_object);
		Assert.assertNotNull(locate, "The object passes is null");
		locator=splitLocators(locate);
		
		int m_int_size=locator.length;
		m_str_source=locator[0];
		m_str_destination=locator[1];
		
		String [] m_locator=m_str_source.split(":",2);
		//String m_locator_type=m_locator[0];
		String m_locator_value=m_locator[1];
		
		By byFrom=By.xpath(m_locator_value);
		glb_Webdriver_driver.switchTo().frame("cq-cf-frame");   
		String [] m_dest_locator=m_str_destination.split(":",2);
		//String m_locator_type=m_locator[0];
		String m_dest_locator_value=m_dest_locator[1];
		By byTo=By.xpath(m_dest_locator_value);
		
		 WebElement LocatorFrom = glb_Webdriver_driver.findElement(byFrom);
		 WebElement LocatorTo = glb_Webdriver_driver.findElement(byTo);
		 String xto=Integer.toString(LocatorTo.getLocation().x);
		 String yto=Integer.toString(LocatorTo.getLocation().y);
		 ((JavascriptExecutor)glb_Webdriver_driver).executeScript("function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c)){a=b;break}if(!a)return!1;document.createEvent?(b=document.createEvent(a),a==\"HTMLEvents\"?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\"on\"+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; " +
		 "simulate(arguments[0],\"mousedown\",0,0); simulate(arguments[0],\"mousemove\",arguments[1],arguments[2]); simulate(arguments[0],\"mouseup\",arguments[1],arguments[2]); ",
		 LocatorFrom,xto,yto);
	}
	
	public void dragAndDropOldFromOneFrameToAnother(String v_str_object,String v_testdata) throws CommonFunctionsExceptions, InterruptedException
	{
		boolean m_bln_dragAndDrop_status=false;
		
		WebElement m_WebElement_Source = null;
		WebElement m_WebElement_Destination = null;
		String [] locator=null;
		String m_str_source=null;
		String m_str_destination=null;
		String locate=null;
		
		Assert.assertNotNull(v_str_object,"The object passes is null");
		locate= glb_Properties_objectRepository.getProperty(v_str_object);
		Assert.assertNotNull(locate, "The object passes is null");
		locator=splitLocators(locate);
		
		int m_int_size=locator.length;
		m_str_source=locator[0];
		m_str_destination=locator[1];
		//glb_Webdriver_driver.switchTo().defaultContent();
		//WebDriverWait wait = new WebDriverWait(glb_Webdriver_driver,900);
	/*	try{
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(m_str_source)));
	    
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='ext-comp-1144']")));
		//glb_Webdriver_driver.findElement(By.xpath("//*[@id='ext-comp-1144']")).sendKeys("My image");
		}
		catch (Exception e){
			e.printStackTrace();
		}*/
		
		System.out.println("************** " + m_str_source + " : :   " + m_str_destination);
		
		Actions builder = new Actions(glb_Webdriver_driver);          
		Actions action = builder.clickAndHold(locateElement(m_str_source));
		action.build().perform();
		glb_Webdriver_driver.switchTo().frame("cq-cf-frame");   
		
		builder.moveByOffset(-100, 3527).click().build().perform();
		//builder.moveToElement(locateElement(m_str_destination)).moveByOffset(25, 25).build().perform();
		//click the destination 
		//	builder.click(locateElement(m_str_destination)).build().perform();
		//back to main page to release the hold mouse
		Thread.sleep(20000);
		glb_Webdriver_driver.switchTo().defaultContent();
		builder.release().build().perform();
		
	/*	Actions builder = new Actions(glb_Webdriver_driver);
		WebElement source = locateElement(m_str_source);
		builder.clickAndHold(source).build().perform();
		
		glb_Webdriver_driver.switchTo().frame("cq-cf-frame");
		WebElement destination=locateElement(m_str_destination);
	
		builder.moveToElement(destination).perform();
		builder.click(dropElement).build().perform();
		builder.build();
		builder.release();
		builder.release(destination).perform();*/
		
		/*WebElement sourceElement = locateElement(m_str_source);
		Actions builder = new Actions(glb_Webdriver_driver);
		Action dragAndDrop = builder.clickAndHold(sourceElement).build();
		dragAndDrop.perform();
		glb_Webdriver_driver.switchTo().frame("cq-cf-frame");
		WebElement targetElement = locateElement(m_str_destination);
		Thread.sleep(1000);
		dragAndDrop = builder.moveToElement(targetElement).release().build();
		   dragAndDrop.perform();
		   targetElement.click();*/
		/*   Actions builder = new Actions(glb_Webdriver_driver);
		    builder.clickAndHold(locateElement(m_str_source)).build().perform();
		    builder.moveByOffset(300,300);
		    //Action action = builder.build();
		    builder.build().perform();
		    

		    glb_Webdriver_driver.switchTo().frame("cq-cf-frame");*/
		    /*builder = new Actions(glb_Webdriver_driver);
		    WebElement destination=locateElement(m_str_destination);
		    builder.moveToElement(destination).build().perform();
		    builder.click(destination).build().perform();*/
		
		    /*WebElement destination=locateElement(m_str_destination);
		    Actions act=new Actions(glb_Webdriver_driver);
		    act.moveByOffset(destination.getLocation().getX(),destination.getLocation().getY()).build().perform();
		    act.release(destination).build().perform();
		    act.click(destination).build().perform();*/
/*		Not tried with this : : Actions builder = new Actions(glb_Webdriver_driver);
	    builder.clickAndHold(locateElement(m_str_source));
	    Action action = builder.build();
	    action.perform();

	    glb_Webdriver_driver.switchTo().frame(glb_Webdriver_driver.findElement(By.id("cq-cf-frame")));
	    
	    builder.moveToElement(locateElement(m_str_destination));
	    builder.release(locateElement(m_str_destination));     
	    action = builder.build();
	    action.perform();*/
		
	/*	Actions builder = new Actions(glb_Webdriver_driver);
		builder.clickAndHold(locateElement(m_str_source));
		glb_Webdriver_driver.switchTo().frame(1);
		Actions action = builder.moveToElement(locateElement(m_str_destination)).release();
		builder.build();
		action.perform();*/
		
		/*// switch to the frame (you havent told webdriver to un-grab
		driver.switchTo().frame("newFrame"); //switching frames

		// move and drop
		Actions action = builder.moveToElement(destination);
		builder.release(destination);
		builder.build();
		action.perform();*/
		
	}
	/**
	 * @author mkarthik
	 * date: October 2nd
	 * date of review: 
	 * Description: This method is used to locate the element using different locating strategy
	 * @param locator
	 * @return m_WebElemnt_element
	 * @throws CommonFunctionsExceptions
	 */
	private static WebElement locateElement(String locator) throws CommonFunctionsExceptions
	{
		WebElement m_WebElemnt_element=null;
		try{
			if(locator!=null)
			{
				if(locator.contains(":"))
				{   
					String [] m_locator=locator.split(":",2);
					String m_locator_type=m_locator[0];
					String m_locator_value=m_locator[1];
				
					WebDriverWait wait=new WebDriverWait(glb_Webdriver_driver,explicit_wait_value);
				
					switch(m_locator_type)
					{
						case "xpath": try{
										wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(m_locator_value)));
										m_WebElemnt_element=glb_Webdriver_driver.findElement(By.xpath(m_locator_value));
										if(m_WebElemnt_element!=null)
										{
											glb_Logger_commonlogs.info("WebElement located with using xpath : "+m_locator_value);
											
										}										
									 }
									catch(TimeoutException e)
									{
										glb_Logger_commonlogs.error(e.getMessage());
										 captureAndSaveScreenshot();
										TestSuiteDriver.glb_Boolean_testResult = false;
							 		   	TestSuiteDriver.glb_String_failureMessage = "Not able to locate --- " + m_locator_value + "Error Description: " + e.getMessage();
									}
									catch(Exception e)
									{
										glb_Logger_commonlogs.info("Not able to locate" + e.getMessage());
										 captureAndSaveScreenshot();
										TestSuiteDriver.glb_Boolean_testResult = false;
							 		   	TestSuiteDriver.glb_String_failureMessage = "Not able to locate --- " + m_locator_value + "Error Description: " + e.getMessage();
									}
					 			  	break;
								 
						
						case "id": try{
								wait.until(ExpectedConditions.presenceOfElementLocated(By.id(m_locator_value)));
								m_WebElemnt_element=glb_Webdriver_driver.findElement(By.id(m_locator_value));
								if(m_WebElemnt_element!=null)
								{
									glb_Logger_commonlogs.info("WebElement located with using id : "+m_locator_value);
								}
								}
								catch(TimeoutException e)
								{
									glb_Logger_commonlogs.error(e.getMessage());
								}
								catch(Exception e)
								{
									glb_Logger_commonlogs.error(e.getMessage());
								}
								break;
								
						
						case "name": try{wait.until(ExpectedConditions.presenceOfElementLocated(By.name(m_locator_value)));
								 m_WebElemnt_element=glb_Webdriver_driver.findElement(By.name(m_locator_value));
								 if(m_WebElemnt_element!=null)
									{
										glb_Logger_commonlogs.info("WebElement located with using name : "+m_locator_value);
									}
									}
									catch(TimeoutException e)
									{
										glb_Logger_commonlogs.error(e.getMessage());
									}
									catch(Exception e)
									{
										glb_Logger_commonlogs.error(e.getMessage());
									}break;
						
						case "class":try{wait.until(ExpectedConditions.presenceOfElementLocated(By.className(m_locator_value)));
								 m_WebElemnt_element=glb_Webdriver_driver.findElement(By.className(m_locator_value));
								 if(m_WebElemnt_element!=null)
									{
										glb_Logger_commonlogs.info("WebElement located with using classname : "+m_locator_value);
									}
									}
									catch(TimeoutException e)
									{
										glb_Logger_commonlogs.error(e.getMessage());
									}
									catch(Exception e)
									{
										glb_Logger_commonlogs.error(e.getMessage());
									}break;
					
					
						case "css":try{wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(m_locator_value)));
								m_WebElemnt_element=glb_Webdriver_driver.findElement(By.cssSelector(m_locator_value));
								if(m_WebElemnt_element!=null)
								{
									glb_Logger_commonlogs.info("WebElement located with using css : "+m_locator_value);
								}
								}catch(TimeoutException e)
								{
									glb_Logger_commonlogs.error(e.getMessage());
								}
								catch(Exception e)
								{
									glb_Logger_commonlogs.error(e.getMessage());
								}
								break;
						
						default : Exceptions m_exeption=Exceptions.INVALID_LOCATOR_TYPE_EXCEPTION;
							  if(m_exeption==Exceptions.INVALID_LOCATOR_TYPE_EXCEPTION)
							  {	  
								  throw new CommonFunctionsExceptions(m_exeption,m_locator_type);
							  }
					}
				}
				else
				{
					Exceptions m_Exceptions=Exceptions.LOCATOR_FORMAT_EXCEPTION;
					throw new CommonFunctionsExceptions(m_Exceptions);
					
				}
			}
			else
			{
				Exceptions m_Exception=Exceptions.LOCATOR_VALUE_NULL_EXCEPTION;
				if(m_Exception==Exceptions.LOCATOR_VALUE_NULL_EXCEPTION)
				{
					throw new CommonFunctionsExceptions(m_Exception);
				}
			}
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.error(e.getMessage());
		}	
		return m_WebElemnt_element;
	}
	/**
	 * @author mkarthik
	 * date: October 2nd
	 * date of review: 
	 * Description: This method is used to enter text to element based on locator
	 * @param locator
	 * @param text_Value
	 * @return m_bln_enter_text_state
	 */
	public boolean enterText(String v_str_object,String v_text_Value)
	{
		boolean m_bln_enter_text_state=false;
		
		WebElement m_WebElement_textfield = null;
		
		String [] locator=null;
		
		String m_str_locator=null;
		
		Assert.assertNotNull(v_str_object,"The object passes is null");
		locator=splitLocators(v_str_object);
		
		int m_int_size=locator.length;
		
		if(m_int_size==1)
		{
			m_str_locator=locator[0];
		}
		else
		{
			glb_Logger_commonlogs.info("In enterText()");
			glb_Logger_commonlogs.info("Expected only 1 element. But number of elements found: "+m_int_size);
		}
		
		try
		{
			Assert.assertNotNull(m_str_locator, "The value passed is null. Hence halting the execution...");
			
			if(m_str_locator!=null)
			{
				m_WebElement_textfield=locateElement(m_str_locator);
				if(m_WebElement_textfield!=null)
				{
					m_WebElement_textfield.sendKeys(v_text_Value);
					Assert.assertEquals(m_WebElement_textfield.getAttribute("value"),v_text_Value);
					glb_Logger_commonlogs.info("In enterText()");
					glb_Logger_commonlogs.info("Completed entering text to element...");
					m_bln_enter_text_state=true;
				}
				else
				{
					glb_Logger_commonlogs.info("In enterText()");
					Exceptions m_Exceptions=Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
					if(m_Exceptions==Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION)
					{
						throw new CommonFunctionsExceptions(m_Exceptions,m_str_locator);
					}
				}
			}
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.info("In enterText()");
			glb_Logger_commonlogs.error(e.getMessage());
		}
		return m_bln_enter_text_state;
	}
	/**
	 * @author mkarthik
	 * date: October 2nd
	 * date of review:
	 * Description: This function is used to get the attribute of the element
	 * @param locator
	 * @param attribute
	 * @param expected_Value
	 * @return m_Str_attribute_value
	 */
	private String getAttributeOfElement(String v_str_locator,String v_str_expectedAttribute_value)
	{
		String m_Str_attribute_value=null;
		
		WebElement m_WebElement_element = null;
		
		String [] locators=null;
		String [] testdata=null;
		String attribute=null;
		String value=null; 
		
		String m_str_locators=null;
		
		
		testdata=v_str_expectedAttribute_value.split(",");
		Assert.assertNotNull(testdata,"There are no testdata passed..");
		
		int m_int_size_testdata=testdata.length;
		
		if(m_int_size_testdata==1)
		{
			glb_Logger_commonlogs.info("In getAttributeOfElement()");
			glb_Logger_commonlogs.info("Expected one attribute and value for an element. But only one string is passed");
		}
		else if(m_int_size_testdata==2)
		{
			attribute=testdata[0];
			value=testdata[1];
		}
		else
		{
			glb_Logger_commonlogs.info("In getAttributeOfElement()");
			glb_Logger_commonlogs.info("Expected one attribute and value for an element. But number of string passed may be more.");
		}
		
		Assert.assertNotNull(v_str_locator,"The object passed is null");
		locators=splitLocators(v_str_locator);
		
		int m_int_size=locators.length;
		
		if(m_int_size==1)
		{
			m_str_locators=locators[0];
		}
		else
		{
			glb_Logger_commonlogs.info("In getAttributeOfElement()");
			glb_Logger_commonlogs.info("Expected only 1 element. But number of elements found: "+m_int_size);
		}
		try
		{
			Assert.assertNotNull(m_str_locators, "The value passed is null. Hence halting the execution...");
			
			if(m_str_locators!=null)
			{
				m_WebElement_element=locateElement(m_str_locators);
				if(m_WebElement_element!=null)
				{
					m_Str_attribute_value=m_WebElement_element.getAttribute(attribute);
					if(m_Str_attribute_value!=null)
					{
						glb_Logger_commonlogs.info("In getAttributeOfElement()");
						glb_Logger_commonlogs.info("Got attribute :"+attribute+" and value: "+m_Str_attribute_value+" for the specified element..");
					}
				}
				else
				{
					glb_Logger_commonlogs.info("In getAttributeOfElement()");
					Exceptions m_Exceptions=Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
					if(m_Exceptions==Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION)
					{
						throw new CommonFunctionsExceptions(m_Exceptions,m_str_locators);
					}
				}
			}
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.info("In getAttributeOfElement()");
			glb_Logger_commonlogs.error(e.getMessage());
		}
		
		return m_Str_attribute_value;
	}
	
	/**
	 * @author Rajdeep
	 * date: October-6th
	 * date of review: October-7th
	 * Description: This function is used to get the text of an element
	 * @param locator
	 * @return String- m_Str_text_value
	 */
	private String getTextFromElement(String v_str_object){
		
		String m_Str_text = null;
		
		try{
			Assert.assertNotNull(v_str_object, "The locator passed is null. Hence halting the execution...");
			List<WebElement> elements = getListOfWebElementObjects(v_str_object);
			if(elements.size()>1){
				System.out.println("More than two elements found. Getting text from first element.");
			}
			m_Str_text = elements.get(0).getText();
			System.err.println(elements.size() + " - Text got from element " + v_str_object + " :: " + m_Str_text);
		}
		catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
		}
		
		return m_Str_text;	
	}
	
	/**
	 * @author Rajdeep
	 * date: October-6th
	 * date of review: October-7th
	 * Description: This function is used to highlight an element
	 * @param locator
	 * @return boolean- m_bln_highlight
	 */
	private void highlightElement(String locator){
		
		WebElement m_WebElement_element = null;
		//boolean m_bln_highlight = false;
		
		JavascriptExecutor js = (JavascriptExecutor) glb_Webdriver_driver;
		
		try{
			Assert.assertNotNull(locator, "The locator passed is null. Hence halting the execution...");
			
			if(locator!=null){
				m_WebElement_element= locateElement(locator);
				
				if(m_WebElement_element != null){
					js.executeScript("arguments[0].setAttribute('style', arguments[1]);", m_WebElement_element, "border: 3px solid red;");
					js.executeScript("arguments[0].setAttribute('style', arguments[1]);", m_WebElement_element, "");
					//m_bln_highlight = true;
				}else{
					Exceptions m_exception = Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
					throw new CommonFunctionsExceptions(m_exception, locator);
				}
			}
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
		}
	}
	
	/**
	 * @author Rajdeep
	 * date: October-7th
	 * date of review: October-8th
	 * Description: This function is used to compare two texts
	 * @param text1, text2
	 * @return boolean- m_bln_compare
	 */
	public boolean compareTexts(String v_text1, String v_text2){
		boolean m_bln_compare = false;
		try{
			Assert.assertNotNull(v_text1, "Text passed is null. Hence halting the execution...");
			Assert.assertNotNull(v_text2, "Text passed is null. Hence halting the execution...");
			m_bln_compare = v_text1.equals(v_text2);
			if(m_bln_compare == false){
				glb_Logger_commonlogs.error("Text - " + v_text1 + " and Text- " + v_text2 + " did not match... ");
			}
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
		}
		return m_bln_compare;
		
	}
	
	/**
	 * @author Rajdeep
	 * date: October-7th
	 * date of review: October-8th
	 * Description: This function is used to get text from an element and compare with expected text
	 * @param locator, expectedText
	 * @return boolean- m_bln_compareText
	 */
	public boolean getTextFromElementAndCompare(String v_str_object, String v_str_expectedText) throws IOException{
		
		String m_Str_webelement_text = null;
		boolean m_bln_compareText = false;
		String failMsg = "Texts did not match. Expected: " + v_str_expectedText + " :: Actual: " + m_Str_webelement_text;
		
		try{
			Assert.assertNotNull(v_str_object, "The locator passed is null. Hence halting the execution...");
			Assert.assertNotNull(v_str_expectedText, "Text passed is null. Hence halting the execution...");
			m_Str_webelement_text = getTextFromElement(v_str_object);
			m_bln_compareText = compareTexts(m_Str_webelement_text, v_str_expectedText);
			
			if(!m_bln_compareText){
					System.out.println(failMsg);
				glb_Logger_commonlogs.error(failMsg);
				actionOnFailure(failMsg);
			/*	captureAndSaveScreenshot();
	 		   	TestSuiteDriver.glb_Boolean_testResult = false;
	 		   	TestSuiteDriver.glb_String_failureMessage = "Text Comparison not successful for " +v_str_object ;
				glb_Logger_commonlogs.info("Text Comparison not successful for " +v_str_object + "Expected: " + v_str_expectedText +" : : Actual: "+ m_Str_webelement_text); */
			}
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
		       glb_Logger_commonlogs.error(failMsg);
			   			actionOnFailure(failMsg);
		/*	captureAndSaveScreenshot();
 		   	TestSuiteDriver.glb_Boolean_testResult = false;
 		   	TestSuiteDriver.glb_String_failureMessage = "Not able to get text and compare " +v_str_object + "Error Description" + e.getMessage();
			glb_Logger_commonlogs.error(e.getMessage());*/
		}
		return m_bln_compareText;
		
	}
	/**
	 * @author Rajdeep
	 * date: October-7th
	 * date of review: October-8th
	 * Description: This function is used to check whether a locator is present or not
	 * @param locator
	 * @return boolean- m_bln_element_presence
	 * @throws Exception 
	 */
	/*private String readPdfFile(String filepath) throws Exception{
		failMsg = "Not able to read pdf file- " + filepath;
		String page = null;
		
		try{
			Assert.assertNotNull(filepath, "The file path passed is null. Hence halting the execution...");
			PdfReader reader = new PdfReader(filepath);
			page = PdfTextExtractor.getTextFromPage(reader, 1);
			
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
			System.out.println(failMsg);
			glb_Logger_commonlogs.error(failMsg);
			actionOnFailure(failMsg);
		}
		return page;
	}*/
	
	/**
	 * @author Rajdeep
	 * date: December -7th
	 * date of review:
	 * Description: This function is used download pdf from current window url
	 * 
	 */
	public static String downloadPdfFromCurrentWindowUrl(String v_str_object, String v_str_data) throws IOException{
		String urlString = glb_Webdriver_driver.getCurrentUrl();
		
		pdfSavePath = System.getProperty("user.dir") + "\\test.pdf";
		URL url = new URL(urlString);
		InputStream in = url.openStream();
		Files.copy(in, Paths.get(pdfSavePath), StandardCopyOption.REPLACE_EXISTING);
		in.close();
		return pdfSavePath;
	}
	
	/**
	 * @author Rajdeep
	 * date: December -7th
	 * date of review: 
	 * Description: This function is used to verify a text present in a pdf or not
	 */
	/*public void verifyTextPresentInPdf(String v_str_object, String v_str_data) throws Exception{
	  try{
		failMsg = "Text " + v_str_data + " does not exist in pdf";
		
		Assert.assertNotNull(v_str_data, "Text passed is null. Halting executiom...");
		//String pdfPath = downloadPdfFromCurrentWindowUrl();
		
		String pdfText = readPdfFile(pdfSavePath);
		//System.out.println(pdfText);
		
		if(!pdfText.contains(v_str_data)){
			glb_Logger_commonlogs.info("In verifyTextPresentInPdf");
			System.out.println(failMsg);
			glb_Logger_commonlogs.error(failMsg);
			actionOnFailure(failMsg);
		}
	  }catch(Exception e){
		  	glb_Logger_commonlogs.info("In verifyTextPresentInPdf");
			glb_Logger_commonlogs.error(e.getMessage());
			System.out.println(failMsg);
			glb_Logger_commonlogs.error(failMsg);
			actionOnFailure(failMsg);
		}
	}*/
	
	/**
	 * @author Rajdeep
	 * date: October-7th
	 * date of review: October-8th
	 * Description: This function is used to check whether a locator is present or not
	 * @param locator
	 * @return boolean- m_bln_element_presence
	 */
	public boolean verifyElementPresent(String v_str_object, String v_str_data){
		
		WebElement m_WebElement_element = null;
		boolean m_bln_element_presence = false;
		failMsg = "Not able to find element- " + v_str_object;
		
		try{
			Assert.assertNotNull(v_str_object, "The locator passed is null. Hence halting the execution...");
			m_WebElement_element = locateElement(v_str_object);
			
			if(m_WebElement_element != null){
				glb_Logger_commonlogs.info("Successfully locate element: " + v_str_object);
				m_bln_element_presence = true; 
			}else{
				Exceptions m_exception = Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
				throw new CommonFunctionsExceptions(m_exception, v_str_object);
			}
		}catch(Exception e){
			glb_Logger_commonlogs.info("In verifyElementPresent");
			glb_Logger_commonlogs.error(e.getMessage());
			System.out.println(failMsg);
			glb_Logger_commonlogs.error(failMsg);
			actionOnFailure(failMsg);
		}
		return m_bln_element_presence;
	}
	
	/**
	 * @author Rajdeep
	 * date: December 7th
	 * date of review:
	 * Description: This function is used to switch to last opened window
	 */
	public void switchToLatestOpenedWindow(String v_str_object, String v_str_data){
		failMsg = "Switching to latest window is failed.";
		try{
			Set<String> windowIds = glb_Webdriver_driver.getWindowHandles();
			Iterator<String> it = windowIds.iterator();
			while(it.hasNext()){
				glb_Webdriver_driver.switchTo().window((String) it.next());
			}
		}catch(Exception e){
			glb_Logger_commonlogs.info("In switchToLatestOpenedWindow()");
			glb_Logger_commonlogs.error(e.getMessage());
			System.out.println(failMsg);
			glb_Logger_commonlogs.error(failMsg);
			actionOnFailure(failMsg);
		  }
	}
	
	/**
	 * @author Rajdeep
	 * date: December 7th
	 * date of review:
	 * Description: This function is used to get url of current window and find text in it
	 */
	public void getCurrentWindowURLAndFindText(String v_str_object, String v_str_data){
	  try{
		  
		String windowUrl = glb_Webdriver_driver.getCurrentUrl();
		failMsg = v_str_data + " is not found in url- " + windowUrl;
		
		Assert.assertNotNull(v_str_data, "Text passed is null. So halting the execution");
		
		if(!windowUrl.contains(v_str_data)){
			System.out.println(failMsg);
			glb_Logger_commonlogs.error(failMsg);
			actionOnFailure(failMsg);
		}
	  }catch(Exception e){
		  glb_Logger_commonlogs.info("In getCurrentWindowURLAndFindText()");
			glb_Logger_commonlogs.error(e.getMessage());
			System.out.println(failMsg);
			glb_Logger_commonlogs.error(failMsg);
			actionOnFailure(failMsg);
	  }
	}
	
	/**
	 * @author Rajdeep
	 * date: October-7th
	 * date of review: October-8th
	 * Description: This function is used to check whether a locator is present or not after waiting
	 * @param locator
	 * @return boolean- m_bln_element_presence
	 */
	public boolean waitForElementToBeVisible(String v_str_object, String v_str_data){
		
		WebElement m_WebElement_element = null;
		boolean m_bln_element_presence = false;
		failMsg = "Not able to find element- " + v_str_object;
		WebDriverWait wait = new WebDriverWait(glb_Webdriver_driver, 30);
		
		String locatorValue = getLocatorFromRepository(v_str_object);
		String[] arrStr = locatorValue.split(":");
		String xpathExpression = arrStr[1];
		
		try{
			Assert.assertNotNull(v_str_object, "The locator passed is null. Hence halting the execution...");
			m_WebElement_element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathExpression)));
			
			if(m_WebElement_element != null){
				glb_Logger_commonlogs.info("Successfully locate element: " + v_str_object);
				m_bln_element_presence = true; 
			}else{
				Exceptions m_exception = Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
				throw new CommonFunctionsExceptions(m_exception, v_str_object);
			}
		}catch(Exception e){
			glb_Logger_commonlogs.info("In verifyElementPresent");
			glb_Logger_commonlogs.error(e.getMessage());
			System.out.println(failMsg);
			glb_Logger_commonlogs.error(failMsg);
			actionOnFailure(failMsg);
		}
		return m_bln_element_presence;
	}
	
	/**
	 * @author Rajdeep
	 * date: October-8th
	 * date of review: October-8th
	 * Description: This function is used to move mouse over an element
	 * @param locator
	 * @return boolean- m_bln_element_mouseOver
	 */
	private boolean mouseOverElement(String locator){
		boolean m_bln_element_mouseOver = false;
		WebElement m_WebElement_element = null;
		
		try{
			Assert.assertNotNull(locator, "The locator passed is null. Hence halting the execution...");
			m_WebElement_element = locateElement(locator);
			
			if(m_WebElement_element != null){
				Actions act = new Actions(glb_Webdriver_driver);
				act.moveToElement(m_WebElement_element).build().perform();
				glb_Logger_commonlogs.info("Mouse successfully move to element: " + locator);
				m_bln_element_mouseOver = true;
			}
			else{
				Exceptions m_exception = Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
				throw new CommonFunctionsExceptions(m_exception, locator);
			}
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
		}
			
		return m_bln_element_mouseOver;
	}
	/**
	 *  @author mkarthik
	 * date: October-20th
	 * date of review: 
	 * Description: This function is used to split the string and return the locators if more than one elements are passed in single string
	 * @param v_object
	 * @return locators
	 */
	
	private static String[] splitLocators(String v_object)
	{
		String [] locators=null;
		
		try{	
			if(v_object!=null)
			{
				if(v_object.contains("<>"))
				{
					glb_Logger_commonlogs.info("In splitText()");
					glb_Logger_commonlogs.info("More than one locator exists...");
					locators=v_object.split("<>");
				}
				else
				{
					glb_Logger_commonlogs.info("In splitText()");
					glb_Logger_commonlogs.info("Only one locator exists..");
					locators=new String[1];
					locators[0]=v_object;
				}
			}
			else
			{
				glb_Logger_commonlogs.info("In splitText()");
				Exceptions m_exceptions=Exceptions.NULL_VALUE_PASSED_EXCEPTION;
				throw new CommonFunctionsExceptions(m_exceptions);
			}
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.info("In splitText()");
			glb_Logger_commonlogs.error(e.getMessage());
		}
		return locators;
	}
	/**
	 * @author mkarthik
	 * date: October-20th
	 * date of review: 
	 * Description: This function is used to split the string and return the testdata if more than one elements are passed in single string
	 * @param v_object
	 * @return
	 */
	private String[] splitTestData(String v_object)
	{
		String [] testdata=null;
		
		try{	
			if(v_object!=null)
			{
				if(v_object.contains(","))
				{
					glb_Logger_commonlogs.info("In splitTestData()");
					glb_Logger_commonlogs.info("More than one testdata exists...");
					testdata=v_object.split(",");
				}
				else
				{
					glb_Logger_commonlogs.info("In splitTestData()");
					glb_Logger_commonlogs.info("Only one testdata exists..");
					testdata=new String[1];
					testdata[0]=v_object;
				}
			}
			else
			{
				glb_Logger_commonlogs.info("In splitTestData()");
				Exceptions m_exceptions=Exceptions.NULL_VALUE_PASSED_EXCEPTION;
				throw new CommonFunctionsExceptions(m_exceptions);
			}
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.info("In splitTestData()");
			glb_Logger_commonlogs.error(e.getMessage());
		}
		return testdata;
	}
	/**
	 *  @author mkarthik
	 * date: October-20th
	 * date of review: 
	 * Description: This function is used to find the attribute and value for an element and compare it with exected attibute value
	 * @param v_object
	 * @param v_expectedAttributeValue
	 * @return m_bln_attribute_compare
	 * example: locator,src='name'
	 */
	public boolean getAttributeAndCompare(String v_object,String v_expectedAttributeValue)
	{
		boolean m_bln_attribute_compare=false;
		String m_str_attribute=null;
		String m_str_value=null; 
		String []testData=null;
		
		try{
			testData=v_expectedAttributeValue.split("=");
			m_str_attribute=testData[0];
			m_str_value=testData[1];
			String m_act_value=getAttributeOfElement(v_object, m_str_attribute);
		
			failMsg = "Attribute value not matching. Expected: " + m_str_value + " :: Actual: " + m_act_value;
			if(compareTexts(m_str_value, m_act_value))
			{
				m_bln_attribute_compare=true;
				glb_Logger_commonlogs.info("In getAttributeAndCompare()");
				glb_Logger_commonlogs.info("Attribute value matching...");
			}
			else{
				System.out.println(failMsg);
				glb_Logger_commonlogs.error(failMsg);
				actionOnFailure(failMsg);
			}
			
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.info("In getAttributeAndCompare()");
			glb_Logger_commonlogs.error(e.getMessage());
			System.out.println(failMsg);
			glb_Logger_commonlogs.error(failMsg);
			actionOnFailure(failMsg);
		}
		return m_bln_attribute_compare;
	}
	/**
	 * @author mkarthik
	 * date: October-21st
	 * date of review: 
	 * Description: This function is used to verify if the specified web element is displayed
	 * @param v_str_object
	 * @param v_str_testData
	 * @return m_bln_display
	 */
	public boolean isElementDisplayed(String v_str_object,String v_str_testData)
	{
		boolean m_bln_display=false;
		WebElement m_WebElement_element = null;
		
		String [] locators=null;
		String m_str_locator=null;
		
		
		try{
			Assert.assertNotNull(v_str_object,"The object passes is null");
			locators=splitLocators(v_str_object);
			
			int m_int_size=locators.length;
			
			if(m_int_size==1)
			{
				m_str_locator=locators[0];
				Assert.assertNotNull(m_str_locator, "The value passed is null. Hence halting the execution...");
				
				if(m_str_locator!=null)
				{
					m_WebElement_element=locateElement(m_str_locator);
					if(m_WebElement_element!=null)
					{
						m_bln_display=m_WebElement_element.isDisplayed();
						
						glb_Logger_commonlogs.info("In isElementDisplayed()");
						if(m_bln_display==true)
						{
						glb_Logger_commonlogs.info("Element is displayed..");
						}
					}
					else
					{
						glb_Logger_commonlogs.info("In isElementDisplayed()");
						Exceptions m_Exceptions=Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
						if(m_Exceptions==Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION)
						{
							throw new CommonFunctionsExceptions(m_Exceptions,m_str_locator);
						}
					}
				
			}
			else
			{
				glb_Logger_commonlogs.info("In isElementDisplayed()");
				glb_Logger_commonlogs.info("Expected only 1 element. But number of elements found: "+m_int_size);
			}
		}
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.info("In isElementDisplayed()");
			glb_Logger_commonlogs.error(e.getMessage());
		}
		return m_bln_display;
}
	/**
	 * @author mkarthik
	 * date: October-21st
	 * date of review: 
	 * Description: This function is used to verify if the specified web element is enabled
	 * @param v_str_object
	 * @param v_str_testData
	 * @return m_bln_enabled
	 */
	public boolean isElementEnabled(String v_str_object,String v_str_testData)
	{
		boolean m_bln_enabled=false;
		WebElement m_WebElement_element = null;
		
		String [] locators=null;
		String m_str_locator=null;
		
		
		try{
			Assert.assertNotNull(v_str_object,"The object passes is null");
			locators=splitLocators(v_str_object);
			
			int m_int_size=locators.length;
			
			if(m_int_size==1)
			{
				m_str_locator=locators[0];
				Assert.assertNotNull(m_str_locator, "The value passed is null. Hence halting the execution...");
				
				if(m_str_locator!=null)
				{
					m_WebElement_element=locateElement(m_str_locator);
					if(m_WebElement_element!=null)
					{
						m_bln_enabled=m_WebElement_element.isEnabled();
						
						glb_Logger_commonlogs.info("In isElementEnabled()");
						if(m_bln_enabled==true)
						{
							glb_Logger_commonlogs.info("Element is Enabled..");
						}
					}
					else
					{
						glb_Logger_commonlogs.info("In isElementEnabled()");
						Exceptions m_Exceptions=Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
						if(m_Exceptions==Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION)
						{
							throw new CommonFunctionsExceptions(m_Exceptions,m_str_locator);
						}
					}
				
			}
			else
			{
				glb_Logger_commonlogs.info("In isElementEnabled()");
				glb_Logger_commonlogs.info("Expected only 1 element. But number of elements found: "+m_int_size);
			}
		}
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.info("In isElementEnabled()");
			glb_Logger_commonlogs.error(e.getMessage());
		}
		return m_bln_enabled;
}
	/**
	 * @author mkarthik
	 * date: October-21st
	 * date of review: 
	 * Description: This function is used to verify if the specified web element is selected
	 * @param v_str_object
	 * @param v_str_testData
	 * @return m_bln_enabled
	 */
	public boolean isElementSelected(String v_str_object,String v_str_testData)
	{
		boolean m_bln_selected=false;
		WebElement m_WebElement_element = null;
		
		String [] locators=null;
		String m_str_locator=null;
		
		
		try{
			Assert.assertNotNull(v_str_object,"The object passes is null");
			locators=splitLocators(v_str_object);
			
			int m_int_size=locators.length;
			
			if(m_int_size==1)
			{
				m_str_locator=locators[0];
				Assert.assertNotNull(m_str_locator, "The value passed is null. Hence halting the execution...");
				
				if(m_str_locator!=null)
				{
					m_WebElement_element=locateElement(m_str_locator);
					if(m_WebElement_element!=null)
					{
						m_bln_selected=m_WebElement_element.isSelected();
						
						glb_Logger_commonlogs.info("In isElementSelected()");
						if(m_bln_selected==true)
						{
							glb_Logger_commonlogs.info("Element is Selected..");
						}
					}
					else
					{
						glb_Logger_commonlogs.info("In isElementSelected()");
						Exceptions m_Exceptions=Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
						if(m_Exceptions==Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION)
						{
							throw new CommonFunctionsExceptions(m_Exceptions,m_str_locator);
						}
					}
				
			}
			else
			{
				glb_Logger_commonlogs.info("In isElementSelected()");
				glb_Logger_commonlogs.info("Expected only 1 element. But number of elements found: "+m_int_size);
			}
		}
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.info("In isElementSelected()");
			glb_Logger_commonlogs.error(e.getMessage());
			glb_Logger_commonlogs.error("Unable to select element");
			captureAndSaveScreenshot();
 		   	TestSuiteDriver.glb_Boolean_testResult = false;
 		   	TestSuiteDriver.glb_String_failureMessage = "Invalid URL";
		}
		return m_bln_selected;
}
	/**
	 * @author mkarthik
	 * date: October-21st
	 * date of review: 
	 * Description: This function is navigate to the specified link
	 * @param v_str_object
	 * @param v_str_testdata
	 * @return m_bln_navigate
	 * @throws IOException 
	 */

	public boolean navigateToLink(String v_str_object,String v_str_testdata) 
	{
		boolean m_bln_navigate=false;
		int m_int_status = 0;
		try
		{
			if(v_str_testdata!=null)
			{
				m_int_status=sendGet("", v_str_testdata);
				Assert.assertEquals(m_int_status, 200,"The response code is not 200");
				if(m_int_status==200)
				{
					glb_Webdriver_driver.navigate().to(v_str_testdata);
					waitForPageToLoad();
					m_bln_navigate=true;
					glb_Logger_commonlogs.info("In navigateToLink()");
					glb_Logger_commonlogs.info("Navigated to page..");
				}
				else
				{
					glb_Logger_commonlogs.info("In navigateToLink()");
					Exceptions m_exceptions=Exceptions.INVALID_URL_EXCEPTION;
					if(m_exceptions==Exceptions.INVALID_URL_EXCEPTION)
					{
						throw new CommonFunctionsExceptions(m_int_status, v_str_testdata);
					}
				}
			}
			else
			{
				glb_Logger_commonlogs.info("In navigateToLink()");
				Exceptions m_exceptions=Exceptions.NULL_URL_EXCEPTION;
				if(m_exceptions==Exceptions.NULL_URL_EXCEPTION)
				{
					throw new CommonFunctionsExceptions(v_str_testdata);
				}
			}
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.error(e.getMessage());
			glb_Logger_commonlogs.error("Invalid URL");
			String failMsg = "Invalid URL -- Error Description: " + e.getMessage();
			actionOnFailure(failMsg);	
		}
		return m_bln_navigate;
	}
	/**
	 *  @author mkarthik
	 * date: October-21th
	 * date of review: 
	 * Description: This function is used to implicitly wait for the specified time for the elements in the page to load up
	 * @throws IOException 
	 */
	private static void implicitwait()
	{
		try{
		glb_Webdriver_driver.manage().timeouts().implicitlyWait(Application_Constants.implict_wait_value,TimeUnit.SECONDS);
		glb_Logger_commonlogs.info("In implicitwait()");
		glb_Logger_commonlogs.info("Impilicit wait completed..");
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.error(e.getMessage());
			glb_Logger_commonlogs.error("Implicit wait fails..");
			String failMsg = "Implicit wait fails -- Error Description: " + e.getMessage();
			actionOnFailure(failMsg);	
 		
		}
	}
	/**
	 *  @author mkarthik
	 * date: October-21th
	 * date of review: 
	 * Description: This function is used to for the entire page to load up in the specified time
	 */
	private static void waitForPageToLoad()
	{
		try{
		glb_Webdriver_driver.manage().timeouts().pageLoadTimeout(Application_Constants.page_load_timeout_value, TimeUnit.SECONDS);
		glb_Logger_commonlogs.info("In waitForPageToLoad()");
		glb_Logger_commonlogs.info("Wait for Page load completed..");
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.error(e.getMessage());
			TestSuiteDriver.glb_Boolean_testResult = false;
		}
	}
	/**
	 *  @author mkarthik
	 * date: October-21th
	 * date of review: 
	 * Description: This function is used to for the refresh the current page
	 */
	public void refreshPage()
	{
		try{
	    glb_Logger_commonlogs.info("In refreshPage()");
		glb_Webdriver_driver.navigate().refresh();
		waitForPageToLoad();
		glb_Logger_commonlogs.info("Refresh page completed..");
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.error(e.getMessage());
		}
	}
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method will initialize the browser specified in the external data source.
	 */
	public static void openBrowser(String v_str_object,String v_str_data){
		String path;
		glb_Logger_commonlogs.info("The webdriver to be initialized is : " + v_str_data);
		try{				
			switch (v_str_data) {
			
				case "Mozilla": ProfilesIni profile = new ProfilesIni();
								FirefoxProfile fp = profile.getProfile("default");
								glb_Webdriver_driver = new FirefoxDriver(fp);
				            	glb_Logger_commonlogs.info(v_str_data + " webdriver is initialized...");
				            	break;
				            				
	            case "IE":  	path = System.getProperty("user.dir") + "\\BrowserDrivers\\IEDriverServer.exe";
								System.setProperty("webdriver.ie.driver", path);
								glb_Webdriver_driver = new InternetExplorerDriver();
								glb_Logger_commonlogs.info(v_str_data + " webdriver is initialized...");
								break;
											
	            case "Chrome":  path = System.getProperty("user.dir") + "\\BrowserDrivers\\chrome.exe";
								System.setProperty("webdriver.chrome.driver", path);
								glb_Webdriver_driver = new ChromeDriver();
								glb_Logger_commonlogs.info(v_str_data + " webdriver is initialized...");
								break;
											
	            case "Opera":  	path = System.getProperty("user.dir") + "\\BrowserDrivers\\opera.exe";
								System.setProperty("webdriver.opera.driver", path);
								glb_Webdriver_driver = new OperaDriver();
								glb_Logger_commonlogs.info(v_str_data + " webdriver is initialized...");
								break;
											
	            case "Safari": 	path = System.getProperty("user.dir") + "\\BrowserDrivers\\safari.exe";
								System.setProperty("webdriver.safari.driver", path);
								glb_Webdriver_driver = new SafariDriver();
								glb_Logger_commonlogs.info(v_str_data + " webdriver is initialized...");
								break;
											
	            default : 		glb_Logger_commonlogs.info("invalid name of driver");
	            				break;
			
			}
			glb_Webdriver_driver.manage().window().maximize();
			implicitwait();
		}catch (Exception e){
			glb_Logger_commonlogs.error("Not able to open the Browser --- " + e.getMessage());
			TestSuiteDriver.glb_Boolean_testResult = false;
		}
	}
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method will navigate to the URL specified in the external data source.
	 */
	public static void navigate(String v_str_object, String v_str_data){
		try{
			glb_Logger_commonlogs.info("Navigating to URL..");
			glb_Webdriver_driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			glb_Webdriver_driver.get(v_str_data);
		}catch(Exception e){
			glb_Logger_commonlogs.error("Not able to navigate --- " + e.getMessage());
			TestSuiteDriver.glb_Boolean_testResult = false;
			}
		}
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method will click on the object specified in the external data source.
	 * @throws IOException 
	 */
	public static void click(String v_str_object, String v_str_data) throws IOException{
		System.out.println("+++++++++++++++" + v_str_data);
		
/*		if(v_str_data.equals("frame1")){
			System.out.println("+++++++++++++++" + v_str_data);
			waitForPageToLoad();
			glb_Webdriver_driver.switchTo().frame(1);	
		}*/
		try{
			glb_Logger_commonlogs.info("Clicking on Webelement.." + v_str_object);
			WebElement m_WebElement_element = locateElement(glb_Properties_objectRepository.getProperty(v_str_object));
			if(m_WebElement_element!=null)
			 m_WebElement_element.click();
		 }catch(Exception e){
			glb_Logger_commonlogs.error("Not able to click " + v_str_object + "Error Description" + e.getMessage());
			captureAndSaveScreenshot();
			TestSuiteDriver.glb_Boolean_testResult = false;
			TestSuiteDriver.glb_String_failureMessage = "Not able to click " +v_str_object + "Error Description" + e.getMessage();       	
			}
}
	/**
	 * @author Rajdeep
	 * date: October-12th
	 * date of review: 
	 * Description: This function will take care of taking screenshot and setting failure message in report.
	 * 
	 **/
	private static void actionOnFailure(String failMsg){
		captureAndSaveScreenshot();
		TestSuiteDriver.glb_Boolean_testResult = false;
		TestSuiteDriver.glb_String_failureMessage = failMsg;
	}
	
	public static void doubleClick(String v_str_object, String v_str_data) throws IOException{
		System.out.println("+++++++++++++++" + v_str_data);
/*		if(v_str_data.equals("frame1")){
			System.out.println("+++++++++++++++" + v_str_data);
			waitForPageToLoad();
			glb_Webdriver_driver.switchTo().frame(1);	
		}*/
		try{
			Actions builder = new Actions(glb_Webdriver_driver);   
			glb_Logger_commonlogs.info("Clicking on Webelement.." + v_str_object);
			WebElement m_WebElement_element = locateElement(glb_Properties_objectRepository.getProperty(v_str_object));
			if(m_WebElement_element!=null)
				builder.moveToElement(m_WebElement_element).doubleClick().build().perform(); 	
				
		 }catch(Exception e){
			glb_Logger_commonlogs.error("Not able to double click " + v_str_object + "Error Description" + e.getMessage());
			captureAndSaveScreenshot();
 		   	TestSuiteDriver.glb_Boolean_testResult = false;
 		   	TestSuiteDriver.glb_String_failureMessage = "Not able to double click " +v_str_object + "Error Description" + e.getMessage();
         	}
		}
	/**
	 * @author Rajdeep
	 * date: December 6th
	 * date of review: 
	 * Description: This method will enter data in the object specified in the external data source.
	 * @throws IOException 
	 * @throws CommonFunctionsExceptions 
	 */
	public static void inputPassword1(String v_str_object, String v_str_data) throws IOException, CommonFunctionsExceptions{
	
			glb_Logger_commonlogs.info("Entering the text in " + v_str_object);
			List<WebElement> m_WebElement_elements = getListOfWebElementObjects(v_str_object);
			
			if(m_WebElement_elements == null){
				glb_Logger_commonlogs.error("No WebElement found: " + v_str_object);
				TestSuiteDriver.glb_Boolean_testResult = false;
			}
			if(m_WebElement_elements.size()>1){
				glb_Logger_commonlogs.warn("Click function works on only 1 WebElement...");	
			}
			if(m_WebElement_elements != null){
				m_WebElement_elements.get(0).clear();
				byte[] encodedByte = v_str_data.getBytes();
				
				byte[] decodedByte = Base64.decode(encodedByte);
				String decodedString = new String(decodedByte);
				
				m_WebElement_elements.get(0).sendKeys(decodedString);
				glb_Logger_commonlogs.info("Successfully input password on: " + v_str_object);
			}
			
		 }

	
	
	public static void switchToFrame(String v_str_object, String v_str_data){
		waitForPageToLoad();
		if(v_str_data.equals("frame1")){
			System.out.println("+++++++++++++++" + v_str_data);
		glb_Webdriver_driver.switchTo().frame(1);	
		}else if(v_str_data.equals("frame2")){
			glb_Webdriver_driver.switchTo().frame(2);	
		}
	}
	
	
	public static void switchToDefaultContent(String v_str_object, String v_str_data){
			//waitForPageToLoad();
			glb_Webdriver_driver.switchTo().defaultContent();
		}
	
	
	
	
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method will enter data in the object specified in the external data source.
	 * @throws IOException 
	 */
	public static void input(String v_str_object, String v_str_data) throws IOException{
		try{
			glb_Logger_commonlogs.info("Entering the text in " + v_str_object);
			WebElement element = locateElement(glb_Properties_objectRepository.getProperty(v_str_object));
			System.out.println("###########   " + v_str_data);
			element.clear();
			if(element!=null){
				element.clear();
				element.sendKeys(v_str_data);
			}
				
			//glb_Webdriver_driver.findElement(By.xpath(glb_Properties_objectRepository.getProperty(object))).sendKeys(data);
		 }catch(Exception e){
			 glb_Logger_commonlogs.error("Not able to enter text --- " + e.getMessage());
			 captureAndSaveScreenshot();
			 TestSuiteDriver.glb_Boolean_testResult = false;
			 TestSuiteDriver.glb_String_failureMessage = "Not able to click --- " + e.getMessage();
		 	}
		}
	public static void inputPassword(String v_str_object, String v_str_data) throws IOException{
		try{
			glb_Logger_commonlogs.info("Entering the text in " + v_str_object);
			WebElement element = locateElement(glb_Properties_objectRepository.getProperty(v_str_object));
			element.clear();
			if(element!=null){
				element.clear();
				  byte[] decodedByte = Base64.decode(v_str_data.getBytes());
				  String decodedString = new String(decodedByte);
				element.sendKeys(decodedString);
			}
				
			//glb_Webdriver_driver.findElement(By.xpath(glb_Properties_objectRepository.getProperty(object))).sendKeys(data);
		 }catch(Exception e){
			 glb_Logger_commonlogs.error("Not able to enter password --- " + e.getMessage());
			 captureAndSaveScreenshot();
			 TestSuiteDriver.glb_Boolean_testResult = false;
			 TestSuiteDriver.glb_String_failureMessage = "Not able to click --- " + e.getMessage();
		 	}
		}
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method will capture screenshot for each failure and save it in target/extent-reports.
	 * @throws IOException 
	 */
	private static void captureAndSaveScreenshot() {
		File scrFile = ((TakesScreenshot)glb_Webdriver_driver).getScreenshotAs(OutputType.FILE);
		try {
			ImageIO.write(ImageIO.read(scrFile), DriverConstants.FAILURE_FILE_FORMAT, new File(DriverConstants.FAILURE_IMAGE_PATH + DriverConstants.FAILURE_FILE_PREFIX +glb_Int_captureScreenshotCount + DriverConstants.FAILURE_FILE_EXTENSION));
			glb_Int_captureScreenshotCount++;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method will wait for the object specified for five seconds.
	 */
	public static void waitForSeconds(String object, String data) throws Exception{
		try{
			int v_waitTimeInSeconds = (int) Double.parseDouble(data);
			glb_Logger_commonlogs.info("Waiting for " + v_waitTimeInSeconds + " seconds");
			int v_waitTimeInMilliSeconds = (v_waitTimeInSeconds * 1000);
			//Thread.sleep(50000);
			Thread.sleep(v_waitTimeInMilliSeconds);
			
		 }catch(Exception e){
			 glb_Logger_commonlogs.error("Not able to wait --- " + e.getMessage());
			 TestSuiteDriver.glb_Boolean_testResult = false;
         	}
		}
	
	/**
	 * @author Rajdeep
	 * date: October-12th
	 * date of review: 
	 * Description: This function is used to get all texts from auto-suggest list
	 * @param locatorSerachBox, locatorAutoCompleteDropdown, textToSearch
	 * @return List<String>- textsFromDropDown
	 */
	public List<String> getTextFromAutoComplete(String locatorSerachBox, String locatorAutoCompleteDropdown, String textToSearch){
		List<String> textsFromDropDown = new ArrayList<String>();
		List<WebElement> elementsFromDropDown = new ArrayList<WebElement>();
		WebElement m_WebElement_searchBox = null;
		WebElement m_WebElement_AutoCompleteDropdown = null;
		
		try{
			Assert.assertNotNull(locatorSerachBox, "The locator passed is null. Hence halting the execution...");
			Assert.assertNotNull(locatorAutoCompleteDropdown, "The locator passed is null. Hence halting the execution...");
			Assert.assertNotNull(textToSearch, "The text passed is null. Hence halting the execution...");
			
			locatorSerachBox = getLocatorFromRepository(locatorSerachBox);
			locatorAutoCompleteDropdown = getLocatorFromRepository(locatorAutoCompleteDropdown);
				
			m_WebElement_searchBox = locateElement(locatorSerachBox);
			m_WebElement_AutoCompleteDropdown = locateElement(locatorAutoCompleteDropdown);
			
			if(m_WebElement_searchBox!=null && m_WebElement_AutoCompleteDropdown!=null){
				m_WebElement_searchBox.sendKeys(textToSearch);
				elementsFromDropDown = m_WebElement_AutoCompleteDropdown.findElements(By.tagName("li"));
			
				for(WebElement element : elementsFromDropDown){
					textsFromDropDown.add(element.getText());
				}
			}else{
				Exceptions m_exception = Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
				if(m_WebElement_searchBox == null)
					throw new CommonFunctionsExceptions(m_exception, locatorSerachBox);
				else if(m_WebElement_AutoCompleteDropdown == null)
					throw new CommonFunctionsExceptions(m_exception, locatorAutoCompleteDropdown);
			}
			
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
		}
		return textsFromDropDown;
	}
	
	/**
	 * @author Rajdeep
	 * date: October-12th
	 * date of review: 
	 * Description: This function is used to locate a link using linkText or partialLinkText
	 * @param linkTextOrPartialLinkText
	 * @return WebElement- m_WebElement_element
	 */
	public WebElement locateLinkElement(String linkTextOrPartialLinkText){
		WebElement m_WebElement_element = null;
		
		try{
			Assert.assertNotNull(linkTextOrPartialLinkText, "The link text passed is null. Hence halting the execution...");
			m_WebElement_element = glb_Webdriver_driver.findElement(By.linkText(linkTextOrPartialLinkText));
			
			if(m_WebElement_element == null){
				m_WebElement_element = glb_Webdriver_driver.findElement(By.partialLinkText(linkTextOrPartialLinkText));
				
				if(m_WebElement_element == null){
					Exceptions m_exception = Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
					throw new CommonFunctionsExceptions(m_exception);
				}
			}
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
		}
		
		return m_WebElement_element;
	}
	
	/**
	 * @author Rajdeep
	 * date: October-12th
	 * date of review: 
	 * Description: This function is used to locate an element using tagName
	 * @param tagName
	 * @return WebElement- m_WebElement_element
	 */
	private WebElement getElementByTagName(String tagName){
		WebElement m_WebElement_element = null;
		
		try{
			Assert.assertNotNull(tagName, "The tagname passed is null. Hence halting the execution...");
			m_WebElement_element = glb_Webdriver_driver.findElement(By.tagName(tagName));
			
			if(m_WebElement_element == null){
				Exceptions m_exception = Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
				throw new CommonFunctionsExceptions(m_exception);
			}
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
		}
		
		return m_WebElement_element;
	}
	
	/**
	 * @author Rajdeep
	 * date: October-12th
	 * date of review: 
	 * Description: This function is used to deselect all selections in a dropdown
	 * @param locator
	 */
	public void deselectAllFromDropDown(String v_str_object, String v_str_data){
		WebElement m_WebElement_element = null;
		String locatorValue = null;
		
		try{
			Assert.assertNotNull(v_str_object, "The locator passed is null. Hence halting the execution...");
			locatorValue = getLocatorFromRepository(v_str_object);
			m_WebElement_element = locateElement(locatorValue);
			
			if(m_WebElement_element != null){
				Select select = new Select(m_WebElement_element);
				select.deselectAll();
			}else{
				Exceptions m_exception = Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
				throw new CommonFunctionsExceptions(m_exception, v_str_object);
			}
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
		}
	}
	
	/**
	 * @author Rajdeep
	 * date: October-20th
	 * date of review: 
	 * Description: This function fetches locator value from properties file using locator key
	 * @param locatorKey
	 * @return locatorValue
	 */
	private static String getLocatorFromRepository(String locatorKey){
		
		String locatorValue = null;
		
		try{
			Assert.assertNotNull(locatorKey, "Locator passed is null. Hence halting execution...");
			locatorValue = glb_Properties_objectRepository.getProperty(locatorKey);
			
			if(locatorValue == null){
				Exceptions m_exception = Exceptions.COULD_NOT_FIND_LOCATOR_FROM_REPOSITORY;
				throw new CommonFunctionsExceptions(m_exception, locatorKey);
			}
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
		}
		return locatorValue;
	}
	
	/**
	 * @author Rajdeep
	 * date: 25-Feb-2016
	 * date of review: 
	 * Description: This function takes two images as input and compares them
	 * @param Image1_Path and Image2_Path
	 * @return boolean
	 */
	/*public static boolean compareImage(String Image1_Path,String Image2_Path){	
	  boolean flag = false;
	  try{
		Assert.assertNotNull(Image1_Path, "The Path of Image1 is null. Hence halting the execution...");
		Assert.assertNotNull(Image2_Path, "The Path of Image2 is null. Hence halting the execution...");
		
		Image img1 = Toolkit.getDefaultToolkit().getImage(Image1_Path);
		Image img2 = Toolkit.getDefaultToolkit().getImage(Image2_Path);
		
		PixelGrabber pg1 = new PixelGrabber(img1, 0, 0, -1, -1, false);
		PixelGrabber pg2 = new PixelGrabber(img2, 0, 0, -1, -1, false);
		
		int[] pixelImg1 = null;
		int[] pixelImg2 = null;
		
		int height1 = 0; int height2 = 0;
		int width1 = 0; int width2 = 0; 
		
		if(pg1.grabPixels()){
			height1 = pg1.getHeight();
			width1 = pg1.getWidth();
			pixelImg1 = new int[height1 * width1];
			pixelImg1 = (int[]) pg1.getPixels();
		}
		
		if(pg2.grabPixels()){
			height2 = pg2.getHeight();
			width2 = pg2.getWidth();
			pixelImg2 = new int[height2 * width2];
			pixelImg2 = (int[]) pg2.getPixels();
		}
			
		if((height1 == height2) || (width1 == width2)){
			if(Arrays.equals(pixelImg1, pixelImg2)){
				flag = true;
				glb_Logger_commonlogs.info("Images are same.");
			}
			else{
				glb_Logger_commonlogs.error("Images are not same.");
			}
		}
		else{
			glb_Logger_commonlogs.error("Images are not same.");
		}
	  }catch(Exception e){
		glb_Logger_commonlogs.error(e.getMessage());
		String failMsg = "Not able to compare images: " + Image1_Path + " and " + Image2_Path;
		actionOnFailure(failMsg);
	  }
	  return flag;
	}*/
	
	/**
	 * @author Rajdeep
	 * date: 25-Feb-2016
	 * date of review: 
	 * Description: This function moves slider across slidingbar with offset x,y
	 * @param String v_str_object, String v_str_data (in the format: x,y)
	 * @return void
	 */
	public static void sliderAndSlidingBar(String v_str_object, String v_str_data){	
	  
	  try{
		  Assert.assertNotNull(v_str_object, "Object passed is null. Hence halting execution...");
		  
		  List<WebElement> elements = getListOfWebElementObjects(v_str_object);
		  
		  if(elements!=null && elements.size()==2){
			  WebElement slider = elements.get(0);
			  WebElement slidebar = elements.get(1);
			  String[] offset = v_str_data.split(",");
			  
			  if(offset!=null && offset.length==2){
				  Actions act = new Actions(glb_Webdriver_driver);
				  act.clickAndHold(slider).moveByOffset(Integer.parseInt(offset[0]), Integer.parseInt(offset[1])).release(slider).build().perform();
			  }else{
				  glb_Logger_commonlogs.error("Please provide proper offset data in the format: x,y");
			  }
		  }else{
			  glb_Logger_commonlogs.error("Xpaths for the objects are not properly given: " + v_str_object);
		  }
		  
	  }catch(Exception e){
		glb_Logger_commonlogs.error(e.getMessage());
		String failMsg = "Not able to slide: " + v_str_object;
		actionOnFailure(failMsg);
	  }
	 
	}
	
	/**
	 * @author Rajdeep
	 * date: 26-Feb-2016
	 * date of review: 
	 * Description: This function switches the webdriver control to a window
	 * @param String v_str_object, String v_str_data (window id)
	 * @return void
	 */
	public static void switchToWindow(String v_str_object, String v_str_data){
		try{
			Assert.assertNotNull(v_str_data, "Object passed is null. Hence halting execution...");
			glb_Webdriver_driver.switchTo().window(v_str_data);
			
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
			String failMsg = "Not able to switch to window: " + v_str_data;
			actionOnFailure(failMsg);
		  }
	}
	
	/**
	 * @author Rajdeep
	 * date: 26-Feb-2016
	 * date of review: 
	 * Description: This function switches the webdriver control to a window until it can find an element in that window
	 * @param String v_str_object (locator), String v_str_data (window id)
	 * @return void
	 */
	public static boolean switchToWindowAndFindElement(String v_str_object, String v_str_data){
		boolean found = false;
		
		try{
			Assert.assertNotNull(v_str_object, "Locator object passed is null. Hence halting execution...");
			Assert.assertNotNull(v_str_data, "Windows-id passed is null. Hence halting execution...");
			
			Set<String> windows = glb_Webdriver_driver.getWindowHandles();
			Iterator<String> it = windows.iterator();
			while(it.hasNext()){
				glb_Webdriver_driver.switchTo().window(it.next());
				List<WebElement> objectToFound = getListOfWebElementObjects(v_str_object);
				if(objectToFound!=null && objectToFound.size()==1){
					found = true;
					break;
				}
			}		
			
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
			String failMsg = "Not able to switch to window: " + v_str_data;
			actionOnFailure(failMsg);
		  }
		
		return found;
	}
	
	/**
	 * @author Rajdeep
	 * date: 26-Feb-2016
	 * date of review: 
	 * Description: This function waits until an element contains a text 
	 * @param String v_str_object (locator), String v_str_data (text to contain)
	 * @return void
	 */
	public static void waitUntilElementContainsText(String v_str_object, String v_str_data){
		try{
			Assert.assertNotNull(v_str_object, "Locator object passed is null. Hence halting execution...");
			Assert.assertNotNull(v_str_data, "Text passed is null. Hence halting execution...");
			
			List<WebElement> objects = getListOfWebElementObjects(v_str_object);
			
			if(objects!=null && objects.size()==1){
				String text = objects.get(0).getText();
				
				while(!text.equals(v_str_data)){
					
				}
			}		
		}
		catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
			String failMsg = "Not able to switch to window: " + v_str_data;
			actionOnFailure(failMsg);
		}
	}
	
	/**
	  * @author Rajdeep
	  * date: November- 4th
	  * date of review: 
	  * Description: This function is used to get webelement objects from locator
	  * @param v_str_object
	  * @return List<WebElement> - webElements
	  */
	 private static List<WebElement> getListOfWebElementObjects(String v_str_object) throws CommonFunctionsExceptions{
	  // v_str_object = locator
	  // 1. get locator from properties file- this may have multiple locators separated by "<>".
	  // 2. pass it to splitLocator function to separate multiple locators
	  // 3. split function returns string array of each locator- pass it to locateElement function ony by one to fetch separte webelements
	  
	  List<WebElement> m_listOfWebElements = new ArrayList<WebElement>();
	  try{
	   Assert.assertNotNull(v_str_object, "The locator passed is null. Hence halting the execution...");
	   String locator = getLocatorFromRepository(v_str_object);
	   String[] locators = splitLocators(locator);
	   for(int i=0; i<locators.length; i++){
	    m_listOfWebElements.add(locateElement(locators[i]));
	   }
	  }catch(Exception e){
	   glb_Logger_commonlogs.error(e.getMessage());
	  }
	  
	  return m_listOfWebElements;
	 }
}