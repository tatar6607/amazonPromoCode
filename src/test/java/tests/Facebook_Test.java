package tests;

import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;
import pages.Facebook_Page;
import utilities.ConfigReader;
import utilities.ConfigReaderSecrets;
import utilities.Driver;
import utilities.ReusableMethods;


public class Facebook_Test {

    Facebook_Page facebookPage = new Facebook_Page();
    Actions actions = new Actions(Driver.getDriver());

    @Test
    public void login() {

        Driver.getDriver().get(ConfigReader.getProperty("facebook_url"));
        try {
            facebookPage.cookiesAcceptedButton.click();
        }catch (Exception e){
            System.out.println("There is no cokies accaptance alert");
        }
        facebookPage.emailTextbox.sendKeys(ConfigReaderSecrets.getProperty("facebook_email"));
        facebookPage.passwordTextbox.sendKeys(ConfigReaderSecrets.getProperty("facebook_password"));
        facebookPage.loginButton.click();

//        Driver.getDriver().switchTo().alert().dismiss(); // TO DO Search how to handling this problem.

        actions.doubleClick(facebookPage.groupsMenuLink).perform();

        ReusableMethods.waitFor(3);
        actions.doubleClick(facebookPage.promotionsGroupMenuLink).perform();

//        facebookPage.promotionCarts;


    }
}
