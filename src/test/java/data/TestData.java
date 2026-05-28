package data;

import org.testng.annotations.DataProvider;

public class TestData {

    @DataProvider(name = "loginData")
    public static Object[][] loginData() {
        return new Object[][] {
            // username           password        expectedSuccess  expectedErrorFragment
            {"standard_user",   "secret_sauce",  true,  ""},
            {"standard_user",   "wrong_pass",    false, "Username and password do not match"},
            {"locked_out_user", "secret_sauce",  false, "Sorry, this user has been locked out"},
        };
    }

    @DataProvider(name = "checkoutData")
    public static Object[][] checkoutData() {
        return new Object[][] {
            {"Jane", "Doe", "12345"},
        };
    }
}
