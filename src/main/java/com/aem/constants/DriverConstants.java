package com.aem.constants;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.NetworkMode;

/**
 * 
 * @author Narendra Prasad
 * 
 * This class should have all the constants declared related to keyword driver
 * 
 * please add description for all the methods defined and declared in this class
 *
 */
public class DriverConstants {
	
	//Keyword Driver Constants
	public static final String TEST_SUITE_PATH=System.getProperty("user.dir")+"/src/test/resources/com/aem/datasource/TestSuite.xlsx";
	public static final String OBJECT_REPOSITORY_PATH=System.getProperty("user.dir")+"/src/main/resources/com/aem/objectRepository/ObjectRepository.properties";
	public static final String FAIL = "FAIL";
	public static final String PASS = "PASS";
	public static final String YES = "Yes";
	
	// Test Suite Sheet Constants
	public static final String TEST_STEPS_SHEET = "Test Steps";
	public static final String TEST_CASES_SHEET = "Test Cases";
	public static final String FAILURE_FILE_PREFIX = "FAILURE00";
	public static final String FAILURE_FILE_EXTENSION = ".jpg";
	public static final String FAILURE_FILE_FORMAT = "JPG";
	public static final String DOUBLE_QUOTES = "";
	public static final String SPACE = " ";
	
	
	
	//Data Sheet Column Numbers
	public static final int TEST_CASE_ID = 0;	
	public static final int TEST_SCENARIO_DESCRIPTION = 1 ;
	public static final int PAGE_OBJECT =4 ;
	public static final int TEST_STEP_DESCRIPTION =2 ;
	public static final int ACTION_KEYWORD =5 ;
	public static final int RUN_MODE =2 ;
	public static final int RESULT =3 ;
	public static final int DATA_SET =6 ;
	public static final int TEST_STEP_RESULT =7 ;
	
	public static final String REPORT_PATH  = System.getProperty("user.dir")+"/target/extent-reports/AEMTestResults.html" ;
	//public static final String REPORT_PATH  = System.getProperty("user.dir")+"/src/test/resources/com/aem/datasource/test.html" ;
	public static final String FAILURE_IMAGE_PATH  = System.getProperty("user.dir")+"/target/extent-reports/" ;
	//public static final String FAILURE_IMAGE_PATH  = System.getProperty("user.dir")+"/src/test/resources/com/aem/datasource/Fail.png" ;
	public static final boolean REPLACE_EXISTING =  false;
	public static final DisplayOrder DISPLAY_ORDER = DisplayOrder.NEWEST_FIRST ;
	public static final NetworkMode NETWORK_MODE = NetworkMode.OFFLINE;
	
	
	
}
