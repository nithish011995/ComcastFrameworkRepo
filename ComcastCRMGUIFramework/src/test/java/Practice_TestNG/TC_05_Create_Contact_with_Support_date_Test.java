package Practice_TestNG;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

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
import org.testng.annotations.Test;

public class TC_05_Create_Contact_with_Support_date_Test {
	@Test
	public void createContactTest() throws Throwable {
		
		FileInputStream fis=new FileInputStream(".\\configAppData\\commondata.properties.txt");
		Properties p=new Properties();
		p.load(fis);
		
		String URL = p.getProperty("url");
		String BROWSER=p.getProperty("browser");
		String USERNAME=p.getProperty("username");
		String PASSWORD=p.getProperty("password");
		
		Random r=new Random();
		int random = r.nextInt(1000);
		
		FileInputStream fis1=new FileInputStream(".\\testdata\\testScriptdata.xlsx");
		Workbook wb = WorkbookFactory.create(fis1);
		Sheet sh= wb.getSheet("contact");
		Row row = sh.getRow(1);
		String lastname = row.getCell(2).toString()+random;
		
		
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
		
		driver.findElement(By.linkText("Contacts")).click();
		driver.findElement(By.xpath("//img[@src='themes/softed/images/btnL3Add.gif']")).click();
		driver.findElement(By.name("lastname")).sendKeys(lastname);
		
		Date dateobj=new Date();
		
		//to get particular pattern and month MM always capital letter
		SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sim.format(dateobj);

		//to get before or after few days date
		Calendar cal=sim.getCalendar();
		cal.add(Calendar.DAY_OF_MONTH, 30);
		String enddate = sim.format(cal.getTime());
		
		driver.findElement(By.name("support_start_date")).clear();
		driver.findElement(By.name("support_start_date")).sendKeys(startDate);
		driver.findElement(By.name("support_end_date")).clear();
		driver.findElement(By.name("support_end_date")).sendKeys(enddate);
		
	
		driver.findElement(By.xpath("(//input[@title='Save [Alt+S]'])[1]")).click();
		
		String actualdate = driver.findElement(By.id("dtlview_Support Start Date")).getText();
		
		if( actualdate.equals(startDate))
		{
			System.out.println(actualdate +" information is verified==PASS");
		}else
		{
			System.out.println(actualdate +" information is not verified==FAIL");	
		}
		
		String actualenddate = driver.findElement(By.id("dtlview_Support End Date")).getText();
		
		if( actualenddate.equals(enddate))
		{
			System.out.println(enddate +" information is verified==PASS");
		}else
		{
			System.out.println(enddate +" information is not verified==FAIL");	
		}
		
		Actions a=new Actions(driver);
		a.moveToElement(driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']"))).perform();
		driver.findElement(By.linkText("Sign Out")).click();
		driver.quit();
	}
}
