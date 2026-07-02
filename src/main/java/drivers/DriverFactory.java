package drivers;

import config.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import models.AppiumSettings;

import java.net.URL;
import java.time.Duration;
import java.util.Objects;

public class DriverFactory {
    private static AndroidDriver driver;

    public static AndroidDriver getDriver() {
        if (driver == null) {
            driver = createDriver();
        }

        return driver;
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private static AndroidDriver createDriver() {
        AppiumSettings settings = ConfigReader.getSettings();

        // Always use Maven output directory for APK
        String appPath = System.getProperty("user.dir") +
                "/target/classes/" +
                settings.App;

        var options = new UiAutomator2Options()
                .setAutomationName(settings.AutomationName)
                .setDeviceName(settings.DeviceName)
                .setPlatformName(settings.PlatformName)
                .setPlatformVersion(settings.IsCi ? "14" : settings.PlatformVersion)
                .setApp(appPath)
                .setNoReset(settings.NoReset)
                .setAppWaitActivity("*")
                .setAppWaitForLaunch(true)
                .setDisableWindowAnimation(true)
                .setAdbExecTimeout(Duration.ofSeconds(settings.IsCi ? 120 : 60))
                .setNewCommandTimeout(Duration.ofSeconds(settings.IsCi ? 300 : 60))
                .setAppWaitDuration(Duration.ofMillis(settings.IsCi ? 60000 : 10000));

        try {
            driver = new AndroidDriver(new URL(settings.ServerUrl), options);

            // Appium startup sleep waiting for stable application, OS process
            if (settings.IsCi) {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException ignored) {}
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(settings.IsCi ? 10 : 5));
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Invalid Appium server URL: " + settings.ServerUrl, e);
        }
    }
}
