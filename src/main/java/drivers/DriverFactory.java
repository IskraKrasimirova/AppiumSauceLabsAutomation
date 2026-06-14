package drivers;

import config.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import models.AppiumSettings;

import java.net.URL;
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

        String appPath = Objects.requireNonNull(
                DriverFactory.class.getClassLoader().getResource(settings.App),
                "APK file not found in resources!"
        ).getPath().replaceFirst("^/", "");

        var options = new UiAutomator2Options()
                .setAutomationName(settings.AutomationName)
                .setDeviceName(settings.DeviceName)
                .setPlatformName(settings.PlatformName)
                .setPlatformVersion(settings.PlatformVersion)
                .setApp(appPath)
                .setNoReset(settings.NoReset)
                .setAppWaitActivity("*")
                .setAppWaitForLaunch(true);

        try {
            return new AndroidDriver(new URL(settings.ServerUrl), options);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Appium server URL: " + settings.ServerUrl, e);
        }
    }
}
