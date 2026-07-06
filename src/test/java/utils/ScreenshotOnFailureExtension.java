package utils;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestWatcher;

import java.nio.file.Files;
import java.nio.file.Path;

public class ScreenshotOnFailureExtension implements TestExecutionExceptionHandler, LifecycleMethodExecutionExceptionHandler, TestWatcher {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        attachScreenshot(context);
        throw throwable;
    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        attachScreenshot(context);
        throw throwable;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        attachScreenshot(context);
    }

    private void attachScreenshot(ExtensionContext context) {
        String testName = context.getDisplayName();
        String screenshotPath = ScreenshotUtils.takeScreenshot(testName);

        if (screenshotPath != null) {
            try {
                Allure.addAttachment(
                        "Screenshot - " + testName,
                        "image/png",
                        Files.newInputStream(Path.of(screenshotPath)),
                        ".png"
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
