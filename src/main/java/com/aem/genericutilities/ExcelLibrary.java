package com.aem.genericutilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aem.constants.DriverConstants;
import com.aem.keyword.driver.TestSuiteDriver;

/**
 * 
 * @author mkarthik
 * 
 * This class should have all the  functions which help in dealing with microsoft excel
 * 
 * please add description for all the methods defined and declared in this class
 *
 */

public class ExcelLibrary {
	//public static Logger glb_Logger_commonlogs=null;
	private static XSSFWorkbook glb_XSSFWorkbook_testSuiteWorkBook;
	private static XSSFSheet glb_XSSFSheet_testCaseWorkSheet;
	private static org.apache.poi.ss.usermodel.Cell glb_Cell_cell;
	private static XSSFRow glb_XSSFRow_row;
	private static XSSFCellStyle style = null;
	
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method will initialize XSSF workbook by configuring the test suite
	 * @param testSuitePath: Is the path of the spreadsheet where all the test cases and step exists.
	 * @throws Exception
	 */
	public static void setTestSuiteFile(String testSuitePath) throws Exception {
    	try {
            FileInputStream excelFile = new FileInputStream(testSuitePath);
            glb_XSSFWorkbook_testSuiteWorkBook = new XSSFWorkbook(excelFile);
    	} catch (Exception e){
    		TestSuiteDriver.glb_Logger_commonlogs.error("Class :: ExcelLibrary | Method :: setTestSuiteFile | Exception desc : " + e.getMessage());
    		TestSuiteDriver.glb_Boolean_testResult = false;
        	}
    	}
	
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method will get the row count based on the sheet name specified
	 * @param testSuitePath: Is the path of the spreadsheet where all the test cases and step exists.
	 * @throws Exception
	 */
	public static int getRowCount(String sheetName){
		int rowCount=0;
		try {
			System.out.println(sheetName);
			glb_XSSFSheet_testCaseWorkSheet = glb_XSSFWorkbook_testSuiteWorkBook.getSheet(sheetName);
			rowCount=glb_XSSFSheet_testCaseWorkSheet.getLastRowNum()+1;
		} catch (Exception e){
			TestSuiteDriver.glb_Logger_commonlogs.error("Class :: ExcelLibrary | Method :: getRowCount | Exception desc : " + e.getMessage());
			TestSuiteDriver.glb_Boolean_testResult = false;
			}
		return rowCount;
		}
	
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method will get the row that contains the test case name
	 * @param testCaseName: Test case name specified in test suite
	 * @param columnNumber: Column Number of the test case name as specified in test suite
	 * @param sheetName: Sheet name specified in test suite
	 * @throws Exception
	 */
	public static int getRowContains(String testCaseName, int columnNumber,String sheetName) throws Exception{
		int rowNumber=0;	
		try {
			int rowCount = ExcelLibrary.getRowCount(sheetName);
			for (; rowNumber<rowCount; rowNumber++){
				if  (ExcelLibrary.getCellData(rowNumber,columnNumber,sheetName).equalsIgnoreCase(testCaseName)){
					break;
				}
			}       			
		} catch (Exception e){
			TestSuiteDriver.glb_Logger_commonlogs.error("Class :: ExcelLibrary | Method :: getRowContains | Exception desc : " + e.getMessage());
			TestSuiteDriver.glb_Boolean_testResult = false;
			}
		return rowNumber;
		}
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method will get the test steps count based on test suite specification.
	 * @param testCaseId: Test case id specified in test suite
	 * @param testCaseStart: Starting test number
	 * @param sheetName: Sheet name specified in test suite
	 * @throws Exception
	 */
	public static int getTestStepsCount(String sheetName, String testCaseId, int testCaseStart) throws Exception{
		try {
    		for(int i=testCaseStart;i<ExcelLibrary.getRowCount(sheetName);i++){
    			if(!testCaseId.equals(ExcelLibrary.getCellData(i, DriverConstants.TEST_CASE_ID, sheetName))){
    				int number = i;
    				return number;      				
    				}
    			}
    		glb_XSSFSheet_testCaseWorkSheet = glb_XSSFWorkbook_testSuiteWorkBook.getSheet(sheetName);
    		int number=glb_XSSFSheet_testCaseWorkSheet.getLastRowNum()+1;
    		return number;
		} catch (Exception e){
			TestSuiteDriver.glb_Logger_commonlogs.error("Class :: ExcelLibrary | Method :: getTestStepsCount | Exception desc : " + e.getMessage());
			TestSuiteDriver.glb_Boolean_testResult = false;
			return 0;
        }
	}
	
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method will set the result in test suite
	 * @param result: Result of the test step or a test case
	 * @param rowNumber: Row number where the data need to be updated.
	 * @param columnNumber: Column number where the data need to be updated.
	 * @param sheetName: Sheet name specified in test suite
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public static void setCellData(String result,  int rowNumber, int columnNumber, String sheetName) throws Exception    {
           try{
        	   
        	   glb_XSSFSheet_testCaseWorkSheet = glb_XSSFWorkbook_testSuiteWorkBook.getSheet(sheetName);
        	   glb_XSSFRow_row  = glb_XSSFSheet_testCaseWorkSheet.getRow(rowNumber);
               glb_Cell_cell = glb_XSSFRow_row.getCell(columnNumber, glb_XSSFRow_row.RETURN_BLANK_AS_NULL);
               
               if (glb_Cell_cell == null) {
            	   glb_Cell_cell = glb_XSSFRow_row.createCell(columnNumber);
            	   glb_Cell_cell.setCellValue(result);  
            	   customCellStyle(result, glb_Cell_cell);

                } else {
                	glb_Cell_cell.setCellValue(result);
                	customCellStyle(result, glb_Cell_cell);
                	
                }
                 FileOutputStream fileOut = new FileOutputStream(DriverConstants.TEST_SUITE_PATH);
                 glb_XSSFWorkbook_testSuiteWorkBook.write(fileOut);
                 fileOut.close();
                 glb_XSSFWorkbook_testSuiteWorkBook = new XSSFWorkbook(new FileInputStream(DriverConstants.TEST_SUITE_PATH));
             }catch(Exception e){
            	 TestSuiteDriver.glb_Logger_commonlogs.error("Class :: ExcelLibrary | Method :: setCellData | Exception desc : " + e.getMessage());
            	 TestSuiteDriver.glb_Boolean_testResult = false;
      
             }
        }
	
