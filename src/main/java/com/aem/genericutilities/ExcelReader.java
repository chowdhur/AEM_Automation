package com.aem.genericutilities;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.support.Colors;

public class ExcelReader {

	XSSFWorkbook wb = null;
	XSSFSheet sheet = null;
	XSSFRow row = null;
	String excelPath;
	XSSFCell cell = null;
	FileOutputStream fileOut =null;
	FileInputStream fileIn = null;
	XSSFCellStyle style = null;
	XSSFFont font = null;
	
	public ExcelReader(String path) throws Exception{
		excelPath = path;
		fileIn = new FileInputStream(path);
		wb = new XSSFWorkbook(fileIn);

	}
	
	public ExcelReader(){
		
	}

	public int rowCount(String sheetName){
		sheet = wb.getSheet(sheetName);
		if(sheet == null){
			System.out.println("Invalid sheet name: " + sheetName);
			return 0;
		}
		int lastRowNum = sheet.getLastRowNum();
		//System.out.println(sheet.getFirstRowNum()); // 0
		int totalRows = lastRowNum + 1; //row num starts with 0
		return totalRows;

	}

	public int columnCount(String sheetName){
		sheet = wb.getSheet(sheetName);
		if(sheet == null){
			System.out.println("Invalid sheet name: " + sheetName);
			return 0;
		}
		row = sheet.getRow(0);
		int totalCols = row.getLastCellNum();
		return totalCols;
	}

	public String getCellData(String sheetName, int rowNum, String colName){
		
		sheet = wb.getSheet(sheetName);
		if(sheet == null){
			return "Invalid sheet name: " + sheetName;
		}
		
		if(rowNum > sheet.getLastRowNum()+1){
			return "Invalid row num: " + rowNum;
		}
		
		row = sheet.getRow(0);
		int colNum = -1;
		XSSFCell cell = null;	
		
		for(int i=0; i<row.getLastCellNum(); i++){
			cell = row.getCell(i);
			if(colName.equalsIgnoreCase(cell.getStringCellValue())){
				colNum = i;

			}
		}

		if(colNum == -1){
			return "No data found. Invalid column name: " + colName;
		}
		
		row = sheet.getRow(rowNum-1);
		cell = row.getCell(colNum);
		int cellType = cell.getCellType();
		String data = null ;
		if(cellType == XSSFCell.CELL_TYPE_NUMERIC){
			data = String.valueOf(cell.getNumericCellValue());

		}

		else if(cellType == XSSFCell.CELL_TYPE_STRING){
			data = cell.getStringCellValue();
		}

		return data;

	}
	
public String getCellData(String sheetName, int rowNum, int colNum){
		
		sheet = wb.getSheet(sheetName);
		if(sheet == null){
			return "Invalid sheet name: " + sheetName;
		}
		
		if(rowNum > sheet.getLastRowNum()+1){
			return "Invalid row num: " + rowNum;
		}
		
		row = sheet.getRow(0);
		XSSFCell cell = null;	

		if(colNum == -1){
			return "No data found. Invalid column Num: " + colNum;
		}
		
		row = sheet.getRow(rowNum-1);
		cell = row.getCell(colNum);
		int cellType = cell.getCellType();
		String data = null ;
		
		if(cellType == XSSFCell.CELL_TYPE_NUMERIC){
			data = String.valueOf(cell.getNumericCellValue());
		}

		else if(cellType == XSSFCell.CELL_TYPE_STRING){
			data = cell.getStringCellValue();
		}

		return data;

	}
	
