package Practice_TestNG;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

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

public class TC_06_CreateContactWithOrgTest {
	
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
		Sheet sh= wb.getSheet("contact");
		Row row = sh.getRow(7);
		String orgName = row.getCell(2).toString()+random;
		String LastName=row.getCell(3).toString();
		wb.close();
		
		
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
		driver.findElement(By.xpath("(//input[@title='Save [Alt+S]'])[1]")).click();
		String headerInfo  = driver.findElement(By.className("dvHeaderText")).getText();
		if( headerInfo.contains(orgName))
		{
			System.out.println(orgName+" Organization successfully created");
		}else
		{
			System.out.println(orgName+" Organization is not successfully created");
		}
		
		driver.findElement(By.linkText("Contacts")).click();
		driver.findElement(By.xpath("//img[@src='themes/softed/images/btnL3Add.gif']")).click();
		driver.findElement(By.name("lastname")).sendKeys(LastName);
		driver.findElement(By.xpath("//input[@name='account_name']/following-sibling::img")).click();
		String parentwindow = driver.getWindowHandle();
		Set<String> windowhandles = driver.getWindowHandles();
		for(String s:windowhandles) 
		{
			driver.switchTo().window(s);
			String url=driver.getCurrentUrl();
			if(url.contains("module=Accounts"))
			{
				break;
			}
			
		}
		driver.findElement(By.id("search_txt")).sendKeys(orgName);
		driver.findElement(By.name("search")).click();
		
		//we give dynamic xpath here
		driver.findElement(By.xpath("//a[text()='"+orgName+"']")).click();
		
		driver.switchTo().window(parentwindow);
		driver.findElement(By.xpath("(//input[@title='Save [Alt+S]'])[1]")).click();
		String headerInfo1  = driver.findElement(By.className("dvHeaderText")).getText();
		if( headerInfo1.contains(LastName))
		{
			System.out.println(LastName +" contact successfully created");
		}else
		{
			System.out.println(LastName +" contact is not successfully created");
		}
		
		
		String actorg  = driver.findElement(By.id("mouseArea_Organization Name")).getText();
		System.out.println(actorg);
		if( actorg.trim().equals(orgName))//we use trim because to ignore spaces
		{
			System.out.println(orgName+" information is verified==PASS");
		}else
		{
			System.out.println(orgName+" information is not verified==FAIL");	
		}
		
		Actions a=new Actions(driver);
		a.moveToElement(driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']"))).perform();
		driver.findElement(By.linkText("Sign Out")).click();
		driver.quit();
	}
}
