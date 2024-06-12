package Practice_TestNG;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.Random;

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

public class TC_03_Create_Organization_With_Phone_no_Test {
	
//smoke testing(checking only cretical functions)
	public static void main(String[] args) throws Throwable {
	
	FileInputStream fis=new FileInputStream(".\\data\\commondata.properties.txt");
	Properties p=new Properties();
	p.load(fis);
	
	String URL = p.getProperty("url");
	String BROWSER=p.getProperty("browser");
	String USERNAME=p.getProperty("username");
	String PASSWORD=p.getProperty("password");
	Random r=new Random();
	int random = r.nextInt(1000);
	
	FileInputStream fis1=new FileInputStream(".\\data\\testScriptdata.xlsx");
	Workbook wb = WorkbookFactory.create(fis1);
	Sheet sh= wb.getSheet("org");
	Row row = sh.getRow(7);
	String orgName = row.getCell(2).toString()+random;
	String phoneNum=row.getCell(3).toString();
	
	
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
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	driver.get(URL);
	driver.findElement(By.name("user_name")).sendKeys(USERNAME);
	driver.findElement(By.name("user_password")).sendKeys(PASSWORD);
	driver.findElement(By.id("submitButton")).click();
	
	driver.findElement(By.linkText("Organizations")).click();
	driver.findElement(By.xpath("//img[@src='themes/softed/images/btnL3Add.gif']")).click();
	driver.findElement(By.name("accountname")).sendKeys(orgName);
	
	driver.findElement(By.id("phone")).sendKeys(phoneNum);
	
	driver.findElement(By.xpath("(//input[@title='Save [Alt+S]'])[1]")).click();

	String actPhoneNum  = driver.findElement(By.id("dtlview_Phone")).getText();
	if( actPhoneNum.equals(phoneNum))
	{
		System.out.println(phoneNum+" information is verified==PASS");
	}else
	{
		System.out.println(phoneNum+" information is not verified==FAIL");	
	}
	
	Actions a=new Actions(driver);
	a.moveToElement(driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']"))).perform();
	driver.findElement(By.linkText("Sign Out")).click();
	driver.quit();
	
	Cell cell = row.createCell(4);
	cell.setCellType(CellType.STRING);
	cell.setCellValue("PASS");
	
	FileOutputStream fos=new FileOutputStream(".\\data\\testScriptdata.xlsx");
	wb.write(fos);
	System.out.println("result in excel");
	wb.close();
	
	System.out.println("===pass===");
}
}
