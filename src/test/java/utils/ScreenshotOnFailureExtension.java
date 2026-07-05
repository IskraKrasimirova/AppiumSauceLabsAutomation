package utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class ScreenshotOnFailureExtension implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        String testName = context.getDisplayName();
        String screenshotPath = ScreenshotUtils.takeScreenshot(testName);
        System.out.println("Screenshot saved at: " + screenshotPath);

        throw throwable;
    }
}
