package com.aem.keyword.driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.aem.constants.DriverConstants;
import com.aem.genericutilities.CommonFunctions;
import com.aem.genericutilities.CommonLogging;
import com.aem.genericutilities.ExcelLibrary;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author Narendra Prasad
 * date of creation:October 26th 2015
 * date of review:
 * Description: This Class contains methods to execute the functions matching the keywords as specified in the Test Suite data source
 */

public class TestSuiteDriver {
	public static ExtentReports glb_ExtentReports_reports = null;
	public static CommonFunctions glb_CommonFunctions_commonfunctions;
	public static Method glb_Method_method[];
	public static boolean glb_Boolean_testResult;
	public static String glb_String_testCaseName;
	public static Properties glb_Properties_objectRepository;
	public static Logger glb_Logger_commonlogs=null;
	public static String glb_String_runMode;
	public static String glb_String_data;
	public static int glb_Int_testStep;
	public static int glb_Int_testLastStep;
	public static String glb_String_actionKeyword;
	public static String glb_String_pageObject;
	public static ExtentTest glb_ExtentTest_test=null;
	public static String glb_String_testCaseDescription=null;
	public static String glb_String_testStepDescription=null;
	public static int glb_Int_failCount =0;
	public static String glb_String_failureMessage = null;
	
	
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method is main method of data driven framework, it drives data driven framework by invoking all common functions by using the external data source.
	 * @param args: args is array of strings, which will be helpful, whenever command line arguments need to be captured.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		glb_CommonFunctions_commonfunctions = new CommonFunctions();		
		glb_Method_method = glb_CommonFunctions_commonfunctions.getClass().getMethods();	
		glb_Logger_commonlogs=CommonLogging.getLogObj(TestSuiteDriver.class);
	
		try {
			ExcelLibrary.setTestSuiteFile(DriverConstants.TEST_SUITE_PATH);
		} catch (Exception e) {
			glb_Logger_commonlogs.error("Class :: TestSuiteDriver | Method :: main | Exception desc : " + e.getMessage());
		}
    	String objectRepositoryPath = DriverConstants.OBJECT_REPOSITORY_PATH;
		FileInputStream fs;
		try {
			fs = new FileInputStream(objectRepositoryPath);
			glb_Properties_objectRepository= new Properties(System.getProperties());
			glb_Properties_objectRepository.load(fs);
		} catch (IOException e) {
			glb_Logger_commonlogs.error("Class :: TestSuiteDriver | Method :: main | Exception desc : " + e.getMessage());
		}
		
