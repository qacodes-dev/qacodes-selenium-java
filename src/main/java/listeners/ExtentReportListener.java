package listeners;

import base.BaseTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtil;

import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentReportListener implements ITestListener {

    private ExtentReports extent;
    private final ConcurrentHashMap<Long, ExtentTest> testMap = new ConcurrentHashMap<>();

    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
        spark.config().setDocumentTitle("QACodes Selenium Java — Test Report");
        spark.config().setReportName("Sauce Demo Test Results");
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Environment", "Sauce Demo (https://www.saucedemo.com)");
        extent.setSystemInfo("Browser", System.getProperty("browser", "chrome"));
    }

    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(name);
        testMap.put(Thread.currentThread().getId(), test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = testMap.get(Thread.currentThread().getId());
        if (test != null) test.pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = testMap.get(Thread.currentThread().getId());
        if (test == null) return;
        test.fail(result.getThrowable());
        try {
            Object instance = result.getInstance();
            if (instance instanceof BaseTest baseTest) {
                WebDriver driver = baseTest.getDriver();
                if (driver != null) {
                    String base64 = Base64.getEncoder()
                            .encodeToString(ScreenshotUtil.capture(driver));
                    test.addScreenCaptureFromBase64String(base64, "Failure Screenshot");
                }
            }
        } catch (Exception e) {
            test.warning("Could not capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = testMap.get(Thread.currentThread().getId());
        if (test != null) {
            Throwable t = result.getThrowable();
            test.skip(t != null ? t.getMessage() : "Skipped");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) extent.flush();
    }
}
