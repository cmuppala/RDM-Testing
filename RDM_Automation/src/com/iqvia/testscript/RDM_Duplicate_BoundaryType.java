package com.iqvia.testscript;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.html.applets.AppletClassLoader;
import com.iqvia.lib.ApplicationUtility;
import com.iqvia.lib.UtilLib;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class RDM_Duplicate_BoundaryType {

	//Starting the report generation
	String currentTime = UtilLib.systemTimeStamp();
	public static UtilLib util = new UtilLib();
	public static String TestCase_Name = "RDM_Duplicate_BoundaryType";
	ExtentReports report;


	@BeforeMethod
	public void reportconfig() throws SAXException, IOException, ParserConfigurationException {
		report=UtilLib.Instance(TestCase_Name);
		report.config().documentTitle("RDM Duplicate boundary type scenario");
		report.config().reportHeadline("RDM Duplicate boundary type scenario");
	}


	@Test
	public void rdm_Duplicate_PanelMember() throws Exception{
		ExtentTest logger = report.startTest("RDM Duplicate boundary type");

		String TestdataName = util.getPropertiesValue("TestData");
		String xlFilePath=System.getProperty("user.dir")+"/RDM_TestData/"+TestdataName;
		String UrlLaunch = util.getPropertiesValue("RDM_URL");
		String Username_property = util.getPropertiesValue("RDM_Login_UserName");
		String Password_property = util.getPropertiesValue("RDM_Password");
		String sheet = "Non_GeoBrick";


		ApplicationUtility.RDM_Login(report, logger, UrlLaunch, Username_property, Password_property);
		ApplicationUtility.RDM_Duplicate_BoundaryType_Verification(report, logger);
		ApplicationUtility.RDM_Logout(report, logger);

		report.endTest(logger);
	}

	@AfterTest
	public void teardown(){
		report.flush();
	}

}
