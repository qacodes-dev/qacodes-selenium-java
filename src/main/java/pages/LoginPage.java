package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME     = By.cssSelector("[data-test='username']");
    private static final By PASSWORD     = By.cssSelector("[data-test='password']");
    private static final By LOGIN_BUTTON = By.cssSelector("[data-test='login-button']");
    private static final By ERROR        = By.cssSelector("[data-test='error']");
    private static final By INVENTORY    = By.cssSelector(".inventory_list");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void loginAs(String username, String password) {
        wait.waitForClickable(USERNAME).sendKeys(username);
        wait.waitForClickable(PASSWORD).sendKeys(password);
        wait.waitForClickable(LOGIN_BUTTON).click();
    }

    public String getErrorMessage() {
        return wait.waitForVisible(ERROR).getText();
    }

    public boolean isInventoryLoaded() {
        try {
            wait.waitForVisible(INVENTORY);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
