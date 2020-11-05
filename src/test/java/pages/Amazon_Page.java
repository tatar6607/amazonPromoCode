package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

import java.util.ArrayList;
import java.util.List;

public class Amazon_Page {
    public Amazon_Page() {
        PageFactory.initElements(Driver.getDriver(),this);
    }

    @FindBy(xpath = "//*[@id=\"nav-link-accountList\"]/div/span")
    public WebElement amazonSignInLink;

    @FindBy(id = "ap_email")
    public WebElement amazonEmailTextbox;

    @FindBy(id = "continue")
    public WebElement amazonContinueButton;

    @FindBy(id = "ap_password")
    public WebElement amazonPasswordTextbox;

    @FindBy(id = "signInSubmit")
    public WebElement amazonSignInButton;

    @FindBy(id = "add-to-cart-button")
    public WebElement amazonAddToCartButton;

    @FindBy(id = "nav-cart")
    public WebElement amazonCartButton;

    @FindBy(xpath = "//*[@id=\"sc-buy-box-ptc-button\"]/span/input")
    public  WebElement amazonCheckoutButton;

    @FindBy(id = "spc-gcpromoinput")
    public WebElement amazonPromoCodeInput;

    @FindBy(xpath = "//*[@id=\"gcApplyButtonId\"]/span/input")
    public  WebElement amazonApplyButton;

//    @FindBy(id = "subtotals-marketplace-table")
//    public ArrayList<WebElement> amazonOrdersTotal;

//    @FindBy(xpath = "//form[@id='activeCartViewForm']/input[@value='Delete']")
    @FindBy(xpath = "//form[@id='activeCartViewForm']//span[@data-action='delete']//input")
    public List<WebElement> amazonCartItemDeleteButtonList;

}
