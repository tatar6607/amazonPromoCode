package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.SourceType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.Facebook_Page;
import utilities.ConfigReader;
import utilities.ConfigReaderSecrets;
import utilities.Driver;
import utilities.ReusableMethods;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;


public class Facebook_Test {

    Facebook_Page facebookPage = new Facebook_Page();
    Actions actions = new Actions(Driver.getDriver());
    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), 10);
    JavascriptExecutor jse = (JavascriptExecutor) Driver.getDriver();
    List<String> promoCodesList = new ArrayList<>();
    List<String> promoLinksList = new ArrayList<>();

    @Test
    public void login() {

        Driver.getDriver().get(ConfigReader.getProperty("facebook_url"));
        try {
            facebookPage.cookiesAcceptedButton.click();
        } catch (Exception e) {
            System.out.println("There is no cookies accaptance alert");
        }
        facebookPage.emailTextbox.sendKeys(ConfigReaderSecrets.getProperty("facebook_email"));
        facebookPage.passwordTextbox.sendKeys(ConfigReaderSecrets.getProperty("facebook_password"));
        facebookPage.loginButton.click();

        jse.executeScript("arguments[0].click()", facebookPage.groupsMenuLink);
        jse.executeScript("arguments[0].click()", facebookPage.promotionsGroupMenuLink);

        int j = 1;
        for (int i = 1; i < 100; i++) {
            ReusableMethods.waitFor(2);
            //scrolling down
            jse.executeScript("window.scrollBy(0,250)");
            ReusableMethods.waitFor(2);
            WebElement promoCodeElement = Driver.getDriver().findElement(By.xpath("(//div[@class='qzhwtbm6 knvmm38d'])[" + i + "]"));
            String stringPromeCode = promoCodeElement.getText();
            if (stringPromeCode.contains("See More")) {
                WebElement seeMoreButton = Driver.getDriver().findElement(By.xpath("(//div[.='See More'])[" + j + "]"));
                jse.executeScript("arguments[0].click()", seeMoreButton);
                WebElement promoCodeElementExtend = Driver.getDriver().findElement(By.xpath("(//div[@class='qzhwtbm6 knvmm38d'])[" + i + "]"));
                stringPromeCode = promoCodeElementExtend.getText();
            }
//            System.out.println("====================================");
//            System.out.println(stringPromeCode);
//            System.out.println("====================================");
            // TODO: REGEX USING

            if (stringPromeCode.contains("code:")) {
//                System.out.println(stringPromeCode);
                int codeIndex = stringPromeCode.indexOf("code:");
                int codeStart = codeIndex + 6;

                String promoCode = stringPromeCode.substring(codeStart);
                promoCode = promoCode.substring(0, promoCode.indexOf(" "));
                System.out.println("======== Promo kodumuz ====== : " + promoCode);
                promoCodesList.add(promoCode);

                int linkIndex = stringPromeCode.indexOf("https:");

                String promoLink = stringPromeCode.substring(linkIndex);
                promoLink = promoLink.substring(0,promoLink.indexOf(" "));
                System.out.println("======== Promo linkimiz  ====== : " + promoLink);
                promoLinksList.add(promoLink);


            } else if (stringPromeCode.contains("CODE:")) {
//                System.out.println(stringPromeCode);
                int codeIndex = stringPromeCode.indexOf("CODE:");
                int codeStart = codeIndex + 6;
                String promoCode = stringPromeCode.substring(codeStart);
                promoCode = promoCode.substring(0, promoCode.indexOf(" "));
                System.out.println("======== Promo kodumuz ====== : " + promoCode);
                promoCodesList.add(promoCode);

                int linkIndex = stringPromeCode.indexOf("https:");
                String promoLink = stringPromeCode.substring(linkIndex);
                promoLink = promoLink.substring(0,promoLink.indexOf(" "));
                System.out.println("======== Promo linkimiz  ====== : " + promoLink);
                promoLinksList.add(promoLink);

            }else {
                System.out.println(i + ". Code not found.");
            }
        }
        System.out.println( "Total Promo Codes: " + promoCodesList.size() );
        System.out.println( "Total Promo Links: " + promoLinksList.size() );

    }

    @AfterMethod
    public void tearDown() {
        Driver.closeDriver();
    }


}
