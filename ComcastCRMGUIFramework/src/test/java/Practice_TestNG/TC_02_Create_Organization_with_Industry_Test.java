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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class TC_02_Create_Organization_with_Industry_Test {
	
//regression testing
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
	Row row = sh.getRow(4);
	String orgName = row.getCell(2).toString()+random;
	String industry=row.getCell(3).toString();
	String type=row.getCell(4).toString();
	
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
	
	WebElement dropdown1 = driver.findElement(By.name("industry"));
	Select s1=new Select(dropdown1);
	s1.selectByVisibleText(industry);
	
	WebElement dropdown2 = driver.findElement(By.name("accounttype"));
	Select s2=new Select(dropdown2);
	s2.selectByVisibleText(type);
	
	driver.findElement(By.xpath("(//input[@title='Save [Alt+S]'])[1]")).click();
	String actIndustry  = driver.findElement(By.id("mouseArea_Industry")).getText();
	if(actIndustry.equals(industry))
	{
		System.out.println(industry+"information is verified==PASS");
	}else
	{
		System.out.println(industry+"information is not verified==FAIL");
	}
	
	String actType = driver.findElement(By.id("mouseArea_Type")).getText();
	
	if(actType.equals(type))
	{
		System.out.println(type+"information is verified==PASS");
	}else
	{
		System.out.println(type+"information is not verified==FAIL");
	}
	
	Actions a=new Actions(driver);
	a.moveToElement(driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']"))).perform();
	driver.findElement(By.linkText("Sign Out")).click();
	driver.quit();
	
	Cell cell = row.createCell(5);
	cell.setCellType(CellType.STRING);
	cell.setCellValue("PASS");
	
	FileOutputStream fos=new FileOutputStream(".\\data\\testScriptdata.xlsx");
	wb.write(fos);
	System.out.println("result in excel");
	wb.close();
	
	System.out.println("===pass===");
}
}
