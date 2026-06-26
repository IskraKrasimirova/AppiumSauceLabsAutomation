package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import utils.ScreenshotUtils;

import java.util.Optional;

public class ExtentReportExtension implements TestWatcher, BeforeTestExecutionCallback, AfterAllCallback {

    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private static final ExtentReports extent = ExtentManager.getInstance();

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String testName = context.getDisplayName();
        ExtentTest test = extent.createTest(testName);
        testThread.set(test);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        ExtentTest test = testThread.get();
        if (test == null) return;

        test.fail(cause);

        try {
            String screenshotPath = ScreenshotUtils.takeScreenshot(context.getDisplayName());
            test.addScreenCaptureFromPath(screenshotPath);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        ExtentTest test = testThread.get();
        if (test == null) return;

        test.pass("Test passed");
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        ExtentTest test = testThread.get();
        if (test == null) return;

        test.skip("Test skipped: " + reason.orElse("No reason"));
    }

    @Override
    public void afterAll(ExtensionContext context) {
        extent.flush();
    }
}
