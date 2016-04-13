package com.aem.keyword.driver;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;

import com.aem.constants.DriverConstants;
import com.aem.genericutilities.CommonFunctions;
import com.aem.genericutilities.ExcelReader;

public class GetKeywords {

	public static void main(String[] args) throws Exception {
		
		/*URL location = GetKeywords.class.getProtectionDomain().getCodeSource().getLocation();
		String pro = location.getFile();
		System.out.println("prop: " + pro);	
		String path = pro.substring(0, pro.lastIndexOf("/GetKeywords.jar")); //for excel macro
		//String path = pro.substring(0, pro.lastIndexOf("/target")); //for eclipse
		System.out.println("Path: " + path );		
		
		String tempKeywordExcel = "Keywords.xlsx";*/
		
		/*File file = new File(path + "/" + tempKeywordExcel);					
		//if excel does not exist then create new 
		if(!file.exists()){
			new ExcelReader().createNewExcel(path + "/" + tempKeywordExcel);
		}*/
		
		//ExcelReader excel = new ExcelReader(path + "/src/test/resources/com/aem/datasource/" + tempKeywordExcel); //for eclipse
		//ExcelReader excel = new ExcelReader(path + "/" + tempKeywordExcel); //for excel macro
		ExcelReader excel = new ExcelReader(DriverConstants.TEST_SUITE_PATH );
		
		Class tClass = new CommonFunctions().getClass();
		Method[] methods = tClass.getMethods();
		int lastRow = excel.getSheet("Settings").getLastRowNum();
		
		//clearing row data
		int row = 1;
		for(int i=0; i<=lastRow; i++){
			excel.setCellData("Settings", row, "Action Keywords", "");
			row = row + 1;
		}
		
		String[] arr = new String[methods.length];
		
		//inserting keywords in the excel
		
		for (int i = 0; i < methods.length; i++) {
			//System.out.println(methods[i].getName());
			arr[i] = methods[i].getName();
		}
		
		Arrays.sort(arr);
		
		row = 1;
		for (int i = 0; i < methods.length; i++) {
			//System.out.println(arr[i]);
			excel.setCellData("Settings", row, "Action Keywords", arr[i]);
			row = row + 1;
		}

	}

}