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
                .setPlatformVersion(settings.PlatformVersion)
                .setApp(appPath)
                .setNoReset(settings.NoReset)
                .setAppWaitActivity("*")
                .setAppWaitForLaunch(true)
                .setDisableWindowAnimation(true)
                .setAdbExecTimeout(Duration.ofSeconds(settings.IsCi ? 120 : 60));;

        try {
            driver = new AndroidDriver(new URL(settings.ServerUrl), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(settings.IsCi ? 10 : 5));

            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Invalid Appium server URL: " + settings.ServerUrl, e);
        }
    }
}
