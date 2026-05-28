package pages;

import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    protected void navigate(String url) {
        driver.get(url);
    }

    protected String getTitle() {
        return driver.getTitle();
    }
}
