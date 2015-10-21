package com.aem.testcases;

import org.openqa.selenium.By;

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
		comm.navigateToLink("","https://login.yahoo.com/");
		comm.refreshPage();
		//comm.enterText("xpath:.//*[@id='Passwd']","admin");
		//comm.getAttributeAndCompare("xpath:.//*[@id='Passwd']","name,Passwd");
		//comm.isElementDisplayed("xpath:.//*[@id='Passssswd']", "");
		//comm.isElementEnabled("xpath:.//*[@id='Passswd']", "");
    	//comm.glb_Webdriver_driver.findElement(By.xpath("//label[contains(text(),'Keep me')]/preceding-sibling::input[@class='checkbox']")).click();
    	System.out.println("state"+ comm.isElementSelected("xpath://input[@class='checkbox']",""));
		
		System.out.println("done..");
		}
		finally{
			comm.closeBrowser("","");
			comm.glb_Webdriver_driver.quit();
		}
		
	}

}
