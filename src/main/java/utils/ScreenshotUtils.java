package utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScreenshotUtils {
    private static AppiumDriver driver;

    public static void setDriver(AppiumDriver appiumDriver) {
        driver = appiumDriver;
    }

    public static String takeScreenshot(String testName) {
        if (driver == null) {
            System.out.println("Driver is null, cannot take screenshot");
            return null;
        }

        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Create folder if missing
            Path screenshotsDir = Path.of("Reports/screenshots");
            if (!Files.exists(screenshotsDir)) {
                Files.createDirectories(screenshotsDir);
            }

            // Unique filename
            String filePath = "Reports/screenshots/" + testName + "_" + System.currentTimeMillis() + ".png";

            File destFile = new File(filePath);
            Files.copy(srcFile.toPath(), destFile.toPath());

            return filePath;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
