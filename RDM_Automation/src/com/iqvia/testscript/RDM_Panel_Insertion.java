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

public class RDM_Panel_Insertion {

	//Starting the report generation
	String currentTime = UtilLib.systemTimeStamp();
	public static UtilLib util = new UtilLib();
	public static String TestCase_Name = "RDM_PanelInsertion";
	ExtentReports report;


	@BeforeMethod
	public void reportconfig() throws SAXException, IOException, ParserConfigurationException {
		report=UtilLib.Instance(TestCase_Name);
		report.config().documentTitle("RDM Panel Insertion");
		report.config().reportHeadline("RDM Panel Insertion");
	}


	@Test
	public void rdm_Panel_Insertion() throws Exception{
		ExtentTest logger = report.startTest("RDM Panel Insertion , verification in technical view and kafka");

		String TestdataName = util.getPropertiesValue("TestData");
		String xlFilePath=System.getProperty("user.dir")+"/RDM_TestData/"+TestdataName;
		String UrlLaunch = util.getPropertiesValue("RDM_URL");
		String Username_property = util.getPropertiesValue("RDM_Login_UserName");
		String Password_property = util.getPropertiesValue("RDM_Password");
		String sheet = "Panel";
		String RDMPanelCode = util.getCellData(xlFilePath, sheet,"RDM_PanelCode", 1, "string");
		String RDMPanelStatus = util.getCellData(xlFilePath, sheet,"RDM_PanelStatusCode", 1,"string");
		String RDMPanelStatusCode_Search = UtilLib.getCellData(xlFilePath, sheet,"RDM_Panel_StatusCode", 1, "string");

		String Kafka_LoginName = util.getPropertiesValue("RDM_Kafka_Login_Username");
		String Kafka_password = util.getPropertiesValue("RDM_Kafka_Password");


		/********************* Panel Insertion **********************************/
		ApplicationUtility.RDM_Login(report, logger, UrlLaunch, Username_property, Password_property);
		String PanelCodeArray[]=RDMPanelCode.split(":");
		String PanelSearch_Code[] = RDMPanelStatusCode_Search.split(":");
		ApplicationUtility.RDM_Panelsearching(report, logger,PanelCodeArray[0],PanelSearch_Code[0]);
		//ApplicationUtility.RDM_Panel_Insertion(report, logger);

		/*String PanelStatusArray[]=RDMPanelStatus.split(":");
		ApplicationUtility.RDM_ExtendedAttribute_Validation(report, logger, PanelCodeArray[0], PanelStatusArray[1]);

		*//************ Technical view verification ************************//*
		ApplicationUtility.RDM_TechnicalView_Validation(report, logger,PanelCodeArray[0]);
		ApplicationUtility.RDM_Logout(report, logger);


		*//********* Kafka verification ***************************//*
		ApplicationUtility.Kafka_Login(report, logger, UrlLaunch, Kafka_LoginName, Kafka_password);
		ApplicationUtility.kafKa_Verification(report, logger);
		ApplicationUtility.Kafka_Logout(report, logger);*/

		report.endTest(logger);
	}

	@AfterTest
	public void teardown(){
		report.flush();
	}

}
