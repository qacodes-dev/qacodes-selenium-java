package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

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
        // Sauce Demo derives the button's data-test from the product name:
        // "Sauce Labs Backpack" -> "add-to-cart-sauce-labs-backpack"
        String slug = productName.toLowerCase()
                .replaceAll("[()]", "")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        By addButton = By.cssSelector("[data-test='add-to-cart-" + slug + "']");
        wait.waitForVisible(INVENTORY_LIST);
        wait.waitForClickable(addButton).click();
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
