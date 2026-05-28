package tests;

import base.BaseTest;
import data.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductPage;

public class CheckoutTest extends BaseTest {

    @Test(dataProvider = "checkoutData", dataProviderClass = TestData.class)
    public void testCheckout(String firstName, String lastName, String zip) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginAs("standard_user", "secret_sauce");

        ProductPage productPage = new ProductPage(getDriver());
        productPage.addToCart("Sauce Labs Backpack");
        productPage.goToCart();

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.startCheckout();
        checkoutPage.fillShipping(firstName, lastName, zip);
        checkoutPage.submitOrder();

        String confirmation = checkoutPage.getConfirmation();
        Assert.assertTrue(confirmation.contains("Thank you for your order!"),
                "Unexpected confirmation message: " + confirmation);
    }
}
