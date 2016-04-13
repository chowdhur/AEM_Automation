package com.aem.keyword.driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import com.aem.constants.DriverConstants;
import com.aem.genericutilities.ExcelReader;

public class AutoPopulateObjectsInExcel {
	private static SequencedProperties glb_Properties_objectRepository = null;
	
	public static void main(String[] args) throws Exception {
		ExcelReader excel = new ExcelReader(DriverConstants.TEST_SUITE_PATH );
		
		String objectRepositoryPath = DriverConstants.OBJECT_REPOSITORY_PATH;
		FileInputStream fs;
		try {
			fs = new FileInputStream(objectRepositoryPath);
			glb_Properties_objectRepository= new SequencedProperties();
			glb_Properties_objectRepository.load(fs);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		
		Enumeration e  = glb_Properties_objectRepository.keys();
		//Iterator iterator = iterable.iterator();
		
		int rowNum = 1;
		while(e.hasMoreElements()){
			String value = e.nextElement().toString();
			//System.out.println(value);
			
			if((value.contains("txt")) || (value.contains("text"))){
				excel.setCellData("Test Steps", rowNum, "Action Keyword", "input");
			}else if((value.contains("btn")) || (value.contains("button")) || (value.contains("link")) || (value.contains("checkbox"))){
				excel.setCellData("Test Steps", rowNum, "Action Keyword", "click");
			}else if(value.contains("dropdown")){
				excel.setCellData("Test Steps", rowNum, "Action Keyword", "selectByVisibleText");
			}
			excel.setCellData("Test Steps", rowNum, "Page Object", value);
			
			rowNum++;
		}
		
	}

}

class SequencedProperties extends Properties {

    private List keyList = new ArrayList();

    @Override
    public synchronized Enumeration keys() {
        return Collections.enumeration(keyList);
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        if (! containsKey(key)) {
            keyList.add(key);
        }

        return super.put(key, value);
    }

    @Override
    public synchronized Object remove(Object key) {
        keyList.remove(key);

        return super.remove(key);
    }

    @Override
    public synchronized void putAll(Map values) {
        for (Object key : values.keySet()) {
            if (! containsKey(key)) {
                keyList.add(key);
            }
        }

        super.putAll(values);
    }
}
