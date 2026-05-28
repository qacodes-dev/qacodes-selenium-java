package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() {}

    public static byte[] capture(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public static String captureToFile(WebDriver driver, String name) {
        byte[] screenshot = capture(driver);
        String timestamp = LocalDateTime.now().format(FORMATTER);
        Path dir = Paths.get("test-output", "screenshots");
        try {
            Files.createDirectories(dir);
            Path filePath = dir.resolve(name + "_" + timestamp + ".png");
            Files.write(filePath, screenshot);
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save screenshot: " + name, e);
        }
    }
}
