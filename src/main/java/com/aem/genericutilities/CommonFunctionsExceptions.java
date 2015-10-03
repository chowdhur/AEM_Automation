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
		System.out.println("The URL parameter is null.."+url);
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
		System.out.println("The URL parameter is.."+url+" and response code is: "+b);
	}
	/**
	 * @author mkarthik
	 * date of creation : October 2nd
	 * date of review:
	 * Description : This exception is used when webdriver object is null
	 */
	public CommonFunctionsExceptions()
	{
		System.out.println("The webdriver is not initialized and is null..");
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
		System.out.println("The source and destination locator values are null..source: "+source+" destination: "+destination);
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
			System.out.println("Invalid locator type being sent...Locator type sent: "+m_locator_type);
		}
		else if (m_exeption==Exceptions.COULD_NOT_LOCATE_ELEMENT_EXCEPTION) 
		{
			System.out.println("Could not not be located using locator : "+m_locator_type);
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
			System.out.println("Locator value given is null...");
		}
	}

		
}
