package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

import java.util.List;

public class Facebook_Page {

    public Facebook_Page() {
        PageFactory.initElements(Driver.getDriver(),this);
    }


    @FindBy(xpath = "//button[@title='Accept All']")
    public WebElement cookiesAcceptedButton;

    @FindBy(id = "email")
    public WebElement emailTextbox;

    @FindBy(id = "pass")
    public WebElement passwordTextbox;

    @FindBy(xpath = "//button[@name='login']")
    public WebElement loginButton;

    @FindBy(xpath =  "//a[.='Return home']")
    public  WebElement returnHomeButton;

    @FindBy(xpath = "(//span[.='Groups'])[2]")
    public WebElement groupsMenuLink;

    @FindBy(xpath = "(//span[contains(text(),'Promotions')])[1]")
    public WebElement promotionsGroupMenuLink;

    @FindBy(xpath = "//div[@class='qzhwtbm6 knvmm38d']")
    public List <WebElement> promotionCarts;

    @FindBy(xpath = "//div[.='See More']")
    public WebElement seeMoreLink;

    @FindBy(xpath = "//body")
    public WebElement bodyForESC;






}
