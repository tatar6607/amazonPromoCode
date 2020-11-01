package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import pages.Facebook_Page;
import utilities.ConfigReader;
import utilities.ConfigReaderSecrets;
import utilities.Driver;
import utilities.ReusableMethods;

import java.util.ArrayList;
import java.util.List;


public class Facebook_Test {

    Facebook_Page facebookPage = new Facebook_Page();
    Actions actions = new Actions(Driver.getDriver());
    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), 10);

    @Test
    public void login() {

        Driver.getDriver().get(ConfigReader.getProperty("facebook_url"));
        try {
            facebookPage.cookiesAcceptedButton.click();
        } catch (Exception e) {
            System.out.println("There is no cokies accaptance alert");
        }
        facebookPage.emailTextbox.sendKeys(ConfigReaderSecrets.getProperty("facebook_email"));
        facebookPage.passwordTextbox.sendKeys(ConfigReaderSecrets.getProperty("facebook_password"));
        facebookPage.loginButton.click();

//        Driver.getDriver().switchTo().alert().dismiss(); // TO DO Search how to handling this problem.

        actions.doubleClick(facebookPage.groupsMenuLink).perform();

        ReusableMethods.waitFor(3);
        actions.doubleClick(facebookPage.promotionsGroupMenuLink).perform();

        ReusableMethods.waitFor(2);;

        JavascriptExecutor jse = (JavascriptExecutor) Driver.getDriver();
        for (int i = 1; i < 50; i++) {
            ReusableMethods.waitFor(2);
            //scrolling down
            jse.executeScript("window.scrollBy(0,250)");
//            try {
//                WebElement seeMoreLink = Driver.getDriver().findElement(By.xpath("(//div[.='See More'])["+i+"]"));
//                seeMoreLink.click();
//            }catch (Exception e) {
//
//            }
            WebElement promoCodeElement = Driver.getDriver().findElement(By.xpath("(//div[@class='qzhwtbm6 knvmm38d'])["+i+"]"));
            String stringPromeCode = promoCodeElement.getText();
            System.out.println(stringPromeCode);
        }



    }


}
