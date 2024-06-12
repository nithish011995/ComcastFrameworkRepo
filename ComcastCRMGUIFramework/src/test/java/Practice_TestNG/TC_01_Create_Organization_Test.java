package Practice_TestNG;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.Random;

import javax.crypto.Cipher;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import com.comcast.crm.generic.fileutility.ExcelUtility;
import com.comcast.crm.generic.fileutility.FileUtility;
import com.comcast.crm.generic.webdriverutility.JavaUtilty;
import com.comcast.crm.generic.webdriverutility.WebDriverUtility;
import com.comcast.crm.objectrepository.CreateNewOrganizationPage;
import com.comcast.crm.objectrepository.HomePage;
import com.comcast.crm.objectrepository.LoginPage;
import com.comcast.crm.objectrepository.OrganizationInfoPage;
import com.comcast.crm.objectrepository.OrganizationPage;

public class TC_01_Create_Organization_Test {
	
//smoke testing(checking only cretical functions)
	public static void main(String[] args) throws Throwable {
	
	FileUtility flib=new FileUtility();
	String URL = flib.getDataFromPropertyFile("url");
	String BROWSER = flib.getDataFromPropertyFile("browser");
	String USERNAME = flib.getDataFromPropertyFile("username");
	String PASSWORD = flib.getDataFromPropertyFile("password");
	
	JavaUtilty jlib=new JavaUtilty();
	int random = jlib.getRandomNumber();
	
	ExcelUtility elib=new ExcelUtility();
	String orgName = elib.getDataFromExcelFile("org", 1, 2)+random;
	
	WebDriver driver=null;
	
	if(BROWSER.equals("chrome"))
	{
		driver=new ChromeDriver();
	}else if(BROWSER.equals("edge"))
	{
		driver=new EdgeDriver();
	}else
	{
		driver=new FirefoxDriver();
	}
	
	WebDriverUtility wlib=new WebDriverUtility();
	
	wlib.maximizeWindow(driver);
	wlib.waitForPageToLoad(driver);
	
	driver.get(URL);
	
	LoginPage lp=new LoginPage(driver);
	lp.getUserNameEdt().sendKeys(USERNAME);
	lp.getPasswordEdt().sendKeys(PASSWORD);
	lp.getLoginBtn().click();
	
	HomePage hp=new HomePage(driver);
	hp.getOrgLink().click();
	
	OrganizationPage op=new OrganizationPage(driver);
	op.getCreateOrgLookUpImg().click();
	
	CreateNewOrganizationPage cop=new CreateNewOrganizationPage(driver);
	cop.getOrgnameEdt().sendKeys(orgName);
	cop.getSaveBtn().click();
	
	OrganizationInfoPage oip=new OrganizationInfoPage(driver);
	String headerInfo=oip.getOrgHeaderText().getText();
	
	if( headerInfo.contains(orgName))
	{
		System.out.println(orgName+"Organization successfully created");
	}else
	{
		System.out.println(orgName+"Organization is not successfully created");
	}
	
	String actualOrgName = oip.getActualOrgnameText().getText();
	if( actualOrgName.contains(orgName))
	{
		System.out.println(orgName+" information is verified==PASS");
	}else
	{
		System.out.println(orgName+" information is not verified==Fail");
	}
	
	
	
	elib.setDataIntoExcel("org", 1, 7, "PASS1");
	System.out.println("PASS");
	hp.Logout(driver);
}
}
