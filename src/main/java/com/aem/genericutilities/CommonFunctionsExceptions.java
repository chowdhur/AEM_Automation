package com.aem.genericutilities;
/**
 * 
 * @author mkarthik
 * 
 * This class should have all the exceptions which are thrown by the common functions
 * 
 * please add description for all the methods defined and declared in this class
 *
 */
public class CommonFunctionsExceptions extends Exception
{
	/**
	 * @author mkarthik
	 * date of creation : October 2nd
	 * date of review:
	 * Description : This exception is used when the url value is null
	 * @param url : The url to be opened
	 */
	public CommonFunctionsExceptions(String url)
	{
		CommonFunctions.glb_Logger_commonlogs.error("The URL parameter is null.."+url);
	}
	/**
	 * @author mkarthik
	 * date of creation : October 2nd
	 * date of review:
	 * Description : This exception is used when the response code for the url hit is not 200
	 * @param b = response code
	 * @param url : The url to be opened
	 */
	public CommonFunctionsExceptions(int b,String url)
	{
		CommonFunctions.glb_Logger_commonlogs.error("The URL parameter is.."+url+" and response code is: "+b);
	}
	/**
	 * @author mkarthik
	 * date of creation : October 2nd
	 * date of review:
	 * Description : This exception is used when webdriver object is null
	 */
	public CommonFunctionsExceptions()
	{
		CommonFunctions.glb_Logger_commonlogs.error("The webdriver is not initialized and is null..");
	}
	/**
	 * @author mkarthik
	 * date of creation : October 2nd
	 * date of review:
	 * Description : This exception is used when source and destination locators are null
	 * @param source : locator of the source element
	 * @param destination : locator of the destination element
	 */
	public CommonFunctionsExceptions(String source, String destination) 
	{
		CommonFunctions.glb_Logger_commonlogs.error("The source and destination locator values are null..source: "+source+" destination: "+destination);
	}
	/**
	 * @author mkarthik
	 * date: October 2nd
	 * date of review:
	 * Description: This exception is thrown when element cannot be located due to invalid locator startegy of invalid locator
	 * @param m_exeption
	 * @param m_locator_type
	 */
	public CommonFunctionsExceptions(Exceptions m_exeption, String m_locator_type) 
	{
		if(m_exeption==Exceptions.INVALID_LOCATOR_TYPE_EXCEPTION)
		{
			CommonFunctions.glb_Logger_commonlogs.error("Invalid locator type being sent...Locator type sent: "+m_locator_type);
		}
		else if (m_exeption==Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION) 
		{
			CommonFunctions.glb_Logger_commonlogs.error("Could not not be located using locator : "+m_locator_type);
		}
		// TODO Auto-generated constructor stub
	}
	/**
	 * @author mkarthik
	 * date: October 2nd
	 * date of review:
	 * Description: This exception gets called when the string sent to locate the element is null
	 * @param m_exeption
	 */
	public CommonFunctionsExceptions(Exceptions m_exeption) 
	{
		if(m_exeption==Exceptions.LOCATOR_VALUE_NULL_EXCEPTION)
		{
			CommonFunctions.glb_Logger_commonlogs.error("Locator value given is null...");
		}
		else if(m_exeption==Exceptions.NULL_VALUE_PASSED_EXCEPTION)
		{
			CommonFunctions.glb_Logger_commonlogs.error("NULL Value passed...");
		}
		else if(m_exeption==Exceptions.LOCATOR_FORMAT_EXCEPTION)
		{
			CommonFunctions.glb_Logger_commonlogs.error("Locator string is not of the expected format...");
		}
	}

		
}
