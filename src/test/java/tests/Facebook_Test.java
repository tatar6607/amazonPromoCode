package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.SourceType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.Facebook_Page;
import utilities.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Facebook_Test {

    Facebook_Page facebookPage = new Facebook_Page();
    Actions actions = new Actions(Driver.getDriver());
    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), 10);
    JavascriptExecutor jse = (JavascriptExecutor) Driver.getDriver();
    public List<String> promoCodesList = new ArrayList<>();
    public List<String> promoLinksList = new ArrayList<>();


    // EXCEL DATA STORE
    ExcelUtil excelUtil;
    String filePath = "src\\test\\resources\\codes_links.xlsx";
    String sheetName = "codes";

    public void loginFacebook() {
        Driver.getDriver().get(ConfigReader.getProperty("facebook_url"));
        try {
            facebookPage.cookiesAcceptedButton.click();
        } catch (Exception e) {
            System.out.println("There is no cookies accaptance alert");
        }
        facebookPage.emailTextbox.sendKeys(ConfigReaderSecrets.getProperty("facebook_email"));
        facebookPage.passwordTextbox.sendKeys(ConfigReaderSecrets.getProperty("facebook_password"));
        facebookPage.loginButton.click();
    }

    @Test
    public void getPromoCodesAndLinks() {
        loginFacebook();

        try {
            if(facebookPage.returnHomeButton.isDisplayed()){
                loginFacebook();
            }
        }catch (Exception e){

        }

        jse.executeScript("arguments[0].click()", facebookPage.groupsMenuLink);
        jse.executeScript("arguments[0].click()", facebookPage.promotionsGroupMenuLink);

        //Trying to Zoom Out
//        for(int i=0; i<5; i++){
//            Driver.getDriver().findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL,Keys.SUBTRACT));
//        }

        jse.executeScript("document.body.style.zoom = '0.75'");
        int j = 1;
        for (int i = 1; i < 500; i++) {
            ReusableMethods.waitFor(3);
            //scrolling down
            jse.executeScript("window.scrollBy(0,250)");
            ReusableMethods.waitFor(3);
            WebElement promoCodeElement = Driver.getDriver().findElement(By.xpath("(//div[@class='qzhwtbm6 knvmm38d'])[" + i + "]"));
            String stringPromeCode = promoCodeElement.getText();
            if (stringPromeCode.contains("See More")) {
                WebElement seeMoreButton = Driver.getDriver().findElement(By.xpath("(//div[.='See More'])[" + j + "]"));
                jse.executeScript("arguments[0].click()", seeMoreButton);
                WebElement promoCodeElementExtend = Driver.getDriver().findElement(By.xpath("(//div[@class='qzhwtbm6 knvmm38d'])[" + i + "]"));
                stringPromeCode = promoCodeElementExtend.getText();
            }
            System.out.println("====================================");
            System.out.println(stringPromeCode);
            System.out.println("====================================");
            // TODO: REGEX USING

            if (stringPromeCode.contains("code:")) {
//                System.out.println(stringPromeCode);
                int codeIndex = stringPromeCode.indexOf("code:");
                int codeStart = codeIndex + 6;

                String promoCode = stringPromeCode.substring(codeStart);
//                promoCode = promoCode.substring(0, promoCode.indexOf(" "));
                promoCode = promoCode.substring(0, 8);
                System.out.println("======== Promo kodumuz ====== : " + promoCode);
                promoCodesList.add(promoCode);

                int linkIndex = stringPromeCode.indexOf("https:");

                String promoLink = stringPromeCode.substring(linkIndex);
                System.out.println(promoLink);
//                promoLink = promoLink.substring(0,promoLink.indexOf(" "));
                promoLink = promoLink.substring(0,23);
                System.out.println("======== Promo linkimiz  ====== : " + promoLink);
                promoLinksList.add(promoLink);


            } else if (stringPromeCode.contains("CODE:")) {
//                System.out.println(stringPromeCode);
                int codeIndex = stringPromeCode.indexOf("CODE:");
                int codeStart = codeIndex + 6;
                String promoCode = stringPromeCode.substring(codeStart);
//                promoCode = promoCode.substring(0, promoCode.indexOf(" "));
                promoCode = promoCode.substring(0, 8);
                System.out.println("======== Promo kodumuz ====== : " + promoCode);
                promoCodesList.add(promoCode);

                int linkIndex = stringPromeCode.indexOf("https:");
                String promoLink = stringPromeCode.substring(linkIndex);
//                promoLink = promoLink.substring(0,promoLink.indexOf(" "));
                System.out.println(promoLink);
                promoLink = promoLink.substring(0,23);
                System.out.println("======== Promo linkimiz  ====== : " + promoLink);
                promoLinksList.add(promoLink);

            }else {
                System.out.println(i + ". Code not found.");
            }
        }
        System.out.println( "Total Promo Codes: " + promoCodesList.size() );
        System.out.println( "Total Promo Links: " + promoLinksList.size() );

    }

    @Test(dependsOnMethods = "getPromoCodesAndLinks")
    public void writeCodesData() {
        excelUtil = new ExcelUtil(filePath, sheetName);
        int row = 1;
        int j = 0;
        for (String code : promoCodesList) {
            excelUtil.setCellData(code , "PROMO CODE", row);
            excelUtil.setCellData( promoLinksList.get(j), "AMAZON LINK", row);
            row++;
            j++;
        }
    }

    @AfterClass
    public void tearDown() {
//        Driver.closeDriver();
    }


}
