package com.iqvia.testscript;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.iqvia.lib.ApplicationUtility;
import com.iqvia.lib.UtilLib;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class RDM_Duplicate_PanelMember {

	//Starting the report generation
	String currentTime = UtilLib.systemTimeStamp();
	public static UtilLib util = new UtilLib();
	public static String TestCase_Name = "RDM_Duplicate_PanelMember";
	ExtentReports report;


	@BeforeMethod
	public void reportconfig() throws SAXException, IOException, ParserConfigurationException {
		report=UtilLib.Instance(TestCase_Name);
		report.config().documentTitle("RDM Duplicate panel member scenario");
		report.config().reportHeadline("RDM Duplicate panel member scenario");
	}


	@Test
	public void rdm_Duplicate_PanelMember() throws Exception{
		ExtentTest logger = report.startTest("RDM Duplicate panel member");

		String TestdataName = util.getPropertiesValue("TestData");
		String xlFilePath=System.getProperty("user.dir")+"/RDM_TestData/"+TestdataName;
		String UrlLaunch = util.getPropertiesValue("RDM_URL");
		String Username_property = util.getPropertiesValue("RDM_Login_UserName");
		String Password_property = util.getPropertiesValue("RDM_Password");
		String sheet = "Panel";
		String RDMPanelCode = util.getCellData(xlFilePath, sheet,"RDM_PanelCode", 1, "string");
		String RDMPanelStatus = util.getCellData(xlFilePath, sheet,"RDM_PanelStatusCode", 1,"string");
		String RDMPanelStatusCode_Search = UtilLib.getCellData(xlFilePath, sheet,"RDM_Panel_StatusCode", 1, "string");


		ApplicationUtility.RDM_Login(report, logger, UrlLaunch, Username_property, Password_property);
		String PanelCodeArray[]=RDMPanelCode.split(":");
		String PanelStatus[] = RDMPanelStatus.split(":");
		String PanelStatus_Search[] = RDMPanelStatusCode_Search.split(":");
		ApplicationUtility.RDM_Panelsearching(report, logger,PanelCodeArray[0],PanelStatus_Search[1]);
		ApplicationUtility.RDM_Duplicate_Panel_Verification(report, logger,PanelCodeArray[0],PanelStatus[1]);
		ApplicationUtility.RDM_Logout(report, logger);

		report.endTest(logger);
	}

	@AfterTest
	public void teardown(){
		report.flush();
	}

}
