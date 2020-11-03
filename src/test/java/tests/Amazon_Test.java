package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import pages.Amazon_Page;
import utilities.ConfigReader;
import utilities.ConfigReaderSecrets;
import utilities.Driver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Amazon_Test {
    //Create Test object
    Amazon_Page amazonPage = new Amazon_Page();
    Actions actions = new Actions(Driver.getDriver());
    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), 10);
    JavascriptExecutor jse = (JavascriptExecutor) Driver.getDriver();

    //List that holds codes amd links
    List<String> promoCodesList = new ArrayList<>();
    List<String> promoLinksList = new ArrayList<>();

    //List that holds price before discount amd price after discount
    List<String> priceBeforeDiscountList = new ArrayList<>();
    List<String> priceAfterDiscountList = new ArrayList<>();


    //Create test method
    @Test
    public void checkPromoLinksAndCodes(){
        promoCodesList.add("69226XKI");
        promoLinksList.add("https://amzn.to/2GphbKL");
        Driver.getDriver().get(promoLinksList.get(0));

        //Click the signin link
        amazonPage.amazonSignInLink.click();

        //Enter email and click continue
        amazonPage.amazonEmailTextbox.sendKeys(ConfigReaderSecrets.getProperty("amazon_email"));
        amazonPage.amazonContinueButton.click();
        //Enter password and click signin
        amazonPage.amazonPasswordTextbox.sendKeys(ConfigReaderSecrets.getProperty("amazon_password"));
        amazonPage.amazonSignInButton.click();

        //Add product to the cart
        amazonPage.amazonAddToCartButton.click();

        //Click to teh cart
        amazonPage.amazonCartButton.click();

        //Click checkout button
        amazonPage.amazonCheckoutButton.click();

        //Enter the promo code
        amazonPage.amazonPromoCodeInput.sendKeys(promoCodesList.get(0));
        //Click Apply button
        amazonPage.amazonApplyButton.click();

        //get Order summary table
        List<WebElement>  amazonOrdersTotal = Driver.getDriver().findElements(By.xpath("//table[@id='subtotals-marketplace-table']//tr"));
        //print each column name one by one
        for (int i = 0; i < amazonOrdersTotal.size(); i++) {
            String rowText = amazonOrdersTotal.get(i).getText();
//            System.out.println(i + ". " + rowText);
            if(rowText.contains("Items")){
                String currencySymbol = rowText.substring(rowText.indexOf(":")+2, rowText.indexOf(":")+3);
                String priceBeforeDiscount = rowText.substring(rowText.indexOf(":")+3);
                System.out.println(currencySymbol + priceBeforeDiscount);
                priceBeforeDiscountList.add(priceBeforeDiscount);
            }
            if(rowText.contains("Order total")){
                String currencySymbol = rowText.substring(rowText.indexOf(":")+2, rowText.indexOf(":")+3);
                String priceAfterDiscount = rowText.substring(rowText.indexOf(":")+3);
                System.out.println(currencySymbol + priceAfterDiscount);
                priceAfterDiscountList.add(priceAfterDiscount);
            }
        }
    }
    
}
