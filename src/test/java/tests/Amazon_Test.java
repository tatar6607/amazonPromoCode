package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.Amazon_Page;
import utilities.ConfigReaderSecrets;
import utilities.Driver;
import org.openqa.selenium.WebElement;
import utilities.ExcelUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Amazon_Test {
    /*
    This class holds test to open links that we saved from facebook group.
    The links and codes are saved in a excel spreadsheet. This before we start going to Amazon
    we need to read the data from the spreadsheet and save them in a list.
    The next step is to open an Amazon page and sign in to Amazon using secrets in the config.
    After sign in process,  we go through each list, open the link in the list, add the item to the cart,
    go to the cart and do the checkout. At the checkout page, we enter the code, click apply. Once the code is applied
    we should see an order summary which we can read, full price, discount amount and discounted price.
    We save these three information in lists and then save to the same spreadsheet.
     */

    //Create Test object
    Amazon_Page amazonPage = new Amazon_Page();
    Actions actions = new Actions(Driver.getDriver());
    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), 10);
    JavascriptExecutor jse = (JavascriptExecutor) Driver.getDriver();

    //List that holds price before discount amd price after discount
    List<String> priceBeforeDiscountList = new ArrayList<>();
    List<String> priceDiscountList = new ArrayList<>();
    List<String> priceAfterDiscountList = new ArrayList<>();

    // EXCEL DATA STORE
    String filePath = "src\\test\\resources\\codes_links.xlsx";
    String sheetName = "codes";
    ExcelUtil excelUtil;
    List<Map<String, String>> testData;

    @BeforeMethod
    public void readData() {
        /*
        Reads data from the excel sheet and adds each row an element in the list as a map
        which the key is the column name and value is the cell value
        Ex: [
            <"AMAZON LINK" : "https://amzn.to/3249PnA", "PROMO CODE": "50IMF641">,
            <"AMAZON LINK" : "https://amzn.to/3eo9mBm", "PROMO CODE": "50DR4J2D">
           ]
         */
        excelUtil = new ExcelUtil(filePath, sheetName);
        testData = excelUtil.getDataList();
    }

    //Create test method
    @Test
    public void checkPromoLinksAndCodes() {
        Driver.getDriver().get("https://www.amazon.com/");
        //Click the signin link
        amazonPage.amazonSignInLink.click();

        //Enter email and click continue
        amazonPage.amazonEmailTextbox.sendKeys(ConfigReaderSecrets.getProperty("amazon_email"));
        amazonPage.amazonContinueButton.click();
        //Enter password and click signin
        amazonPage.amazonPasswordTextbox.sendKeys(ConfigReaderSecrets.getProperty("amazon_password"));
        amazonPage.amazonSignInButton.click();
        for (Map<String, String> row : testData) {
            Driver.getDriver().get(row.get("AMAZON LINK"));

            //Sometimes the item is not available, this we don's see an add to cart button
            //thus we put that in the try, catch
            try {
                //Add product to the cart
                amazonPage.amazonAddToCartButton.click();
            } catch (Exception e) {
                continue;
            }
            ;
            //Click to teh cart
            amazonPage.amazonCartButton.click();

            //Click checkout button
            amazonPage.amazonCheckoutButton.click();

            //Enter the promo code
            String code = row.get("PROMO CODE");
            amazonPage.amazonPromoCodeInput.sendKeys(code);
            //Click Apply button
            amazonPage.amazonApplyButton.click();

            //get Order summary table
            List<WebElement> amazonOrdersTotal = Driver.getDriver().findElements(By.xpath("//table[@id='subtotals-marketplace-table']//tr"));
            //print each column name one by one
            String priceDiscount = "0.0";
            for (int i = 0; i < amazonOrdersTotal.size(); i++) {
                String rowText = amazonOrdersTotal.get(i).getText();
                if (rowText.contains("Items")) {
                    String currencySymbol = rowText.substring(rowText.indexOf(":") + 2, rowText.indexOf(":") + 3);
                    String priceBeforeDiscount = rowText.substring(rowText.indexOf(":") + 3);
                    priceBeforeDiscountList.add(priceBeforeDiscount);

                } else if (rowText.contains(code)) {
                    String currencySymbol = rowText.substring(rowText.indexOf(":") + 3, rowText.indexOf(":") + 4);
                    priceDiscount = rowText.substring(rowText.indexOf(":") + 4);
                } else if (rowText.contains("Order total")) {
                    String currencySymbol = rowText.substring(rowText.indexOf(":") + 2, rowText.indexOf(":") + 3);
                    String priceAfterDiscount = rowText.substring(rowText.indexOf(":") + 3);
                    priceAfterDiscountList.add(priceAfterDiscount);
                }
            }
            priceDiscountList.add(priceDiscount);
            //Once we get price, we need to go a page back
            jse.executeScript("window.history.go(-1)");

            //Delete each items in the cart
            for (WebElement amazonCartItemDeleteButton : amazonPage.amazonCartItemDeleteButtonList) {
                amazonCartItemDeleteButton.click();
            }
        }

        Assert.assertEquals(priceBeforeDiscountList.get(0), "16.99");
        List<String> expectedDiscount = new ArrayList<String>();
        expectedDiscount.add("0.0");
        Assert.assertEquals(priceDiscountList, expectedDiscount);

    }

    @Test(dependsOnMethods = "checkPromoLinksAndCodes")
    public void writeCodesData() {
        /*
        This methods writes all price information to the excel sheet that we have for amazon links and codes.
         */
        excelUtil = new ExcelUtil(filePath, sheetName);
        for (int i = 0; i < priceBeforeDiscountList.size(); i++) {
            System.out.println(priceBeforeDiscountList.get(i) + " " + priceDiscountList.get(i) + " " + priceAfterDiscountList.get(i));
            excelUtil.setCellData(priceBeforeDiscountList.get(i) , "PRICE BEFORE DISCOUNT", i+1);
            excelUtil.setCellData(priceDiscountList.get(i), "DISCOUNT", i+1);
            excelUtil.setCellData(priceAfterDiscountList.get(i) , "PRICE AFTER DISCOUNT", i+1);
        }
    }

    @AfterClass
    public void tearDown() {
        Driver.closeDriver();
    }
}
