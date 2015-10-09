package com.aem.constants;

/**
 * 
 * @author mkarthik
 * 
 * This interface should have all such configurations which are constant throughout the test execution
 * 
 * Please add the decription for all the members declared and defined in this interface
 *
 */
public interface Aem_Constants
{
	static String logPath=System.getProperty("user.dir")+"/src/log4j.properties";
	static String logFileDestPath = System.getProperty("user.dir")+"/logs/Execution.log";
}
