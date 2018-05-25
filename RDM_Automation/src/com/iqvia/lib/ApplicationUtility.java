package com.iqvia.lib;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.bcel.classfile.Utility;
import org.apache.commons.codec.binary.Base64;
import org.apache.regexp.recompile;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.iqvia.lib.UtilLib;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.iqvia.lib.ObjectDefinitionLibrary;

public class ApplicationUtility {
	private static WebDriver driver;
	public static ObjectDefinitionLibrary element = new ObjectDefinitionLibrary();
	static String OneKeyIdValue;
	static String PaneloldOnkeyID;
	static String panelOnkeyID;
	static String MemberNo_Value;
	static String UpdatedDate_Value;
	static List<WebElement> RecordCount_NonGeoBrick_Boundary;
	static String Insertion_Date;
	static String Runtime_date;
	static int record_NonGeoBrick_Boundary_Table_Count;
	static int record_count_technicalView;

	public ApplicationUtility(WebDriver driver) {
		super();
		this.driver = driver;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @throws InterruptedException
	 * @Function_Name : RDM_Login
	 * @Description : Initiate Browser and navigate to the URL with valid credentials
	 ********************************************************************************************/
	public static void RDM_Login(ExtentReports report, ExtentTest logger, String RDM_URL, String RDM_LoginName,
			String password) throws IOException {
		try {
			driver = UtilLib.getDriver();
			driver.manage().deleteAllCookies();

			// Launch the application URL
			driver.get(RDM_URL);
			Thread.sleep(2000);

			// Enter the user name and password
			driver.findElement(By.xpath(element.RDM_Username)).clear();
			driver.findElement(By.xpath(element.RDM_Username)).sendKeys(RDM_LoginName);

			byte[] decodedBytes = Base64.decodeBase64(password);
			driver.findElement(By.xpath(element.RDM_Password)).sendKeys(new String(decodedBytes));

			String login = UtilLib.screenshot(driver, "Login to RDM Application");
			logger.log(LogStatus.PASS,"Login to RDM Application: <b> " + RDM_URL + "</b>" + logger.addScreenCapture(login));

			// Click on login button
			driver.findElement(By.xpath(element.RDM_LoginButton)).click();

		} catch (Exception e) {
			e.getMessage();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
	}

	/********************************************************************************************
	 * @throws InterruptedException
	 * @Function_Name : Kafka_Login
	 * @Description : Initiate Browser and navigate to the URL with valid
	 *              credentials
	 ********************************************************************************************/
	public static void Kafka_Login(ExtentReports report, ExtentTest logger, String RDM_URL, String Kafka_LoginName,
			String password) {
		try {
			driver = UtilLib.getDriver();
			driver.manage().deleteAllCookies();

			// Launch the application URL
			driver.get(RDM_URL);

			// Enter the user name and password
			driver.findElement(By.xpath(element.RDM_Username)).clear();
			driver.findElement(By.xpath(element.RDM_Username)).sendKeys(Kafka_LoginName);

			// byte[] decodedBytes = Base64.decodeBase64(password);
			driver.findElement(By.xpath(element.RDM_Password)).sendKeys(password);

			String login = UtilLib.screenshot(driver, "Login to Kafka browser");
			logger.log(LogStatus.PASS,"Login to Kafka Browser: <b> " + RDM_URL + "</b>" + logger.addScreenCapture(login));

			// Click on login button
			driver.findElement(By.xpath(element.RDM_LoginButton)).click();

		} catch (Exception e) {
			e.getMessage();
		}
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_Panelsearching
	 * @Description : Finding the record for which the panel is not added (Validate the One key ID for the panel which doesn't exists)
	 ********************************************************************************************/
	public static boolean RDM_Panelsearching(ExtentReports report, ExtentTest logger, String RDM_PanelCode,
			String PanelCode_Search) throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, 150);
		try {
			Thread.sleep(10000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			driver.findElement(By.xpath(element.RDM_TechView)).click();

			Thread.sleep(2000);
			// Switch to the frame
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			// Click on Panel
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Panel)));
			driver.findElement(By.xpath(element.RDM_Panel)).click();

			// Select the Panel Member
			Thread.sleep(2000);
			driver.findElement(By.xpath(element.RDM_Panel_Member)).click();
			Thread.sleep(3000);

			String NavigationWorkplace = UtilLib.screenshot(driver,"User is successfully navigated to technical view Workplace page");
			logger.log(LogStatus.PASS, "User is successfully navigated to technical view Workplace page"+ logger.addScreenCapture(NavigationWorkplace));

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_SearchIcon)));

			// Click on Search Icon in work place page of technical view
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// select the panel code from technical view
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_panelcode)));

			Select panelcode = new Select(driver.findElement(By.xpath(element.RDM_panelcode)));
			panelcode.selectByVisibleText("Panel Code");

			// Enter panel code
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Enter_Panelcode)));
			driver.findElement(By.xpath(element.RDM_Enter_Panelcode)).sendKeys(RDM_PanelCode);

			// Select Panel member status code
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_PanelstatusCode)));
			Select panelstatuscode = new Select(driver.findElement(By.xpath(element.RDM_PanelstatusCode)));
			panelstatuscode.selectByVisibleText("Panel Member Status Code");

			// Enter panel member status code
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Enter_Panelstatuscode)));
			driver.findElement(By.xpath(element.RDM_Enter_Panelstatuscode)).sendKeys(PanelCode_Search);

			// Click on Apply
			driver.findElement(By.xpath(element.RDM_Click_Apply)).click();

			Thread.sleep(4000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Clickon_Onekey)));
			String Navigation_PanelMember_TechView = UtilLib.screenshot(driver,"Filtered the values in the search window from technical view");
			logger.log(LogStatus.PASS, "Filtered the values in the search window from technical view"+ logger.addScreenCapture(Navigation_PanelMember_TechView));
			
			
			List<WebElement> recount=driver.findElements(By.xpath("//*[@id='ebx_workspaceTable_fixedScroller']/table/tbody/tr"));
			int j=recount.size();
			
			System.out.println("Record count :"+j);
			
              //for(int i=0;)
			// Click on onekey record
			WebElement OneKeyIDElementDoubleClick = driver.findElement(By.xpath(element.RDM_Clickon_Onekey));
			Actions action_OneKeyId = new Actions(driver);
			action_OneKeyId.doubleClick(OneKeyIDElementDoubleClick).perform();

			// Thread.sleep(2000);
			// Get onekey ID
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Onekey)));
			WebElement getonekeyID = driver.findElement(By.xpath(element.RDM_Onekey));

			String localpanelOnkeyID = getonekeyID.getText();
			

			String PannelMember_OnekeyID = UtilLib.screenshot(driver,"User has successfully taken OnekeyID from technical view");
			logger.log(LogStatus.PASS,"User has successfully taken OnekeyID from technical view and the one key ID is <b> " + OneKeyIdValue + "</b>" + logger.addScreenCapture(PannelMember_OnekeyID));

			// Click on Workplace
			driver.switchTo().defaultContent();
			driver.findElement(By.xpath(element.RDM_Workplace)).click();
			Thread.sleep(6000);

			// Switch to the frame
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			// Click on Search Icon in work place page
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// select the OnekeyID
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Select_OnekeyID)));
			Thread.sleep(5000);
			Select Onekeyid = new Select(driver.findElement(By.xpath(element.RDM_Select_OnekeyID)));
			Onekeyid.selectByVisibleText("OneKey ID");

			// Enter OnekeyID
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Enter_OnekeyID)));
			driver.findElement(By.xpath(element.RDM_Enter_OnekeyID)).sendKeys(OneKeyIdValue);
			// Click on Apply
			driver.findElement(By.xpath(element.RDM_Click_Apply)).click();

			// Get Onekey from Workplace
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(7000);

			String Navigation_Workplace = UtilLib.screenshot(driver,
					"User is successfully navigate to workplace page and applied onekey ID filter for panel ");
			logger.log(LogStatus.PASS,
					"User is successfully navigate to workplace page and applied onekey ID filter for panel <b> "
							+ RDM_PanelCode + "</b>" + logger.addScreenCapture(Navigation_Workplace));

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_OnekeyID)));
			WebElement WUK_OnekeyID = driver.findElement(By.xpath(element.RDM_OnekeyID));

			// String Workplace_OnekeyID=WUK_OnekeyID.getText();
			Actions Workplace_OneKeyId = new Actions(driver);
			Workplace_OneKeyId.doubleClick(WUK_OnekeyID).perform();

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_Panel_ErrorHandling
	 * @Description :Verify the buttons and error messages for the listed fields
	 ********************************************************************************************/
	public static boolean RDM_Panel_ErrorHandling(ExtentReports report, ExtentTest logger, String PanelCode) throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, 150);
		String TestdataName = UtilLib.getPropertiesValue("TestData");
		String xlFilePath = System.getProperty("user.dir") + "/RDM_TestData/" + TestdataName;
		String sheet = "Panel";
		String RDMPanelCode = UtilLib.getCellData(xlFilePath, sheet, "RDM_PanelCode", 1, "string");
		System.out.println("RDM Panel Code is : " + RDMPanelCode);
		String RDMPanelStatusCode = UtilLib.getCellData(xlFilePath, sheet, "RDM_PanelStatusCode", 1, "string");
		try {
			// Click on update work place button
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(5000);
			driver.findElement(By.xpath(element.RDM_UpdateWorkplace_button)).click();

			Thread.sleep(4000);

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(2000);
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			String OneKeyID_Details = UtilLib.screenshot(driver,
					"User is successfully navigated to workplace authorizing");
			logger.log(LogStatus.PASS, "User is successfully navigated to workplace authorizing"
					+ logger.addScreenCapture(OneKeyID_Details));

			// Verify the buttons Accept,Cancel and save present in Work place
			// authoring page
			boolean AcceptButtonVerification = driver.findElement(By.xpath(element.RDM_Acceptbutton)).isDisplayed();
			String AcceptButtonValue = driver.findElement(By.xpath(element.RDM_Acceptbutton)).getText();
			if (AcceptButtonVerification == true) {
				logger.log(LogStatus.PASS, AcceptButtonValue + "Button is displayed in workplace authoring page");
				Reporter.log(AcceptButtonValue + "Button is displayed in workplace authoring page");
			} else {
				logger.log(LogStatus.FAIL, AcceptButtonValue + "Button is not displayed in workplace authoring page");
				Reporter.log(AcceptButtonValue + "Button is not displayed in workplace authoring page");
			}

			boolean CancelButtonVerification = driver.findElement(By.xpath(element.RDM_CancelButton)).isDisplayed();
			String CancelButtonValue = driver.findElement(By.xpath(element.RDM_CancelButton)).getText();
			if (CancelButtonVerification == true) {
				logger.log(LogStatus.PASS, CancelButtonValue + "Button is displayed in workplace authoring page");
			} else {
				logger.log(LogStatus.FAIL, CancelButtonValue + "Button is not displayed in workplace authoring page");
			}

			boolean SaveButtonVerification = driver.findElement(By.xpath(element.RDM_CancelButton)).isDisplayed();
			String SaveButtonValue = driver.findElement(By.xpath(element.RDM_CancelButton)).getText();
			if (SaveButtonVerification == true) {
				logger.log(LogStatus.PASS, SaveButtonValue + "Button is displayed in workplace authoring page");
			} else {
				logger.log(LogStatus.FAIL, SaveButtonValue + "Button is not displayed in workplace authoring page");
			}

			// Click on panel header
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			WebElement HeaderPanel_Click = driver.findElement(By.xpath(element.RDM_Panel_HeaderName));
			Actions action_headerPanel = new Actions(driver);
			action_headerPanel.doubleClick(HeaderPanel_Click).perform();
			Thread.sleep(4000);

			// Click on + icon
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Add_Icon)));

			WebElement AddIcon_Click = driver.findElement(By.xpath(element.RDM_Add_Icon));
			Actions AddIcon_Action = new Actions(driver);
			AddIcon_Action.doubleClick(AddIcon_Click).perform();

			String OutletPanel_memberDetails = UtilLib.screenshot(driver,
					"User is successfully navigated to outlet panel member with new record");
			logger.log(LogStatus.PASS, "User is successfully navigated to outlet panel member with new record"
					+ logger.addScreenCapture(OutletPanel_memberDetails));

			// verify the values under the section panel
			Thread.sleep(5000);
			driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);
			driver.findElement(By.xpath(element.RDM_PanelMember_dropdown)).click();

			Thread.sleep(1000);
			List<WebElement> Panel_Dropdown_Values = driver.findElements(By.xpath(element.RDM_Panel_DropdownValues));
			String PanelCodeArray[] = RDMPanelCode.split(":");

			for (WebElement PanelDropdownValues : Panel_Dropdown_Values) {
				System.out.println("The Panel dropdown values are:" + PanelDropdownValues.getText());
				for (int i = 0; i < PanelCodeArray.length; i++) {
					if (PanelCodeArray[i].equalsIgnoreCase(PanelDropdownValues.getText())) {
						logger.log(LogStatus.PASS,
								"The loop up values : <b> " + PanelCodeArray[i]+ " </b> are matching to the panel dropdown values : <b> " + PanelDropdownValues.getText() + "</b>");
						System.out.println("The loop up values : " + PanelCodeArray[i]
								+ " are matching to the panel dropdown values : " + PanelDropdownValues.getText());
					}
				}
			}

			Thread.sleep(1000);

			// Click on save button and verify the error message as "Field Panel
			// is mandatory"
			driver.findElement(By.xpath(element.RDM_SaveButton)).click();

			Thread.sleep(2000);
			String ErrorMessage_Panel = driver.findElement(By.xpath(element.RDM_Panel_errorMessage)).getText();
			System.out.println("Error message generated for panel code is : " + ErrorMessage_Panel);

			if (ErrorMessage_Panel.equalsIgnoreCase("Field 'Panel' is mandatory.")) {
				String ErrorMessageForPanel = UtilLib.screenshot(driver, "Error message generated for the panel");
				logger.log(LogStatus.PASS,
						"Error message generated for the panel" + logger.addScreenCapture(ErrorMessageForPanel));
				logger.log(LogStatus.PASS, "Error message generated for panel is : <b> " + ErrorMessage_Panel + "</b>");
			} else {
				String ErrorMessagePanel_Failure = UtilLib.screenshot(driver, "Error message generated for the panel");
				logger.log(LogStatus.FAIL,"Error message  generated for the panel" + logger.addScreenCapture(ErrorMessagePanel_Failure));
				logger.log(LogStatus.FAIL, "Error message generated for panel is : <b> " + ErrorMessage_Panel + "</b>");
			}

			// Select any panel from the drop down and click on Save button and
			// verify the error message generated
			driver.findElement(By.xpath(element.RDM_PanelStatus_Dropdown)).click();
			Thread.sleep(1000);
			System.out.println("Panel code array is " + PanelCodeArray[0]);
			driver.findElement(By.xpath("//*[@class='ebx_ISS_Item']/div[@class='ebx_ISS_Item_WithPreview' and text()='"+ PanelCode+"']")).click();
			driver.findElement(By.xpath(element.RDM_SaveButton)).click();

			Thread.sleep(5000);
			String ErrorMessage_PanelStatus = driver.findElement(By.xpath(element.RDM_PanelStatus_ErrorMessage))
					.getText();
			System.out.println("Error message generated for panel status code is : " + ErrorMessage_PanelStatus);

			if (ErrorMessage_PanelStatus.equalsIgnoreCase("Field 'Panel Status' is mandatory.")) {
				String OutletPanel_ErrorMessageForPanel = UtilLib.screenshot(driver,
						"Error message generated for the panel status");
				logger.log(LogStatus.PASS, "Error message generated for the panel status"
						+ logger.addScreenCapture(OutletPanel_ErrorMessageForPanel));
				logger.log(LogStatus.PASS,
						"Error message generated for panel status is : <b> " + ErrorMessage_PanelStatus + "</b>");
			} else {
				String ErrorMessagePanel_Failure = UtilLib.screenshot(driver,
						"Error message generated for the panel status");
				logger.log(LogStatus.FAIL, "Error message generated for the panel status"
						+ logger.addScreenCapture(ErrorMessagePanel_Failure));
				logger.log(LogStatus.FAIL,
						"Error message generated for panel status is : <b> " + ErrorMessage_PanelStatus + "</b>");
			}
			Thread.sleep(2000);

			// verify the values under the section panel status and click on
			// Save button and verify the error message generated
			driver.findElement(By.xpath(element.RDM_PanelStatus_Dropdown)).click();
			Thread.sleep(5000);

			List<WebElement> PanelStatus_Dropdown_Values = driver
					.findElements(By.xpath(element.RDM_Panel_DropdownValues));
			String PanelStatusCodeArray[] = RDMPanelStatusCode.split(":");

			for (WebElement PanelStatusDropDown : PanelStatus_Dropdown_Values) {
				System.out.println("The Panel Status dropdown values are:" + PanelStatusDropDown.getText());
				for (int j = 0; j < PanelStatusCodeArray.length; j++) {
					if (PanelStatusCodeArray[j].equalsIgnoreCase(PanelStatusDropDown.getText())) {
						logger.log(LogStatus.PASS,
								"The loop up values : <b> " + PanelStatusCodeArray[j] + " </b> are matching to the panel status dropdown values : <b> "
										+ PanelStatusDropDown.getText() + "</b>");
						System.out.println("The loop up values : " + PanelStatusCodeArray[j]
								+ " are matching to the panel status dropdown values : "
								+ PanelStatusDropDown.getText());
					}
				}
			}
			Thread.sleep(4000);

			driver.findElement(
					By.xpath("//*[@class='ebx_ISS_Item_WithPreview' and text()='" + PanelStatusCodeArray[1] + "']"))
			.click();

			// Verify the warning message as "Please save any changes made
			// within General Information before moving on."
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element.RDM_WarningMessage_PanelSatus)));
			String WarningMessage = driver.findElement(By.xpath(element.RDM_WarningMessage_PanelSatus)).getText();
			System.out.println("The comment after adding the panel status is: " + WarningMessage);

			Thread.sleep(2000);
			driver.findElement(By.xpath(element.RDM_SaveButton)).click();

			Thread.sleep(4000);
			if (WarningMessage
					.equalsIgnoreCase("Please save any changes made within General Information before moving on.")) {
				String CommentForPanel = UtilLib.screenshot(driver, "Comment for Panel after selecting Panel Status");
				logger.log(LogStatus.PASS, "Comment for Panel after selecting Panel Status is "
						+ logger.addScreenCapture(CommentForPanel));
				logger.log(LogStatus.PASS,
						"Comment for Panel after selecting Panel Status is : <b> " + WarningMessage + "</b>");
			} else {
				String CommentForPanel_Failure = UtilLib.screenshot(driver,
						"Comment for Panel after selecting Panel Status");
				logger.log(LogStatus.FAIL, "Comment for Panel after selecting Panel Status is "
						+ logger.addScreenCapture(CommentForPanel_Failure));
				logger.log(LogStatus.FAIL,
						"Comment for Panel after selecting Panel Status is : <b> " + WarningMessage + "</b>");
			}

			// Verify the error generated for member number on clicking panel
			// status as 'Active'
			String ErrorMessage_MemberNo = driver.findElement(By.xpath(element.RDM_Error_MemberNumber)).getText();
			System.out.println("Error message generated for member No is: " + ErrorMessage_MemberNo);

			if (ErrorMessage_MemberNo.equalsIgnoreCase("Member No is mandatory when the panel status is not LFP")) {
				String OutletPanel_ErrorMessageForMemberNo = UtilLib.screenshot(driver,
						"Error message generated for Member Number on panel status ACTIVE ");
				logger.log(LogStatus.PASS, "Error message generated for Member number on panel status ACTIVE "
						+ logger.addScreenCapture(OutletPanel_ErrorMessageForMemberNo));
				logger.log(LogStatus.PASS, "Error message generated for Member number : <b> " + ErrorMessage_MemberNo
						+ "</b>" + " for panel status <b> " + PanelStatusCodeArray[1] + "</b>");
			} else {
				String ErrorMessageForMemberNo = UtilLib.screenshot(driver,
						"Error message generated for Member Number on panel status ACTIVE ");
				logger.log(LogStatus.FAIL, "Error message generated for Member number on panel status ACTIVE "
						+ logger.addScreenCapture(ErrorMessageForMemberNo));
				logger.log(LogStatus.FAIL, "Error message generated for Member number is : <b> " + ErrorMessage_MemberNo
						+ "</b>" + " for panel status <b> " + PanelStatusCodeArray[1] + "</b>");
			}

			Thread.sleep(1000);

			// Verify the error message is not generated for member number for
			// panel status as 'On Test' and 'Left Panel'
			driver.findElement(By.xpath(element.RDM_ClearField_PanelStatus)).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_PanelStatus_Dropdown)).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath(element.RDM_Left_PanelStatus)).click();
			driver.findElement(By.xpath(element.RDM_SaveButton)).click();

			String Err_Message = UtilLib.screenshot(driver,
					"Error message is not generated for Member Number on panel status LEFT PANEL ");
			logger.log(LogStatus.PASS, "Error message is not generated for Member number on panel status LEFT PANEL "
					+ logger.addScreenCapture(Err_Message));
			logger.log(LogStatus.PASS,
					"Error message is not generated  for panel status <b> " + PanelStatusCodeArray[0] + "</b>");

			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_ClearField_PanelStatus)).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_PanelStatus_Dropdown)).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath(element.RDM_OnTest_PanelStatus)).click();
			driver.findElement(By.xpath(element.RDM_SaveButton)).click();

			if (ErrorMessage_MemberNo.equalsIgnoreCase("Member No is mandatory when the panel status is not LFP")) {
				String Err_Message_OnTest = UtilLib.screenshot(driver,
						"Error message is  generated for Member Number  on panel status ON TEST ");
				logger.log(LogStatus.PASS, "Error message is generated for Member number on panel status ON TEST "
						+ logger.addScreenCapture(Err_Message_OnTest));
				logger.log(LogStatus.PASS, "Error message generated  for Member panel <b> " + ErrorMessage_MemberNo
						+ " </b> on panel status <b> " + PanelStatusCodeArray[2] + "</b>");
			} else {
				String Err_Message_OnTest = UtilLib.screenshot(driver,
						"Error message is not generated for Member Number on panel status ON TEST ");
				logger.log(LogStatus.FAIL, "Error message is not generated for Member number on panel status ON TEST "
						+ logger.addScreenCapture(Err_Message_OnTest));
				logger.log(LogStatus.FAIL,
						"Error message is not generated  for panel status <b> " + PanelStatusCodeArray[2] + "</b>");
			}
		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_PanelInsertion
	 * @Description : Inserting a panel
	 ********************************************************************************************/
	public static boolean RDM_Panel_Insertion(ExtentReports report, ExtentTest logger) throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, 150);
		try {
			// Click on update work place button
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_UpdateWorkplace_button)).click();

			Thread.sleep(10000);

			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			String OneKeyID_Details = UtilLib.screenshot(driver,
					"User is successfully navigated to workplace authorizing");
			logger.log(LogStatus.PASS, "User is successfully navigated to workplace authorizing"
					+ logger.addScreenCapture(OneKeyID_Details));

			// Click on panel header
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			WebElement HeaderPanel_Click = driver.findElement(By.xpath(element.RDM_Panel_HeaderName));
			Actions action_headerPanel = new Actions(driver);
			action_headerPanel.doubleClick(HeaderPanel_Click).perform();
			Thread.sleep(4000);

			// Click on + icon
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Add_Icon)));

			WebElement AddIcon_Click = driver.findElement(By.xpath(element.RDM_Add_Icon));
			Actions AddIcon_Action = new Actions(driver);
			AddIcon_Action.doubleClick(AddIcon_Click).perform();

			String OutletPanel_memberDetails = UtilLib.screenshot(driver,
					"User is successfully navigated to outlet panel member with new record");
			logger.log(LogStatus.PASS, "User is successfully navigated to outlet panel member with new record"
					+ logger.addScreenCapture(OutletPanel_memberDetails));

			driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_ExtendedAttribute_Validation
	 * @Description : Validation of extended attribute for all panel code
	 ********************************************************************************************/
	public static boolean RDM_ExtendedAttribute_Validation(ExtentReports report, ExtentTest logger, String Panel,
			String PanelStatus) throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, 150);
		String TestdataName = UtilLib.getPropertiesValue("TestData");
		String sheet = "Panel";
		String xlFilePath = System.getProperty("user.dir") + "/RDM_TestData/" + TestdataName;
		int TestdataRowcount = UtilLib.getRowCount(xlFilePath, sheet);
		String AttributeValueHPA = UtilLib.getCellData(xlFilePath, sheet, "RDM_AttributeValue_HPA_DataSuppl", 1,
				"string");

		// Logic for member panel
		DateFormat df = new SimpleDateFormat("dd:MM:yyyy:hh:mm:ss");
		Date today = Calendar.getInstance().getTime();
		String runTime = df.format(today);
		String split_string_array[] = runTime.split(":");
		MemberNo_Value = String.join("", split_string_array);
		System.out.println("The member id is: " + MemberNo_Value);

		// Logic for retrieving the system date
		DateFormat dff = new SimpleDateFormat("dd/MM/yyyy");
		Date today_Date = Calendar.getInstance().getTime();
		Runtime_date = dff.format(today_Date);
		System.out.println("The system date is : " + Runtime_date);

		try {

			// click on panel drop down
			driver.findElement(By.xpath(element.RDM_Panel_DropdownList)).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath(
					"//*[@class='ebx_ISS_Item']/div[@class='ebx_ISS_Item_WithPreview' and text()='" + Panel + "']"))
			.click();
			Thread.sleep(3000);

			// click on panel status drop down and enter the member number
			if (PanelStatus.equalsIgnoreCase("ACTIVE") || PanelStatus.equalsIgnoreCase("ON TEST")) {

				// Enter the member ID
				driver.findElement(By.xpath(element.RDM_MemberNo_Field)).sendKeys(MemberNo_Value);

				// Select the panel status
				driver.findElement(By.xpath(element.RDM_PanelStatus_DropdownList)).click();
				Thread.sleep(6000);
				driver.findElement(
						By.xpath("//*[@class='ebx_ISS_Item']/div[@class='ebx_ISS_Item_WithPreview' and text()='"
								+ PanelStatus + "']"))
				.click();

				// click on save button
				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
			} else {

				// Select the panel status
				driver.findElement(By.xpath(element.RDM_Panel_DropdownList)).click();
				driver.findElement(
						By.xpath("//*[@class='ebx_ISS_Item']/div[@class='ebx_ISS_Item_WithPreview' and text()='"
								+ PanelStatus + "']"))
				.click();

				// click on save button
				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
			}

			String OutletPanelAddValues = UtilLib.screenshot(driver,
					"Values entered in general tab for panel , panel status and member number");
			logger.log(LogStatus.PASS, "Values entered in general tab for panel , panel status and member number"
					+ logger.addScreenCapture(OutletPanelAddValues));

			Thread.sleep(3000);
			// MemberNo_Value =
			// driver.findElement(By.xpath(element.RDM_MemberNo_Field)).getText();
			System.out.println("Member No inserted is : " + MemberNo_Value);
			logger.log(LogStatus.PASS, "Member No enterted is : <b> " + MemberNo_Value + "</b>"
					+ " for panel code is <b> " + Panel + "</b> ");

			// Validated the updated date with today's date
			UpdatedDate_Value = driver.findElement(By.xpath(element.RDM_UpdatedDate_Value)).getText();
			if (UpdatedDate_Value.contains(Runtime_date)) {
				System.out.println("The updated date value is : <b> " + UpdatedDate_Value
						+ " </b> which should be equal to the today's date <b> " + Runtime_date + "</b>");
				logger.log(LogStatus.PASS, "The updated date value is : <b> " + UpdatedDate_Value
						+ " </b> which should be equal to the today's date <b> " + Runtime_date + "</b>");
				Reporter.log("The updated date value is : <b> " + UpdatedDate_Value
						+ " </b> which should be equal to the today's date <b> " + Runtime_date + "</b>");
			} else {
				logger.log(LogStatus.FAIL, "The updated date value is : <b> " + UpdatedDate_Value
						+ " </b> which is not equal to the today's date <b> " + Runtime_date + "</b>");
				Reporter.log("The updated date value is : <b> " + UpdatedDate_Value
						+ " </b> which is not equal to the today's date <b> " + Runtime_date + "</b>");
			}

			// Validate the inserted date
			Insertion_Date = driver.findElement(By.xpath(element.RDM_InsertionDate_Value)).getText();
			logger.log(LogStatus.PASS,
					"Insertion date value is : <b>" + Insertion_Date + " </b> while inserting the panel");

			// validate the extended attribute
			switch (Panel) {
			case "HPA":

				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				driver.switchTo().frame(element.RDM_PanelExtensionAttribute_Frame);
				Thread.sleep(4000);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(3000);
				driver.findElement(By.xpath(element.RDM_AssignedAttribute_Value)).click();

				String PanelExtension = UtilLib.screenshot(driver,
						"Panel extension attribute value page for HPA Data Suppl");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for HPA Data Suppl"
						+ logger.addScreenCapture(PanelExtension));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();

				// derive the attribute name
				String AssignedAttribute = driver.findElement(By.xpath(element.RDM_AssginedAttribute)).getText();
				System.out.println("Assigned attribute name : " + AssignedAttribute);

				Thread.sleep(1000);
				String Assigned_attribute_Values = driver.findElement(By.xpath(element.RDM_AssignedAttribute_Value))
						.getText();
				System.out.println("Assigned attribute value : " + Assigned_attribute_Values);

				if (AssignedAttribute.contains("HPA Data Suppl")) {
					logger.log(LogStatus.PASS, "The assigned attribute name is : <b> " + AssignedAttribute + "</b>");

					String AssignedAttributeValue_char = Assigned_attribute_Values.replaceAll("[^a-zA-Z0-9]-,", "");
					System.out.println(AssignedAttributeValue_char);

					if (AttributeValueHPA.contains(AssignedAttributeValue_char)) {
						logger.log(LogStatus.PASS,
								"The assigned attribute value of HPA Data Suppl is : <b> " + AssignedAttributeValue_char
								+ " </b> is matching with the look up value <b> " + AttributeValueHPA + "</b>");
						System.out.println(
								"The assigned attribute value of HPA Data Suppl is : <b> " + AssignedAttributeValue_char
								+ " </b> is matching with the look up value <b> " + AttributeValueHPA + "</b>");

						// click on the assigned attribute value
						driver.findElement(By.xpath(element.RDM_AssignedAttribute_Value)).click();

					} else {
						System.out.println("The assigned attribute value of HPA Data Suppl is : <b> "
								+ Assigned_attribute_Values + " </b> is not matching with the look up value <b> "
								+ AttributeValueHPA + " </b>");
						logger.log(LogStatus.FAIL,
								"The assigned attribute value of HPA Data Suppl is : <b> " + Assigned_attribute_Values
								+ " </b> is not matching with the look up value <b> " + AttributeValueHPA
								+ " </b>");

					}

				} else {
					logger.log(LogStatus.FAIL, "The assigned attribute name is : <b> " + AssignedAttribute + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// switching to default content and switching outlet panel
				// member
				driver.switchTo().defaultContent();

				WebElement frameText = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension = driver.switchTo().frame(element.RDM_OuterPanelMember_Frame)
						.findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension.size());

				// Verify the HPA IHD attribute
				String AssignedAttribute_IHD = driver.findElement(By.xpath(element.RDM_AssignedAttributeSelected))
						.getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_IHD);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_HPAIHD_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for HPA IHD");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for HPA IHD"
						+ logger.addScreenCapture(PanelExtn_HPAIHD_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_IHD.contains("HPA IHD")) {
					logger.log(LogStatus.PASS, "The assigned attribute name is : <b> " + AssignedAttribute + "</b>");

					((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight,250)");
					List<WebElement> AssignedAttribute_HPAIHD = driver
							.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println("Assigned attribute value size is ===== " + AssignedAttribute_HPAIHD.size());

					for (WebElement AssignedValues_Dropdown : AssignedAttribute_HPAIHD) {
						String AssignedAttribute_HPAIHD_char = AssignedValues_Dropdown.getText()
								.replaceAll("[^a-zA-Z0-9]-,", "");
						System.out.println("Assigned values for HPA IHD after removing the special character is : "
								+ AssignedAttribute_HPAIHD_char);

						for (int k = 1; k < TestdataRowcount; k++) {

							String RDM_AttributeValue_HPAIHD = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_HPA_IHD", k, "string");
							if (RDM_AttributeValue_HPAIHD.contains(AssignedAttribute_HPAIHD_char)) {

								System.out
								.println("Assigned value is ******======:" + AssignedValues_Dropdown.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of HPA IHD is : <b> "
												+ AssignedAttribute_HPAIHD_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_HPAIHD + "</b>");
								System.out.println("The assigned attribute value of HPA IHD is : <b> "
										+ AssignedAttribute_HPAIHD_char
										+ " </b> is matching with the look up value <b> " + RDM_AttributeValue_HPAIHD
										+ "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_IHD + "</b>");
				}

				// click on save and close button
				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// switching to default content and switching outlet panel
				// member
				driver.switchTo().defaultContent();

				WebElement frameText_3 = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_3.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_3 = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_3.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_3 = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_3.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_3 = driver.switchTo().frame(element.RDM_OuterPanelMember_Frame)
						.findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_3.size());

				// Verify the HPA OBD attribute
				String AssignedAttribute_OBD = driver.findElement(By.xpath(element.RDM_AssignedAttributeSelected))
						.getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_OBD);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_HPAOBD_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for HPA OBD");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for HPA OBD"
						+ logger.addScreenCapture(PanelExtn_HPAOBD_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_OBD.contains("HPA OBD")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_OBD + "</b>");

					List<WebElement> AssignedAttribute_HPAOBD = driver
							.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println("Assigned attribute value size is ===== " + AssignedAttribute_HPAOBD.size());

					for (WebElement AssignedValues_Dropdown : AssignedAttribute_HPAOBD) {
						String AssignedAttribute_HPAOBD_char = AssignedValues_Dropdown.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println("Assigned values for HPA OBD after removing the special character is : "
								+ AssignedAttribute_HPAOBD_char);

						for (int k = 1; k < TestdataRowcount; k++) {

							String RDM_AttributeValue_HPAOBD = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_HPA_OBD", k, "string");
							if (RDM_AttributeValue_HPAOBD.contains(AssignedAttribute_HPAOBD_char)) {

								System.out
								.println("Assigned value is ******======:" + AssignedValues_Dropdown.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of HPA OBD is : <b> "
												+ AssignedAttribute_HPAOBD_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_HPAOBD + "</b>");
								Reporter.log("The assigned attribute value of HPA OBD is : <b> "
										+ AssignedAttribute_HPAOBD_char
										+ " </b> is matching with the look up value <b> " + RDM_AttributeValue_HPAOBD
										+ "</b>");
								System.out.println("The assigned attribute value of HPA OBD is : <b> "
										+ AssignedAttribute_HPAOBD_char
										+ " </b> is matching with the look up value <b> " + RDM_AttributeValue_HPAOBD
										+ "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_OBD + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				String PanelMember_PanelExtn = UtilLib.screenshot(driver,
						"Values entered for extended attribute for panel code HPA");
				logger.log(LogStatus.PASS, "Values entered for extended attribute for panel code <b> HPA </b>"
						+ logger.addScreenCapture(PanelMember_PanelExtn));

				break;
			case "PMRE":

				// click on expand icon of error
				driver.findElement(By.xpath(element.RDM_ExpandIcon_Error)).click();

				// validate the error message
				String Warning_OutletPanelMember_PMRE = driver.findElement(By.xpath(element.RDM_WarningMsg)).getText();
				System.out
				.println("Warning message for PMRE extended attribute is : " + Warning_OutletPanelMember_PMRE);
				logger.log(LogStatus.PASS, "Warning message for PMRE extended attribute is : <br> "
						+ Warning_OutletPanelMember_PMRE + " </br> ");

				// Collapse the warning/Error icon
				driver.findElement(By.xpath(element.RDM_CollapseIcon_Warning)).click();

				// Click on add +Icon for extended attribute
				Thread.sleep(4000);
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				driver.switchTo().frame(element.RDM_PanelExtensionAttribute_Frame);

				// verify PMRE Data status extended attribute
				Thread.sleep(3000);
				String AssignedAttribute_PMRE_DataStatus = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRE_DataStatus);

				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMREDataStatus_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for PMRE Data Status");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRE Data status"
						+ logger.addScreenCapture(PanelExtn_PMREDataStatus_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRE_DataStatus.contains("PMRE Data Status")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_DataStatus + "</b>");

					List<WebElement> AssignedAttribute_PMRE_DataStatus_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttributeDropdown_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_PMRE_DataStatus_Size.size());

					for (WebElement AssignedValues_Dropdown_PMRE_Data_Status : AssignedAttribute_PMRE_DataStatus_Size) {

						String AssignedAttribute_PMRE_DataStatus_char = AssignedValues_Dropdown_PMRE_Data_Status
								.getText().replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println(
								"Assigned values for PMRE Data Status after removing the special character is : "
										+ AssignedAttribute_PMRE_DataStatus_char);

						for (int a = 1; a < TestdataRowcount; a++) {
							String RDM_AttributeValue_PRMEDataStatus = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_PMRE_DataStatus", a, "string");

							if (RDM_AttributeValue_PRMEDataStatus.contains(AssignedAttribute_PMRE_DataStatus_char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_PMRE_Data_Status.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of PMRE data status is : <b> "
												+ AssignedAttribute_PMRE_DataStatus_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_PRMEDataStatus + "</b>");
								System.out.println("The assigned attribute value of PMRE data status is : <b> "
										+ AssignedAttribute_PMRE_DataStatus_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_PRMEDataStatus + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_DataStatus + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_PMRE = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_PMRE.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_PMRE = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_PMRE.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_PMRE = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_PMRE.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_PMRE = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_PMRE.size());

				// Verify the PMRE Group attribute
				Thread.sleep(3000);
				String AssignedAttribute_PMRE_Group = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRE_Group);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMREGroup_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for PMRE Group Attribute");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRE group attribute"
						+ logger.addScreenCapture(PanelExtn_PMREGroup_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRE_Group.contains("PMRE Group")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_Group + "</b>");

					List<WebElement> AssignedAttribute_PRME_Group = driver
							.findElements(By.xpath(element.RDM_AssignedAttributeDropdown_Value));
					System.out.println("Assigned attribute value size is ===== " + AssignedAttribute_PRME_Group.size());

					for (WebElement AssignedValues_Dropdown_PMREGroup : AssignedAttribute_PRME_Group) {
						String AssignedAttribute_PMREGroup_char = AssignedValues_Dropdown_PMREGroup.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println("Assigned values for PMRE Group after removing the special character is : "
								+ AssignedAttribute_PMREGroup_char);
						for (int b = 1; b < TestdataRowcount; b++) {
							String RDM_AttributeValue_PMREGroup = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_PMRE_Group", b, "string");
							if (RDM_AttributeValue_PMREGroup.contains(AssignedAttribute_PMREGroup_char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_PMREGroup.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of PMRE Group is : <b> "
												+ AssignedAttribute_PMREGroup_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_PMREGroup + "</b>");
								System.out.println("The assigned attribute value of PMRE Group is : <b> "
										+ AssignedAttribute_PMREGroup_char
										+ " </b> is matching with the look up value <b> " + RDM_AttributeValue_PMREGroup
										+ "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_Group + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_PMREGrp = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_PMREGrp.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_PMREGrp = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_PMREGrp.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_PMREGRP = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_PMREGRP.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_PMREGRP = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_PMREGRP.size());

				// Verify the PMRE Part Group attribute
				Thread.sleep(3000);
				String AssignedAttribute_PMRE_PartGroup = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRE_PartGroup);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMREPartGroup_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for PMRE Part Group");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRE Part Group"
						+ logger.addScreenCapture(PanelExtn_PMREPartGroup_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRE_PartGroup.contains("PMRE Part Group")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_PartGroup + "</b>");

					List<WebElement> AssignedAttribute_PMRE_PartGroup_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttributeDropdown_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_PMRE_PartGroup_Size.size());

					for (WebElement AssignedValues_Dropdown_PMREPartGroup : AssignedAttribute_PMRE_PartGroup_Size) {
						String AssignedAttribute_PMRE_PartGroup_char = AssignedValues_Dropdown_PMREPartGroup.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println(
								"Assigned values for PMRE Part Group after removing the special character is : "
										+ AssignedAttribute_PMRE_PartGroup_char);
						for (int c = 1; c < TestdataRowcount; c++) {
							String RDM_AttributeValue_PMRE_PartGroup = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_PMRE_Part_Group", c, "string");
							if (RDM_AttributeValue_PMRE_PartGroup.contains(AssignedAttribute_PMRE_PartGroup_char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_PMREPartGroup.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of PMRE Part Group is : <b> "
												+ AssignedAttribute_PMRE_PartGroup_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_PMRE_PartGroup + "</b>");
								System.out.println("The assigned attribute value of PMRE Part group is : <b> "
										+ AssignedAttribute_PMRE_PartGroup_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_PMRE_PartGroup + "</b>");
								break;
							}
						}
					}

				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_PartGroup + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_PMRE_Part_Group = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_PMRE_Part_Group.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_PMRE_PartGroup = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_PMRE_PartGroup.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_PMRE_PartGroup = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println(
						"id of 2nd frame -------" + iframe_outletPanelMember_PMRE_PartGroup.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_PMRE_Part_Group = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_PMRE_Part_Group.size());

				// Verify the PMRE Pharm Type attribute
				Thread.sleep(3000);
				String AssignedAttribute_PMRE_PharmType = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRE_PharmType);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMRE_PharmGroup_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for PMRE Pharm Group");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRE Pharm Group"
						+ logger.addScreenCapture(PanelExtn_PMRE_PharmGroup_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRE_PharmType.contains("PMRE Pharm Type")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_PharmType + "</b>");

					List<WebElement> AssignedAttribute_PMRE_PharmType_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttributeDropdown_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_PMRE_PharmType_Size.size());

					for (WebElement AssignedValues_Dropdown_PharmType : AssignedAttribute_PMRE_PharmType_Size) {
						String AssignedAttribute_PharmType_char = AssignedValues_Dropdown_PharmType.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println(
								"Assigned values for PMRE Pharm Group after removing the special character is : "
										+ AssignedAttribute_PharmType_char);
						for (int d = 1; d < TestdataRowcount; d++) {
							String RDM_AttributeValue_PMREPharmType = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_PMRE_Pharm_Type", d, "string");
							if (RDM_AttributeValue_PMREPharmType.contains(AssignedAttribute_PharmType_char)) {
								System.out.println("Assigned value is ******======:" + AssignedValues_Dropdown_PharmType.getText());
								logger.log(LogStatus.PASS, "The assigned attribute value of PMRE Pharm Group is : <b> " + AssignedAttribute_PharmType_char +" </b> is matching with the look up value <b> "+ RDM_AttributeValue_PMREPharmType + "</b>");
								System.out.println("The assigned attribute value of PMRE Pharm Group is : <b> " + AssignedAttribute_PharmType_char + " </b> is matching with the look up value <b> "+ RDM_AttributeValue_PMREPharmType + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_PharmType + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_PharmGrp = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_PharmGrp.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_PharmGrp = driver.switchTo().frame("ebx_SubSessioniFrame").findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_PharmGrp.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_PharmGrp = driver.switchTo().frame("ebx_SubSessioniFrame").findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_PharmGrp.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_PharmGrp = driver.switchTo().frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_PharmGrp.size());

				// Verify the PMRE Prov Others
				Thread.sleep(3000);
				String AssignedAttribute_PMRE_ProvOther = driver.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRE_ProvOther);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMRE_ProvOther_Screenshot = UtilLib.screenshot(driver,"Panel extension attribute value page for PMRE Prov Other");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRE Prov Other"+ logger.addScreenCapture(PanelExtn_PMRE_ProvOther_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRE_ProvOther.contains("PMRE Prov Other")) {
					logger.log(LogStatus.PASS,"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_ProvOther + "</b>");

					List<WebElement> AssignedAttribute_ProvOther = driver.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println("Assigned attribute value size is ===== " + AssignedAttribute_ProvOther.size());
					for (WebElement AssignedValues_Dropdown_ProvOther : AssignedAttribute_ProvOther) {
						String AssignedAttribute_ProvOther_char = AssignedValues_Dropdown_ProvOther.getText().replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println("Assigned values for PMRE Prov Other after removing the special character is : " + AssignedAttribute_ProvOther_char);
						for (int e = 1; e < TestdataRowcount; e++) {
							String RDM_AttributeValue_PMREProv_Other = UtilLib.getCellData(xlFilePath, sheet,"RDM_AttributeValue_PMRE_Prov_Other", e, "string");
							if (RDM_AttributeValue_PMREProv_Other.contains(AssignedAttribute_ProvOther_char)) {
								System.out.println("Assigned value is ******======:" + AssignedValues_Dropdown_ProvOther.getText());
								logger.log(LogStatus.PASS, "The assigned attribute value of PMRE Prov Other is : <b> " + AssignedAttribute_ProvOther_char+ " </b> is matching with the look up value <b> "+ RDM_AttributeValue_PMREProv_Other + "</b>");
								System.out.println("The assigned attribute value of PMRE Prov Other is : <b> " + AssignedAttribute_ProvOther_char+ " </b> is matching with the look up value <b> "+ RDM_AttributeValue_PMREProv_Other + "</b>");
								break;
							}
						}

					}
				} else {
					logger.log(LogStatus.FAIL, "The assigned attribute name is : <b> " + AssignedAttribute_PMRE_ProvOther + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_PMRE_ProvOther = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_PMRE_ProvOther.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_ProvOther = driver.switchTo().frame("ebx_SubSessioniFrame").findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_ProvOther.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_Pmre_ProvOther = driver.switchTo().frame("ebx_SubSessioniFrame").findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_Pmre_ProvOther.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_PMRE_ProvOther = driver.switchTo().frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_PMRE_ProvOther.size());

				// Verify the PMRE SysHouse attribute
				Thread.sleep(3000);
				String AssignedAttribute_PMRE_SysHouse = driver.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRE_SysHouse);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMRESysHouse_Screenshot = UtilLib.screenshot(driver,"Panel extension attribute value page for PMRE Sys House");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRE Sys House" + logger.addScreenCapture(PanelExtn_PMRESysHouse_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRE_SysHouse.contains("PMRE Sys House")) {
					logger.log(LogStatus.PASS,"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_SysHouse + "</b>");

					List<WebElement> AssignedAttribute_PMRE_SysHouse_Size = driver.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println("Assigned attribute value size is ===== " + AssignedAttribute_PMRE_SysHouse_Size.size());
					for (WebElement AssignedValues_Dropdown_SysHouse_PMRE : AssignedAttribute_PMRE_SysHouse_Size) {
						String AssignedAttribute_SysHouse_char_PMRE = AssignedValues_Dropdown_SysHouse_PMRE.getText().replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println("Assigned values for PMRE Sys House after removing the special character is : " + AssignedAttribute_SysHouse_char_PMRE);

						for (int f = 1; f < TestdataRowcount; f++) {
							String RDM_AttributeValue_PMRESysHouse = UtilLib.getCellData(xlFilePath, sheet,"RDM_AttributeValue_PMRE_Sys_House", f, "string");
							System.out.println("Assigned value is ******======:"+ AssignedValues_Dropdown_SysHouse_PMRE.getText());
							logger.log(LogStatus.PASS,"The assigned attribute value of PMRE SysHouse is : <b> "+ AssignedAttribute_SysHouse_char_PMRE+ " </b> is matching with the look up value <b> "+ RDM_AttributeValue_PMRESysHouse + "</b>");
							System.out.println("The assigned attribute value of PMRE SysHouse is : <b> " + AssignedAttribute_SysHouse_char_PMRE+ " </b> is matching with the look up value <b> " + RDM_AttributeValue_PMRESysHouse+ "</b>");
							break;
						}
					}

				} else {
					logger.log(LogStatus.FAIL,"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_SysHouse + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_PMRESysHouse = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_PMRESysHouse.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_PMRESysHouse = driver.switchTo().frame("ebx_SubSessioniFrame").findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_PMRESysHouse.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_PMRESysHouse = driver.switchTo().frame("ebx_SubSessioniFrame").findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_PMRESysHouse.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_PMRESysHouse = driver.switchTo().frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_PMRESysHouse.size());

				// Verify the PMRE TOC attribute
				Thread.sleep(3000);
				String AssignedAttribute_PMRE_TOC = driver.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRE_TOC);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMRE_TOC_Screenshot = UtilLib.screenshot(driver,"Panel extension attribute value page for PMRE TOC");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRE TOC"+ logger.addScreenCapture(PanelExtn_PMRE_TOC_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRE_TOC.contains("PMRE TOC")) {
					logger.log(LogStatus.PASS,"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_TOC + "</b>");

					List<WebElement> AssignedAttribute_PMRE_TOC_Size = driver.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println("Assigned attribute value size is ===== " + AssignedAttribute_PMRE_TOC_Size.size());

					for (WebElement AssignedValues_Dropdown_PMRETOC : AssignedAttribute_PMRE_TOC_Size) {
						String AssignedAttribute_PMRETOC_char = AssignedValues_Dropdown_PMRETOC.getText().replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println("Assigned values for PMRE TOC after removing the special character is : "+ AssignedAttribute_PMRETOC_char);

						for (int g = 1; g < TestdataRowcount; g++) {
							String RDM_AttributeValue_PMRETOC = UtilLib.getCellData(xlFilePath, sheet,"RDM_AttributeValue_PMRE_TOC", g, "string");
							if (RDM_AttributeValue_PMRETOC.contains(AssignedAttribute_PMRETOC_char)) {
								System.out.println("Assigned value is ******======:" + AssignedValues_Dropdown_PMRETOC.getText());
								logger.log(LogStatus.PASS,"The assigned attribute value of PMRE TOC is : <b> "+ AssignedAttribute_PMRETOC_char + " </b> is matching with the look up value <b> "+ RDM_AttributeValue_PMRETOC + "</b>");
								System.out.println("The assigned attribute value of PMRE TOC is : <b> " + AssignedAttribute_PMRETOC_char + " </b> is matching with the look up value <b> " + RDM_AttributeValue_PMRETOC + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,"The assigned attribute name is : <b> " + AssignedAttribute_PMRE_TOC + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				String PanelMember_PanelExtn_PMRE = UtilLib.screenshot(driver,
						"Values entered for extended attribute for panel code PMRE");
				logger.log(LogStatus.PASS, "Values entered for extended attribute for panel code <b> PMRE </b>"
						+ logger.addScreenCapture(PanelMember_PanelExtn_PMRE));

				break;

			case "PMRC":
				// click on expand icon of error
				driver.findElement(By.xpath(element.RDM_ExpandIcon_Error)).click();

				// validate the error message
				String Warning_OutletPanelMember_PMRC = driver.findElement(By.xpath(element.RDM_WarningMsg)).getText();
				System.out
				.println("Warning message for PMRC extended attribute is : " + Warning_OutletPanelMember_PMRC);
				logger.log(LogStatus.PASS, "Warning message for PMRC extended attribute is : <b> "
						+ Warning_OutletPanelMember_PMRC + " </b> ");

				// Collapse the warning/Error icon
				driver.findElement(By.xpath(element.RDM_CollapseIcon_Warning)).click();

				// Click on add +Icon for extended attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				driver.switchTo().frame(element.RDM_PanelExtensionAttribute_Frame);

				// verify PMRC Data status extended attribute
				Thread.sleep(3000);
				String AssignedAttribute_PMRC_DataStatus = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRC_DataStatus);

				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMRCDataStatus_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for PMRC Data Status");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRC Data status"
						+ logger.addScreenCapture(PanelExtn_PMRCDataStatus_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRC_DataStatus.contains("PMRC Data Status")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_DataStatus + "</b>");
					List<WebElement> AssignedAttribute_PMRC_DataStatus_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttributeDropdown_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_PMRC_DataStatus_Size.size());

					for (WebElement AssignedValues_Dropdown_PMRC_Data_Status : AssignedAttribute_PMRC_DataStatus_Size) {

						String AssignedAttribute_PMRC_DataStatus_char = AssignedValues_Dropdown_PMRC_Data_Status
								.getText().replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println(
								"Assigned values for PMRC Data Status after removing the special character is : "
										+ AssignedAttribute_PMRC_DataStatus_char);

						for (int u = 1; u < TestdataRowcount; u++) {
							String RDM_AttributeValue_PRMCDataStatus = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_PMRC_DataStatus", u, "string");

							if (RDM_AttributeValue_PRMCDataStatus.contains(AssignedAttribute_PMRC_DataStatus_char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_PMRC_Data_Status.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of PMRC data status is : <b> "
												+ AssignedAttribute_PMRC_DataStatus_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_PRMCDataStatus + "</b>");
								System.out.println("The assigned attribute value of PMRC data status is : <b> "
										+ AssignedAttribute_PMRC_DataStatus_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_PRMCDataStatus + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_DataStatus + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_PMRCDataStatus = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_PMRCDataStatus.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_PMRCDataStatus = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_PMRCDataStatus.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_PMRCDataStatus = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println(
						"id of 2nd frame -------" + iframe_outletPanelMember_PMRCDataStatus.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_PMRCDataStatus = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_PMRCDataStatus.size());

				// Verify the PMRC Group attribute
				Thread.sleep(3000);
				String AssignedAttribute_PMRC_Group = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRC_Group);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMRCGroup_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for PMRC Group Attribute");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRC group attribute"
						+ logger.addScreenCapture(PanelExtn_PMRCGroup_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRC_Group.contains("PMRC Group")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_Group + "</b>");

					List<WebElement> AssignedAttribute_PRMC_Group = driver
							.findElements(By.xpath(element.RDM_AssignedAttributeDropdown_Value));
					System.out.println("Assigned attribute value size is ===== " + AssignedAttribute_PRMC_Group.size());

					for (WebElement AssignedValues_Dropdown_PMRCGroup : AssignedAttribute_PRMC_Group) {
						String AssignedAttribute_PMRCGroup_char = AssignedValues_Dropdown_PMRCGroup.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println("Assigned values for PMRC Group after removing the special character is : "
								+ AssignedAttribute_PMRCGroup_char);

						for (int v = 1; v < TestdataRowcount; v++) {
							String RDM_AttributeValue_PMRCGroup = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_PRMC_Group", v, "string");
							if (RDM_AttributeValue_PMRCGroup.contains(AssignedAttribute_PMRCGroup_char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_PMRCGroup.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of PMRC Group is : <b> "
												+ AssignedAttribute_PMRCGroup_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_PMRCGroup + "</b>");
								System.out.println("The assigned attribute value of PMRC Group is : <b> "
										+ AssignedAttribute_PMRCGroup_char
										+ " </b> is matching with the look up value <b> " + RDM_AttributeValue_PMRCGroup
										+ "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_Group + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_PMRCGrp = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_PMRCGrp.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_PMRCGrp = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_PMRCGrp.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_PMRCGRP = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_PMRCGRP.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_PMRCGRP = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_PMRCGRP.size());

				// Verify the PMRC Part Group attribute
				Thread.sleep(3000);
				String AssignedAttribute_PMRC_PartGroup = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRC_PartGroup);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMRCPartGroup_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for PMRC Part Group");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRc Part Group"
						+ logger.addScreenCapture(PanelExtn_PMRCPartGroup_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRC_PartGroup.contains("PMRC Part Group")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_PartGroup + "</b>");

					List<WebElement> AssignedAttribute_PMRC_PartGroup_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttributeDropdown_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_PMRC_PartGroup_Size.size());

					for (WebElement AssignedValues_Dropdown_PMRCPartGroup : AssignedAttribute_PMRC_PartGroup_Size) {
						String AssignedAttribute_PMRE_PartGroup_char = AssignedValues_Dropdown_PMRCPartGroup.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println(
								"Assigned values for PMRC Part Group after removing the special character is : "
										+ AssignedAttribute_PMRE_PartGroup_char);
						for (int w = 1; w < TestdataRowcount; w++) {
							String RDM_AttributeValue_PMRC_PartGroup = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_PRMC_Part_Group", w, "string");
							if (RDM_AttributeValue_PMRC_PartGroup.contains(AssignedAttribute_PMRE_PartGroup_char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_PMRCPartGroup.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of PMRC Part Group is : <b> "
												+ AssignedAttribute_PMRE_PartGroup_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_PMRC_PartGroup + "</b>");
								System.out.println("The assigned attribute value of PMRC Part group is : <b> "
										+ AssignedAttribute_PMRE_PartGroup_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_PMRC_PartGroup + "</b>");
								break;
							}
						}
					}

				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_PartGroup + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_PMRC_Part_Group = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_PMRC_Part_Group.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_PMRC_PartGroup = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_PMRC_PartGroup.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_PMRC_PartGroup = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println(
						"id of 2nd frame -------" + iframe_outletPanelMember_PMRC_PartGroup.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_PMRC_Part_Group = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_PMRC_Part_Group.size());

				// Verify the PMRC Pharm Type attribute
				Thread.sleep(3000);
				String AssignedAttribute_PMRC_PharmType = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRC_PharmType);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMRC_PharmGroup_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for PMRC Pharm type");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRC Pharm type"
						+ logger.addScreenCapture(PanelExtn_PMRC_PharmGroup_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRC_PharmType.contains("PMRC Pharm Type")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_PharmType + "</b>");

					List<WebElement> AssignedAttribute_PMRC_PharmType_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttributeDropdown_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_PMRC_PharmType_Size.size());

					for (WebElement AssignedValues_Dropdown_PharmType_PMRC : AssignedAttribute_PMRC_PharmType_Size) {
						String AssignedAttribute_PharmType_PMRC_char = AssignedValues_Dropdown_PharmType_PMRC.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println(
								"Assigned values for PMRC Pharm Group after removing the special character is : "
										+ AssignedAttribute_PharmType_PMRC_char);
						for (int x = 1; x < TestdataRowcount; x++) {
							String RDM_AttributeValue_PMRCPharmType = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_PRMC_Pharm_Type", x, "string");
							if (RDM_AttributeValue_PMRCPharmType.contains(AssignedAttribute_PharmType_PMRC_char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_PharmType_PMRC.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of PMRC Pharm type is : <b> "
												+ AssignedAttribute_PharmType_PMRC_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_PMRCPharmType + "</b>");
								System.out.println("The assigned attribute value of PMRC Pharm type is : <b> "
										+ AssignedAttribute_PharmType_PMRC_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_PMRCPharmType + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_PharmType + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_PharmGrp_PMRC = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_PharmGrp_PMRC.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_PharmGrp_PMRC = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_PharmGrp_PMRC.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_PharmGrp_PMRC = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out
				.println("id of 2nd frame -------" + iframe_outletPanelMember_PharmGrp_PMRC.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_PharmGrp_PMRc = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_PharmGrp_PMRc.size());

				// Verify the PMRC Prov Others
				Thread.sleep(3000);
				String AssignedAttribute_PMRC_ProvOther = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRC_ProvOther);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMRC_ProvOther_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for PMRC Prov Other");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRC Prov Other"
						+ logger.addScreenCapture(PanelExtn_PMRC_ProvOther_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRC_ProvOther.contains("PMRC Prov Other")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_ProvOther + "</b>");

					List<WebElement> AssignedAttribute_ProvOther_PMRC_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_ProvOther_PMRC_Size.size());
					for (WebElement AssignedValues_Dropdown_ProvOther_PMRC : AssignedAttribute_ProvOther_PMRC_Size) {
						String AssignedAttribute_ProvOther_char_PMRC = AssignedValues_Dropdown_ProvOther_PMRC.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println(
								"Assigned values for PMRC Prov Other after removing the special character is : "
										+ AssignedAttribute_ProvOther_char_PMRC);
						for (int y = 1; y < TestdataRowcount; y++) {
							String RDM_AttributeValue_PMRCProv_Other = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_PMRC_Prov_Other", y, "string");
							if (RDM_AttributeValue_PMRCProv_Other.contains(AssignedAttribute_ProvOther_char_PMRC)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_ProvOther_PMRC.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of PMRC Prov Other is : <b> "
												+ AssignedAttribute_ProvOther_char_PMRC
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_PMRCProv_Other + "</b>");
								System.out.println("The assigned attribute value of PMRC Prov Other is : <b> "
										+ AssignedAttribute_ProvOther_char_PMRC
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_PMRCProv_Other + "</b>");
								break;
							}
						}

					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_ProvOther + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_PMRC_ProvOther = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_PMRC_ProvOther.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_ProvOther_PMRc = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_ProvOther_PMRc.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_Pmrc_ProvOther = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println(
						"id of 2nd frame -------" + iframe_outletPanelMember_Pmrc_ProvOther.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_PMRC_ProvOther = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_PMRC_ProvOther.size());

				// Verify the PMRC SysHouse attribute
				Thread.sleep(3000);
				String AssignedAttribute_PMRC_SysHouse = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRC_SysHouse);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMRCSysHouse_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for PMRC Sys House");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRC Sys House"
						+ logger.addScreenCapture(PanelExtn_PMRCSysHouse_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRC_SysHouse.contains("PMRC Sys House")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_SysHouse + "</b>");

					List<WebElement> AssignedAttribute_PMRC_SysHouse_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_PMRC_SysHouse_Size.size());
					for (WebElement AssignedValues_Dropdown_SysHouse_PMRC : AssignedAttribute_PMRC_SysHouse_Size) {
						String AssignedAttribute_SysHouse_char_PMRC = AssignedValues_Dropdown_SysHouse_PMRC.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out
						.println("Assigned values for PMRC Sys House after removing the special character is : "
								+ AssignedAttribute_SysHouse_char_PMRC);

						for (int z = 1; z < TestdataRowcount; z++) {
							String RDM_AttributeValue_PMRCSysHouse = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_PMRC_Sys_House", z, "string");
							System.out.println("Assigned value is ******======:"
									+ AssignedValues_Dropdown_SysHouse_PMRC.getText());
							logger.log(LogStatus.PASS,
									"The assigned attribute value of PMRC SysHouse is : <b> "
											+ AssignedAttribute_SysHouse_char_PMRC
											+ " </b> is matching with the look up value <b> "
											+ RDM_AttributeValue_PMRCSysHouse + "</b>");
							System.out.println("The assigned attribute value of PMRC SysHouse is : <b> "
									+ AssignedAttribute_SysHouse_char_PMRC
									+ " </b> is matching with the look up value <b> " + RDM_AttributeValue_PMRCSysHouse
									+ "</b>");
							break;
						}
					}

				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_SysHouse + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_PMRCSysHouse = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_PMRCSysHouse.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_PMRCSysHouse = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_PMRCSysHouse.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_PMRCSysHouse = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out
				.println("id of 2nd frame -------" + iframe_outletPanelMember_PMRCSysHouse.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_PMRCSysHouse = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_PMRCSysHouse.size());

				// Verify the PMRC TOC attribute
				Thread.sleep(3000);
				String AssignedAttribute_PMRC_TOC = driver.findElement(By.xpath(element.RDM_AssignedAttributeSelected))
						.getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRC_TOC);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_PMRC_TOC_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for PMRC TOC");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for PMRC TOC"
						+ logger.addScreenCapture(PanelExtn_PMRC_TOC_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_PMRC_TOC.contains("PMRC TOC")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_TOC + "</b>");

					List<WebElement> AssignedAttribute_PMRC_TOC_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_PMRC_TOC_Size.size());

					for (WebElement AssignedValues_Dropdown_PMRCTOC : AssignedAttribute_PMRC_TOC_Size) {
						String AssignedAttribute_PMRCTOC_char = AssignedValues_Dropdown_PMRCTOC.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println("Assigned values for PMRC TOC after removing the special character is : "
								+ AssignedAttribute_PMRCTOC_char);

						for (int g = 1; g < TestdataRowcount; g++) {
							String RDM_AttributeValue_PMRCTOC = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_PMRC_TOC", g, "string");
							if (RDM_AttributeValue_PMRCTOC.contains(AssignedAttribute_PMRCTOC_char)) {
								System.out.println(
										"Assigned value is ******======:" + AssignedValues_Dropdown_PMRCTOC.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of PMRC TOC is : <b> "
												+ AssignedAttribute_PMRCTOC_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_PMRCTOC + "</b>");
								System.out.println("The assigned attribute value of PMRC TOC is : <b> "
										+ AssignedAttribute_PMRCTOC_char
										+ " </b> is matching with the look up value <b> " + RDM_AttributeValue_PMRCTOC
										+ "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_PMRC_TOC + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				String PanelMember_PanelExtn_PMRC = UtilLib.screenshot(driver,
						"Values entered for extended attribute for panel code PMRC");
				logger.log(LogStatus.PASS, "Values entered for extended attribute for panel code <b> PMRC </b>"
						+ logger.addScreenCapture(PanelMember_PanelExtn_PMRC));

				break;

			case "IRPMR":
				// click on expand icon of error
				driver.findElement(By.xpath(element.RDM_ExpandIcon_Error)).click();

				// validate the error message
				String Warning_OutletPanelMember_IRPMR = driver.findElement(By.xpath(element.RDM_WarningMsg)).getText();
				System.out.println(
						"Warning message for IRPMR extended attribute is : " + Warning_OutletPanelMember_IRPMR);
				logger.log(LogStatus.PASS, "Warning message for IRPMR extended attribute is : <b> "
						+ Warning_OutletPanelMember_IRPMR + " </b> ");

				// Click on add +Icon for extended attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				driver.switchTo().frame(element.RDM_PanelExtensionAttribute_Frame);

				// verify IRPMR Data Status
				Thread.sleep(3000);
				String AssignedAttribute_IRPMR_DataStatus = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_IRPMR_DataStatus);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_IRPMRDataStatus_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for IRPMR Data Status");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for IRPMR Data status"
						+ logger.addScreenCapture(PanelExtn_IRPMRDataStatus_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_IRPMR_DataStatus.contains("IRPMR Data Status")) {

					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_IRPMR_DataStatus + "</b>");

					((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight,0)");
					List<WebElement> AssignedAttribute_DataStatus = driver
							.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println("Assigned attribute value size is ===== " + AssignedAttribute_DataStatus.size());

					for (WebElement AssignedValues_Dropdown_Data_Status : AssignedAttribute_DataStatus) {
						String AssignedAttribute_DataStatus_char = AssignedValues_Dropdown_Data_Status.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println(
								"Assigned values for IRPMR Data Status after removing the special character is : "
										+ AssignedAttribute_DataStatus_char);

						for (int k = 1; k < TestdataRowcount; k++) {
							String RDM_AttributeValue_IRPMRDataStatus = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_IRPMR_DataStatus", k, "string");
							if (RDM_AttributeValue_IRPMRDataStatus.contains(AssignedAttribute_DataStatus_char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_Data_Status.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of IRPMR data status is : <b> "
												+ AssignedAttribute_DataStatus_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_IRPMRDataStatus + "</b>");
								System.out.println("The assigned attribute value of IRPMR data status is : <b> "
										+ AssignedAttribute_DataStatus_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_IRPMRDataStatus + "</b>");
								break;
							}

						}
					}

				}
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_IRPMR = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_IRPMR.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_IRPMR = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_IRPMR.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_IRPMR = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_IRPMR.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_IRPMR = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_IRPMR.size());

				// Verify the IRPMR SysHouse attribute
				Thread.sleep(3000);
				String AssignedAttribute_IRPMR = driver.findElement(By.xpath(element.RDM_AssignedAttributeSelected))
						.getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_IRPMR);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_IRPMRSysHouse_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for IRPMR Sys House");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for IRPMR Sys House"
						+ logger.addScreenCapture(PanelExtn_IRPMRSysHouse_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_IRPMR.contains("IRPMR SysHouse")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_IRPMR + "</b>");

					List<WebElement> AssignedAttribute_SysHouse = driver
							.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println("Assigned attribute value size is ===== " + AssignedAttribute_SysHouse.size());

					for (WebElement AssignedValues_Dropdown_SysHouse : AssignedAttribute_SysHouse) {
						String AssignedAttribute_SysHouse_char = AssignedValues_Dropdown_SysHouse.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println(
								"Assigned values for IRPMR Sys House after removing the special character is : "
										+ AssignedAttribute_SysHouse_char);
						for (int l = 1; l < TestdataRowcount; l++) {

							String RDM_AttributeValue_IRPMRSysHouse = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_IRPMR_SysHouse", l, "string");
							if (RDM_AttributeValue_IRPMRSysHouse.contains(AssignedAttribute_SysHouse_char)) {

								System.out.println(
										"Assigned value is ******======:" + AssignedValues_Dropdown_SysHouse.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of IRPMR SysHouse is : <b> "
												+ AssignedAttribute_SysHouse_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_IRPMRSysHouse + "</b>");
								Reporter.log("The assigned attribute value of IRPMR SysHouse is : <b> "
										+ AssignedAttribute_SysHouse_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_IRPMRSysHouse + "</b>");
								System.out.println("The assigned attribute value of IRPMR SysHouse is : <b> "
										+ AssignedAttribute_SysHouse_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_IRPMRSysHouse + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_IRPMR + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				String PanelMember_PanelExtn_IRPMR = UtilLib.screenshot(driver,
						"Values entered for extended attribute for panel code IRPMR");
				logger.log(LogStatus.PASS, "Values entered for extended attribute for panel code <b> IRPMR </b>"
						+ logger.addScreenCapture(PanelMember_PanelExtn_IRPMR));

				break;

			case "SUPPL":
				// click on expand icon of error
				driver.findElement(By.xpath(element.RDM_ExpandIcon_Error)).click();

				// validate the error message
				String Warning_OutletPanelMember_SUPPL = driver.findElement(By.xpath(element.RDM_WarningMsg)).getText();
				System.out.println(
						"Warning message for SUPPL extended attribute is : " + Warning_OutletPanelMember_SUPPL);
				logger.log(LogStatus.PASS, "Warning message for SUPPL extended attribute is : <b> "
						+ Warning_OutletPanelMember_SUPPL + " </b> ");

				// collapse the error/warning icon
				driver.findElement(By.xpath(element.RDM_CollapseIcon_Warning)).click();

				// Click on add +Icon for extended attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				driver.switchTo().frame(element.RDM_PanelExtensionAttribute_Frame);

				// verify PMRE Data status extended attribute
				Thread.sleep(3000);
				String AssignedAttribute_Suppl_Auto_bex = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_Suppl_Auto_bex);

				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_Suppl_Auto_bex_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for Suppl Auto bex");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for Suppl Auto bex"
						+ logger.addScreenCapture(PanelExtn_Suppl_Auto_bex_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_Suppl_Auto_bex.contains("SUPPL AUTO_BEX")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Auto_bex + "</b>");

					List<WebElement> AssignedAttribute_Suppl_Auto_Bex = driver
							.findElements(By.xpath(element.RDM_AssignedAttributeDropdown_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_Suppl_Auto_Bex.size());

					for (WebElement AssignedValues_Dropdown_Suppl_AutoBEX : AssignedAttribute_Suppl_Auto_Bex) {
						String AssignedAttribute_Suppl_Autobex_char = AssignedValues_Dropdown_Suppl_AutoBEX.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out
						.println("Assigned values for Suppl auto bex after removing the special character is : "
								+ AssignedAttribute_Suppl_Autobex_char);
						for (int m = 1; m < TestdataRowcount; m++) {
							String RDM_AttributeValue_Suppl_Auto_Bex = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_Suppl_Auto_BEX", m, "string");
							if (RDM_AttributeValue_Suppl_Auto_Bex.contains(AssignedAttribute_Suppl_Autobex_char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_Suppl_AutoBEX.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of Supply Auto BEX is : <b> "
												+ AssignedAttribute_Suppl_Autobex_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_Suppl_Auto_Bex + "</b>");
								System.out.println("The assigned attribute value of Supply Auto BEX is : <b> "
										+ AssignedAttribute_Suppl_Autobex_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_Suppl_Auto_Bex + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Auto_bex + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_Suppl_Auto = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_Suppl_Auto.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_Suppl_Auto = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_Suppl_Auto.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_Suppl_Auto = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_Suppl_Auto.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_Suppl_Auto = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_Suppl_Auto.size());

				// Verify the Suppl data st attribute
				Thread.sleep(3000);
				String AssignedAttribute_Suppl_Data = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_Suppl_Data);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_SupplData_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for Suppl data ST");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for Suppl data ST"
						+ logger.addScreenCapture(PanelExtn_SupplData_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_Suppl_Data.contains("SUPPL DATA_ST")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Data + "</b>");

					List<WebElement> AssignedAttribute_Suppl_ST_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttributeDropdown_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_Suppl_ST_Size.size());

					for (WebElement AssignedValues_Dropdown_SupplData_ST : AssignedAttribute_Suppl_ST_Size) {
						String AssignedAttribute_Suppl_Data_ST_Char = AssignedValues_Dropdown_SupplData_ST.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out
						.println("Assigned values for Suppl Data ST after removing the special character is : "
								+ AssignedAttribute_Suppl_Data_ST_Char);
						for (int n = 1; n < TestdataRowcount; n++) {
							String RDM_AttributeValue_Suppl_Data_ST = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_Suppl_Data_ST", n, "string");
							if (RDM_AttributeValue_Suppl_Data_ST.contains(AssignedAttribute_Suppl_Data_ST_Char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_SupplData_ST.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of SUPPL Data ST is : <b> "
												+ AssignedAttribute_Suppl_Data_ST_Char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_Suppl_Data_ST + "</b>");
								System.out.println("The assigned attribute value of SUPPl Data is : <b> "
										+ AssignedAttribute_Suppl_Data_ST_Char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_Suppl_Data_ST + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Data + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_SupplData = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_SupplData.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_SupplData = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_SupplData.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_SupplData = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_SupplData.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_SupplData = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_SupplData.size());

				// Verify the SUPPl direct FL
				Thread.sleep(3000);
				String AssignedAttribute_Suppl_Direct_FL = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_Suppl_Direct_FL);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_Suppl_Direct_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for Suppl Direct FL");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for Suppl Direct FL"
						+ logger.addScreenCapture(PanelExtn_Suppl_Direct_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_Suppl_Direct_FL.contains("SUPPL DIRECT_FL")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Direct_FL + "</b>");

					List<WebElement> AssignedAttribute_Suppl_Direct_FL_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttributeDropdown_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_Suppl_Direct_FL_Size.size());

					for (WebElement AssignedValues_Dropdown_Suppl_Direct_FL : AssignedAttribute_Suppl_Direct_FL_Size) {
						String AssignedAttribute_Suppl_Direct_FT_char = AssignedValues_Dropdown_Suppl_Direct_FL
								.getText().replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println(
								"Assigned values for Suppl direct FL after removing the special character is : "
										+ AssignedAttribute_Suppl_Direct_FT_char);
						for (int o = 1; o < TestdataRowcount; o++) {
							String RDM_AttributeValue_Suppl_Direct_FT = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_Suppl_Direct_FL", o, "string");
							if (RDM_AttributeValue_Suppl_Direct_FT.contains(AssignedAttribute_Suppl_Direct_FT_char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_Suppl_Direct_FL.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of Suppl direct FL is : <b> "
												+ AssignedAttribute_Suppl_Direct_FT_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_Suppl_Direct_FT + "</b>");
								System.out.println("The assigned attribute value of Suppl direct FL is : <b> "
										+ AssignedAttribute_Suppl_Direct_FT_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_Suppl_Direct_FT + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Direct_FL + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_Suppl_Data = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_Suppl_Data.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_Suppl_Data = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_Suppl_Data.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_Suppl_Data = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_Suppl_Data.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_Suppl_Data = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_Suppl_Data.size());

				// Verify the SUPPl EXCL EI attribute
				Thread.sleep(3000);
				String AssignedAttribute_Suppl_Excl_EI = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_Suppl_Excl_EI);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_SupplExcl_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for Suppl Excl EI");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for Suppl Excl EI"
						+ logger.addScreenCapture(PanelExtn_SupplExcl_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_Suppl_Excl_EI.contains("SUPPL EXCL_EI")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Excl_EI + "</b>");

					List<WebElement> AssignedAttribute_SUPPL_Excl_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttributeDropdown_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_SUPPL_Excl_Size.size());

					for (WebElement AssignedValues_Dropdown_Suppl_Excl : AssignedAttribute_SUPPL_Excl_Size) {
						String AssignedAttribute_Suppl_Excl_char = AssignedValues_Dropdown_Suppl_Excl.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out
						.println("Assigned values for Suppl Excl EI after removing the special character is : "
								+ AssignedAttribute_Suppl_Excl_char);
						for (int p = 1; p < TestdataRowcount; p++) {
							String RDM_AttributeValue_Suppl_Excl = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_Suppl_EXCL_EI", p, "string");
							if (RDM_AttributeValue_Suppl_Excl.contains(AssignedAttribute_Suppl_Excl_char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_Suppl_Excl.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of Suppl Excl EI is : <b> "
												+ AssignedAttribute_Suppl_Excl_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_Suppl_Excl + "</b>");
								System.out.println("The assigned attribute value of Suppl Excl EI is : <b> "
										+ AssignedAttribute_Suppl_Excl_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_Suppl_Excl + "</b>");
								break;
							}

						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Excl_EI + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_ExcelEI = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_ExcelEI.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_ExcelEI = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_ExcelEI.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_ExcelEI = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_ExcelEI.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_ExcelEI = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_ExcelEI.size());

				// Verify the Suppl Excl out attribute
				Thread.sleep(3000);
				String AssignedAttribute_Suppl_Excel_Out = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_Suppl_Excel_Out);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_Suppl_Excl_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for Suppl Excel Out");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for Suppl Excel Out"
						+ logger.addScreenCapture(PanelExtn_Suppl_Excl_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_Suppl_Excel_Out.contains("SUPPL EXCL_OUT")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Excel_Out + "</b>");

					List<WebElement> AssignedAttribute_Suppl_Excl_Out = driver
							.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_Suppl_Excl_Out.size());

					for (WebElement AssignedValues_Dropdown_ExclOut : AssignedAttribute_Suppl_Excl_Out) {
						String AssignedAttribute_ExclOut_char = AssignedValues_Dropdown_ExclOut.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out
						.println("Assigned values for Suppl Excl out after removing the special character is : "
								+ AssignedAttribute_ExclOut_char);
						for (int q = 1; q < TestdataRowcount; q++) {
							String RDM_AttributeValue_ExclOut_Other = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_Suppl_EXCL_Out", q, "string");
							if (RDM_AttributeValue_ExclOut_Other.contains(AssignedAttribute_ExclOut_char)) {
								System.out.println(
										"Assigned value is ******======:" + AssignedValues_Dropdown_ExclOut.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of Suppl Excl Out is : <b> "
												+ AssignedAttribute_ExclOut_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_ExclOut_Other + "</b>");
								System.out.println("The assigned attribute value of Suppl Excl Out  is : <b> "
										+ AssignedAttribute_ExclOut_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_ExclOut_Other + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Excel_Out + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_SupplStream = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_SupplStream.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_SupplStream = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_SupplStream.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMemberSuppl = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMemberSuppl.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_Suppl = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_Suppl.size());

				// Verify the Suppl Stream attribute
				Thread.sleep(3000);
				String AssignedAttribute_Suppl_Stream = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_Suppl_Stream);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_Suppl_Stream_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for Suppl Stream");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for Suppl Stream"
						+ logger.addScreenCapture(PanelExtn_Suppl_Stream_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_Suppl_Stream.contains("SUPPL STREAM")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Stream + "</b>");

					List<WebElement> AssignedAttribute_SupplStream_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_SupplStream_Size.size());
					for (WebElement AssignedValues_Dropdown_Suppl_Stream : AssignedAttribute_SupplStream_Size) {
						String AssignedAttribute_Stream_char_Suppl = AssignedValues_Dropdown_Suppl_Stream.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println("Assigned values for Suppl stream after removing the special character is : "
								+ AssignedAttribute_Stream_char_Suppl);
						for (int r = 1; r < TestdataRowcount; r++) {
							String RDM_AttributeValue_Suppl_Stream = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_Suppl_Stream", r, "string");
							if (RDM_AttributeValue_Suppl_Stream.contains(AssignedAttribute_Stream_char_Suppl)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_Suppl_Stream.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of Suppl Stream is : <b> "
												+ AssignedAttribute_Stream_char_Suppl
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_Suppl_Stream + "</b>");
								System.out.println("The assigned attribute value of Suppl Stream is : <b> "
										+ AssignedAttribute_Stream_char_Suppl
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_Suppl_Stream + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Stream + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_Suppl_st = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_Suppl_st.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_Suppl_St = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_Suppl_St.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_Suppl_St = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_Suppl_St.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_Suppl_St = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_Suppl_St.size());

				// Verify the Suppl WK MTH attribute
				Thread.sleep(3000);
				String AssignedAttribute_Suppl_WKMTH = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_Suppl_WKMTH);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_Suppl_WKMTH_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for SUPPL WK_MTH");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for SUPPL WK_MTH"
						+ logger.addScreenCapture(PanelExtn_Suppl_WKMTH_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_Suppl_WKMTH.contains("SUPPL WK_MTH")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_WKMTH + "</b>");

					List<WebElement> AssignedAttribute_Suppl_WKMTH_Size = driver
							.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_Suppl_WKMTH_Size.size());

					for (WebElement AssignedValues_Dropdown_SupplMTH : AssignedAttribute_Suppl_WKMTH_Size) {
						String AssignedAttribute_SupplMTH_char = AssignedValues_Dropdown_SupplMTH.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out.println("Assigned values for Suppl MTH after removing the special character is : "
								+ AssignedAttribute_SupplMTH_char);
						for (int s = 1; s < TestdataRowcount; s++) {
							String RDM_AttributeValue_Suppl_WKMTH = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_Suppl_WK_MTH", s, "string");
							if (RDM_AttributeValue_Suppl_WKMTH.contains(AssignedAttribute_SupplMTH_char)) {
								System.out.println(
										"Assigned value is ******======:" + AssignedValues_Dropdown_SupplMTH.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of Suppl WK_MTH is : <b> "
												+ AssignedAttribute_SupplMTH_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_Suppl_WKMTH + "</b>");
								System.out.println("The assigned attribute value of Suppl WK_MTH is : <b> "
										+ AssignedAttribute_SupplMTH_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_Suppl_WKMTH + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_WKMTH + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				// Switching to outer panel member
				driver.switchTo().defaultContent();

				WebElement frameText_Suppl_WKMTH = driver.findElement(By.tagName("iframe"));
				System.out.println("**********" + frameText_Suppl_WKMTH.getAttribute("cd_frame_id_"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);

				WebElement iframeElements_WKMTH = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframeElements_WKMTH.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// Switching the frame to panel extension attribute
				driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

				Thread.sleep(7000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
				.frame(element.RDM_WorkPlace_TableFrame);
				WebElement iframe_outletPanelMember_Suppl_WKMTH = driver.switchTo().frame("ebx_SubSessioniFrame")
						.findElement(By.tagName("iframe"));
				System.out.println("id of 2nd frame -------" + iframe_outletPanelMember_Suppl_WKMTH.getAttribute("id"));

				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				List<WebElement> iframe_panelExtension_Suppl_MTH = driver.switchTo()
						.frame(element.RDM_OuterPanelMember_Frame).findElements(By.tagName("iframe"));
				System.out.println("id of 3rd frame size --- " + iframe_panelExtension_Suppl_MTH.size());

				// Verify the Suppl WSM SUP attribute
				Thread.sleep(3000);
				String AssignedAttribute_Suppl_Wsp_Sup = driver
						.findElement(By.xpath(element.RDM_AssignedAttributeSelected)).getText();
				System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_Suppl_Wsp_Sup);

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				String PanelExtn_Suppl_Wsp_Screenshot = UtilLib.screenshot(driver,
						"Panel extension attribute value page for Suppl WSM SUP");
				logger.log(LogStatus.PASS, "Panel extension attribute value page for Suppl WSM SUP"
						+ logger.addScreenCapture(PanelExtn_Suppl_Wsp_Screenshot));

				driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
				Thread.sleep(4000);

				if (AssignedAttribute_Suppl_Wsp_Sup.contains("SUPPL WSM_SUP")) {
					logger.log(LogStatus.PASS,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Wsp_Sup + "</b>");

					List<WebElement> AssignedAttribute_Suppl_WSM_SUPSize = driver
							.findElements(By.xpath(element.RDM_AssignedAttribute_Value));
					System.out.println(
							"Assigned attribute value size is ===== " + AssignedAttribute_Suppl_WSM_SUPSize.size());

					for (WebElement AssignedValues_Dropdown_Suppl_WSM_SUP : AssignedAttribute_Suppl_WSM_SUPSize) {
						String AssignedAttribute_Suppl_WSM_SUP_char = AssignedValues_Dropdown_Suppl_WSM_SUP.getText()
								.replaceAll("[^a-zA-Z0-9]-,", " ");
						System.out
						.println("Assigned values for Suppl WSM SUP after removing the special character is : "
								+ AssignedAttribute_Suppl_WSM_SUP_char);

						for (int t = 1; t < TestdataRowcount; t++) {
							String RDM_AttributeValue_Suppl_WSM_SUP = UtilLib.getCellData(xlFilePath, sheet,
									"RDM_AttributeValue_Suppl_WSM_SUP", t, "string");
							if (RDM_AttributeValue_Suppl_WSM_SUP.contains(AssignedAttribute_Suppl_WSM_SUP_char)) {
								System.out.println("Assigned value is ******======:"
										+ AssignedValues_Dropdown_Suppl_WSM_SUP.getText());
								logger.log(LogStatus.PASS,
										"The assigned attribute value of Suppl WSM SUP is : <b> "
												+ AssignedAttribute_Suppl_WSM_SUP_char
												+ " </b> is matching with the look up value <b> "
												+ RDM_AttributeValue_Suppl_WSM_SUP + "</b>");
								System.out.println("The assigned attribute value of Suppl WSM SUP is : <b> "
										+ AssignedAttribute_Suppl_WSM_SUP_char
										+ " </b> is matching with the look up value <b> "
										+ RDM_AttributeValue_Suppl_WSM_SUP + "</b>");
								break;
							}
						}
					}
				} else {
					logger.log(LogStatus.FAIL,
							"The assigned attribute name is : <b> " + AssignedAttribute_Suppl_Wsp_Sup + "</b>");
				}

				driver.findElement(By.xpath(element.RDM_AssignedValue_FirstSelection)).click();

				driver.findElement(By.xpath(element.RDM_SaveButton)).click();
				Thread.sleep(1000);

				// click on cross symbol
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(9000);

				String PanelMember_PanelExtn_Suppl = UtilLib.screenshot(driver,
						"Values entered for extended attribute for panel code Suppl");
				logger.log(LogStatus.PASS, "Values entered for extended attribute for panel code <b> Suppl </b>"
						+ logger.addScreenCapture(PanelMember_PanelExtn_Suppl));

				break;
			default:
				break;
			}

			// switching to default content and switching outlet panel member
			driver.switchTo().defaultContent();

			WebElement frameText_member = driver.findElement(By.tagName("iframe"));
			System.out.println("**********" + frameText_member.getAttribute("cd_frame_id_"));
			Thread.sleep(4000);
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
			.frame(element.RDM_WorkPlace_TableFrame);

			WebElement iframeElements_member = driver.switchTo().frame("ebx_SubSessioniFrame")
					.findElement(By.tagName("iframe"));
			System.out.println("id of 2nd frame -------" + iframeElements_member.getAttribute("id"));

			driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

			// Click on Save button
			Thread.sleep(2000);
			driver.findElement(By.xpath(element.RDM_SaveButton)).click();

			driver.switchTo().defaultContent();
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
			.frame(element.RDM_WorkPlace_TableFrame);

			// Click on Accept button
			driver.findElement(By.xpath(element.RDM_Acceptbutton)).click();

			String Accept_scrrenshot = UtilLib.screenshot(driver,
					"Accept pop up after entering the details of extended attribute");
			logger.log(LogStatus.PASS, "Accept pop up after entering the details of extended attribute"
					+ logger.addScreenCapture(Accept_scrrenshot));

			// handling accept pop up
			driver.findElement(By.xpath(element.RDM_OK_Button)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(10000);

			String PanelInsertion_Screencapture = UtilLib.screenshot(driver,
					"User has successfully performed the panel insertion for panel code ");
			logger.log(LogStatus.PASS, "User has successfully performed the panel insertion for panel code <b> " + Panel
					+ "</b>" + logger.addScreenCapture(PanelInsertion_Screencapture));

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_TechnicalView_Validation
	 * @Description : Verification of one key ID in technical view for the panel
	 *              which is inserted
	 ********************************************************************************************/
	public static boolean RDM_TechnicalView_Validation(ExtentReports report, ExtentTest logger, String Panel)
			throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, 150);
		try {
			driver.findElement(By.xpath(element.RDM_TechView)).click();

			Thread.sleep(2000);
			// Switch to the frame
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			// Click on Panel
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Panel)));
			driver.findElement(By.xpath(element.RDM_Panel)).click();

			// Select the Panel Member
			Thread.sleep(2000);
			driver.findElement(By.xpath(element.RDM_Panel_Member)).click();
			Thread.sleep(3000);

			String NavigationWorkplace_TV = UtilLib.screenshot(driver, "User is successfully navigated to technical view Workplace page");
			logger.log(LogStatus.PASS, "User is successfully navigated to technical view Workplace page"+ logger.addScreenCapture(NavigationWorkplace_TV));

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_SearchIcon)));

			// Click on Search Icon in work place page of technical view
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// select the panel code from technical view
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_panelcode)));

			Select SourceSystemKey = new Select(driver.findElement(By.xpath(element.RDM_panelcode)));
			SourceSystemKey.selectByVisibleText("    Source system key");

			// Enter One key ID for which the panel is inserted
			Thread.sleep(3000);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Enter_Panelcode)));
			driver.findElement(By.xpath(element.RDM_Enter_Panelcode)).sendKeys(OneKeyIdValue);

			// Click on Apply
			Thread.sleep(2000);
			driver.findElement(By.xpath(element.RDM_Click_Apply)).click();

			Thread.sleep(4000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// Scroll till source creation time stamp and click on sorting ICon
			Thread.sleep(3000);
			WebElement src_creation_record = driver.findElement(By.xpath(element.RDM_SourceCreation_FirstRow));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", src_creation_record);

			WebElement SourceCreation = driver.findElement(By.xpath(element.RDM_SourceCreation_TimeStamp_HeaderName));
			WebElement sortingicon_sourcecreationdate = driver
					.findElement(By.xpath(element.RDM_SortButton_SourceCreation_Timestamp));

			Actions action_src_creation = new Actions(driver);
			action_src_creation.moveToElement(SourceCreation).perform();
			Thread.sleep(3000);

			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", sortingicon_sourcecreationdate);

			Thread.sleep(3000);

			JavascriptExecutor executor1 = (JavascriptExecutor) driver;
			executor1.executeScript("arguments[0].click();", sortingicon_sourcecreationdate);

			// click on the first row for which the table is sorted in
			// descending order
			Thread.sleep(2000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			driver.findElement(By.xpath(element.RDM_FirstRowCheckbox)).click();

			String Navigation_OneKey_TechView = UtilLib.screenshot(driver,
					"Filtered the values in the search window from technical view based on the One Key ID");
			logger.log(LogStatus.PASS,
					"Filtered the values in the search window from technical view based on the oneKey ID"
							+ logger.addScreenCapture(Navigation_OneKey_TechView));

			// Click on Actions
			Thread.sleep(3000);
			WebElement ActionButton_Element = driver.findElement(By.xpath(element.RDM_ActionsTab_TechnicalView));
			Actions action_button_click = new Actions(driver);
			action_button_click.doubleClick(ActionButton_Element).perform();

			// Click on view history
			driver.findElement(By.xpath(element.RDM_ViewHistory_value)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// Navigated to Panel member of technical view
			Thread.sleep(5000);
			String Navigation_OneKey_TechView_PanelMember = UtilLib.screenshot(driver,
					"Record of the panel insertion in technical view");
			logger.log(LogStatus.PASS, "Record of the panel insertion in technical view"
					+ logger.addScreenCapture(Navigation_OneKey_TechView_PanelMember));

			driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);
			Thread.sleep(5000);
			WebElement panelmember_click = driver.findElement(By.xpath(element.RDM_PanelMember_FirstRow_CheckBox));
			Actions action_firstRow = new Actions(driver);
			action_firstRow.doubleClick(panelmember_click).perform();

			// Expand the source system ID
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(4000);
			driver.findElement(By.xpath(element.RDM_SourceSystemID_ExpandIcon)).click();

			String Details_OneKey_TechView_PanelMember = UtilLib.screenshot(driver,
					"Details of One key ID in technical view for which the panel is inserted");
			logger.log(LogStatus.PASS, "Details of One key ID in technical view for which the panel is inserted"
					+ logger.addScreenCapture(Details_OneKey_TechView_PanelMember));

			// operation type value
			String OpertaionType_Value = driver.findElement(By.xpath(element.RDM_OpertaionType_Value)).getText();
			System.out.println("Operation type value is : " + OpertaionType_Value);
			logger.log(LogStatus.PASS, "Operation type value is : <b> " + OpertaionType_Value + "</b>");

			// Retrieve the source transaction time stamp value and verify the
			// value with panel inserted date
			String SourcetransactionTimeStamp_value = driver
					.findElement(By.xpath(element.RDM_SourceTransactionTimeStamp_Value)).getText();
			if (SourcetransactionTimeStamp_value.contains(Runtime_date)) {
				logger.log(LogStatus.PASS,
						"Source transaction time stamp value is : <b> " + SourcetransactionTimeStamp_value
						+ " </b> in technical view is matching to the date of the panel insertion <b> "
						+ Runtime_date + "</b>");
				System.out.println("Source transaction time stamp value is : <b> " + SourcetransactionTimeStamp_value
						+ " </b> in technical view is matching to the date of the panel insertion <b> " + Runtime_date
						+ "</b>");
			} else {
				logger.log(LogStatus.FAIL,
						"Source transaction time stamp value is : <b> " + SourcetransactionTimeStamp_value
						+ " </b> in technical view is not matching to the date of the panel insertion <b> "
						+ Runtime_date + "</b>");
				System.out.println("Source transaction time stamp value is : " + SourcetransactionTimeStamp_value
						+ "in technical view is not matching to the date of the panel insertion" + Runtime_date);
			}

			// Retrieve the source creation time stamp value and verify the
			// value with panel inserted date
			String SourceCreationTimeStamp_value = driver
					.findElement(By.xpath(element.RDM_SourceCreationTimeStamp_Value)).getText();
			if (SourceCreationTimeStamp_value.contains(Runtime_date)) {
				logger.log(LogStatus.PASS,
						"Source creation time stamp value is : <b> " + SourceCreationTimeStamp_value
						+ " </b> in technical view is matching to the date of the panel insertion <b> "
						+ Runtime_date + "</b>");
				System.out.println("Source creation time stamp value is : " + SourceCreationTimeStamp_value
						+ " </b> in technical view is matching to the date of the panel insertion <b> " + Runtime_date
						+ " </b>");
			} else {
				logger.log(LogStatus.FAIL,
						"Source creation time stamp value is :  <b> " + SourceCreationTimeStamp_value
						+ " </b> in technical view is not matching to the date of the panel insertion <b> "
						+ Runtime_date + "</b>");
				System.out.println("Source creation time stamp value is : <b>" + SourceCreationTimeStamp_value
						+ " </b> in technical view is not matching to the date of the panel insertion <b> "
						+ Runtime_date + "</b>");
			}

			// Retrieve the date effective from value and verify the value with
			// panel inserted date
			String DateEffectiveFrom_value = driver.findElement(By.xpath(element.RDM_DateEffectiveFrom_Value))
					.getText();
			if (DateEffectiveFrom_value.contains(Runtime_date)) {
				logger.log(LogStatus.PASS,
						"Date effective from value is : <b>  " + DateEffectiveFrom_value
						+ " </b> in technical view is matching to the date of the panel insertion <b> "
						+ Runtime_date + " </b>");
				System.out.println("Date effective from stamp value is : <b> " + DateEffectiveFrom_value
						+ " </b> in technical view is matching to the date of the panel insertion <b> " + Runtime_date
						+ " </b>");
			} else {
				logger.log(LogStatus.FAIL,
						"Date effective from value is : <b> " + DateEffectiveFrom_value
						+ " </b> in technical view is not matching to the date of the panel insertion <b> "
						+ Runtime_date + " </b> ");
				System.out.println("Date effective from value is : <b> " + DateEffectiveFrom_value
						+ " </b> in technical view is not matching to the date of the panel insertion <b> "
						+ Runtime_date + "</b>");
			}

			driver.findElement(By.xpath(element.RDM_Close_Button)).click();

			// click on close symbol of panel member
			Thread.sleep(4000);
			driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();

			// click on Panel extended attribute value
			Thread.sleep(6000);
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
			driver.findElement(By.xpath(element.RDM_PanelExtensionAttributeValue)).click();

			// Click on Search Icon in work place page of panel member extended
			// attribute
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			String append_memberNo_PanelCode = MemberNo_Value + " - " + Panel;
			System.out.println("The appended member No Value is : " + append_memberNo_PanelCode);

			// Expand the text search expand icon
			Thread.sleep(4000);
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();
			driver.findElement(By.xpath(element.RDM_TextSearch_ExpandIcon)).click();

			// TextBox field search
			driver.findElement(By.xpath(element.RDM_FieldContains_TextBox_Field)).sendKeys(append_memberNo_PanelCode);

			driver.findElement(By.xpath(element.RDM_SelectAll_Checkbox_Filter)).click();

			driver.findElement(By.xpath(element.RDM_PanelMemberNumber_Checkbox_Filter)).click();
			driver.findElement(By.xpath(element.RDM_Click_Apply_textSearch)).click();

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();
			Thread.sleep(7000);

			String Filter_Condition_TechnicalView_ExtendedAttribute = UtilLib.screenshot(driver,
					"User has sucessfully applied the filter for extended attribute");
			logger.log(LogStatus.PASS, "User has sucessfully applied the filter for extended attribute"
					+ logger.addScreenCapture(Filter_Condition_TechnicalView_ExtendedAttribute));

			// Get the record count from extended attribute
			List<WebElement> ExtendedAttribute_RecordCount = driver
					.findElements(By.xpath(element.RDM_ExtendedAttribute_RecordCount));
			System.out.println("Extended Attribute record count is : " + ExtendedAttribute_RecordCount.size());
			record_count_technicalView = ExtendedAttribute_RecordCount.size();
			logger.log(LogStatus.PASS, "Extended attribute record count is : <b> " + record_count_technicalView
					+ " </b> for Panel code <b> " + Panel + "</b>");

			// Verify the dates added in extended attribute
			if (ExtendedAttribute_RecordCount.size() > 0) {
				for (int x = 1; x <= ExtendedAttribute_RecordCount.size(); x++) {

					WebElement RowElement_extendedAttribute = driver.findElement(
							By.xpath("//*[@id='ebx_workspaceTable_fixedScroller']/table[@class='ebx_tvFixed']/tbody/tr["
									+ x + "]"));
					Actions action_Click_Rows = new Actions(driver);
					action_Click_Rows.doubleClick(RowElement_extendedAttribute).perform();

					String Details_TechnicalView_ExtendedAttribute = UtilLib.screenshot(driver,
							"Details of each extended attribute for panel ");
					logger.log(LogStatus.PASS, "Details of each extended attribute for panel <b> " + Panel + "</b>"
							+ logger.addScreenCapture(Details_TechnicalView_ExtendedAttribute));

					// retrieve the extended attribute value for the panel code
					Thread.sleep(5000);
					String ExtendedAttributeValue = driver.findElement(By.xpath(element.RDM_ExtendedAttribute_Value))
							.getText();
					System.out.println("Extended attribute value is " + ExtendedAttributeValue);

					// retrieve the date effective from for the panel code
					String DateEffectiveFrom = driver
							.findElement(By.xpath(element.RDM_ExtendedAttribute_DateEffectiveFrom_Value)).getText();
					System.out.println("Date Effective from Value is : " + DateEffectiveFrom);

					if (DateEffectiveFrom.contains(Runtime_date)) {
						logger.log(LogStatus.PASS, "Date effective from is <b> " + DateEffectiveFrom
								+ " </b> is matching to the panel insertion creation timestamp <b> " + Runtime_date
								+ " </b> for extended attribute <b> " + ExtendedAttributeValue + "</b>");
						System.out.println("Date effective from is <b> " + DateEffectiveFrom
								+ " </b> is matching to the panel insertion creation timestamp <b> " + Runtime_date
								+ " </b> for extended attribute <b> " + ExtendedAttributeValue + "</b>");
					} else {
						logger.log(LogStatus.FAIL, "Date effective from is <b> " + DateEffectiveFrom
								+ " </b> is not matching to the panel insertion creation timestamp <b> " + Runtime_date
								+ " </b> for extended attribute <b> " + ExtendedAttributeValue + "</b>");
						System.out.println("Date effective from is <b> " + DateEffectiveFrom
								+ " </b> is not matching to the panel insertion creation timestamp <b> " + Runtime_date
								+ " </b> for extended attribute <b> " + ExtendedAttributeValue + "</b>");
					}

					driver.findElement(By.xpath(element.RDM_Close_Button)).click();
					Thread.sleep(4000);
				}
			} else {
				System.out.println("Record is not present");
			}

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());

		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_Panel_Updation_Verification
	 * @Description : Updating the panel for which the panel is inserted
	 ********************************************************************************************/
	public static boolean RDM_Panel_Updation_Verification(ExtentReports report, ExtentTest logger, String PanelCode)
			throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, 150);
		try {
			// Click on update work place button
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(5000);
			driver.findElement(By.xpath(element.RDM_UpdateWorkplace_button)).click();

			Thread.sleep(10000);

			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			String OneKeyID_Details = UtilLib.screenshot(driver,
					"User is successfully navigated to workplace authorizing");
			logger.log(LogStatus.PASS, "User is successfully navigated to workplace authorizing"
					+ logger.addScreenCapture(OneKeyID_Details));

			// Click on panel header
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			Thread.sleep(4000);
			WebElement HeaderPanel_Click = driver.findElement(By.xpath(element.RDM_Panel_HeaderName));
			Actions action_headerPanel = new Actions(driver);
			action_headerPanel.doubleClick(HeaderPanel_Click).perform();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// Click on the record which is having the panel status 'ACTIVE'
			Thread.sleep(7000);
			WebElement ActiveRecord = driver
					.findElement(By.xpath("//*[@class='ebx_tvMainScroller']//table/tbody/tr/td[text()='" + PanelCode
							+ "']/parent::tr/parent::tbody/parent::table/parent::td//tr/parent::tbody/parent::table/parent::td//tr/parent::tbody/parent::table/parent::td/parent::tr/td/table[@class='ebx_tableCellWithButtonOnRight_table']/tbody/tr/td[text()='ACTIVE']"));
			Actions doubleClickonActiveRecord = new Actions(driver);
			doubleClickonActiveRecord.doubleClick(ActiveRecord).perform();

			Thread.sleep(5000);
			String OneKeyID_Status_Details = UtilLib.screenshot(driver,
					"User is successfully navigated to the onekeyID which is having the record status ACTIVE");
			logger.log(LogStatus.PASS,
					"User is successfully navigated to the onekeyID which is having the record status ACTIVE "
							+ logger.addScreenCapture(OneKeyID_Status_Details));

			// Change the status from 'ACTIVE' to 'LEFT Panel'
			driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

			driver.findElement(By.xpath(element.RDM_PanelStatus_DropdownList)).click();
			Thread.sleep(6000);
			driver.findElement(By.xpath(element.RDM_DropDown_LeftPanel)).click();

			// Fetch the member No
			String Member_No_FieldValue = driver.findElement(By.xpath(element.RDM_MemberNo_Field))
					.getAttribute("value");
			System.out.println("Member no value is : " + Member_No_FieldValue);
			logger.log(LogStatus.PASS,
					"Member no value is : <b> " + Member_No_FieldValue + " </b> for Panel  <b> " + PanelCode + "</b>");

			driver.findElement(By.xpath(element.RDM_SaveButton)).click();

			Thread.sleep(4000);

			// Verify whether the previous member number is matching to the
			// member number
			String PreviousMember_No_FieldValue = driver.findElement(By.xpath(element.RDM_PreviousMemberNo_Field_Value))
					.getText();
			System.out.println("Previous Member number value : " + PreviousMember_No_FieldValue);
			if (PreviousMember_No_FieldValue.equalsIgnoreCase(Member_No_FieldValue)) {
				logger.log(LogStatus.PASS, "Previous Member No is <b> " + PreviousMember_No_FieldValue
						+ " </b> is matching to the member No <b> " + Member_No_FieldValue + "</b>");
				System.out.println("Previous Member No is <b> " + PreviousMember_No_FieldValue
						+ " </b> is matching to the member No <b> " + Member_No_FieldValue + "</b>");
			} else {
				logger.log(LogStatus.FAIL, "Previous Member No is <b> " + PreviousMember_No_FieldValue
						+ " </b> is not matching to the member No <b> " + Member_No_FieldValue + "</b>");
				System.out.println("Previous Member No is <b> " + PreviousMember_No_FieldValue
						+ " </b> is not matching to the member No <b> " + Member_No_FieldValue + "</b>");
			}

			// update the extended attributes
			Thread.sleep(3000);
			List<WebElement> ExtendedAttribute_PMRC = driver
					.findElements(By.xpath("//*[@id='divExtendedAttributes_']/div[2]/table/tbody/tr/td[3]"));
			System.out.println("The extended attribute inserted is : " + ExtendedAttribute_PMRC.size());

			for (int i = 1; i < ExtendedAttribute_PMRC.size(); i++) {
				System.out.println("=========");

				Thread.sleep(3000);
				List<WebElement> ExtendedAttribute_PMRC_1 = driver
						.findElements(By.xpath("//*[@id='divExtendedAttributes_']/div[2]/table/tbody/tr/td[3]"));
				System.out.println("The extended attribute  inserted is : " + ExtendedAttribute_PMRC_1.size());

				String ExtendedAttribute_PMRC_InsertedValue = ExtendedAttribute_PMRC_1.get(i).getText().trim();
				System.out.println("Extended attribute value inserted are : " + ExtendedAttribute_PMRC_InsertedValue);

				if (!ExtendedAttribute_PMRC_InsertedValue.isEmpty()) {
					driver.findElement(By.xpath(".//*[@id='divExtendedAttributes_']//table/tbody/tr/td/span[text()='"+ ExtendedAttribute_PMRC_InsertedValue + "']/parent::td/../td/div/img")).click();

					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
					Thread.sleep(6000);
					driver.switchTo().frame(element.RDM_PanelExtensionAttribute_Frame);

					Thread.sleep(3000);
					String AssignedAttribute_PMRC = driver.findElement(By.xpath(element.RDM_AssignedAttributeSelected))
							.getText();
					System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_PMRC);

					driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
					Thread.sleep(3000);

					if (AssignedAttribute_PMRC.trim().equalsIgnoreCase("PMRC TOC")
							|| AssignedAttribute_PMRC.trim().equalsIgnoreCase("PMRE TOC")) {
						driver.findElement(By.xpath("(//*[@class='ebx_ISS_Item']/div)[1]")).click();
					} else {
						driver.findElement(By.xpath(element.RDM_AssignedAttribute_SecondValue)).click();
					}

					String PanelExtension_Update_PMRC = UtilLib.screenshot(driver,
							"Panel extension attribute for which the updation has performed is : <b> "
									+ AssignedAttribute_PMRC);
					logger.log(LogStatus.PASS,
							"Panel extension attribute for which the updation has performed is : <b> "
									+ AssignedAttribute_PMRC + "</b>"
									+ logger.addScreenCapture(PanelExtension_Update_PMRC));

					driver.findElement(By.xpath(element.RDM_SaveButton)).click();

					// click on cross symbol
					Thread.sleep(4000);
					driver.switchTo().defaultContent();
					WebElement frameText_PMRC = driver.findElement(By.tagName("iframe"));
					System.out.println("**********" + frameText_PMRC.getAttribute("cd_frame_id_"));
					Thread.sleep(4000);
					driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
					.frame(element.RDM_WorkPlace_TableFrame);

					WebElement iframeElements1_PMRC = driver.switchTo().frame("ebx_SubSessioniFrame")
							.findElement(By.tagName("iframe"));
					System.out.println("id of 2nd frame -------" + iframeElements1_PMRC.getAttribute("id"));

					driver.switchTo().frame("ebx_InternalPopup_frame")
					.findElement(By.xpath(element.RDM_Close_updateButton)).click();

					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
					Thread.sleep(9000);

					// switching to default content and switching outlet panel
					// member
					driver.switchTo().defaultContent();

					WebElement frameText_PMRC_1 = driver.findElement(By.tagName("iframe"));
					System.out.println("**********" + frameText_PMRC_1.getAttribute("cd_frame_id_"));
					Thread.sleep(4000);
					driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
					.frame(element.RDM_WorkPlace_TableFrame);

					WebElement iframeElements_PMRC = driver.switchTo().frame("ebx_SubSessioniFrame")
							.findElement(By.tagName("iframe"));
					System.out.println("id of 2nd frame -------" + iframeElements_PMRC.getAttribute("id"));

					driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

					driver.findElement(By.xpath(element.RDM_SaveButton)).click();

				} else {
					System.out.println("Data is not inserted");

					Thread.sleep(3000);
					driver.findElement(By.xpath("(//*[@class='ebx_Button ebx_IconButton ebx_Add'])[1]")).click();

					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
					driver.switchTo().frame(element.RDM_PanelExtensionAttribute_Frame);

					Thread.sleep(3000);
					driver.findElement(By.xpath(element.RDM_AssignedAttribute_dropdown)).click();
					Thread.sleep(3000);

					String AssignedAttribute_Value = driver.findElement(By.xpath(element.RDM_AssignedAttributeSelected))
							.getText();
					System.out.println("Assigned attribute name in panel extension: " + AssignedAttribute_Value);

					driver.findElement(By.xpath(element.RDM_AssignedAttribute_Value)).click();

					String PanelExtension_IHD = UtilLib.screenshot(driver,
							"Panel extension attribute value page for updating");
					logger.log(LogStatus.PASS, "Panel extension attribute value page for updating"
							+ AssignedAttribute_Value + logger.addScreenCapture(PanelExtension_IHD));

					driver.findElement(By.xpath(element.RDM_SaveButton)).click();
					Thread.sleep(1000);

					// click on cross symbol
					driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
					Thread.sleep(9000);

					// switching to default content and switching outlet panel
					// member
					driver.switchTo().defaultContent();

					WebElement frameText1 = driver.findElement(By.tagName("iframe"));
					System.out.println("**********" + frameText1.getAttribute("cd_frame_id_"));
					Thread.sleep(4000);
					driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
					.frame(element.RDM_WorkPlace_TableFrame);

					WebElement iframeElements1 = driver.switchTo().frame("ebx_SubSessioniFrame")
							.findElement(By.tagName("iframe"));
					System.out.println("id of 2nd frame -------" + iframeElements1.getAttribute("id"));

					driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

					driver.findElement(By.xpath(element.RDM_SaveButton)).click();

				}
			}

			// click on accept button
			driver.switchTo().defaultContent();
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo()
			.frame(element.RDM_WorkPlace_TableFrame);
			driver.findElement(By.xpath(element.RDM_Acceptbutton)).click();

			Thread.sleep(3000);

			String Accept_scrrenshot = UtilLib.screenshot(driver,
					"Accept pop up after updating the details of extended attribute");
			logger.log(LogStatus.PASS, "Accept pop up after updating the details of extended attribute"
					+ logger.addScreenCapture(Accept_scrrenshot));

			// handling accept pop up
			driver.findElement(By.xpath(element.RDM_OK_Button)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(10000);

			String PanelInsertion_Screencapture = UtilLib.screenshot(driver,
					"User has successfully performed the panel updation for panel code ");
			logger.log(LogStatus.PASS, "User has successfully performed the panel updation for panel code <b> "
					+ PanelCode + "</b>" + logger.addScreenCapture(PanelInsertion_Screencapture));

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_TechnicalView_Validation_Update
	 * @Description : Verification of one key ID in technical view for the panel which is updated
	 ********************************************************************************************/
	public static boolean RDM_TechnicalView_Validation_Update(ExtentReports report, ExtentTest logger, String Panel)throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, 150);

		// Logic for retrieving the system date
		DateFormat dff = new SimpleDateFormat("dd/MM/yyyy");
		Date today_Date = Calendar.getInstance().getTime();
		String todays_Date = dff.format(today_Date);
		System.out.println("The system date is : " + todays_Date);
		try {
			driver.findElement(By.xpath(element.RDM_TechView)).click();

			Thread.sleep(2000);
			// Switch to the frame
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			// Click on Panel
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Panel)));
			driver.findElement(By.xpath(element.RDM_Panel)).click();

			// Select the Panel Member
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Panel_Member)));
			driver.findElement(By.xpath(element.RDM_Panel_Member)).click();
			Thread.sleep(3000);

			String NavigationWorkplace_TV = UtilLib.screenshot(driver,"User is successfully navigated to technical view Workplace page");
			logger.log(LogStatus.PASS, "User is successfully navigated to technical view Workplace page" + logger.addScreenCapture(NavigationWorkplace_TV));

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_SearchIcon)));

			// Click on Search Icon in work place page of technical view
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// select the panel code from technical view
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_panelcode)));

			Select SourceSystemKey = new Select(driver.findElement(By.xpath(element.RDM_panelcode)));
			SourceSystemKey.selectByVisibleText("    Source system key");

			// Enter One key ID for which the panel is inserted
			Thread.sleep(3000);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Enter_Panelcode)));
			driver.findElement(By.xpath(element.RDM_Enter_Panelcode)).sendKeys(OneKeyIdValue);

			// Click on Apply
			Thread.sleep(2000);
			driver.findElement(By.xpath(element.RDM_Click_Apply)).click();

			Thread.sleep(2000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			Thread.sleep(4000);
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// Scroll till source creation time stamp and click on sorting ICon
			Thread.sleep(3000);
			WebElement src_creation_record = driver.findElement(By.xpath(element.RDM_SourceCreation_FirstRow));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", src_creation_record);

			WebElement SourceCreation = driver.findElement(By.xpath(element.RDM_SourceCreation_TimeStamp_HeaderName));
			WebElement sortingicon_sourcecreationdate = driver.findElement(By.xpath(element.RDM_SortButton_SourceCreation_Timestamp));

			Actions action_src_creation = new Actions(driver);
			action_src_creation.moveToElement(SourceCreation).perform();
			Thread.sleep(3000);

			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", sortingicon_sourcecreationdate);

			Thread.sleep(3000);

			JavascriptExecutor executor1 = (JavascriptExecutor) driver;
			executor1.executeScript("arguments[0].click();", sortingicon_sourcecreationdate);

			// click on the first row for which the table is sorted in
			// descending order
			Thread.sleep(2000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			driver.findElement(By.xpath(element.RDM_FirstRowCheckbox)).click();

			String Navigation_OneKey_TechView = UtilLib.screenshot(driver,"Filtered the values in the search window from technical view based on the One Key ID " + OneKeyIdValue);
			logger.log(LogStatus.PASS, "Filtered the values in the search window from technical view based on the oneKey ID " + OneKeyIdValue + logger.addScreenCapture(Navigation_OneKey_TechView));

			// Click on Actions
			Thread.sleep(2000);
			WebElement ActionButton_Element = driver.findElement(By.xpath(element.RDM_ActionsTab_TechnicalView));
			Actions action_button_click = new Actions(driver);
			action_button_click.doubleClick(ActionButton_Element).perform();

			// Click on view history
			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_ViewHistory_value)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// Navigated to Panel member of technical view
			Thread.sleep(5000);
			String Navigation_OneKey_TechView_PanelMember = UtilLib.screenshot(driver, "Record of the panel insertion in technical view");
			logger.log(LogStatus.PASS, "Record of the panel insertion in technical view " + logger.addScreenCapture(Navigation_OneKey_TechView_PanelMember));

			driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);
			WebElement panelmember_click = driver.findElement(By.xpath(element.RDM_PanelMember_FirstRow_CheckBox));
			Actions action_firstRow = new Actions(driver);
			action_firstRow.doubleClick(panelmember_click).perform();

			// Expand the source system ID
			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_SourceSystemID_ExpandIcon)).click();

			String Details_OneKey_TechView_PanelMember = UtilLib.screenshot(driver,"Details of One key ID in technical view for which the panel is updated");
			logger.log(LogStatus.PASS, "Details of One key ID in technical view for which the panel is updated"+ logger.addScreenCapture(Details_OneKey_TechView_PanelMember));

			// operation type value
			String OpertaionType_Value = driver.findElement(By.xpath(element.RDM_OpertaionType_Value)).getText();
			System.out.println("Operation type value is : " + OpertaionType_Value);
			logger.log(LogStatus.PASS, "Operation type value is : <b> " + OpertaionType_Value + "</b>");

			// Retrieve the source transaction time stamp value and verify the
			// value with panel updated date
			String SourcetransactionTimeStamp_value = driver
					.findElement(By.xpath(element.RDM_SourceTransactionTimeStamp_Value)).getText();
			if (SourcetransactionTimeStamp_value.contains(todays_Date)) {
				logger.log(LogStatus.PASS,
						"Source transaction time stamp value is : <b> " + SourcetransactionTimeStamp_value
						+ " </b> in technical view is matching to the date of the panel update <b> "
						+ todays_Date + "</b>");
				System.out.println("Source transaction time stamp value is : <b> " + SourcetransactionTimeStamp_value
						+ " </b> in technical view is matching to the date of the panel update <b> " + todays_Date
						+ "</b>");
			} else {
				logger.log(LogStatus.FAIL,
						"Source transaction time stamp value is : <b> " + SourcetransactionTimeStamp_value
						+ " </b> in technical view is not matching to the date of the panel update <b> "
						+ todays_Date + "</b>");
				System.out.println("Source transaction time stamp value is : " + SourcetransactionTimeStamp_value
						+ "in technical view is not matching to the date of the panel update" + todays_Date);
			}

			// Retrieve the date effective from value and verify the value with
			// panel updated date
			String DateEffectiveFrom_value = driver.findElement(By.xpath(element.RDM_DateEffectiveFrom_Value))
					.getText();
			if (DateEffectiveFrom_value.contains(todays_Date)) {
				logger.log(LogStatus.PASS,
						"Date effective from value is : <b>  " + DateEffectiveFrom_value
						+ " </b> in technical view is matching to the date of the panel update <b> "
						+ todays_Date + " </b>");
				System.out.println("Date effective from stamp value is : <b> " + DateEffectiveFrom_value
						+ " </b> in technical view is matching to the date of the panel update <b> " + todays_Date
						+ " </b>");
			} else {
				logger.log(LogStatus.FAIL,
						"Date effective from value is : <b> " + DateEffectiveFrom_value
						+ " </b> in technical view is not matching to the date of the panel update <b> "
						+ todays_Date + " </b> ");
				System.out.println("Date effective from value is : <b> " + DateEffectiveFrom_value
						+ " </b> in technical view is not matching to the date of the panel update <b> " + todays_Date
						+ "</b>");
			}

			driver.findElement(By.xpath(element.RDM_Close_Button)).click();

			// Compare 2 rows which contains update and insert
			driver.switchTo().defaultContent();

			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
			driver.switchTo().frame(element.RDM_PanelExtensionAttribute_Frame);

			Thread.sleep(5000);
			WebElement panelmember_click2 = driver
					.findElement(By.xpath("//*[@id='ebx_workspaceTable_fixedScroller']/table/tbody/tr[2]/td/input"));
			Actions action_firstRow2 = new Actions(driver);
			action_firstRow2.doubleClick(panelmember_click2).perform();

			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_Close_Button)).click();

			// Click on Action button
			driver.switchTo().defaultContent();
			Thread.sleep(1000);
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
			Thread.sleep(3000);
			driver.switchTo().frame(element.RDM_PanelExtensionAttribute_Frame);

			driver.findElement(By.xpath(element.RDM_Action_Button_PanelMember)).click();

			// Click on compare button
			Thread.sleep(4000);
			driver.findElement(By.xpath(element.RDM_Compare_Button)).click();

			// Click on View button
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_View_Button_ComparePage)));
			Thread.sleep(6000);
			driver.findElement(By.xpath(element.RDM_View_Button_ComparePage)).click();

			// Click on hide similarities attribute
			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_Hide_Similarities_Field)).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_CompareRecord_Text)));

			String Compare_Records = UtilLib.screenshot(driver,
					"User can successfully verify the record compare for both insertion and updation of the panel");
			logger.log(LogStatus.PASS,
					"User can successfully verify the record compare for both insertion and updation of the panel"
							+ logger.addScreenCapture(Compare_Records));

			// click on close symbol of panel member
			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();

			/**********************
			 * Extended attribute verification
			 **************/
			// click on Panel extended attribute value
			Thread.sleep(4000);
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
			driver.findElement(By.xpath(element.RDM_PanelExtensionAttributeValue)).click();

			// Click on Search Icon in work place page of panel member extended
			// attribute
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			Select SourceSystemKey1 = new Select(driver.findElement(By.xpath(element.RDM_panelcode)));
			SourceSystemKey1.selectByVisibleText("    Source system key");

			// Enter One key ID for which the panel is inserted
			Thread.sleep(3000);
			// wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Enter_Panelcode)));
			driver.findElement(By.xpath(element.RDM_Enter_Panelcode)).sendKeys(OneKeyIdValue);

			Select panelcode = new Select(driver.findElement(By.xpath(element.RDM_panelcode)));
			panelcode.selectByVisibleText("Panel Code");

			// Enter panel code
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Enter_Panelcode)));
			Thread.sleep(4000);
			driver.findElement(By.xpath(element.RDM_Enter_Panelstatuscode)).sendKeys(Panel);

			// Click on Apply
			Thread.sleep(2000);
			driver.findElement(By.xpath(element.RDM_Click_Apply)).click();

			Thread.sleep(2000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			String Filter_Condition_TechnicalView_ExtendedAttribute = UtilLib.screenshot(driver,
					"User has sucessfully applied the filter for extended attribute");
			logger.log(LogStatus.PASS, "User has sucessfully applied the filter for extended attribute"
					+ logger.addScreenCapture(Filter_Condition_TechnicalView_ExtendedAttribute));

			// Get the record count from extended attribute

			List<WebElement> ExtendedAttribute_RecordCount = driver
					.findElements(By.xpath(element.RDM_ExtendedAttribute_RecordCount));
			System.out.println("Extended Attribute record count is : " + ExtendedAttribute_RecordCount.size());
			record_count_technicalView = ExtendedAttribute_RecordCount.size();
			logger.log(LogStatus.PASS, "Extended attribute record count is : <b> " + record_count_technicalView
					+ " </b> for Panel code <b> " + Panel + "</b>");

			// Verify the dates added in extended attribute
			if (ExtendedAttribute_RecordCount.size() > 0) {
				for (int x = 1; x <= ExtendedAttribute_RecordCount.size(); x++) {

					WebElement RowElement_extendedAttribute = driver.findElement(
							By.xpath("//*[@id='ebx_workspaceTable_fixedScroller']/table[@class='ebx_tvFixed']/tbody/tr["
									+ x + "]"));
					Actions action_Click_Rows = new Actions(driver);
					action_Click_Rows.doubleClick(RowElement_extendedAttribute).perform();

					String Details_TechnicalView_ExtendedAttribute = UtilLib.screenshot(driver,
							"Details of each extended attribute for panel ");
					logger.log(LogStatus.PASS, "Details of each extended attribute for panel <b> " + Panel + "</b>"
							+ logger.addScreenCapture(Details_TechnicalView_ExtendedAttribute));

					// retrieve the extended attribute value for the panel code
					Thread.sleep(4000);
					String ExtendedAttributeValue = driver.findElement(By.xpath(element.RDM_ExtendedAttribute_Value))
							.getText();
					System.out.println("Extended attribute value is " + ExtendedAttributeValue);

					// retrieve the date effective from for the panel code
					String DateEffectiveFrom = driver
							.findElement(By.xpath(element.RDM_ExtendedAttribute_DateEffectiveFrom_Value)).getText();
					System.out.println("Date Effective from Value is : " + DateEffectiveFrom);

					if (DateEffectiveFrom.contains(Runtime_date)) {
						logger.log(LogStatus.PASS, "Date effective from is <b> " + DateEffectiveFrom
								+ " </b> is matching to the panel insertion creation timestamp <b> " + Runtime_date
								+ " </b> for extended attribute <b> " + ExtendedAttributeValue + "</b>");
						System.out.println("Date effective from is <b> " + DateEffectiveFrom
								+ " </b> is matching to the panel insertion creation timestamp <b> " + Runtime_date
								+ " </b> for extended attribute <b> " + ExtendedAttributeValue + "</b>");
					} else {
						logger.log(LogStatus.FAIL, "Date effective from is <b> " + DateEffectiveFrom
								+ " </b> is not matching to the panel insertion creation timestamp <b> " + Runtime_date
								+ " </b> for extended attribute <b> " + ExtendedAttributeValue + "</b>");
						System.out.println("Date effective from is <b> " + DateEffectiveFrom
								+ " </b> is not matching to the panel insertion creation timestamp <b> " + Runtime_date
								+ " </b> for extended attribute <b> " + ExtendedAttributeValue + "</b>");
					}

					Thread.sleep(3000);
					driver.findElement(By.xpath(element.RDM_Close_Button)).click();
					Thread.sleep(4000);
				}
			} else {
				System.out.println("Record is not present");
			}

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());

		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_Duplicate_Panel_Verification
	 * @Description : RDM Duplicate panel verification
	 ********************************************************************************************/
	public static boolean RDM_Duplicate_Panel_Verification(ExtentReports report, ExtentTest logger, String Panel,
			String PanelStatus) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 100);
		try {

			// Logic for member panel
			DateFormat df = new SimpleDateFormat("dd:MM:yyyy:hh:mm:ss");
			Date today = Calendar.getInstance().getTime();
			String runTime = df.format(today);
			String split_string_array[] = runTime.split(":");
			MemberNo_Value = String.join("", split_string_array);
			System.out.println("The member id is: " + MemberNo_Value);

			// Click on update work place button
			Thread.sleep(4000);
			driver.findElement(By.xpath(element.RDM_UpdateWorkplace_button)).click();

			Thread.sleep(6000);

			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			String OneKeyID_Details = UtilLib.screenshot(driver,
					"User is successfully navigated to workplace authorizing");
			logger.log(LogStatus.PASS, "User is successfully navigated to workplace authorizing"
					+ logger.addScreenCapture(OneKeyID_Details));

			// Click on panel header
			Thread.sleep(3000);
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			WebElement HeaderPanel_Click = driver.findElement(By.xpath(element.RDM_Panel_HeaderName));
			Actions action_headerPanel = new Actions(driver);
			action_headerPanel.doubleClick(HeaderPanel_Click).perform();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// Click on Add icon
			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_Add_Icon)).click();

			// click on panel drop down
			Thread.sleep(5000);

			driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);
			driver.findElement(By.xpath(element.RDM_Panel_DropdownList)).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath(
					"//*[@class='ebx_ISS_Item']/div[@class='ebx_ISS_Item_WithPreview' and text()='" + Panel + "']"))
			.click();
			Thread.sleep(3000);

			// Insert the Panel Status as 'ACTIVE' or 'On test'
			driver.findElement(By.xpath(element.RDM_PanelStatus_DropdownList)).click();
			Thread.sleep(6000);
			driver.findElement(By.xpath("//*[@id='ebx_ISS_Item_1']/div[text()='" + PanelStatus + "']")).click();

			// Enter the member No
			driver.findElement(By.xpath(element.RDM_MemberNo_Field)).sendKeys(MemberNo_Value);

			driver.findElement(By.xpath(element.RDM_SaveButton)).click();

			String Panel_Details_Duplicate = UtilLib.screenshot(driver,"Added the duplicate value for the panel status for panel <b> " + Panel + "</b>");
			logger.log(LogStatus.PASS, "Added the duplicate value for the panel status for panel <b> " + Panel + "</b>"
					+ logger.addScreenCapture(Panel_Details_Duplicate));

			/*** Error Message verification ***/
			// click on expand icon of error
			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_ExpandIcon_Error)).click();

			// validate the error message
			String ErrorMessage_OutletPanelMember = driver.findElement(By.xpath(element.RDM_ErrorMessage_Duplicate)).getText();
			System.out.println("Error Message generated for duplicate Panel is : <b> " + ErrorMessage_OutletPanelMember
					+ " </b> for Panel <b> " + Panel + "</b>");
			logger.log(LogStatus.PASS, "Error Message generated for duplicate Panel is : <b> "
					+ ErrorMessage_OutletPanelMember + " </b> for Panel <b> " + Panel + "</b>");

			// collapse the error/warning icon
			driver.findElement(By.xpath(element.RDM_CollapseIcon_Warning)).click();

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_BoundaryType_NonGeoBrick_Insert
	 * @Description : RDM BoundaryType Non Geo brick insert
	 ********************************************************************************************/
	public static boolean RDM_BoundaryType_NonGeoBrick_Insert(ExtentReports report, ExtentTest logger)
			throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 150);
		String TestdataName = UtilLib.getPropertiesValue("TestData");
		String xlFilePath = System.getProperty("user.dir") + "/RDM_TestData/" + TestdataName;
		String sheet = "Non_GeoBrick";
		int TestdataRowcount = UtilLib.getRowCount(xlFilePath, sheet);
		try {

			/********** * Search functionality insert******************************/
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(2000);
			// Switch to the frame
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			// Click on Search Icon in work place page of technical view
			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// Expand the text search expand icon
			driver.findElement(By.xpath(element.RDM_TextSearch_ExpandIcon)).click();

			// TextBox field search
			driver.findElement(By.xpath(element.RDM_FieldContains_TextBox_Field)).sendKeys("GB");

			driver.findElement(By.xpath(element.RDM_SelectAll_Checkbox_Filter)).click();

			driver.findElement(By.xpath(element.RDM_County_TextSearch_Field_Checkbox)).click();
			driver.findElement(By.xpath(element.RDM_Click_Apply_textSearch)).click();
			Thread.sleep(3000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(7000);

			String BoundaryType_Filters = UtilLib.screenshot(driver, "Applied the filters for the boundary type ");
			logger.log(LogStatus.PASS,"Applied the filters for the boundary type " + logger.addScreenCapture(BoundaryType_Filters));

			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			/****************** Non Geo Brick Boundary Type insert **********************************/
			Thread.sleep(4000);
			List<WebElement> Workplace_recordcount = driver.findElements(By.xpath(element.RDM_Workplace_Table));
			System.out.println("WorkPlace record count which the data is filtered : " + Workplace_recordcount.size());

			for (int j = 1; j <= Workplace_recordcount.size(); j++) {
				Thread.sleep(3000);
				WebElement FilteredOneKeyID = driver.findElement(By.xpath("//*[@id='ebx_workspaceTable_fixedScroller']/table/tbody/tr[" + j + "]"));
				Actions Workplace_OneKeyId = new Actions(driver);
				Workplace_OneKeyId.doubleClick(FilteredOneKeyID).perform();

				Thread.sleep(4000);
				driver.findElement(By.xpath(element.RDM_UpdateWorkplace_button)).click();

				Thread.sleep(8000);

				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

				String OneKeyID_Details = UtilLib.screenshot(driver,"User is successfully navigated to workplace authorizing");
				logger.log(LogStatus.PASS, "User is successfully navigated to workplace authorizing"+ logger.addScreenCapture(OneKeyID_Details));

				// Click on Non Geo Brick header
				Thread.sleep(3000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

				WebElement NonGeoBrick_Click = driver.findElement(By.xpath(element.RDM_NonGeoBrick_Header));
				Actions action_NonGeoBrick = new Actions(driver);
				action_NonGeoBrick.doubleClick(NonGeoBrick_Click).perform();
				Thread.sleep(7000);

				String OneKeyId_BusinessWorkplace = driver.findElement(By.xpath(element.RDM_OneKeyID_NonGeoBrickInsertion_Page)).getText();
				String OneKeyId_BusinessWork[] = OneKeyId_BusinessWorkplace.split("-");
				OneKeyIdValue = OneKeyId_BusinessWork[1].trim();
				System.out.println("One key ID value is : " + OneKeyIdValue);

				// fetch the record count in Non Geo Brick code
				try {
					RecordCount_NonGeoBrick_Boundary = driver.findElements(By.xpath(element.RDM_NonGeoBrick_Record_Count));
					List<ArrayList<String>> rowsData = new ArrayList<ArrayList<String>>();
					System.out.println("Record Count in Non Geo Brick header table " + RecordCount_NonGeoBrick_Boundary.size());
					record_NonGeoBrick_Boundary_Table_Count = RecordCount_NonGeoBrick_Boundary.size();
					ArrayList<String> rowData = new ArrayList<String>();

					if (RecordCount_NonGeoBrick_Boundary.size() > 0) {
						for (WebElement row : RecordCount_NonGeoBrick_Boundary) {
							rowData.add(row.getText().trim());
						}
						rowsData.add(rowData);
						System.out.println("Records in Non Geo Brick header table" + rowData);

						String Details_BoundaryType_NonGeoBrick = UtilLib.screenshot(driver,"Data present in Non Geo Brick header for boundary type of One Key ID ");
						logger.log(LogStatus.PASS,"Data present in Non Geo Brick header for boundary type of One Key ID "+ logger.addScreenCapture(Details_BoundaryType_NonGeoBrick));

						// read the boundary type from excel
						List<ArrayList<String>> rowsData1 = new ArrayList<ArrayList<String>>();
						ArrayList<String> rowData1 = new ArrayList<String>();
						for (int readexcel_boundaryTpe = 1; readexcel_boundaryTpe < TestdataRowcount; readexcel_boundaryTpe++) {
							String Boundary_Type_Data = UtilLib.getCellData(xlFilePath, sheet, "RDM_Boundary_Type",readexcel_boundaryTpe, "string");

							rowData1.add(Boundary_Type_Data);
							rowsData1.add(rowData1);
							System.out.println("Data to read from excel " + rowData1);

						}

						// *** adding the condition if record count is equal to
						// 8 *****//
						System.out.println("The value is : " + rowData1);

						if (rowData.size() == rowData1.size()) {
							j = j + 1;
							driver.switchTo().defaultContent();
							driver.findElement(By.xpath(element.RDM_BuisnessView_Header)).click();
							wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
							Thread.sleep(6000);
							driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
						} else {
							for (int i = 0; i < rowData1.size(); i++) {
								System.out.println(rowData.size());
								if (rowData.contains(rowData1.get(i))) {
									System.out.println("Exist : " + rowData1.get(i));
								} else {
									System.out.println("Not Exist : " + rowData1.get(i));

									// Click on Add button
									WebElement AddIcon_Click = driver.findElement(By.xpath(element.RDM_AddIcon_NonGeoBrick));
									Actions AddIcon_Action = new Actions(driver);
									AddIcon_Action.doubleClick(AddIcon_Click).perform();

									wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
									Thread.sleep(3000);


									// Select some boundary type value
									driver.switchTo().defaultContent();
									driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
									driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).findElement(By.tagName("iframe"));
									Thread.sleep(3000);
									driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

									// drop down for boundary associate
									Thread.sleep(3000);
									driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).clear();
									driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).sendKeys(rowData1.get(i));
									driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).sendKeys(Keys.ENTER);

									wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Dropdown_Loading)));

									Thread.sleep(5000);
									driver.findElement(By.xpath("(//*[@class='ebx_ISS_Item']/div)[1]")).click();

									String Boundary_Type_Insert = UtilLib.screenshot(driver,"Boundary type inserted for non geo brick one key ID ");
									logger.log(LogStatus.PASS,"Boundary type inserted is <b> " + rowData1.get(i) + " </b> for non geo brick one key ID " + OneKeyIdValue + "</b>" + logger.addScreenCapture(Boundary_Type_Insert));

									// Click on Save and close button
									driver.findElement(By.xpath(element.RDM_SaveAndClose_Button)).click();

									driver.switchTo().defaultContent();
									driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
									driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

									wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element.RDM_BoundaryAssociation_Dropdown_Loading)));
								}
							}

							String Details_Boundary_Inserted = UtilLib.screenshot(driver,"Non Geo Brick boundary types are inserted successfully");
							logger.log(LogStatus.PASS, "Non Geo Brick boundary types are inserted successfully "+ logger.addScreenCapture(Details_Boundary_Inserted));

							// Click on Accept button
							driver.switchTo().defaultContent();
							driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
							driver.findElement(By.xpath(element.RDM_Acceptbutton)).click();

							String Accept_scrrenshot = UtilLib.screenshot(driver,"Accept pop up after inserting the boundary type");
							logger.log(LogStatus.PASS, "Accept pop up after inserting the boundary type"+ logger.addScreenCapture(Accept_scrrenshot));

							// handling accept pop up
							driver.findElement(By.xpath(element.RDM_OK_Button)).click();
							wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
							Thread.sleep(7000);

							String PanelInsertion_Screencapture = UtilLib.screenshot(driver,"User has successfully performed the non Geo Brick insertion ");
							logger.log(LogStatus.PASS,"User has successfully performed the non Geo Brick insertion for one key ID <b> " + OneKeyIdValue + "</b>"+ logger.addScreenCapture(PanelInsertion_Screencapture));
							break;
						}
					} else {
						System.out.println("**************************");
						for (int k = 1; k < TestdataRowcount; k++) {
							String Boundary_Type_Data = UtilLib.getCellData(xlFilePath, sheet, "RDM_Boundary_Type", k,"string");
							// Click on Add button
							WebElement AddIcon_Click = driver.findElement(By.xpath(element.RDM_AddIcon_NonGeoBrick));
							Actions AddIcon_Action = new Actions(driver);
							AddIcon_Action.doubleClick(AddIcon_Click).perform();

							wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

							// Select some boundary type value
							driver.switchTo().defaultContent();
							driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
							driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).findElement(By.tagName("iframe"));
							driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

							// drop down for boundary associate
							Thread.sleep(3000);
							driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).clear();
							driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).sendKeys(Boundary_Type_Data);
							driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).sendKeys(Keys.ENTER);

							wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Dropdown_Loading)));

							Thread.sleep(5000);
							driver.findElement(By.xpath("(//*[@class='ebx_ISS_Item']/div)[1]")).click();

							String Boundary_Type_Insert = UtilLib.screenshot(driver,"Boundary type inserted for non geo brick one key ID ");
							logger.log(LogStatus.PASS,"Boundary type inserted is <b> " + Boundary_Type_Data+ " </b> for non geo brick one key ID <b>" + OneKeyID_Details + "</b>"+ logger.addScreenCapture(Boundary_Type_Insert));

							// Click on Save and close button
							driver.findElement(By.xpath(element.RDM_SaveAndClose_Button)).click();

							driver.switchTo().defaultContent();
							driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
							driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
							Thread.sleep(3000);

							wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element.RDM_BoundaryAssociation_Dropdown_Loading)));
						}
						// Click on Accept button

						driver.switchTo().defaultContent();
						driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
						driver.findElement(By.xpath(element.RDM_Acceptbutton)).click();

						String Accept_scrrenshot = UtilLib.screenshot(driver,"Accept pop up after inserting the boundary type");
						logger.log(LogStatus.PASS, "Accept pop up after inserting the boundary type" + logger.addScreenCapture(Accept_scrrenshot));

						// handling accept pop up
						driver.findElement(By.xpath(element.RDM_OK_Button)).click();
						wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
						Thread.sleep(8000);

						String PanelInsertion_Screencapture = UtilLib.screenshot(driver,"User has successfully performed the non Geo Brick insertion ");
						logger.log(LogStatus.PASS,"User has successfully performed the non Geo Brick insertion for one key ID <b> " + OneKeyIdValue + "</b>"+ logger.addScreenCapture(PanelInsertion_Screencapture));

						break;
					}
				} catch (Exception exe) {
					exe.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;

	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_BoundaryType_NonGeoBrick_TechnicalView_Verification
	 * @Description : RDM BoundaryType Non Geo brick technical view verification
	 ********************************************************************************************/
	public static boolean RDM_BoundaryType_NonGeoBrick_TechnicalView_Verification(ExtentReports report,
			ExtentTest logger) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 100);

		DateFormat dff = new SimpleDateFormat("dd/MM/yyyy");
		Date today_Date = Calendar.getInstance().getTime();
		Runtime_date = dff.format(today_Date);
		System.out.println("The system date is : " + Runtime_date);
		try {

			driver.switchTo().defaultContent();

			// click on technical view
			driver.findElement(By.xpath(element.RDM_TechView)).click();
			Thread.sleep(4000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			// Click on Organization
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Organization_ExpandButton)));
			driver.findElement(By.xpath(element.RDM_Organization_ExpandButton)).click();

			// click on operational activity center assigned boundary type
			wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath(element.RDM_OperationalActivityCenterAssigned_boundary_Type)));
			driver.findElement(By.xpath(element.RDM_OperationalActivityCenterAssigned_boundary_Type)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			Thread.sleep(6000);
			String NavigationWorkplace_TV = UtilLib.screenshot(driver,
					"User is successfully navigated to technical view Workplace page");
			logger.log(LogStatus.PASS, "User is successfully navigated to technical view Workplace page"
					+ logger.addScreenCapture(NavigationWorkplace_TV));

			// Click on Search Icon in work place page of technical view
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// select the panel code from technical view
			Thread.sleep(5000);
			Select SourceSystemKey = new Select(driver.findElement(By.xpath(element.RDM_panelcode)));
			SourceSystemKey.selectByVisibleText("    Source system key");

			// Enter One key ID for which the panel is inserted
			Thread.sleep(5000);
			driver.findElement(By.xpath(element.RDM_Enter_Panelcode)).sendKeys(OneKeyIdValue);

			// Click on Apply
			Thread.sleep(2000);
			driver.findElement(By.xpath(element.RDM_Click_Apply)).click();

			Thread.sleep(2000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// Scroll till source creation time stamp and click on sorting ICon
			Thread.sleep(3000);
			WebElement src_creation_record = driver.findElement(By.xpath(element.RDM_SourceCreation_FirstRow));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", src_creation_record);

			WebElement SourceCreation = driver.findElement(By.xpath(element.RDM_SourceCreation_TimeStamp_HeaderName));
			WebElement sortingicon_sourcecreationdate = driver
					.findElement(By.xpath(element.RDM_SortButton_SourceCreation_Timestamp));

			Actions action_src_creation = new Actions(driver);
			action_src_creation.moveToElement(SourceCreation).perform();
			Thread.sleep(3000);

			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", sortingicon_sourcecreationdate);

			Thread.sleep(3000);
			JavascriptExecutor executor1 = (JavascriptExecutor) driver;
			executor1.executeScript("arguments[0].click();", sortingicon_sourcecreationdate);

			String Navigation_OneKey_TechView = UtilLib.screenshot(driver,"Filtered the values in the search window from technical view based on the One Key ID for which the boundary type are added");
			logger.log(LogStatus.PASS,"Filtered the values in the search window from technical view based on the oneKey ID for which the boundary type are added" + logger.addScreenCapture(Navigation_OneKey_TechView));

			// Get the record count from operational activity center assigned
			// boundary type
			List<WebElement> ExtendedAttribute_RecordCount_TechnicalView = driver.findElements(By.xpath(element.RDM_ExtendedAttribute_RecordCount));
			System.out.println("Extended Attribute record count is : " + ExtendedAttribute_RecordCount_TechnicalView.size());
			int boundaryType_TechnicalView_Count = ExtendedAttribute_RecordCount_TechnicalView.size();
			record_count_technicalView = boundaryType_TechnicalView_Count - record_NonGeoBrick_Boundary_Table_Count;
			System.out.println("The total boundary type added in non geo brick header are : " + record_count_technicalView);

			// ExtendedAttribute_RecordCount = RecordCount_NonGeoBrick_Boundary
			// - ExtendedAttribute_RecordCount_TechnicalView;
			logger.log(LogStatus.PASS, "Extended attribute record count is : <b> "+ ExtendedAttribute_RecordCount_TechnicalView.size() + " </b> for Non Geo Brick boundary type ");

			// Verify the dates added in extended attribute
			if (ExtendedAttribute_RecordCount_TechnicalView.size() > 0) {
				for (int x = 1; x <= ExtendedAttribute_RecordCount_TechnicalView.size(); x++) {

					WebElement RowElement_extendedAttribute = driver.findElement(By.xpath("//*[@id='ebx_workspaceTable_fixedScroller']/table[@class='ebx_tvFixed']/tbody/tr["+ x + "]"));
					Actions action_Click_Rows = new Actions(driver);
					action_Click_Rows.doubleClick(RowElement_extendedAttribute).perform();

					// retrieve the extended attribute value for boundary type
					Thread.sleep(3000);
					String BoundaryType_Value = driver.findElement(By.xpath(element.RDM_BoundaryAssociationSchemaCode_Name)).getText();
					System.out.println("Boundary Type Value is  " + BoundaryType_Value);

					String Details_TechnicalView_BoundaryType = UtilLib.screenshot(driver,"Details of each boundary type");
					logger.log(LogStatus.PASS, "Details of each boundary type <b> " + BoundaryType_Value + "</b>" + logger.addScreenCapture(Details_TechnicalView_BoundaryType));

					// retrieve the creation time stamp from for boundary type
					String SourceTransactionTimeStamp = driver.findElement(By.xpath(element.RDM_SourceTransactionTimeStamp_Value_TechnicalView)).getText();
					System.out.println("Source transaction Time stamp Value is : " + SourceTransactionTimeStamp);

					if (SourceTransactionTimeStamp.contains(Runtime_date)) {
						logger.log(LogStatus.PASS,"Source transaction time stamp  is <b> " + SourceTransactionTimeStamp + " </b> is matching to the transcation timestamp <b> " + Runtime_date + " </b> for boundary type <b> " + BoundaryType_Value + "</b>");
						System.out.println("Source transaction time stamp  is <b> " + SourceTransactionTimeStamp+ " </b> is matching to the transcation timestamp <b> " + Runtime_date+ " </b> for boundary type<b> " + BoundaryType_Value + "</b>");
					} else {
						logger.log(LogStatus.PASS,"The boundary type <b> " + BoundaryType_Value + "</b> already exists hence the Source transaction time stamp  is <b> " + SourceTransactionTimeStamp + " </b>");
						System.out.println("The boundary type <b> " + BoundaryType_Value + "</b> already exists hence the Source transaction time stamp  is <b> " + SourceTransactionTimeStamp + " </b>");
					}

					driver.findElement(By.xpath(element.RDM_Close_Button)).click();
					Thread.sleep(4000);
				}
			} else {
				System.out.println("Record is not present");
			}

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;

	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : kafKa_PANEL_Verification
	 * @Description : Kafka_verifiction
	 ********************************************************************************************/
	public static boolean kafKa_Verification(ExtentReports report, ExtentTest logger) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 100);
		String kafka_rdm = "Kafka_RDM [cdtsapp377d:9092]";
		String sheet = "Panel";
		String TestdataName = UtilLib.getPropertiesValue("TestData");
		String xlFilePath = System.getProperty("user.dir") + "/RDM_TestData/" + TestdataName;
		String Kafka_Publishing_Topic_Panel = UtilLib.getCellData(xlFilePath, sheet, "Kafka_Publishing_TopicName_Panel", 1, "string");
		try {
			Thread.sleep(3000);
			try {
				if (driver.findElement(By.xpath("//*[@id='ebx_SelectorPanelToolbar']")).isDisplayed()) {
					Thread.sleep(5000);
					// Click on EBX Admin Button
					driver.findElement(By.xpath(element.Kafka_Click_EBXAdmin)).click();

					// Click on EBX Admin Button
					Thread.sleep(3000);
					driver.findElement(By.xpath(element.Kafka_Click_EBXAdmin2)).click();
					Thread.sleep(5000);
					// driver.findElement(By.xpath(element.Kafka_RDM_Data_On_boarding)).click();
				} else {
					driver.findElement(By.xpath(element.Kafka_RDM_Data_On_boarding)).click();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			driver.findElement(By.xpath(element.Kafka_RDM_Data_On_boarding)).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.Kafka_Core_Administration_Logs)));

			// Selecting the core_administration_logs
			driver.findElement(By.xpath(element.Kafka_Core_Administration_Logs)).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.Kafka_KafkaBrowser)));

			String Select_Core_Admin_logs = UtilLib.screenshot(driver,
					"Core Admin logs has been selected in the kafka page");
			logger.log(LogStatus.PASS, "Core Admin logs has been selected in the kafka page "
					+ logger.addScreenCapture(Select_Core_Admin_logs));

			// Click on Kafka browser
			driver.findElement(By.xpath(element.Kafka_KafkaBrowser)).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.Kafka_Actions)));

			// Click on Actions
			Thread.sleep(5000);
			driver.findElement(By.xpath(element.Kafka_Actions)).click();

			// Click on Kafka browser
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.Kafka_Actions_KafkaBrowser)));
			driver.findElement(By.xpath(element.Kafka_Actions_KafkaBrowser)).click();

			// Select Kafka_RDM [cdtsapp377d:9092]
			Select Kafka_RDM = new Select(driver.findElement(By.name("serverToBrowse")));

			WebElement option = Kafka_RDM.getFirstSelectedOption();
			String defaultItem = option.getText();

			if (kafka_rdm.equalsIgnoreCase(defaultItem)) {
				System.out.println(defaultItem);
			} else {
				Kafka_RDM.selectByVisibleText("Kafka_RDM [cdtsapp377d:9092]");
			}

			// Click on LIST_Topics
			driver.findElement(By.xpath(element.Kafka_List_Topics)).click();

			// Selecting the topic
			Thread.sleep(5000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Select listTopic = new Select(driver.findElement(By.name("topicToBrowse")));
			System.out.println(Kafka_Publishing_Topic_Panel);
			listTopic.selectByVisibleText(Kafka_Publishing_Topic_Panel);

			// Click on SEEK_LAST
			driver.findElement(By.xpath(element.Kafka_SEEK_LAST)).click();
			Thread.sleep(10000);
			String NavigationListTopics = UtilLib.screenshot(driver, "User is successfully navigated to List_Topics ");
			logger.log(LogStatus.PASS,
					"User is successfully navigated to List_Topics" + logger.addScreenCapture(NavigationListTopics));

			// Switching to iframe
			WebElement el = driver.findElement(By.tagName("iframe"));
			System.out.println("iframe in a page is : " + el.getAttribute("style"));
			System.out.println("iframe in a page is : " + el.getAttribute("src"));

			Thread.sleep(3000);
			WebElement srcFrame = driver
					.findElement(By.xpath("//iframe[contains(@style,'width:100%; height:100%; border:0px;')]"));
			driver.switchTo().frame(srcFrame);

			// Clicking on Last Record
			WebElement webelement = driver.findElement(By.xpath(element.Kafka_SelectRecord));
			Actions action_kafkarecord = new Actions(driver);
			action_kafkarecord.doubleClick(webelement).perform();

			Thread.sleep(2000);

			// Getting Record Count
			WebElement Recordcount = driver.findElement(By.xpath(element.Kafka_RecordCount));
			String Get_RecordCount = Recordcount.getText();
			String Recotd_Count = UtilLib.screenshot(driver,
					"User is successfully able to verify the complete details of one key ID in kafka browser");
			logger.log(LogStatus.PASS, "User is successfully able to verify the complete details of one key ID <b> "
					+ OneKeyIdValue + " </b> in kafka browser" + logger.addScreenCapture(Recotd_Count));
			System.out.println("Record count value is : " + Get_RecordCount);

			// int record_int = ExtendedAttribute_RecordCount.size();
			int result_record = Integer.parseInt(Get_RecordCount);
			if (result_record == record_count_technicalView) {
				logger.log(LogStatus.PASS,
						"The record count in kafka <b> " + result_record
						+ " </b>  is matching to the record count of technical view <b> "
						+ record_count_technicalView + "</b>");
				System.out.println("The record count in kafka <b> " + result_record
						+ " </b>  is matching to the record count of technical view <b> " + record_count_technicalView
						+ "</b>");
			} else {
				logger.log(LogStatus.FAIL,
						"The record count in kafka <b> " + result_record
						+ " </b>  is not matching to the record count of technical view <b> "
						+ record_count_technicalView + "</b>");
				System.out.println("The record count in kafka <b> " + result_record
						+ " </b>  is not matching to the record count of technical view <b> "
						+ record_count_technicalView + "</b>");
			}

			// Getting Timestamp
			WebElement Timestamp = driver.findElement(By.xpath(element.Kafka_Timestamp));
			String Get_Timestamp = Timestamp.getText();

			String Time_stamp = UtilLib.screenshot(driver,
					"User is successfully verified Timestamp :  <b> " + Get_Timestamp + "</b>");
			logger.log(LogStatus.PASS, "User is successfully verified Timestamp <b> : " + Get_Timestamp + "</b>"
					+ logger.addScreenCapture(Time_stamp));

			System.out.println("Record count value is : " + Get_Timestamp);

			WebElement elementjs = driver
					.findElement(By.xpath("//*[@id='ebx_WorkspaceFormBody']/table/tbody/tr[11]/td[2]/div"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", elementjs);
			Thread.sleep(2000);

			// Getting Onekeyid and Panel member from Kafka records
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Object load = js.executeScript(
					"var value = document.evaluate(\"//*[@id='ebx_WorkspaceFormBody']/table/tbody/tr[11]/td[2]/div/span[3]/following::text()[4]\",document, null, XPathResult.STRING_TYPE, null ); return value.stringValue;");
			System.out.println("Load Number : " + load.toString());

			String str = load.toString();
			String okvalue = null;
			String KafkaOKID = null;
			String KafkaEXTAttribute = null;

			String abc[] = str.split("_|:|\\ ");
			for (int i = 0; i < abc.length; i++) {
				System.out.println(abc.length);
				System.out.println(abc[7]);
				okvalue = abc[7];
				break;
			}
			String okvalue1 = okvalue.substring(1, okvalue.length());
			System.out.println(okvalue1);

			String starray[] = okvalue1.split("\\|");
			for (int i = 0; i < starray.length; i++) {
				KafkaOKID = starray[0];
				KafkaEXTAttribute = starray[1];
			}
			System.out.println(KafkaOKID);
			System.out.println(KafkaEXTAttribute);

			String Verify_OnekeyID = UtilLib.screenshot(driver,
					"User can successfully view the details of OneKey ID records in kafka browser ");
			logger.log(LogStatus.PASS, "User can successfully view the details of OneKey ID records in kafka browser"
					+ logger.addScreenCapture(Verify_OnekeyID));

			if (OneKeyIdValue.equalsIgnoreCase(KafkaOKID)) {
				logger.log(LogStatus.PASS,
						"The value captured for OneKey ID in kafka browser is <b> : " + KafkaOKID + "</b>"
								+ " is matching to the panel insertion and  onekey ID value is : <b>" + OneKeyIdValue
								+ "</b>");
			} else {
				logger.log(LogStatus.FAIL,
						"The value captured for OneKey ID in kafka browser is <b> : " + KafkaOKID + "</b>"
								+ " is not matching to the panel insertion and onekey ID  value is : <b>"
								+ OneKeyIdValue + "</b>");
			}

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : kafKa_PANEL_Verification non geo brick
	 * @Description : Kafka_verifiction
	 ********************************************************************************************/
	public static boolean kafKa_Verification_NonGeoBrick(ExtentReports report, ExtentTest logger,
			String publishing_TopicName) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 100);
		String kafka_rdm = "Kafka_RDM [cdtsapp377d:9092]";
		try {
			Thread.sleep(3000);
			try {
				if (driver.findElement(By.xpath("//*[@id='ebx_SelectorPanelToolbar']")).isDisplayed()) {
					Thread.sleep(5000);
					// Click on EBX Admin Button
					driver.findElement(By.xpath(element.Kafka_Click_EBXAdmin)).click();

					// Click on EBX Admin Button
					Thread.sleep(3000);
					driver.findElement(By.xpath(element.Kafka_Click_EBXAdmin2)).click();
					Thread.sleep(5000);
					// driver.findElement(By.xpath(element.Kafka_RDM_Data_On_boarding)).click();
				} else {
					driver.findElement(By.xpath(element.Kafka_RDM_Data_On_boarding)).click();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			driver.findElement(By.xpath(element.Kafka_RDM_Data_On_boarding)).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.Kafka_Core_Administration_Logs)));

			// Selecting the core_administration_logs
			driver.findElement(By.xpath(element.Kafka_Core_Administration_Logs)).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.Kafka_KafkaBrowser)));

			String Select_Core_Admin_logs = UtilLib.screenshot(driver,"Core Admin logs has been selected in the kafka page");
			logger.log(LogStatus.PASS, "Core Admin logs has been selected in the kafka page "+ logger.addScreenCapture(Select_Core_Admin_logs));

			// Click on Kafka browser
			driver.findElement(By.xpath(element.Kafka_KafkaBrowser)).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.Kafka_Actions)));

			// Click on Actions
			Thread.sleep(5000);
			driver.findElement(By.xpath(element.Kafka_Actions)).click();

			// Click on Kafka browser
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.Kafka_Actions_KafkaBrowser)));
			driver.findElement(By.xpath(element.Kafka_Actions_KafkaBrowser)).click();

			// Select Kafka_RDM [cdtsapp377d:9092]
			Select Kafka_RDM = new Select(driver.findElement(By.name("serverToBrowse")));

			WebElement option = Kafka_RDM.getFirstSelectedOption();
			String defaultItem = option.getText();

			if (kafka_rdm.equalsIgnoreCase(defaultItem)) {
				System.out.println(defaultItem);
			} else {
				Kafka_RDM.selectByVisibleText("Kafka_RDM [cdtsapp377d:9092]");
			}

			// Click on LIST_Topics
			driver.findElement(By.xpath(element.Kafka_List_Topics)).click();

			// Selecting the topic
			Thread.sleep(5000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Select listTopic = new Select(driver.findElement(By.name("topicToBrowse")));
			System.out.println(publishing_TopicName);
			listTopic.selectByVisibleText(publishing_TopicName);

			// Click on SEEK_LAST
			driver.findElement(By.xpath(element.Kafka_SEEK_LAST)).click();
			Thread.sleep(10000);
			String NavigationListTopics = UtilLib.screenshot(driver, "User is successfully navigated to List_Topics ");
			logger.log(LogStatus.PASS,"User is successfully navigated to List_Topics" + logger.addScreenCapture(NavigationListTopics));

			// Switching to iframe
			WebElement el = driver.findElement(By.tagName("iframe"));
			System.out.println("iframe in a page is : " + el.getAttribute("style"));
			System.out.println("iframe in a page is : " + el.getAttribute("src"));

			Thread.sleep(3000);
			WebElement srcFrame = driver.findElement(By.xpath("//iframe[contains(@style,'width:100%; height:100%; border:0px;')]"));
			driver.switchTo().frame(srcFrame);

			// Clicking on Last Record
			WebElement webelement = driver.findElement(By.xpath(element.Kafka_SelectRecord));
			Actions action_kafkarecord = new Actions(driver);
			action_kafkarecord.doubleClick(webelement).perform();

			Thread.sleep(2000);

			// Getting Record Count
			WebElement Recordcount = driver.findElement(By.xpath(element.Kafka_RecordCount));
			String Get_RecordCount = Recordcount.getText();
			String Recotd_Count = UtilLib.screenshot(driver,"User is successfully able to verify the complete details of one key ID in kafka browser");
			logger.log(LogStatus.PASS, "User is successfully able to verify the complete details of one key ID <b> " + OneKeyIdValue + " </b> in kafka browser" + logger.addScreenCapture(Recotd_Count));
			System.out.println("Record count value is : " + Get_RecordCount);
			logger.log(LogStatus.PASS, "Record cound value is : " + Get_RecordCount + "in kafka browser");

			// int record_int = ExtendedAttribute_RecordCount.size();
			int result_record = Integer.parseInt(Get_RecordCount);
			if (result_record == record_count_technicalView) {
				logger.log(LogStatus.PASS,"The record count in kafka <b> " + result_record + " </b>  is matching to the record count of technical view <b> " + record_count_technicalView + "</b>");
				System.out.println("The record count in kafka <b> " + result_record + " </b>  is matching to the record count of technical view <b> " + record_count_technicalView + "</b>");
			} else {
				logger.log(LogStatus.FAIL,"The record count in kafka <b> " + result_record + " </b>  is not matching to the record count of technical view <b> " + record_count_technicalView + "</b>");
				System.out.println("The record count in kafka <b> " + result_record + " </b>  is not matching to the record count of technical view <b> " + record_count_technicalView + "</b>");
			}

			// Getting Timestamp
			WebElement Timestamp = driver.findElement(By.xpath(element.Kafka_Timestamp));
			String Get_Timestamp = Timestamp.getText();

			String Time_stamp = UtilLib.screenshot(driver, "User is successfully verified Timestamp :  <b>" + Get_Timestamp + "</b>");
			logger.log(LogStatus.PASS, "User is successfully verified Timestamp <b> : " + Get_Timestamp + "</b>" + logger.addScreenCapture(Time_stamp));

			System.out.println("Record count value is : " + Get_Timestamp);

			WebElement elementjs = driver.findElement(By.xpath("//*[@id='ebx_WorkspaceFormBody']/table/tbody/tr[11]/td[2]/div"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", elementjs);
			Thread.sleep(2000);

			// Getting Onekeyid and Panel member from Kafka records
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Object load = js.executeScript("var value = document.evaluate(\"//*[@id='ebx_WorkspaceFormBody']/table/tbody/tr[11]/td[2]/div/span[3]/following::text()[4]\",document, null, XPathResult.STRING_TYPE, null ); return value.stringValue;");
			System.out.println("Load Number : " + load.toString());
			System.out.println("KafkaOKID values is " + load.toString());

			String kafkaOneKey = load.toString();
			String onkey[] = kafkaOneKey.split(":");
			String KafkaID = onkey[1];
			System.out.println("The splitted one key Id value is : " + KafkaID);

			String UKOK[] = KafkaID.split("~");
			String Kafka_ID = UKOK[0];
			String KafkaOKID = Kafka_ID.replaceAll("\"", "");
			System.out.println("Kafka ID after splitting ~ is " + KafkaOKID.trim());

			String Verify_OnekeyID = UtilLib.screenshot(driver,"User can successfully view the details of OneKey ID records in kafka browser ");
			logger.log(LogStatus.PASS, "User can successfully view the details of OneKey ID records in kafka browser" + logger.addScreenCapture(Verify_OnekeyID));

			if (OneKeyIdValue.trim().contains(KafkaOKID.trim())) {
				logger.log(LogStatus.PASS,"The value captured for OneKey ID in kafka browser is <b> : " + KafkaOKID + "</b>" + " is matching to non geo brick  onekey ID value is : <b>" + OneKeyIdValue+ "</b>");
				System.out.println("The value captured for OneKey ID in kafka browser is <b> : " + KafkaOKID + "</b>" + " is  matching to non geo brick onekey ID  value is : <b>" + OneKeyIdValue + "</b>");
			} else {
				logger.log(LogStatus.FAIL,"The value captured for OneKey ID in kafka browser is <b> : " + KafkaOKID + "</b> is not matching to non geo brick onekey ID  value is : <b>" + OneKeyIdValue + "</b>");
				System.out.println("The value captured for OneKey ID in kafka browser is <b> : " + KafkaOKID + "</b> is not matching to non geo brick onekey ID  value is : <b>" + OneKeyIdValue + "</b>");
			}

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_Duplicate_BoundaryType_Verification
	 * @Description : RDM Duplicate boundary type verification
	 ********************************************************************************************/
	/*public static boolean RDM_Duplicate_BoundaryType_Verification(ExtentReports report, ExtentTest logger)
			throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 100);
		try {

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// click on search
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
			Thread.sleep(4000);
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// Expand the Non Geo brick Search button
			driver.findElement(By.xpath(element.RDM_NonGeoBrick_Search)).click();
			driver.findElement(By.xpath(element.RDM_BoundaryTypeCode_TextBox_Value)).sendKeys("PCG");

			driver.findElement(By.xpath(element.RDM_Apply_Button_NonGeoBrickSearch)).click();

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			String BoundaryType_Filters = UtilLib.screenshot(driver, "Applied the filters for the boundary type ");
			logger.log(LogStatus.PASS,"Applied the filters for the boundary type " + logger.addScreenCapture(BoundaryType_Filters));

			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// Click on first Oney ID from the filtered record
			OneKeyIdValue = driver.findElement(By.xpath("//*[@id='ebx_workspaceTable_mainScroller']/table/tbody/tr[1]/td[1]")).getText();
			System.out.println("The OneKey Id value is : " + OneKeyIdValue);

			WebElement OneKeyIDElementDoubleClick = driver.findElement(By.xpath("//*[@id='ebx_workspaceTable_mainScroller']/table/tbody/tr[1]/td[1]"));
			Actions action_OneKeyId = new Actions(driver);
			action_OneKeyId.doubleClick(OneKeyIDElementDoubleClick).perform();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// Click on update work place button
			Thread.sleep(6000);
			driver.findElement(By.xpath(element.RDM_UpdateWorkplace_button)).click();

			Thread.sleep(8000);

			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			String OneKeyID_Details = UtilLib.screenshot(driver,"User is successfully navigated to workplace authorizing");
			logger.log(LogStatus.PASS, "User is successfully navigated to workplace authorizing"+ logger.addScreenCapture(OneKeyID_Details));

			// Click on Non Geo Brick header
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			WebElement NonGeoBrick_Click = driver.findElement(By.xpath(element.RDM_NonGeoBrick_Header));
			Actions action_NonGeoBrick = new Actions(driver);
			action_NonGeoBrick.doubleClick(NonGeoBrick_Click).perform();
			Thread.sleep(7000);

			// Click on any boundary type code
			String boundaryType_Code = driver.findElement(By.xpath("//*[@class='ebx_tvMainScroller']/table/tbody/tr[1]/td[3]")).getText();
			System.out.println("The boundary Type Code is : " + boundaryType_Code.replaceAll("[\\(\\)\\[\\]\\{\\}]", "").trim());

			// Click on Add button
			WebElement AddIcon_Click = driver.findElement(By.xpath(element.RDM_AddIcon_NonGeoBrick));
			Actions AddIcon_Action = new Actions(driver);
			AddIcon_Action.doubleClick(AddIcon_Click).perform();

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// Select some boundary type value
			driver.switchTo().defaultContent();
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).findElement(By.tagName("iframe"));
			driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

			// drop down for boundary associate
			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).clear();
			driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).sendKeys(boundaryType_Code.replaceAll("[\\(\\)\\[\\]\\{\\}]", "").trim());
			driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).sendKeys(Keys.ENTER);

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Dropdown_Loading)));

			Thread.sleep(5000);
			driver.findElement(By.xpath("(//*[@class='ebx_ISS_Item']/div)[1]")).click();

			// Click on Save and close button
			driver.findElement(By.xpath(element.RDM_SaveAndClose_Button)).click();

			Thread.sleep(2000);
			driver.findElement(By.xpath(element.RDM_Expand_Button_ErrorMessage_Duplicate)).click();

			String ErrorMessage_BoundaryType_Code = UtilLib.screenshot(driver,"Error Message exists for the same boundary type code while inserting");
			logger.log(LogStatus.PASS, "Error Message exists for the same boundary type code while inserting"+ logger.addScreenCapture(ErrorMessage_BoundaryType_Code));

			String ErrorMessage = driver.findElement(By.xpath(element.RDM_ErrorMessage_Duplicate_BoundaryTypeCode)).getText();
			System.out.println("The error message for the duplicate boundary type code is : " + ErrorMessage);
			logger.log(LogStatus.PASS, "The error message for the duplicate boundary type code is : " + ErrorMessage);

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;
	}*/

	public static boolean RDM_Duplicate_BoundaryType_Verification(ExtentReports report, ExtentTest logger)
			throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 100);
		try {

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// click on search
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
			Thread.sleep(4000);
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// Expand the Non Geo brick Search button
			driver.findElement(By.xpath(element.RDM_NonGeoBrick_Search)).click();
			driver.findElement(By.xpath(element.RDM_BoundaryTypeCode_TextBox_Value)).sendKeys("PCG");

			driver.findElement(By.xpath(element.RDM_Apply_Button_NonGeoBrickSearch)).click();

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			String BoundaryType_Filters = UtilLib.screenshot(driver, "Applied the filters for the boundary type ");
			logger.log(LogStatus.PASS,"Applied the filters for the boundary type " + logger.addScreenCapture(BoundaryType_Filters));

			Thread.sleep(3000);
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// Click on first Oney ID from the filtered record
			OneKeyIdValue = driver.findElement(By.xpath("//*[@id='ebx_workspaceTable_mainScroller']/table/tbody/tr[1]/td[1]")).getText();
			System.out.println("The OneKey Id value is : " + OneKeyIdValue);

			WebElement OneKeyIDElementDoubleClick = driver.findElement(By.xpath("//*[@id='ebx_workspaceTable_mainScroller']/table/tbody/tr[1]/td[1]"));
			Actions action_OneKeyId = new Actions(driver);
			action_OneKeyId.doubleClick(OneKeyIDElementDoubleClick).perform();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// Click on update work place button
			Thread.sleep(6000);
			driver.findElement(By.xpath(element.RDM_UpdateWorkplace_button)).click();

			Thread.sleep(8000);

			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			String OneKeyID_Details = UtilLib.screenshot(driver,"User is successfully navigated to workplace authorizing");
			logger.log(LogStatus.PASS, "User is successfully navigated to workplace authorizing"+ logger.addScreenCapture(OneKeyID_Details));

			// Click on Non Geo Brick header
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			WebElement NonGeoBrick_Click = driver.findElement(By.xpath(element.RDM_NonGeoBrick_Header));
			Actions action_NonGeoBrick = new Actions(driver);
			action_NonGeoBrick.doubleClick(NonGeoBrick_Click).perform();
			Thread.sleep(7000);

			// Click on any boundary type code
			String boundaryType_Code = driver.findElement(By.xpath("//*[@class='ebx_tvMainScroller']/table/tbody/tr[1]/td[3]")).getText();
			System.out.println("The boundary Type Code is : " + boundaryType_Code.trim());

			List<WebElement> boundaryType_Codes = driver.findElements(By.xpath("//span[text()='Boundary Type Code']//following::table[@class='ebx_tvMain']/tbody/tr/td[3]"));
			System.out.println(boundaryType_Codes.size());

			for(int i=0; i < boundaryType_Codes.size(); i++){
				List<WebElement> boundaryType_Codes1 = driver.findElements(By.xpath("//span[text()='Boundary Type Code']//following::table[@class='ebx_tvMain']/tbody/tr/td[3]"));
				System.out.println(boundaryType_Codes1.size());

				String boundry_Name = boundaryType_Codes1.get(i).getText().trim();
				System.out.println(boundry_Name);

				// Click on Add button
				Thread.sleep(3000);
				WebElement AddIcon_Click = driver.findElement(By.xpath(element.RDM_AddIcon_NonGeoBrick));
				AddIcon_Click.click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

				//Select some boundary type value
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).findElement(By.tagName("iframe"));
				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// drop down for boundary associate
				Thread.sleep(3000);
				driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).clear();
				driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).sendKeys(boundry_Name);
				driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Dropdown_Loading)));
				Thread.sleep(5000);
				driver.findElement(By.xpath("(//*[@class='ebx_ISS_Item']/div)[1]")).click();

				// Click on Save and close button
				driver.findElement(By.xpath(element.RDM_SaveAndClose_Button)).click();

				//Verify Error message
				Thread.sleep(5000);
				driver.findElement(By.xpath(element.RDM_Expand_Button_ErrorMessage_Duplicate)).click();
				String ErrorMessage_BoundaryType_Code = UtilLib.screenshot(driver,"Error Message exists for the  boundary type code while inserting");
				logger.log(LogStatus.PASS, "Error Message exists for the same boundary type code while inserting <b> " + boundry_Name  + "</b>"+ logger.addScreenCapture(ErrorMessage_BoundaryType_Code));

				String ErrorMessage = driver.findElement(By.xpath(element.RDM_ErrorMessage_Duplicate_BoundaryTypeCode)).getText();
				System.out.println("The error message for the duplicate boundary type code is : " + ErrorMessage);
				logger.log(LogStatus.PASS, "The error message for the duplicate boundary type code is : " + ErrorMessage);

				//Close Error message 
				Thread.sleep(3000);
				driver.findElement(By.xpath(element.RDM_Close_Button)).click();
				Thread.sleep(3000);

				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
				Thread.sleep(2000);

			}



		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_BoundaryType_UpdateAndDelete
	 * @Description : RDM boundary type Update and delete
	 ********************************************************************************************/
	public static boolean RDM_BoundaryType_UpdateAndDelete(ExtentReports report, ExtentTest logger) throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, 100);
		try {
			/********** * Search functionality insert******************************/
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// click on search
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
			Thread.sleep(6000);
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// Expand the Non Geo brick Search button
			driver.findElement(By.xpath(element.RDM_NonGeoBrick_Search)).click();
			driver.findElement(By.xpath(element.RDM_BoundaryTypeCode_TextBox_Value)).sendKeys("PCG");

			driver.findElement(By.xpath(element.RDM_Apply_Button_NonGeoBrickSearch)).click();

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(4000);

			String BoundaryType_Filters = UtilLib.screenshot(driver, "Applied the filters for the boundary type ");
			logger.log(LogStatus.PASS,"Applied the filters for the boundary type " + logger.addScreenCapture(BoundaryType_Filters));


			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// Click on first Oney ID from the filtered record
			OneKeyIdValue = driver.findElement(By.xpath("//*[@id='ebx_workspaceTable_mainScroller']/table/tbody/tr[1]/td[1]")).getText();
			System.out.println("The OneKey Id value is : " + OneKeyIdValue);

			WebElement OneKeyIDElementDoubleClick = driver.findElement(By.xpath("//*[@id='ebx_workspaceTable_mainScroller']/table/tbody/tr[1]/td[1]"));
			Actions action_OneKeyId = new Actions(driver);
			action_OneKeyId.doubleClick(OneKeyIDElementDoubleClick).perform();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			// Click on update work place button
			Thread.sleep(6000);
			driver.findElement(By.xpath(element.RDM_UpdateWorkplace_button)).click();

			Thread.sleep(8000);

			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			String OneKeyID_Details = UtilLib.screenshot(driver,"User is successfully navigated to workplace authorizing");
			logger.log(LogStatus.PASS, "User is successfully navigated to workplace authorizing"+ logger.addScreenCapture(OneKeyID_Details));

			//Click on Non Geo Brick header
			Thread.sleep(3000);
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			WebElement NonGeoBrick_Click = driver.findElement(By.xpath(element.RDM_NonGeoBrick_Header));
			Actions action_NonGeoBrick = new Actions(driver);
			action_NonGeoBrick.doubleClick(NonGeoBrick_Click).perform();
			Thread.sleep(7000);

			String OneKeyId_BusinessWorkplace = driver.findElement(By.xpath(element.RDM_OneKeyID_NonGeoBrickInsertion_Page)).getText();
			String OneKeyId_BusinessWork[] = OneKeyId_BusinessWorkplace.split("-");
			OneKeyIdValue = OneKeyId_BusinessWork[1].trim();
			System.out.println("One key ID value is : " + OneKeyIdValue);

			RecordCount_NonGeoBrick_Boundary = driver.findElements(By.xpath(element.RDM_NonGeoBrick_Record_Count));
			record_NonGeoBrick_Boundary_Table_Count = RecordCount_NonGeoBrick_Boundary.size();
			System.out.println("The record count present in Non - Geo brick header is  : " + record_NonGeoBrick_Boundary_Table_Count );
			ArrayList<String> rowData = new ArrayList<String>();
			List<ArrayList<String>> rowsData = new ArrayList<ArrayList<String>>();

			for (WebElement row : RecordCount_NonGeoBrick_Boundary) {
				rowData.add(row.getText().trim());
			}
			rowsData.add(rowData);
			System.out.println("Records in Non Geo Brick header table are : " + rowData);

			String Details_BoundaryType_NonGeoBrick = UtilLib.screenshot(driver,"Data present in Non Geo Brick header for boundary type of One Key ID ");
			logger.log(LogStatus.PASS,"Data present in Non Geo Brick header for boundary type of One Key ID "+ logger.addScreenCapture(Details_BoundaryType_NonGeoBrick));

			//Click all the checkboxes in table under Non geo brick header
			List<WebElement> count_checkbox = driver.findElements(By.xpath("//*[@class='ebx_tvMainScroller']/table/tbody/tr/td[3]/ancestor::div[@class='ebx_tvMainScroller']/parent::div/div[@class='ebx_tvFixedScrollerWrapper']/div/table/tbody/tr"));
			System.out.println("count of checkbox " + count_checkbox.size());
			for(WebElement row_checkbox : count_checkbox){
				row_checkbox.click();
			}

			//click on Actions and delete
			Thread.sleep(5000);
			driver.findElement(By.xpath(element.RDM_Actions_NonGeoBrickHeader)).click();

			Thread.sleep(4000);
			driver.findElement(By.xpath(element.RDM_DeleteButton_NonGeoBrickHeader)).click();

			String Delete_BoundaryType_NonGeoBrick = UtilLib.screenshot(driver,"Delete pop up after deleting the boundary type ");
			logger.log(LogStatus.PASS,"Delete pop up after deleting the boundary type "+ logger.addScreenCapture(Delete_BoundaryType_NonGeoBrick));

			driver.findElement(By.xpath(element.RDM_OK_Button)).click();

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(3000);

			driver.findElement(By.xpath(element.RDM_SaveButton)).click();

			// Click on Accept button after delete operation
			Thread.sleep(2000);
			driver.switchTo().defaultContent();
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
			driver.findElement(By.xpath(element.RDM_Acceptbutton)).click();

			// handling accept pop up
			driver.findElement(By.xpath(element.RDM_OK_Button)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(7000);

			String Delete_BoundaryType_NonGeoBrick_Details = UtilLib.screenshot(driver,"User has successfully deleted all the boundary types from non geo brick header ");
			logger.log(LogStatus.PASS," User has successfully deleted all the boundary types from non geo brick header  "+ logger.addScreenCapture(Delete_BoundaryType_NonGeoBrick_Details));


			Thread.sleep(3000);
			// click on search
			driver.switchTo().defaultContent();
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
			Thread.sleep(6000);
			driver.findElement(By.xpath(element.RDM_UpdateWorkplace_button)).click();

			OneKeyId_BusinessWorkplace = driver.findElement(By.xpath(element.RDM_OneKeyID_NonGeoBrickInsertion_Page)).getText();
			//			OneKeyId_BusinessWork[] = OneKeyId_BusinessWorkplace.split("-");
			OneKeyIdValue = OneKeyId_BusinessWork[1].trim();
			System.out.println("One key ID value is : " + OneKeyIdValue);

			//Click on Non Geo Brick header
			Thread.sleep(6000);
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			WebElement NonGeoBrick_Click_Element = driver.findElement(By.xpath(element.RDM_NonGeoBrick_Header));
			Actions action_NonGeoBrick_Click = new Actions(driver);
			action_NonGeoBrick_Click.doubleClick(NonGeoBrick_Click_Element).perform();
			Thread.sleep(7000);

			RecordCount_NonGeoBrick_Boundary = driver.findElements(By.xpath(element.RDM_NonGeoBrick_Record_Count));
			record_NonGeoBrick_Boundary_Table_Count = RecordCount_NonGeoBrick_Boundary.size();
			//			System.out.println("The record count present in Non - Geo brick header is  : " + record_NonGeoBrick_Boundary_Table_Count );
			ArrayList<String> rowData_update = new ArrayList<String>();

			for (WebElement row : RecordCount_NonGeoBrick_Boundary) {
				rowData_update.add(row.getText().replaceAll("[\\(\\)\\[\\]\\{\\}]", "").trim());

				rowsData.add(rowData_update);
			}
			//			System.out.println("Records in Non Geo Brick header table are : " + rowData_update);

			//			Thread.sleep(3000);
			for(int i =0; i<rowData.size();i++){

				// Click on Add button
				Thread.sleep(3000);
				WebElement AddIcon_Click = driver.findElement(By.xpath(element.RDM_AddIcon_NonGeoBrick));
				Actions AddIcon_Action = new Actions(driver);
				AddIcon_Action.doubleClick(AddIcon_Click).perform();

				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

				// Select some boundary type value
				Thread.sleep(4000);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).findElement(By.tagName("iframe"));
				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_OuterPanelMember_Frame);

				// drop down for boundary associate
				driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).clear();
				driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).sendKeys(rowData.get(i));
				driver.findElement(By.xpath(element.RDM_BoundaryAssociation_Dropdown)).sendKeys(Keys.ENTER);

				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Dropdown_Loading)));

				Thread.sleep(5000);
				driver.findElement(By.xpath("(//*[@class='ebx_ISS_Item']/div)[1]")).click();

				String Boundary_Type_Insert = UtilLib.screenshot(driver,"Boundary type inserted for non geo brick one key ID ");
				logger.log(LogStatus.PASS,"Boundary type inserted is <b> " + rowData.get(i) + " </b> for non geo brick one key ID " + OneKeyIdValue + "</b>" + logger.addScreenCapture(Boundary_Type_Insert));

				// Click on Save and close button
				driver.findElement(By.xpath(element.RDM_SaveAndClose_Button)).click();

				driver.switchTo().defaultContent();
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

				Thread.sleep(3000);

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element.RDM_BoundaryAssociation_Dropdown_Loading)));
			}


			String Details_Boundary_Inserted = UtilLib.screenshot(driver,"Non Geo Brick boundary types are inserted successfully");
			logger.log(LogStatus.PASS, "Non Geo Brick boundary types are inserted successfully "+ logger.addScreenCapture(Details_Boundary_Inserted));

			// Click on Accept button
			driver.switchTo().defaultContent();
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame).switchTo().frame(element.RDM_WorkPlace_TableFrame);
			driver.findElement(By.xpath(element.RDM_Acceptbutton)).click();

			String Accept_scrrenshot = UtilLib.screenshot(driver,"Accept pop up after updating the boundary type");
			logger.log(LogStatus.PASS, "Accept pop up after updating the boundary type"+ logger.addScreenCapture(Accept_scrrenshot));

			// handling accept pop up
			driver.findElement(By.xpath(element.RDM_OK_Button)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(7000);

			String PanelInsertion_Screencapture = UtilLib.screenshot(driver,"User has successfully performed the non Geo Brick update ");
			logger.log(LogStatus.PASS,"User has successfully performed the non Geo Brick update for one key ID <b> " + OneKeyIdValue + "</b>"+ logger.addScreenCapture(PanelInsertion_Screencapture));

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;

	}
	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_TechnicalView_Validation_Update_NonGeoBrick
	 * @Description : RDM Boundary type update and delete verification in technical view
	 ********************************************************************************************/
	public static boolean RDM_TechnicalView_Validation_Update_NonGeoBrick(ExtentReports report, ExtentTest logger) throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, 100);

		// Logic for retrieving the system date
		DateFormat dff = new SimpleDateFormat("dd/MM/yyyy");
		Date today_Date = Calendar.getInstance().getTime();
		String todays_Date = dff.format(today_Date);
		System.out.println("The system date is : " + todays_Date);

		try{

			driver.switchTo().defaultContent();

			// click on technical view
			driver.findElement(By.xpath(element.RDM_TechView)).click();
			Thread.sleep(4000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);

			// Click on Organization
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_Organization_ExpandButton)));
			driver.findElement(By.xpath(element.RDM_Organization_ExpandButton)).click();

			// click on operational activity center assigned boundary type
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_OperationalActivityCenterAssigned_boundary_Type)));
			driver.findElement(By.xpath(element.RDM_OperationalActivityCenterAssigned_boundary_Type)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));

			Thread.sleep(6000);
			String NavigationWorkplace_TV = UtilLib.screenshot(driver,"User is successfully navigated to technical view Workplace page");
			logger.log(LogStatus.PASS, "User is successfully navigated to technical view Workplace page"+ logger.addScreenCapture(NavigationWorkplace_TV));

			// Click on Search Icon in work place page of technical view
			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			// select the panel code from technical view
			Thread.sleep(5000);
			Select SourceSystemKey = new Select(driver.findElement(By.xpath(element.RDM_panelcode)));
			SourceSystemKey.selectByVisibleText("    Source system key");

			// Enter One key ID for which the panel is inserted
			Thread.sleep(5000);
			driver.findElement(By.xpath(element.RDM_Enter_Panelcode)).sendKeys(OneKeyIdValue);

			// Click on Apply
			Thread.sleep(2000);
			driver.findElement(By.xpath(element.RDM_Click_Apply)).click();

			Thread.sleep(2000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			Thread.sleep(3000);

			String FiltersOneKeyId_Screenshot = UtilLib.screenshot(driver,"User has successfully applied the filter of One Key ID for which the boundary type code are updated and deleted");
			logger.log(LogStatus.PASS, "User has successfully applied the filter of One Key ID for which the boundary type code are updated and deleted "+ logger.addScreenCapture(FiltersOneKeyId_Screenshot));

			driver.findElement(By.xpath(element.RDM_SearchIcon)).click();

			//Read the operational activity center assigned boundary value from the table
			List<WebElement> AssignedBoundaryTypeValue_List = driver.findElements(By.xpath(element.RDM_AssignedBoundary_operationalActivity_List));
			RecordCount_NonGeoBrick_Boundary = driver.findElements(By.xpath(element.RDM_AssignedBoundary_operationalActivity_List));
			System.out.println("Assigned boundary type value : " + AssignedBoundaryTypeValue_List.size());

			for(int j=0; j< AssignedBoundaryTypeValue_List.size();j++){
				String AssignedBoundaryTypeValue = AssignedBoundaryTypeValue_List.get(j).getText();
				System.out.println("Assigned boundary type value " + AssignedBoundaryTypeValue);

				String [] AssignedBoundaryTypeValue_Array = AssignedBoundaryTypeValue.split("-");
				System.out.println("AssignedBoundaryTypeValue_Array is " + AssignedBoundaryTypeValue_Array[1].trim());

				List<WebElement> AssignedValue_Checkbox = driver.findElements(By.xpath("//*[@id='ebx_workspaceTable_fixedScroller']/table/tbody/tr/td[4]/table/tbody/tr/td[contains(text(),'"+AssignedBoundaryTypeValue_Array[1].trim()+"')]/parent::tr/parent::tbody/parent::table/parent::td/ancestor::tr/td[@class='ebx_tvSelectCell']/input"));
				System.out.println("Assigned checkbox value is : " + AssignedValue_Checkbox.size());
				for (int i = 0; i < AssignedValue_Checkbox.size(); i++) {
					AssignedValue_Checkbox.get(i).click();
				}

				//Click on Actions 
				Thread.sleep(6000);
				driver.findElement(By.xpath(element.RDM_Actions_Button_operationalAssignedBoundary)).click();
				Thread.sleep(2000);

				//Click on view history 
				driver.findElement(By.xpath(element.RDM_ViewHistory_OperationalActivity)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
				Thread.sleep(2000);

				// Compare 2 rows which contains update and insert
				driver.switchTo().defaultContent();

				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
				driver.switchTo().frame(element.RDM_PanelExtensionAttribute_Frame);

				Thread.sleep(5000);
				driver.findElement(By.xpath("//*[@id='ebx_workspaceTable_fixedScroller']/table/tbody/tr[2]/td/input")).click();
				driver.findElement(By.xpath("//*[@id='ebx_workspaceTable_fixedScroller']/table/tbody/tr[1]/td/input")).click();

				String Boundary_Type_Code_History_Verification = UtilLib.screenshot(driver,"View the history of One Key ID for which the boundary type code are updated and deleted");
				logger.log(LogStatus.PASS, "View the history of One Key ID for which the boundary type code are updated and deleted  "+ logger.addScreenCapture(Boundary_Type_Code_History_Verification));

				//click on actions button to compare
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_ActionsButton_ActivityCenterAssignedBoundary)));
				driver.findElement(By.xpath(element.RDM_ActionsButton_ActivityCenterAssignedBoundary)).click();

				Thread.sleep(3000);
				driver.findElement(By.xpath(element.RDM_Compare_Button)).click();

				// Click on View button
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_View_Button_ComparePage)));
				Thread.sleep(7000);
				driver.findElement(By.xpath(element.RDM_View_Button_ComparePage)).click();

				// Click on hide similarities attribute
				Thread.sleep(5000);
				driver.findElement(By.xpath(element.RDM_Hide_Similarities_Field)).click();
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element.RDM_CompareRecord_Text)));
				Thread.sleep(4000);

				String Compare_Records = UtilLib.screenshot(driver, "User can successfully verify the record compare for both deletion and updation of the boundary type ");
				logger.log(LogStatus.PASS, "User can successfully verify the record compare for both deletion and updation of boundary type " + logger.addScreenCapture(Compare_Records));

				//Retrieve the source transaction time stamp for creation
				String SourceTransactionTimeStamp_Comparing = driver.findElement(By.xpath(element.RDM_SourceTransactionTimeStamp_Comparing)).getText();
				System.out.println("Source transaction time stamp is " + SourceTransactionTimeStamp_Comparing);
				if(SourceTransactionTimeStamp_Comparing.contains(todays_Date)){
					System.out.println("The source transaction time stamp is : " + SourceTransactionTimeStamp_Comparing + " for creation is matching to the today's date " + todays_Date + " for boundary type" + AssignedBoundaryTypeValue_Array[1].trim());
					logger.log(LogStatus.PASS,"The source transaction time stamp is : <b> " + SourceTransactionTimeStamp_Comparing + " </b> for creation is matching to the today's date <b> " + todays_Date  + " </b>  for boundary type <b> " + AssignedBoundaryTypeValue_Array[1].trim() + "</b>");
				}else{
					System.out.println("The source transaction time stamp is : " + SourceTransactionTimeStamp_Comparing + " for creation is not matching to the today's date " + todays_Date  + " for boundary type" + AssignedBoundaryTypeValue_Array[1].trim());
					logger.log(LogStatus.FAIL,"The source transaction time stamp is : <b> " + SourceTransactionTimeStamp_Comparing + " </b> for creation is not matching to the today's date <b> " + todays_Date  + " </b> for boundary type <b> " + AssignedBoundaryTypeValue_Array[1].trim() + "</b>");
				}

				//Retrieve the source transaction time stamp for update
				String SourceTransactionTimeStamp_Comparing_Update = driver.findElement(By.xpath(element.RDM_SourceTransactionTimeStamp_Update_Comparing)).getText();
				System.out.println("Source transaction time stamp update is " + SourceTransactionTimeStamp_Comparing_Update);
				if(SourceTransactionTimeStamp_Comparing_Update.contains(todays_Date)){
					System.out.println("The source transaction time stamp is : " + SourceTransactionTimeStamp_Comparing_Update + " for update is matching to the today's date " + todays_Date + " for boundary type " + AssignedBoundaryTypeValue_Array[1].trim());
					logger.log(LogStatus.PASS,"The source transaction time stamp is : <b> " + SourceTransactionTimeStamp_Comparing_Update + " </b> for update is matching to the today's date <b> " + todays_Date  + " </b>  for boundary type <b> " + AssignedBoundaryTypeValue_Array[1].trim() + "</b>");
				}else{
					System.out.println("The source transaction time stamp is : " + SourceTransactionTimeStamp_Comparing_Update + " for update is not matching to the today's date " + todays_Date + " for boundary type " + AssignedBoundaryTypeValue_Array[1].trim());
					logger.log(LogStatus.FAIL,"The source transaction time stamp is : <b> " + SourceTransactionTimeStamp_Comparing_Update + " </b> for update is not matching to the today's date <b> " + todays_Date  + " </b>  for boundary type <b> " + AssignedBoundaryTypeValue_Array[1].trim() + "</b>");
				}

				//Verify the deleted status code for creation and update
				String OperationType_Comparing = driver.findElement(By.xpath(element.RDM_OperationType_Comparing)).getText();
				System.out.println("Operation type in comparing page is : " + OperationType_Comparing);

				if(OperationType_Comparing.equalsIgnoreCase("Creation")){
					String deletedStatusCode = driver.findElement(By.xpath("//*[@id='ebx_WorkspaceFormBody']/div[1]/table/tbody/tr[6]/td[text()='"+OperationType_Comparing+"']/parent::tr/parent::tbody/tr/td/span[text()='Deleted Status Code']/parent::td/../td[2]")).getText();
					System.out.println("Deleted Status code is : " + deletedStatusCode);
					logger.log(LogStatus.PASS, "The deleted Status Code is : <b> " + deletedStatusCode + " </b> for operation type <b> " + OperationType_Comparing + "</b> boundary type <b> " + AssignedBoundaryTypeValue_Array[1].trim() + "</b>");
				}else if(OperationType_Comparing.equalsIgnoreCase("Update")){
					String deletedStatusCode1 = driver.findElement(By.xpath("//*[@id='ebx_WorkspaceFormBody']/div[1]/table/tbody/tr[6]/td[text()='"+OperationType_Comparing+"']/parent::tr/parent::tbody/tr/td/span[text()='Deleted Status Code']/parent::td/../td[2]")).getText();
					System.out.println("Deleted Status code is : " + deletedStatusCode1);
					logger.log(LogStatus.PASS, "The deleted Status Code is : <b> " + deletedStatusCode1 + " </b> for operation type <b> " + OperationType_Comparing + "</b> boundary type <b> " + AssignedBoundaryTypeValue_Array[1].trim() + "</b>");
				}else{
					System.out.println("Operation type is : " + OperationType_Comparing);
				}

				String OperationTypeComparing = driver.findElement(By.xpath(element.RDM_OperationType_Comparing_SecondTab)).getText();
				System.out.println("Operation type in comparing page is : " + OperationTypeComparing);

				if(OperationTypeComparing.equalsIgnoreCase("Creation")){
					String deletedStatusCode_Second = driver.findElement(By.xpath("//*[@id='ebx_WorkspaceFormBody']/div[1]/table/tbody/tr[6]/td[text()='"+OperationTypeComparing+"']/parent::tr/parent::tbody/tr/td/span[text()='Deleted Status Code']/parent::td/../td[2]")).getText();
					System.out.println("Deleted Status code is : " + deletedStatusCode_Second);
					logger.log(LogStatus.PASS, "The deleted Status Code is : <b> " + deletedStatusCode_Second + " </b> for operation type <b> " + OperationTypeComparing + "</b> boundary type <b> " + AssignedBoundaryTypeValue_Array[1].trim() + "</b>");
				}else if(OperationTypeComparing.equalsIgnoreCase("Update")){
					String deletedStatusCode1_Second = driver.findElement(By.xpath("//*[@id='ebx_WorkspaceFormBody']/div[1]/table/tbody/tr[6]/td[text()='"+OperationTypeComparing+"']/parent::tr/parent::tbody/tr/td/span[text()='Deleted Status Code']/parent::td/../td[2]")).getText();
					System.out.println("Deleted Status code is : " + deletedStatusCode1_Second);
					logger.log(LogStatus.PASS, "The deleted Status Code is : <b> " + deletedStatusCode1_Second + " </b> for operation type <b> " + OperationTypeComparing + "</b> boundary type <b> " + AssignedBoundaryTypeValue_Array[1].trim() + "</b>");
				}else{
					System.out.println("Operation type is : " + OperationTypeComparing);
				}

				// click on close symbol 
				Thread.sleep(4000);
				driver.findElement(By.xpath(element.RDM_CloseSymbol_PanelExtension)).click();

				Thread.sleep(4000);
				driver.switchTo().frame(element.RDM_WorkPlace_TableFrame);
			}

		}catch(Exception e){
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());	
		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : RDM_Logout
	 * @Description : Logout from RDM application
	 ********************************************************************************************/
	public static boolean RDM_Logout(ExtentReports report, ExtentTest logger) throws IOException {
		WebDriverWait wait = new WebDriverWait(driver, 100);
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element.RDM_Loading)));
			driver.switchTo().defaultContent();

			// click on logout
			driver.findElement(By.xpath(element.RDM_LoggedIn_Link)).click();
			driver.findElement(By.xpath(element.RDM_LogOut_Button)).click();

			String logoutApplication = UtilLib.screenshot(driver,"User is successfully logged out from the RDM application");
			logger.log(LogStatus.PASS, "User is successfully logged out from the RDM application "+ logger.addScreenCapture(logoutApplication));

			driver.close();

		} catch (Exception e) {
			e.printStackTrace();
			String ErrorHandling = UtilLib.screenshot(driver, "Error captured ");
			logger.log(LogStatus.FAIL, "Error captured" + logger.addScreenCapture(ErrorHandling));
			logger.log(LogStatus.FAIL, e.getMessage());
		}
		return false;
	}

	/********************************************************************************************
	 * @throws IOException
	 * @Function_Name : Kafka_Logout
	 * @Description : Logout from Kafka application
	 ********************************************************************************************/
	public static boolean Kafka_Logout(ExtentReports report, ExtentTest logger) throws IOException {
		try {
			// click on logout
			Thread.sleep(2000);
			driver.switchTo().defaultContent();
			driver.findElement(By.xpath(element.Kafka_Click_EBXAdmin)).click();
			driver.findElement(By.xpath(element.RDM_LogOut_Button)).click();

			String logoutApplication = UtilLib.screenshot(driver,"User is successfully logged out from the Kafka application");
			logger.log(LogStatus.PASS, "User is successfully logged out from the Kafka application " + logger.addScreenCapture(logoutApplication));

			driver.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
}
