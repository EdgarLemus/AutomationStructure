package test;

import excel.ReadExcel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTest {

    public WebDriver driver;

    @DataProvider
    public Object[][] getExcelData() throws InvalidFormatException, IOException {
        ReadExcel read = new ReadExcel();
        return read.getCellData("Testdata/NewTourLogin.xls", "Sheet1");
    }

    @BeforeSuite
    public void launchApp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://newtours.demoaut.com/");
    }

    @Test(testName="sencha",dataProvider = "getExcelData")
    public void testNewTourLogin(String Username, String Password) throws InterruptedException{
        WebDriverWait wait = new WebDriverWait(driver, 45);
        driver.findElement(By.name("userName")).clear();
        driver.findElement(By.name("userName")).sendKeys(Username);
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(Password);
        driver.findElement(By.name("login")).click();
        Thread.sleep(2000);
        String InvalidLogin = driver.findElement(By.xpath("//a[contains(text(),'SIGN')]")).getText();
        if(InvalidLogin.contains("SIGN-ON")){
            System.out.println("Login Failed");
        }
        else{
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[contains(text(),'SIGN-OFF')]"), "SIGN-OFF"));
            Assert.assertEquals("SIGN-OFF", driver.findElement(By.xpath("//*[contains(text(),'SIGN-OFF')]")).getText());
            System.out.println("Login successfull");
        }
    }

    @AfterSuite
    public void closeBrowser(){
        driver.quit();
    }
}
