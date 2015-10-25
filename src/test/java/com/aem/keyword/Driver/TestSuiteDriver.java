package com.aem.keyword.Driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.aem.constants.DriverConstants;
import com.aem.genericutilities.CommonFunctions;
import com.aem.genericutilities.ExcelLibrary;

/**
 * 
 * @author Narendra Prasad
 * date of creation:October 26th 2015
 * date of review:
 * Description: This Class contains methods to execute the functions matching the keywords as specified in the Test Suite data source
 */

public class TestSuiteDriver {

	public static CommonFunctions commonfunctions;
	public static Method method[];
	public static boolean glb_Boolean_testResult;
	public static String glb_String_testCaseId;
	public static Properties glb_Properties_objectRepository;
	public static Logger glb_Logger_commonlogs=null;
	public static String glb_String_runMode;
	public static String glb_String_data;
	public static int glb_Int_testStep;
	public static int glb_Int_testLastStep;
	public static String glb_String_actionKeyword;
	public static String glb_String_pageObject;
	
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * parameters : It is a zero parameterized constructor
	 * Description: This method will instantiate common functions and method classes
	 * @throws NoSuchMethodException, SecurityException
	 */
	public TestSuiteDriver() throws NoSuchMethodException, SecurityException{
		commonfunctions = new CommonFunctions();
		method = commonfunctions.getClass().getMethods();	
	}
	
	
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method is main method of data driven framework, it drives data driven framework by invoking all common functions by using the external data source.
	 * @param args: args is array of strings, which will be helpful, whenever command line arguments need to be captured.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
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
		
		TestSuiteDriver suiteDriver = new TestSuiteDriver();
		suiteDriver.executeTestCase();
		
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
	private void executeTestCase() throws Exception  {
    	int totalTestCases = ExcelLibrary.getRowCount(DriverConstants.TEST_CASES_SHEET);
    	for(int testCaseNumber=1;testCaseNumber<totalTestCases;testCaseNumber++){
			glb_Boolean_testResult = true;
			glb_String_testCaseId = ExcelLibrary.getCellData(testCaseNumber, DriverConstants.TEST_CASE_ID, DriverConstants.TEST_CASES_SHEET); 
			glb_String_runMode = ExcelLibrary.getCellData(testCaseNumber, DriverConstants.RUN_MODE,DriverConstants.TEST_CASES_SHEET);
			if (glb_String_runMode.equals(DriverConstants.YES)){
				//glb_Logger_commonlogs.info("******** " + glb_String_testCaseId + " start...");
				glb_Int_testStep = ExcelLibrary.getRowContains(glb_String_testCaseId, DriverConstants.TEST_CASE_ID, DriverConstants.TEST_STEPS_SHEET);
				glb_Int_testLastStep = ExcelLibrary.getTestStepsCount(DriverConstants.TEST_STEPS_SHEET, glb_String_testCaseId, glb_Int_testStep);
				glb_Boolean_testResult=true;
				for (;glb_Int_testStep<glb_Int_testLastStep;glb_Int_testStep++){
		    		glb_String_actionKeyword = ExcelLibrary.getCellData(glb_Int_testStep, DriverConstants.ACTION_KEYWORD,DriverConstants.TEST_STEPS_SHEET);
		    		glb_String_pageObject = ExcelLibrary.getCellData(glb_Int_testStep, DriverConstants.PAGE_OBJECT, DriverConstants.TEST_STEPS_SHEET);
		    		glb_String_data = ExcelLibrary.getCellData(glb_Int_testStep, DriverConstants.DATA_SET, DriverConstants.TEST_STEPS_SHEET);
		    		execute_Actions();
					if(glb_Boolean_testResult==false){
						ExcelLibrary.setCellData(DriverConstants.FAIL,testCaseNumber,DriverConstants.RESULT,DriverConstants.TEST_CASES_SHEET);
						glb_Logger_commonlogs.info("******** " + glb_String_testCaseId + " end...");
						break;
						}						
					}
				if(glb_Boolean_testResult==true){
				ExcelLibrary.setCellData(DriverConstants.PASS,testCaseNumber,DriverConstants.RESULT,DriverConstants.TEST_CASES_SHEET);
			//	glb_Logger_commonlogs.info("******** " + glb_String_testCaseId + " end...");
					}					
				}
			}
		}	
	
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method triggers the actual verification method based the data provided in external data source and updates the step results.
	 * parameters : It is a zero parameterized method
	 * @throws Exception
	 */
	private static void execute_Actions() throws Exception {
			
			for(int i=0;i<method.length;i++){				
				if(method[i].getName().equals(glb_String_actionKeyword)){
					method[i].invoke(commonfunctions,glb_String_pageObject, glb_String_data);
					if(glb_Boolean_testResult==true){
						ExcelLibrary.setCellData(DriverConstants.PASS,glb_Int_testStep,DriverConstants.TEST_STEP_RESULT, DriverConstants.TEST_STEPS_SHEET);
						break;
					}else{
						ExcelLibrary.setCellData(DriverConstants.FAIL, glb_Int_testStep, DriverConstants.TEST_STEP_RESULT, DriverConstants.TEST_STEPS_SHEET);
						CommonFunctions.closeBrowser("","");
						break;
						}
					}
				}
	     }

}
