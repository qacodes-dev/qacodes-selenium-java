package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserFactory {

    private BrowserFactory() {}

    public static WebDriver createDriver(String browser) {
        return switch (browser.toLowerCase()) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (isHeadless()) opts.addArguments("--headless");
                yield new FirefoxDriver(opts);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions opts = new EdgeOptions();
                if (isHeadless()) {
                    opts.addArguments("--headless=new", "--no-sandbox",
                            "--disable-dev-shm-usage", "--disable-gpu",
                            "--window-size=1920,1080");
                }
                yield new EdgeDriver(opts);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                if (isHeadless()) {
                    opts.addArguments("--headless=new", "--no-sandbox",
                            "--disable-dev-shm-usage", "--disable-gpu",
                            "--window-size=1920,1080");
                }
                yield new ChromeDriver(opts);
            }
        };
    }

    private static boolean isHeadless() {
        return System.getenv("CI") != null
                || "true".equalsIgnoreCase(System.getProperty("headless"));
    }
}
