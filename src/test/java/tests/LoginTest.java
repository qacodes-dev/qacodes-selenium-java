package tests;

import base.BaseTest;
import data.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke"})
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginAs("standard_user", "secret_sauce");
        Assert.assertTrue(loginPage.isInventoryLoaded(),
                "Inventory page should load after valid login");
    }

    @Test(dataProvider = "loginData", dataProviderClass = TestData.class)
    public void testLogin(String username, String password,
                          boolean expectedSuccess, String expectedError) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginAs(username, password);

        if (expectedSuccess) {
            Assert.assertTrue(loginPage.isInventoryLoaded(),
                    "Inventory page should load for valid credentials");
        } else {
            String error = loginPage.getErrorMessage();
            Assert.assertTrue(error.contains(expectedError),
                    "Expected error containing '" + expectedError + "' but got: '" + error + "'");
        }
    }
}
