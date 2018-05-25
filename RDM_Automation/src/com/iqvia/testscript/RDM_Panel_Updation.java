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

public class RDM_Panel_Updation {

	//Starting the report generation
	String currentTime = UtilLib.systemTimeStamp();
	public static UtilLib util = new UtilLib();
	public static String TestCase_Name = "RDM_Panel_Updation";
	ExtentReports report;


	@BeforeMethod
	public void reportconfig() throws SAXException, IOException, ParserConfigurationException {
		report=UtilLib.Instance(TestCase_Name);
		report.config().documentTitle("RDM Panel Updation");
		report.config().reportHeadline("RDM Panel Updation");
	}


	@Test
	public void rdm_Panel_Updation() throws Exception{
		ExtentTest logger = report.startTest("RDM Panel Updation, verification in technical view and kafka browser");

		String TestdataName = util.getPropertiesValue("TestData");
		String xlFilePath=System.getProperty("user.dir")+"/RDM_TestData/"+TestdataName;
		String UrlLaunch = util.getPropertiesValue("RDM_URL");
		String Username_property = util.getPropertiesValue("RDM_Login_UserName");
		String Password_property = util.getPropertiesValue("RDM_Password");
		String sheet = "Panel";
		String RDMPanelCode = util.getCellData(xlFilePath, sheet,"RDM_PanelCode", 1, "string");
		String RDMPanelStatus = util.getCellData(xlFilePath, sheet,"RDM_PanelStatusCode", 1,"string");
		String RDM_PanelStatus_Search = util.getCellData(xlFilePath,sheet,"RDM_Panel_StatusCode",1,"string");

		String Kafka_LoginName = util.getPropertiesValue("RDM_Kafka_Login_Username");
		String Kafka_password = util.getPropertiesValue("RDM_Kafka_Password");


		/****************** Panel Insertion ********************************/
		ApplicationUtility.RDM_Login(report, logger, UrlLaunch, Username_property, Password_property);
		String PanelCodeArray[]=RDMPanelCode.split(":");
		String PanelStatus_SearchArray[]=RDM_PanelStatus_Search.split(":");
		ApplicationUtility.RDM_Panelsearching(report, logger,PanelCodeArray[2],PanelStatus_SearchArray[1]);
		ApplicationUtility.RDM_Panel_Updation_Verification(report, logger, PanelCodeArray[2]);

		/************* Technical view verification *******************************/
		ApplicationUtility.RDM_TechnicalView_Validation_Update(report, logger,PanelCodeArray[2]);
		ApplicationUtility.RDM_Logout(report, logger);

		/********* Kafka verification ***************************/
		ApplicationUtility.Kafka_Login(report, logger, UrlLaunch, Kafka_LoginName, Kafka_password);
		ApplicationUtility.kafKa_Verification(report, logger);
		ApplicationUtility.Kafka_Logout(report, logger);


		report.endTest(logger);
	}

	@AfterTest
	public void teardown(){
		report.flush();
	}

}
