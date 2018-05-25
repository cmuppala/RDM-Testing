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

public class RDM_NonGeoBrick_BoundaryType_Insertion {

	//Starting the report generation
	String currentTime = UtilLib.systemTimeStamp();
	public static UtilLib util = new UtilLib();
	public static String TestCase_Name = "RDM_NonGeoBrick_BoundaryType_Insertion";
	ExtentReports report;
	ExtentTest logger;

	@BeforeMethod
	public void reportconfig() throws SAXException, IOException, ParserConfigurationException {
		report=UtilLib.Instance(TestCase_Name);
		report.config().documentTitle("RDM Non Geo Brick Boundary Type insertion");
		report.config().reportHeadline("RDM Non Geo Brick Boundary Type insertion");
	}

	@Test
	public void rdm_NonGeoBrick_BoundaryType_Insertion() throws Exception{


		logger = report.startTest("RDM Non Geo Brick Boundary Code insertion, technical view and kafka verification");
		String TestdataName = util.getPropertiesValue("TestData");
		String xlFilePath=System.getProperty("user.dir")+"/RDM_TestData/"+TestdataName;
		String UrlLaunch = util.getPropertiesValue("RDM_URL");
		String Username_property = util.getPropertiesValue("RDM_Login_UserName");
		String Password_property = util.getPropertiesValue("RDM_Password");
		String sheet = "Non_GeoBrick";

		String Kafka_LoginName = util.getPropertiesValue("RDM_Kafka_Login_Username");
		String Kafka_password = util.getPropertiesValue("RDM_Kafka_Password");
		String Kafka_Publishing_Topic_Boundary =  util.getCellData(xlFilePath,sheet,"Kafka_Publishing_TopicName_NonGeoBrick", 1, "string");

		/************* Non Geo Brick Boundary Insertion **************************/
		ApplicationUtility.RDM_Login(report, logger, UrlLaunch, Username_property, Password_property);
		ApplicationUtility.RDM_BoundaryType_NonGeoBrick_Insert(report, logger);
		ApplicationUtility.RDM_BoundaryType_NonGeoBrick_TechnicalView_Verification(report, logger);
		ApplicationUtility.RDM_Logout(report, logger);


		/********* Kafka verification ***************************/

		ApplicationUtility.Kafka_Login(report, logger, UrlLaunch, Kafka_LoginName, Kafka_password);
		ApplicationUtility.kafKa_Verification_NonGeoBrick(report, logger,Kafka_Publishing_Topic_Boundary);
		ApplicationUtility.Kafka_Logout(report, logger);

		report.endTest(logger);

	}

	@AfterTest
	public void teardown(){
		report.flush();
	}

}
