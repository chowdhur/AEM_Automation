package com.aem.application;

/**
 * 
 * @author mkarthik
 * 
 * This interface should have all such configurations which are constant throughout the test execution.
 * The members added here should be specific to application under test
 * 
 * Please add the decription for all the members declared and defined in this interface
 *
 */
public interface Application_Constants 
{
	String interf_Webdriver_driverType="firefox";
	long explicit_wait_value=10;
	long implict_wait_value=10;
	long page_load_timeout_value=10;
}
