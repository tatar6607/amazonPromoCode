package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.concurrent.TimeUnit;


public class Driver {
    // SINGLETON CLASS CLASS BURADA OLUSTURDUK.
    // Projede kullanilan Web Driver i tekrar tekrar olusturmaktan kurtulmak icin
    // Driver null oldugunda  create edip driver baslatilir.
    // Driver classi farkli browser larin driverini kolayca baslatmak icin de kullanilir.

    static WebDriver driver;

    private Driver() {
        // Constructor private yaparak class disindan obje uretilmesini sagladik.
        //  PRIVATE YAPARAK ==> SINGLETON YAPTIK.
    }

    public static WebDriver getDriver() {

        if (driver == null) {
            switch (ConfigReader.getProperty("browser")) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "ie":
                    WebDriverManager.iedriver().setup();
                    driver = new InternetExplorerDriver();
                    break;
                case "safari":
                    WebDriverManager.getInstance(SafariDriver.class).setup();
                    driver = new SafariDriver();
                    break;
                case "headless-chrome":
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
                    break;
                case "opera":
                    WebDriverManager.operadriver().setup();
                    driver = new OperaDriver();
                    break;
            }


        }

        // Yine Driver cagirirken maximize ve wait i birlikte getirdin.
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;

    }

    public static void closeDriver() {
        if (driver != null) {    //  driver doluysa kapat.
            driver.quit();
            driver = null;  // driveri baska browserlara gecis yapmak icin null olmasi gerekli
        }
    }

}
