package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

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
        wait.waitForVisible(INVENTORY_LIST);
        List<WebElement> items = driver.findElements(By.cssSelector(".inventory_item"));
        for (WebElement item : items) {
            String name = item.findElement(By.cssSelector(".inventory_item_name")).getText();
            if (name.equals(productName)) {
                item.findElement(By.cssSelector("[data-test^='add-to-cart']")).click();
                return;
            }
        }
        throw new RuntimeException("Product not found in inventory: " + productName);
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
