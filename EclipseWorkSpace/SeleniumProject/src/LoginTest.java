/*Author: Brandon Jackson
 * Created: 9/28/2020
 * Final Edit: 10/1/2020
 * Selenium - Test Loginscreen of PrinterLogic
 */

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginTest {
	//Our WebDriver
	private WebDriver driver;
	//Location of Drivers - Path in computer to find each driver
	private String chromeLocation = "D:\\Eclipse\\Selenium\\selenium-java-3.141.59\\chromedriver.exe";
	private String fireFoxLocation = "D:\\Eclipse\\Selenium\\selenium-java-3.141.59\\geckodriver.exe";
	private String edgeLocation = "D:\\Eclipse\\Selenium\\selenium-java-3.141.59\\msedgedriver.exe";
	//Website we are testing
	private String website = "https://brandonjackson.printercloud.com/admin/index.php";
	
	//Sets Property of the driver
	public void Setup(String browse, String location, String web) {
		if(browse == "chrome") {
			System.setProperty("webdriver.chrome.driver", location);
			driver = new ChromeDriver();
		}else if(browse == "firefox") {
			System.setProperty("webdriver.gecko.driver", location);
			driver = new FirefoxDriver();
		}else if(browse == "edge") {
			System.setProperty("webdriver.edge.driver", location);
			driver = new EdgeDriver();
		}else{
			System.out.println("Unimplemented Window");
		}
		driver.get(web);
	}
	
	//Login - We have delays so we can watch it happen, ideally we will remove them.
	public void login(String username, String password) throws InterruptedException {
		driver.findElement(By.id("relogin_user")).sendKeys(username);
        driver.findElement(By.id("relogin_password")).sendKeys(password);
        driver.findElement(By.id("admin-login-btn")).click();
        Thread.sleep(4000);
	}
	
	//Test if Logged in
	public void testLogin() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5);
			WebElement generalTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("oGeneral")));
		
			if(generalTab.isDisplayed()) {
				System.out.println("Login Successful");
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Login Failed");
		}
	}
	
	//this method fullscreens the window
	public void fullScreen() {
		driver.manage().window().fullscreen();
	}
	
	//Test a Valid Login
	public void test1(String b, String bL, String w) throws InterruptedException {
		System.out.println("Test 1\n");
		Setup(b,bL,w);
        Thread.sleep(2000);
        
        login("BrandonJackson","@Tyler1998");
        testLogin();
        driver.close();
	}
	
	// Test Invalid Password with Valid Username
	public void test2(String b, String bL, String w) throws InterruptedException {
		System.out.println("Test 2\n");
		Setup(b,bL,w);
        Thread.sleep(2000);
        
        login("BrandonJackson","Hi1908");
        testLogin();
        driver.close();
	}
	
	// Test Invalid Username with Valid Password
	public void test3(String b, String bL, String w) throws InterruptedException {
		System.out.println("Test 3\n");
		Setup(b,bL,w);
        Thread.sleep(2000);
        
        login("JamesBond","@Tyler1998");
        testLogin();
        driver.close();
	}
	
	// Test Valid Username. See if brute force is possible and how fast someone could brute force 
	public void test4(String b, String bL, String w) throws InterruptedException {	
		System.out.println("Test 4\n");
		Setup(b,bL,w);
        Thread.sleep(2000);
        int count = 0;
        long start = System.currentTimeMillis();
        long end = 0;
        
        //If it fails to login, then it will try again.
        while(driver.findElement(By.id("relogin_user")) != null && count < 10) {
        	count++;
        	driver.findElement(By.id("relogin_user")).sendKeys("BrandonJackson");
	        driver.findElement(By.id("relogin_password")).sendKeys("WrongPass");
	        driver.findElement(By.cssSelector("#admin-login-btn > .ui-button-text")).click();
	        Thread.sleep(2000);
	        if(count == 1) {
	        	end = System.currentTimeMillis()-start-2000;
	        }
        }
        long total = System.currentTimeMillis()-start-2000*count;
        
        System.out.println("The time for one failed attempt was :" + end + "milliseconds");
        System.out.println("The number of failed attempt was :" + count);
        System.out.println("The Total time for " + count + " failed attempts was :" + total + "milliseconds");

        driver.close();
	}
	
	// Change Visibility to Password so be able to see it
	public void test5(String b, String bL, String w) throws InterruptedException {	
		System.out.println("Test 5\n");
		Setup(b,bL,w);
        Thread.sleep(2000);
        driver.findElement(By.id("relogin_user")).sendKeys("BrandonJackson");
        driver.findElement(By.id("relogin_password")).sendKeys("@Tyler1998");
        Thread.sleep(2000);
        
        //Change input type from password to text
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('relogin_password').setAttribute('type', 'text')");
        
        Thread.sleep(4000);
        driver.close();
	}
	
	
	//Test no Username
	public void test6(String b, String bL, String w) throws InterruptedException {
		System.out.println("Test 11\n");
		Setup(b,bL,w);
        Thread.sleep(2000);
        
        login("","@Tyler1998");
        testLogin();
        driver.close();
	}
	
	//Test no Username and no password
	public void test7(String b, String bL, String w) throws InterruptedException {
		System.out.println("Test 12\n");
		Setup(b,bL,w);
        Thread.sleep(2000);
        
        login("","");
        testLogin();
        driver.close();
	}
	
	//Test Add New Folder, Then Delete Folder
	public void test8(String b, String bL, String w) throws InterruptedException {
		System.out.println("Test 8\n");
		Setup(b,bL,w);

        login("BrandonJackson","@Tyler1998");
        
        //Add Folder
        driver.findElement(By.cssSelector("#newfolder > span")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("popupitem_city")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("CityName")).sendKeys("New York");
        driver.findElement(By.cssSelector("#add-btn > .ui-button-text")).click();
        Thread.sleep(2000);
        
        //Delete Folder
        driver.findElement(By.linkText("New York")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("browse-delete")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("delete_confirm_text")).sendKeys("DELETE");
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("#delete_confirm_btn_ok > .ui-button-text")).click();
        
        Thread.sleep(4000);
        driver.close();
	}
		
	//Makes List of Links that are exposed
	public void test9(String b, String bL, String w) throws InterruptedException {
		System.out.println("Test 9\n");
		Setup(b,bL,w);
        Thread.sleep(2000);
		WebElement element;
		List<WebElement> elements = driver.findElements(By.tagName("a")); // <-- change this to link or any other tag
		Iterator<WebElement> it = elements.iterator();
		Thread.sleep(2000);
		while(it.hasNext()){
			try {
				// Try different element 
				element = it.next();
				System.out.println(
									element.getTagName() + "\t" +
									element.getText() + "\t" +
									element.isDisplayed() + "\t" +
									element.isEnabled() + "\t" +
									element.getAttribute("href") + "\t" +   
									element.getAttribute("type"));
								//try more attributes
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
	
	//Test no Password
	public void test10(String b, String bL, String w) throws InterruptedException {
		System.out.println("Test 10\n");
		Setup(b,bL,w);
        Thread.sleep(2000);
        
        login("BrandonJackson","");
        testLogin();
        driver.close();
	}
	
	//Broken Link Check
	private void CheckForBrokenLinks(String id) {	
		String url = "";
		HttpURLConnection huc;
		
		List<WebElement> elements = driver.findElement(By.id(id)).findElements(By.tagName("a"));
		
		Iterator<WebElement> links = elements.iterator();
		
		while(links.hasNext()){
			try {
				url = links.next().getAttribute("href");
				
				//System.out.println(url);
				 
				huc = (HttpURLConnection)(new URL(url).openConnection());
	
				huc.setRequestMethod("HEAD");
				huc.connect();
				
				int respCode = huc.getResponseCode();			 
	
				if(respCode >= 400){
					System.out.println(url+" is a broken link");
				}
				else{
					System.out.println(url+" is a valid link");
				}
	
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	
	//Check Lost Password Link
	public void test11(String b, String bL, String w) throws InterruptedException {
		System.out.println("Test 13\n");
		Setup(b,bL,w);
        Thread.sleep(2000);
        
        CheckForBrokenLinks("forgot-password-container");
        
        driver.close();
	}
	
	
	//Check Privacy Policy Link
	public void test12(String b, String bL, String w) throws InterruptedException {
		System.out.println("Test 14\n");
		Setup(b,bL,w);
        Thread.sleep(2000);
        
        CheckForBrokenLinks("privacy-policy-container");
        
        driver.close();
	}
	
	
	//Main Method Tests
	public static void main(String[] args) throws InterruptedException {
		LoginTest l = new LoginTest();
		//l.test1("chrome",l.chromeLocation,l.website);
		//l.test1("firefox",l.fireFoxLocation,l.website);
		//l.test1("edge",l.edgeLocation,l.website);
		//l.test2("chrome",l.chromeLocation,l.website);
		//l.test3("chrome",l.chromeLocation,l.website);
		//l.test4("chrome",l.chromeLocation,l.website);
		//l.test5("chrome",l.chromeLocation,l.website);
		//l.test6("chrome",l.chromeLocation,l.website);
		//l.test7("chrome",l.chromeLocation,l.website);
		//l.test8("chrome",l.chromeLocation,l.website);
		//l.test9("chrome",l.chromeLocation,l.website);
		//l.test10("chrome",l.chromeLocation,l.website);
		//l.test11("chrome",l.chromeLocation,l.website);
		//l.test12("chrome",l.chromeLocation,l.website);
    }
}

