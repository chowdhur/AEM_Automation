package com.aem.genericutilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.aem.constants.Aem_Constants;

/**
 * 
 * @author mkarthik
 * 
 * This class should have all the functions which are related to logging using log4j
 * 
 * please add description for all the methods defined and declared in this class
 *
 */
public class CommonFunctionsLogging 
{
	public static Logger glb_Logger_commonFunctionLog=null;
	
	public static Logger getLogObj(Class className)
	{
		if(glb_Logger_commonFunctionLog==null)
		{
			glb_Logger_commonFunctionLog=Logger.getLogger(className);
			Properties m_Properties_props = new Properties();
			try {
				m_Properties_props.load(new FileInputStream(Aem_Constants.logPath));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PropertyConfigurator.configure(m_Properties_props);
		}
		return glb_Logger_commonFunctionLog;
	}
}
