
package com.iqvia.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class UtilLib {
	public static WebDriver driver;
	 static String Reportgenenrationfolder = null;
	 public static FileInputStream fis = null;
	 public static XSSFWorkbook workbook = null;
		public static XSSFSheet sheet = null;
		public static XSSFRow row = null;
		public static XSSFCell cell = null;
		
	/******************************************************************************************** 
	 * @throws IOException 
	 * @Function_Name : getDriver
	 * @Description : Creates Driver object to launch scripts in different browser
	 ********************************************************************************************/
	public static  WebDriver getDriver () throws IOException{
		String browserType=getPropertiesValue("Browser");
		switch (browserType) {
		case "Chrome":
			System.out.println("=========="+browserType);

			System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"/chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			break;
			
		case "Firefox":
			System.out.println("**************"+browserType);
			System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir")+"/geckodriver.exe");
			File pathToBinary = new File("C:\\Users\\Madhura.G\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
			FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			driver = new FirefoxDriver();
			break;
			
		case "IE":
			System.out.println("*********"+browserType);
			System.setProperty("webdriver.IEDriverServer.driver",System.getProperty("user.dir")+"/IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			driver.manage().window().maximize();
			break;
		default:
			System.out.println("browser : " + browserType+ " is invalid..");
		}
		return driver;
	}
	/******************************************************************************************** 
	 * @Function_Name : getPropertiesValue
	 * @Description : Read values from property file for constant values
	 ********************************************************************************************/
	public static String getPropertiesValue(String key) throws IOException {
		String workingDir = System.getProperty("user.dir");
		String configpath = workingDir + "/RDM_TestData/config.properties";
		Properties props = new Properties();
		FileInputStream URLPath = new FileInputStream(configpath);
		props.load(URLPath);
		String url = props.getProperty(key);
		return url;
	}
	
	/********************************************************************************************
	 * @throws IOException 
	 * @Function_Name :  screenshot
	 * @Description : Function to capture the screenshot
	 ********************************************************************************************/
	public static String screenshot(WebDriver driver, String screenshotname) throws IOException{

		String SreenshotPath = null;
		DateFormat screenshotName1 = new SimpleDateFormat("dd-MMMM-yyyy_HH-mm-ss");
		Date screenshotDate = new Date();
		String picName = screenshotName1.format(screenshotDate);

		String namescreenshot = screenshotname.replaceAll("[:?/*<>|]","");
		System.out.println("Reportgenenrationfolder is : " + Reportgenenrationfolder);
		
		String folderPath = "./" + picName+"_" + namescreenshot + ".png";
		System.out.println(picName);
		System.out.println(folderPath);

		File file = new File(Reportgenenrationfolder+"/"+folderPath);
		System.out.println("****" + file);
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SreenshotPath = folderPath;
		return SreenshotPath;
	}
	
	/********************************************************************************************
	 * @Function_Name : getCellData
	 * @Description :  Reusable function for retrieving the test data from excel sheet
	 ********************************************************************************************/
	public static String getCellData(String xlFilePath,String sheetName, String colName, int rowNum,String cellvaltype) throws IOException
	{
		String cellvalue="";
		fis = new FileInputStream(xlFilePath);
		workbook = new XSSFWorkbook(fis);
		try
		{
			int col_Num = -1;
			sheet = workbook.getSheet(sheetName);
			row = sheet.getRow(0);
			for(int i = 0; i < row.getLastCellNum(); i++)
			{
				if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num = i;
			}

			row = sheet.getRow(rowNum);
			cell = row.getCell(col_Num);

			if (cell == null)
				return "";

			switch (cellvaltype)

			{
			case "date":
			{
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String time = sdf.format(cell.getDateCellValue());
				cellvalue=String.valueOf(time);
				break;
			}
			case "string":
			{
				cellvalue=cell.getStringCellValue();
				break;
			}
			case "int":
			{
				cellvalue=String.valueOf(cell.getNumericCellValue());
				break;
			}

			} 
			System.out.println(cellvalue);
			return cellvalue;

		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "row "+rowNum+" or column "+colName +" does not exist  in Excel";
		}
	}
	/********************************************************************************************
	 * @Function_Name :  getRowCount
	 * @Description : Function to retrieve the Row count from the excel sheet
	 ********************************************************************************************/
	public static  int getRowCount(String xlpath,String sheetName)
	{
		int rc=0;
		try{
			FileInputStream fis=new FileInputStream(xlpath);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet(sheetName);
			rc = sheet.getPhysicalNumberOfRows();
		}catch(Exception e){
			e.getMessage();
			e.printStackTrace();
		}
		return rc;
	}
	/******************************************************************************************** 
	 * @Function_Name : systemTimeStamp
	 * @Description : Function to fetch the current system date 
	 ********************************************************************************************/
	public static String systemTimeStamp() {
		DateFormat df = new SimpleDateFormat("dd.MMM.yyyy-HH.mm.ss");
		Date today = Calendar.getInstance().getTime();
		String runTime = df.format(today);
		//System.out.println(df.format(today));
		return runTime;
	}

	/******************************************************************************************** 
	 * @Function_Name : Instance
	 * @Description : Function to generate Extent Reports
	 ********************************************************************************************/
	public static ExtentReports Instance(String TestcaseName){
		ExtentReports extent ;
		String currentTime = UtilLib.systemTimeStamp();
		String foldername=TestcaseName+currentTime;
		
		String folderName_New = foldername;
		
//		 create directory which has test case name with date and time
		File folderdir=new File("./ExcecutionReport/" + foldername);
		folderdir.mkdirs(); 

		 Reportgenenrationfolder="./ExcecutionReport/" + foldername;
		 String ReportPath = Reportgenenrationfolder  + "/Automation_" + TestcaseName+"_" + currentTime + "Extentreport.html";
		extent= new ExtentReports(ReportPath,false);
		return extent;
	}

	/********************************************************************************************
	 * @Function_Name :  switchToFrame
	 * @Description : Switch To particular Frame
	 ********************************************************************************************/
	public static boolean switchToFrame(String ElementName, String xpath){
		
		int RerunFlag = 0;
		int count = 1;
		List<WebElement> frames = driver.findElements(By.xpath("//iframe"));
		System.out.println("Total Frames : " + frames.size());

		for(WebElement frame : frames)
		{
			driver.switchTo().frame(frame);
			
			try {

				System.out.println(""+ElementName + " "+driver.findElement(By.id(xpath)).getTagName());
				System.out.println("Found the frame!!!");
				RerunFlag--;
				break;				
				
			}catch(Exception ex) {
				ex.printStackTrace();
//			System.out.println("Element not Found in frame "+count++);
			}
		}
		RerunFlag++;
		
		if(RerunFlag>0){
//			logger.log(LogStatus.FAIL, "Switched to frame and Element: "+ElementName+"", " is not present in the frame");
			System.out.println("Switched to frame and Element: "+ElementName+"is present in the frame ");
			return false;
		} else{
//			logger.log(LogStatus.PASS, "Switched to frame and Element: "+ElementName+"", " is present in the frame");
			System.out.println("Switched to frame and Element: "+ElementName+"is present in the frame ");
			return true;
		}
	
}
	
}