	public String setCellData(String sheetName, int rowNum, String colName, String data) throws Exception{
		sheet = wb.getSheet(sheetName);
		if(sheet == null){
			wb.createSheet(sheetName);
		}
		
		/*if(rowNum > sheet.getLastRowNum()+1){
			return "Invalid row num: " + rowNum;
		}*/
		
		row = sheet.getRow(0);
		int colNum = -1;
		XSSFCell cell = null;	
		
		for(int i=0; i<row.getLastCellNum(); i++){
			cell = row.getCell(i);
			if(colName.equalsIgnoreCase(cell.getStringCellValue())){
				colNum = i;

			}

		}

		if(colNum == -1){
			System.out.println("Invalid column name: " + colName);
		}
		
		row = sheet.getRow(rowNum);
		if (row == null)
			row = sheet.createRow(rowNum);
		
		cell = row.getCell(colNum);
		if (cell == null)
	        cell = row.createCell(colNum);
		
	    cell.setCellValue(data);
	    XSSFCellStyle style = wb.createCellStyle();
	    //style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
	    style.setBorderBottom((short)1);
	    style.setBorderTop((short)1);
	    style.setBorderLeft((short)1);
	    style.setBorderRight((short)1);
	    //style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    cell.setCellStyle(style);


	    // Write the output to a file
	    FileOutputStream fileOut = new FileOutputStream(excelPath);
	    wb.write(fileOut);
	    fileOut.close();
		
		return "Successfully written to excel.";
	}
	public String setCellData(String sheetName, int rowNum, int colNum, String data) throws Exception{
		sheet = wb.getSheet(sheetName);
		if(sheet == null){
			wb.createSheet(sheetName);
		}
		
		/*if(rowNum > sheet.getLastRowNum()+1){
			return "Invalid row num: " + rowNum;
		}*/
		
		row = sheet.getRow(rowNum);
		if (row == null)
			row = sheet.createRow(rowNum);
		
		cell = row.getCell(colNum);	
		if (cell == null)
	        cell = row.createCell(colNum);
		
		sheet.autoSizeColumn(colNum);
		
	    cell.setCellValue(data);
	    style = wb.createCellStyle();
	    style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
	    style.setBorderBottom((short)1);
	    style.setBorderTop((short)1);
	    style.setBorderLeft((short)1);
	    style.setBorderRight((short)1);
	    //style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    cell.setCellStyle(style);


	    // Write the output to a file
	    fileOut = new FileOutputStream(excelPath);
	    wb.write(fileOut);
	    fileOut.close();
		
		return "Successfully written to excel.";
	}
	
	public String setCellDataWithFormat(String sheetName, int rowNum, int colNum, String data) throws Exception{
		sheet = wb.getSheet(sheetName);
		if(sheet == null){
			wb.createSheet(sheetName);
		}
		
		/*if(rowNum > sheet.getLastRowNum()+1){
			return "Invalid row num: " + rowNum;
		}*/
		
		row = sheet.getRow(rowNum);
		if (row == null)
			row = sheet.createRow(rowNum);
		
		cell = row.getCell(colNum);	
		if (cell == null)
	        cell = row.createCell(colNum);
		
		sheet.autoSizeColumn(colNum);
		cell.setCellValue(data);
	     
	    //Set style to data cell
	    style = wb.createCellStyle();
	    style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    font = wb.createFont();
	    font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	    font.setFontName("Calibri (Body)");
	    font.setFontHeightInPoints((short)11);
	    style.setBorderBottom((short)1);
	    style.setBorderTop((short)1);
	    style.setBorderLeft((short)1);
	    style.setBorderRight((short)1);
	    style.setFont(font);
	    cell.setCellStyle(style);

	    // Write the output to a file
	    fileOut = new FileOutputStream(excelPath);
	    wb.write(fileOut);
	    fileOut.close();
		
		return "Successfully written to excel.";
	}
	
	public String setCellDataWithFormat1(String sheetName, int rowNum, int colNum, String data) throws Exception{
		sheet = wb.getSheet(sheetName);
		if(sheet == null){
			wb.createSheet(sheetName);
		}
		
		/*if(rowNum > sheet.getLastRowNum()+1){
			return "Invalid row num: " + rowNum;
		}*/
		
		row = sheet.getRow(rowNum);
		if (row == null)
			row = sheet.createRow(rowNum);
		
		cell = row.getCell(colNum);	
		if (cell == null)
	        cell = row.createCell(colNum);
		
		sheet.autoSizeColumn(colNum);
	    cell.setCellValue(data);
	    
	    //Set style to data cell
	    style = wb.createCellStyle();
	    style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    style.setBorderBottom((short)1);
	    style.setBorderTop((short)1);
	    style.setBorderLeft((short)1);
	    style.setBorderRight((short)1);
	    font = wb.createFont();
	    font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	    style.setFont(font);
	    cell.setCellStyle(style);

	    // Write the output to a file
	    fileOut = new FileOutputStream(excelPath);
	    wb.write(fileOut);
	    fileOut.close();
		
		return "Successfully written to excel.";
	}
	
