package com.aem.genericutilities;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aem.application.Application_Constants;

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
	
	
	/**
	 * @author mkarthik
	 * date: October 2nd
	 * date of review: 
	 * parameters : It is a zero parameterized constructor
	 * Description: This constructor is used to initialize the browser
	 * It takes the name of the browser from the interface com.aem.application.application_Constants
	 * Based on the name it will create the Webdriver object
	 */
	public CommonFunctions()
	{
		glb_Logger_commonlogs=CommonLogging.getLogObj(CommonFunctions.class);
		
		if(glb_Webdriver_driver==null)
		{
			String m_driver_type=interf_Webdriver_driverType;
			glb_Logger_commonlogs.info("The webdriver to be initialized is : "+m_driver_type);
			
			switch(m_driver_type)
			{
				case "firefox": glb_Webdriver_driver=new FirefoxDriver();
								glb_Logger_commonlogs.info("The webdriver is initialized...");
								break;
								
				default : glb_Logger_commonlogs.info("invalid name of driver");
					  	  break;
			}
		}
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
	public boolean closeBrowser(String object,String testdata) throws CommonFunctionsExceptions
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
	public boolean dragAndDrop(String v_object,String v_testdata)
	{
		boolean m_bln_dragAndDrop_status=false;
		
		WebElement m_WebElement_Source = null;
		WebElement m_WebElement_Destination = null;
		String [] locator=null;
		String m_str_source=null;
		String m_str_destination=null;
		
		Assert.assertNotNull(v_object,"The object passes is null");
		locator=splitLocators(v_object);
		
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
	 * @author mkarthik
	 * date: October 2nd
	 * date of review: 
	 * Description: This method is used to locate the element using different locating strategy
	 * @param locator
	 * @return m_WebElemnt_element
	 * @throws CommonFunctionsExceptions
	 */
	private WebElement locateElement(String locator) throws CommonFunctionsExceptions
	{
		WebElement m_WebElemnt_element=null;
		try{
			if(locator!=null)
			{
				String [] m_locator=locator.split(":");
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
									}
									catch(Exception e)
									{
										glb_Logger_commonlogs.error(e.getMessage());
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
			
			if(locator!=null)
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
	private String getTextFromElement(String locator){
		
		WebElement m_WebElement_element = null;
		
		String m_Str_text_value = null;
		
		try{
			Assert.assertNotNull(locator, "The locator passed is null. Hence halting the execution...");
			
			if(locator!=null){		
				m_WebElement_element= locateElement(locator);
				
				if(m_WebElement_element != null){
					m_Str_text_value = m_WebElement_element.getText();
					glb_Logger_commonlogs.info("Got text from locator: " + locator);	
				}else{
					Exceptions m_Exceptions=Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
					throw new CommonFunctionsExceptions(m_Exceptions,locator);
				}
			}
		}
		catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
		}
		
		return m_Str_text_value;	
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
	public boolean getTextFromElementAndCompare(String locator, String expectedText){
		
		String m_Str_webelement_text = null;
		boolean m_bln_compareText = false;
		
		try{
			Assert.assertNotNull(locator, "The locator passed is null. Hence halting the execution...");
			Assert.assertNotNull(expectedText, "Text passed is null. Hence halting the execution...");
			m_Str_webelement_text = getTextFromElement(locator);
			m_bln_compareText = compareTexts(m_Str_webelement_text, expectedText);
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
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
	 */
	public boolean verifyElementPresent(String locator){
		
		WebElement m_WebElement_element = null;
		boolean m_bln_element_presence = false;
		
		try{
			Assert.assertNotNull(locator, "The locator passed is null. Hence halting the execution...");
			m_WebElement_element = locateElement(locator);
			
			if(m_WebElement_element != null){
				glb_Logger_commonlogs.info("Successfully locate element: " + locator);
				m_bln_element_presence = true;
			}else{
				Exceptions m_exception = Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
				throw new CommonFunctionsExceptions(m_exception, locator);
			}
		}catch(Exception e){
			glb_Logger_commonlogs.error(e.getMessage());
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
	
	private String[] splitLocators(String v_object)
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
	public boolean getAttributeAndCompare(String v_object,String v_expectedAttributeValue)
	{
		boolean m_bln_attribute_compare=false;
		String m_str_attribute=null;
		String m_str_value=null; 
		String []testData=null;
		
		try{
			testData=v_expectedAttributeValue.split(",");
			m_str_attribute=testData[0];
			m_str_value=testData[1];
			String m_act_value=getAttributeOfElement(v_object, v_expectedAttributeValue);
		
			if(compareTexts(m_str_value, m_act_value))
			{
				m_bln_attribute_compare=true;
				glb_Logger_commonlogs.info("In getAttributeAndCompare()");
				glb_Logger_commonlogs.info("Attribute value matching...");
			}
			
		}
		catch(Exception e)
		{
			glb_Logger_commonlogs.info("In getAttributeAndCompare()");
			glb_Logger_commonlogs.error(e.getMessage());
		}
		return m_bln_attribute_compare;
	}
}