	private static void customCellStyle(String result, Cell cell){
		style = glb_XSSFWorkbook_testSuiteWorkBook.createCellStyle();
 	   if(result.equalsIgnoreCase(DriverConstants.PASS)){
 		   style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
 	   }else if(result.equalsIgnoreCase(DriverConstants.FAIL)){
 		   style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
 	   }
 	   style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
	    	style.setBorderBottom((short)1);
	    	style.setBorderTop((short)1);
	    	style.setBorderLeft((short)1);
	    	style.setBorderRight((short)1);
	    	cell.setCellStyle(style);
	}
	/**
	 * @author Narendra Prasad
	 * date: October 26th 2015
	 * date of review: 
	 * Description: This method will get the data from a particular cell in excel spreadsheet.
	 * @param rowNumber: Row number from where the data need to be fetched.
	 * @param columnNumber: Column number from where the data need to be fetched.
	 * @param sheetName: Sheet name specified in test suite
	 * @throws Exception
	 */
	 public static String getCellData(int rowNumber, int columnNumber, String sheetName ) throws Exception{
         try{
        	 glb_XSSFSheet_testCaseWorkSheet = glb_XSSFWorkbook_testSuiteWorkBook.getSheet(sheetName);
        	 glb_Cell_cell = glb_XSSFSheet_testCaseWorkSheet.getRow(rowNumber).getCell(columnNumber);
             String CellData = null;
             
             int cellType = glb_Cell_cell.getCellType();
     		     		
     		if(cellType == XSSFCell.CELL_TYPE_NUMERIC){
     			CellData = String.valueOf(glb_Cell_cell.getNumericCellValue());
     		}
     		else if(cellType == XSSFCell.CELL_TYPE_STRING){
     			CellData = glb_Cell_cell.getStringCellValue();
    		}
     		return CellData;
     		
          }catch (Exception e){
        	  TestSuiteDriver.glb_Logger_commonlogs.error("Class :: ExcelLibrary | Method :: getCellData | Exception desc : " + e.getMessage());
              TestSuiteDriver.glb_Boolean_testResult = false;
              return "";
              }
          }

}
