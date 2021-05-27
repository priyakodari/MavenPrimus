package DriverFactory;
import java.io.FileInputStream;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Utilities.ExcelFileUtil;


public class AppTest {
	WebDriver driver;
	ExtentReports report;
	ExtentTest test;
	Properties configprop;
	String inputpath="D:\\Automation\\Maven_Primus\\TestInput\\LoginTest.xlsx";
	String outputpath="D:\\Automation\\Maven_Primus\\TestOutput\\Results.xlsx";
	@BeforeTest
	public void setUp()throws Throwable
	{
		report = new ExtentReports("./ExtentReports/Login.html");
	configprop = new Properties();
	configprop.load(new FileInputStream("D:\\Automation\\Maven_Primus\\PropertyFile\\Environment.properties"));
	if(configprop.getProperty("Browser").equalsIgnoreCase("chrome"))
	{
		System.setProperty("webdriver.chrome.driver", "D:\\Automation\\Maven_Primus\\CommonDrivers\\chromedriver.exe");
		driver = new ChromeDriver();
	}
	else if(configprop.getProperty("Browser").equalsIgnoreCase("firefox"))
	{
		System.setProperty("webdriver.gecko.driver", "D:\\Automation\\Maven_Primus\\CommonDrivers\\geckodriver.exe");
		driver = new FirefoxDriver();
	}
	else
	{
		Reporter.log("Browser value is not matching",true);
	}
	}
	@Test
	public void verifyLogin()throws Throwable
	{
		ExcelFileUtil xl = new ExcelFileUtil(inputpath);
		int rc= xl.rowCount("Login");
		Reporter.log("No of rows are::"+rc,true);
		for(int i=1; i<=rc; i++)
		{
			driver.get(configprop.getProperty("Url"));
			driver.manage().window().maximize();
			test= report.startTest("Validate Login");
			String username = xl.getCellData("Login", i, 0);
			String password = xl.getCellData("Login", i, 1);
			driver.findElement(By.name("txtUsername")).sendKeys(username);
			driver.findElement(By.name("txtPassword")).sendKeys(password);
			driver.findElement(By.name("Submit")).click();
			Thread.sleep(4000);
			if(driver.getCurrentUrl().contains("dashboard"))
			{
				Reporter.log("Login success",true);
				test.log(LogStatus.PASS, "Login Success");
				xl.setCellData("Login", i, 2, "Login success", outputpath);
				xl.setCellData("Login", i, 3, "Pass", outputpath);
			}
			else
			{
				Reporter.log("Login Fail",true);
				test.log(LogStatus.FAIL, "Login Fail");
				xl.setCellData("Login", i, 2, "Login Fail", outputpath);
				xl.setCellData("Login", i, 3, "Fail", outputpath);
			}
			report.endTest(test);
			report.flush();
		}
	}
	@AfterTest
	public void tearDown()
	{
		driver.close();
	}
	}

//3797c11720f8462493383ee988ff0650













