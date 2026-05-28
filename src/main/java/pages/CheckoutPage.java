package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    // Cart page — click to enter checkout flow
    private static final By CHECKOUT_BUTTON   = By.cssSelector("[data-test='checkout']");

    // Step One: customer information
    private static final By FIRST_NAME        = By.cssSelector("[data-test='firstName']");
    private static final By LAST_NAME         = By.cssSelector("[data-test='lastName']");
    private static final By POSTAL_CODE       = By.cssSelector("[data-test='postalCode']");
    private static final By CONTINUE_BUTTON   = By.cssSelector("[data-test='continue']");

    // Step Two: order overview
    private static final By FINISH_BUTTON     = By.cssSelector("[data-test='finish']");

    // Confirmation
    private static final By CONFIRMATION      = By.cssSelector(".complete-header");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void startCheckout() {
        wait.waitForClickableInView(CHECKOUT_BUTTON).click();
    }

    public void fillShipping(String firstName, String lastName, String zip) {
        wait.waitForClickable(FIRST_NAME).sendKeys(firstName);
        wait.waitForClickable(LAST_NAME).sendKeys(lastName);
        wait.waitForClickable(POSTAL_CODE).sendKeys(zip);
    }

    /** Clicks Continue on Step One, then Finish on Step Two. */
    public void submitOrder() {
        wait.waitForClickableInView(CONTINUE_BUTTON).click();
        wait.waitForClickableInView(FINISH_BUTTON).click();
    }

    public String getConfirmation() {
        return wait.waitForVisible(CONFIRMATION).getText();
    }
}
