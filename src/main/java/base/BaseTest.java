package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;

import java.time.Duration;

public class BaseTest {

    protected static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public WebDriver getDriver() {
        return driverThread.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ConfigReader config = ConfigReader.getInstance();
        WebDriver driver = BrowserFactory.createDriver(config.getBrowser());
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        boolean headless = System.getenv("CI") != null
                || "true".equalsIgnoreCase(System.getProperty("headless"));
        if (!headless) {
            driver.manage().window().maximize();
        }
        driverThread.set(driver);
        driver.get(config.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
        }
        driverThread.remove();
    }
}
