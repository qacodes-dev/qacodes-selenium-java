package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductPage extends BasePage {

    private static final By INVENTORY_LIST = By.cssSelector(".inventory_list");
    private static final By SORT_DROPDOWN  = By.cssSelector("[data-test='product-sort-container']");
    private static final By CART_BADGE     = By.cssSelector(".shopping_cart_badge");
    private static final By CART_LINK      = By.cssSelector(".shopping_cart_link");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            wait.waitForVisible(INVENTORY_LIST);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void sortBy(String value) {
        new Select(wait.waitForVisible(SORT_DROPDOWN)).selectByValue(value);
    }

    public void addToCart(String productName) {
        // Sauce Demo derives button data-test values from the product name:
        // "Sauce Labs Backpack" -> add-to-cart-sauce-labs-backpack / remove-sauce-labs-backpack
        String slug = productName.toLowerCase()
                .replaceAll("[()]", "")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        By addButton    = By.cssSelector("[data-test='add-to-cart-" + slug + "']");
        By removeButton = By.cssSelector("[data-test='remove-" + slug + "']");

        wait.waitForVisible(INVENTORY_LIST);

        // JS click bypasses the native-click / React synthetic-event gap that causes no-ops
        // in headless Linux Chrome. Verify the button flipped to "remove"; retry once if not.
        wait.jsClick(addButton);
        if (!isPresentQuickly(removeButton)) {
            wait.jsClick(addButton);
        }
        wait.waitForVisible(removeButton);
    }

    private boolean isPresentQuickly(By locator) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public int getCartCount() {
        try {
            return Integer.parseInt(driver.findElement(CART_BADGE).getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public void goToCart() {
        wait.waitForClickable(CART_LINK).click();
    }
}