		TestSuiteDriver.executeTestCase();
		
	}
	
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method decides whether the test case need to be executed or not based on the information available in external data source 
	 * 				Also this method fetches the other data required from spreadsheet for utilization by the methods that are invoked from this method.
	 * parameters : It is a zero parameterized method
	 * @throws Exception
	 */
	private static void executeTestCase() throws Exception  {
		glb_ExtentReports_reports = new ExtentReports(DriverConstants.REPORT_PATH);
    	int totalTestCases = ExcelLibrary.getRowCount(DriverConstants.TEST_CASES_SHEET);
    	for(int testCaseNumber=1;testCaseNumber<totalTestCases;testCaseNumber++){
			glb_Boolean_testResult = true;
			glb_String_testCaseName = ExcelLibrary.getCellData(testCaseNumber, DriverConstants.TEST_CASE_ID, DriverConstants.TEST_CASES_SHEET); 
			glb_String_runMode = ExcelLibrary.getCellData(testCaseNumber, DriverConstants.RUN_MODE,DriverConstants.TEST_CASES_SHEET);
			glb_String_testCaseDescription = ExcelLibrary.getCellData(testCaseNumber, DriverConstants.TEST_SCENARIO_DESCRIPTION,DriverConstants.TEST_CASES_SHEET);
			
			if (glb_String_runMode.equals(DriverConstants.YES)){
				glb_Logger_commonlogs.info(glb_String_testCaseName + " test case execution started...");
				glb_ExtentTest_test = glb_ExtentReports_reports.startTest(glb_String_testCaseName, glb_String_testCaseDescription);
				glb_Int_testStep = ExcelLibrary.getRowContains(glb_String_testCaseName, DriverConstants.TEST_CASE_ID, DriverConstants.TEST_STEPS_SHEET);
				glb_Int_testLastStep = ExcelLibrary.getTestStepsCount(DriverConstants.TEST_STEPS_SHEET, glb_String_testCaseName, glb_Int_testStep);
				glb_Boolean_testResult=true;
				for (;glb_Int_testStep<glb_Int_testLastStep;glb_Int_testStep++){
		    		glb_String_actionKeyword = ExcelLibrary.getCellData(glb_Int_testStep, DriverConstants.ACTION_KEYWORD,DriverConstants.TEST_STEPS_SHEET);
		    		glb_String_pageObject = ExcelLibrary.getCellData(glb_Int_testStep, DriverConstants.PAGE_OBJECT, DriverConstants.TEST_STEPS_SHEET);
		    		glb_String_data = ExcelLibrary.getCellData(glb_Int_testStep, DriverConstants.DATA_SET, DriverConstants.TEST_STEPS_SHEET);
		    		glb_String_testStepDescription = ExcelLibrary.getCellData(glb_Int_testStep, DriverConstants.TEST_STEP_DESCRIPTION, DriverConstants.TEST_STEPS_SHEET);
		    		execute_Actions();
					if(glb_Boolean_testResult==false){
						ExcelLibrary.setCellData(DriverConstants.FAIL,testCaseNumber,DriverConstants.RESULT,DriverConstants.TEST_CASES_SHEET);
						glb_Logger_commonlogs.info(glb_String_testCaseName + " test case execution is complete...");
						break;
						}						
					}
				if(glb_Boolean_testResult==true){
				ExcelLibrary.setCellData(DriverConstants.PASS,testCaseNumber,DriverConstants.RESULT,DriverConstants.TEST_CASES_SHEET);	
				glb_Logger_commonlogs.info(glb_String_testCaseName + " test case execution is complete...");
				}		
		    	glb_ExtentReports_reports.endTest(glb_ExtentTest_test);
				}
			}
		glb_ExtentReports_reports.flush();
		CommonFunctions.closeBrowser(DriverConstants.DOUBLE_QUOTES,DriverConstants.DOUBLE_QUOTES);
		}	
	
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method triggers the actual verification method based the data provided in external data source 
	 * 				and updates the step results in Extent Reports as well as Test Suite Spreadsheet.
	 * parameters : It is a zero parameterized method
	 * @throws Exception
	 */
	private static void execute_Actions() throws Exception {
			
			for(int i=0;i<glb_Method_method.length;i++){				
				if(glb_Method_method[i].getName().equals(glb_String_actionKeyword)){
					
					glb_Method_method[i].invoke(glb_CommonFunctions_commonfunctions,glb_String_pageObject, glb_String_data);
					
					if(glb_Boolean_testResult==true){
						ExcelLibrary.setCellData(DriverConstants.PASS,glb_Int_testStep,DriverConstants.TEST_STEP_RESULT, DriverConstants.TEST_STEPS_SHEET);
						glb_ExtentTest_test.log(LogStatus.PASS, glb_String_actionKeyword, glb_String_testStepDescription);
						break;
					}else{
						ExcelLibrary.setCellData(DriverConstants.FAIL, glb_Int_testStep, DriverConstants.TEST_STEP_RESULT, DriverConstants.TEST_STEPS_SHEET);
						String path = DriverConstants.FAILURE_IMAGE_PATH + DriverConstants.FAILURE_FILE_PREFIX + glb_Int_failCount + DriverConstants.FAILURE_FILE_EXTENSION;
						System.out.println(path);
					
						String image = glb_ExtentTest_test.addScreenCapture(path);
						glb_ExtentTest_test.log(LogStatus.FAIL, glb_String_actionKeyword, glb_String_testStepDescription + DriverConstants.SPACE + image + DriverConstants.SPACE + glb_String_failureMessage);
						
						CommonFunctions.closeBrowser(DriverConstants.DOUBLE_QUOTES,DriverConstants.DOUBLE_QUOTES);
						glb_Int_failCount++;
						break;
						}
					}
			
				}
	     }

}
