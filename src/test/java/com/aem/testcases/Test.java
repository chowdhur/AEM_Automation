package com.aem.testcases;

import com.aem.genericutilities.CommonFunctions;
import com.aem.genericutilities.CommonFunctionsExceptions;

public class Test {

	static CommonFunctions comm;
	/**
	 * @param args
	 * @throws CommonFunctionsExceptions 
	 */
	public static void main(String[] args) throws CommonFunctionsExceptions {
		// TODO Auto-generated method stub
		try{
			comm=new CommonFunctions();
		comm.openApp("","https://accounts.google.com/ServiceLogin?continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&ltmpl=default&service=mail&sacu=1&scc=1&passive=1209600&acui=0#Email=karthik.m.manjunath%40gmail.com");
		//comm.enterText("xpath:.//*[@id='Passwd']","admin");
		comm.getAttributeAndCompare("xpath:.//*[@id='Passwd']","name,Passwd");
		
		System.out.println("done..");
		}
		finally{
			comm.closeBrowser("","");
			comm.glb_Webdriver_driver.quit();
		}
		
	}

}
