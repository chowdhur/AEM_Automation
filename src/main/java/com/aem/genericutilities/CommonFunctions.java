package com.aem.genericutilities;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aem.application.application_Constants;

/**
 * 
 * @author mkarthik
 * 
 * This class should have all the commonly used functions developed using selenium web driver
 * 
 * please add description for all the methods defined and declared in this class
 *
 */
public class CommonFunctions implements application_Constants
{
	public static WebDriver glb_Webdriver_driver=null;
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
		if(glb_Webdriver_driver==null)
		{
			String m_driver_type=interf_Webdriver_driverType;
			switch(m_driver_type)
			{
				case "firefox": glb_Webdriver_driver=new FirefoxDriver();
								break;
				default : System.out.println("invalid name of driver");
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
	
	public boolean openApp(String url) throws CommonFunctionsExceptions
	{
		boolean m_bln_openApp_Status = false;
		int int_response_code = 0;
		try
		{
			Assert.assertNotNull(url);
			if(url!=null)
			{
				CommonFunctions m_CommonFunctions=new CommonFunctions();
				int_response_code=m_CommonFunctions.sendGet(url);
				Assert.assertEquals(int_response_code, 200,"The response code is not 200. Hence halting the execution");
				if(int_response_code==200)
				{
					glb_Webdriver_driver.get(url);
					m_bln_openApp_Status=true;
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
		catch(CommonFunctionsExceptions e)
		{
			System.out.println(e.getMessage());	
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());	
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
	private int sendGet(String urls) throws Exception 
	{
		String m_url = urls; 
		URL m_obj = new URL(m_url);
        HttpURLConnection m_con = (HttpURLConnection) m_obj.openConnection();
	    // optional default is GET
		m_con.setRequestMethod("GET");
		//add request header
	    //con.setRequestProperty("User-Agent", USER_AGENT);
		int m_responseCode = m_con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + m_url);
		System.out.println("Response Code : " + m_responseCode);
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
	public boolean closeBrowser() throws CommonFunctionsExceptions
	{
		boolean m_bln_close_state=false;
		try
		{
			if(glb_Webdriver_driver!=null)
			{
				glb_Webdriver_driver.close();
				m_bln_close_state=true;
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
		catch(CommonFunctionsExceptions e)
		{
			System.out.println(e.getMessage());
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
	public boolean dragAndDrop(String source,String destination)
	{
		boolean m_bln_dragAndDrop_status=false;
		
		WebElement m_WebElement_Source = null;
		WebElement m_WebElement_Destination = null;
		
		CommonFunctions m_CommonFunctions_commonFunctions=new CommonFunctions();
		try
		{
			Assert.assertNotNull(source, "The source element is null. Hence halting the execution...");
			Assert.assertNotNull(destination, "The destination element is null. Hence halting the execution...");
			
			if((source!=null)&&(destination!=null))
			{
				m_WebElement_Source=m_CommonFunctions_commonFunctions.locateElement(source);
				m_WebElement_Destination=m_CommonFunctions_commonFunctions.locateElement(destination);
				
				Assert.assertNotNull(m_WebElement_Source, "The source element is not located. Hence halting the execution..");
				Assert.assertNotNull(m_WebElement_Destination, "The destination element is not located. Hence halting the execution..");
				
				if((m_WebElement_Source!=null)&&(m_WebElement_Destination!=null))
				{
					Actions m_Actions_action=new Actions(glb_Webdriver_driver);
					m_Actions_action.dragAndDrop(m_WebElement_Source, m_WebElement_Destination).build().perform();
					m_bln_dragAndDrop_status=true;
				}
				else
				{
					Exceptions m_Exceptions=Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION;
					if(m_Exceptions==Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION)
					{
						if(m_WebElement_Source==null)
						{
							throw new CommonFunctionsExceptions(m_Exceptions,source);
						}
						else if(m_WebElement_Destination==null)
						{
							throw new CommonFunctionsExceptions(m_Exceptions,destination);
						}
					}
				}
			}
			else
			{
				Exceptions m_exceptions=Exceptions.SOURCE_DESTINATION_VALUES_NULL_EXCEPTION;
				if(m_exceptions==Exceptions.SOURCE_DESTINATION_VALUES_NULL_EXCEPTION)
				{
					throw new CommonFunctionsExceptions(source,destination);
				}
			}
		}
		catch(CommonFunctionsExceptions e)
		{
			System.out.println(e.getMessage());
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
					case "xpath": wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(m_locator_value)));
								  m_WebElemnt_element=glb_Webdriver_driver.findElement(By.xpath(m_locator_value));
								  break;
						
					case "id": wait.until(ExpectedConditions.presenceOfElementLocated(By.id(m_locator_value)));
								m_WebElemnt_element=glb_Webdriver_driver.findElement(By.id(m_locator_value));
					  			break;
						
					case "name": wait.until(ExpectedConditions.presenceOfElementLocated(By.name(m_locator_value)));
								 m_WebElemnt_element=glb_Webdriver_driver.findElement(By.name(m_locator_value));
					             break;
						
					case "class":wait.until(ExpectedConditions.presenceOfElementLocated(By.className(m_locator_value)));
								 m_WebElemnt_element=glb_Webdriver_driver.findElement(By.className(m_locator_value));
					  			 break;
					case "css":wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(m_locator_value)));
								m_WebElemnt_element=glb_Webdriver_driver.findElement(By.cssSelector(m_locator_value));
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
		catch(CommonFunctionsExceptions e)
		{
			System.out.println(e.getMessage());
		}	
		return m_WebElemnt_element;
	}
}