	public String setCellDataWithFormat2(String sheetName, int rowNum, int colNum, String data) throws Exception{
		sheet = wb.getSheet(sheetName);
		
		if(sheet == null){
			wb.createSheet(sheetName);
		}
		
		/*if(rowNum > sheet.getLastRowNum()+1){
			return "Invalid row num: " + rowNum;
		}*/
		
		row = sheet.getRow(rowNum);
		if (row == null)
			row = sheet.createRow(rowNum);
		
		cell = row.getCell(colNum);	
		if (cell == null)
	        cell = row.createCell(colNum);
		
		sheet.autoSizeColumn(colNum);
	    cell.setCellValue(data);
	    
	    //Set style to data cell
	    style = wb.createCellStyle();
	    style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    font = wb.createFont();
	    font.setFontName("Calibri (Body)");
	    font.setFontHeightInPoints((short)10);
	    font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	    style.setBorderBottom((short)1);
	    style.setBorderTop((short)1);
	    style.setBorderLeft((short)1);
	    style.setBorderRight((short)1);
	    style.setFont(font);
	    cell.setCellStyle(style);

	    // Write the output to a file
	    fileOut = new FileOutputStream(excelPath);
	    wb.write(fileOut);
	    fileOut.close();
		
		return "Successfully written to excel.";
	}
	
	public String setCellDataWithFormat3(String sheetName, int rowNum, int colNum, String data) throws Exception{
		sheet = wb.getSheet(sheetName);
		if(sheet == null){
			wb.createSheet(sheetName);
		}
		
		/*if(rowNum > sheet.getLastRowNum()+1){
			return "Invalid row num: " + rowNum;
		}*/
		
		row = sheet.getRow(rowNum);
		if (row == null)
			row = sheet.createRow(rowNum);
		
		cell = row.getCell(colNum);	
		if (cell == null)
	        cell = row.createCell(colNum);
		
		sheet.autoSizeColumn(colNum);
		cell.setCellValue(data);
	     
	    //Set style to data cell
	    style = wb.createCellStyle();
	    style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    font = wb.createFont();
	    font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	    style.setBorderBottom((short)1);
	    style.setBorderTop((short)1);
	    style.setBorderLeft((short)1);
	    style.setBorderRight((short)1);
	    style.setFont(font);
	    cell.setCellStyle(style);

	    // Write the output to a file
	    fileOut = new FileOutputStream(excelPath);
	    wb.write(fileOut);
	    fileOut.close();
		
		return "Successfully written to excel.";
	}
	
	public void removeExcelData(String sheetName){
		sheet = wb.getSheet(sheetName);
		
		for (int i=0; i<sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			if(row==null){
				continue;
			}
		   sheet.removeRow(row);
		}	
	}

	public void createNewExcel(String newExcelFilePath) throws Exception {
		
		XSSFWorkbook wb1 = new XSSFWorkbook();
		//wb1.createSheet("train");
		FileOutputStream fileOut1 = new FileOutputStream(newExcelFilePath);
		wb1.write(fileOut1);
		fileOut1.flush();
		fileOut1.close();
			
	}

	public XSSFSheet getSheet(String sheetName) {
		XSSFSheet sheet = wb.getSheet(sheetName);
		if(sheet == null){
			System.out.println("Sheet- " + sheetName + " is not present.");
		}
		return sheet;
	}

	public void createSheet(String sheetName) {
		try{
			wb.createSheet(sheetName);
		}catch(Exception e){
			
		}
	}
}