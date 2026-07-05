package utils;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.nio.file.Files;
import java.nio.file.Path;

public class FailureWatcher implements TestWatcher {
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();

        String screenshotPath = ScreenshotUtils.takeScreenshot(testName);

        if (screenshotPath != null) {
            try {
                byte[] screenshotBytes = Files.readAllBytes(Path.of(screenshotPath));
                Allure.addAttachment("Screenshot - " + testName, "image/png",
                        new String(screenshotBytes), ".png");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
