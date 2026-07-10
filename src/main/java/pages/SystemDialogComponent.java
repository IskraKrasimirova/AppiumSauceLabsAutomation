package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SystemDialogComponent {
    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    private final AppiumDriver driver;
    private final By systemDialogLocator = AppiumBy.id("android:id/parentPanel");

    // Crash dialog locators
    private final By crashDialogTextLocator = AppiumBy.id("android:id/alertTitle");
    private final By crashCloseButtonLocator = AppiumBy.id("android:id/aerr_close");
    private final By crashAppInfoButtonLocator = AppiumBy.id("android:id/aerr_app_info");

    // ANR dialog locators (App isn’t responding)
    private final By anrMessageLocator = AppiumBy.id("android:id/message");
    private final By anrCloseButtonLocator = AppiumBy.id("android:id/button1");
    private final By anrWaitButtonLocator = AppiumBy.id("android:id/button2");

    private WebElement crashDialogText() {
        return driver.findElement(crashDialogTextLocator);
    } // My Demo App keeps stopping

    private WebElement crashCloseButton() {
        return driver.findElement(crashCloseButtonLocator);
    }

    private WebElement anrDialogText() {
        return driver.findElement(anrMessageLocator);
    } // Pixel Launcher isn’t responding

    private WebElement anrCloseButton() {
        return driver.findElement(anrCloseButtonLocator);
    }

    private WebElement anrWaitButton() {
        return driver.findElement(anrWaitButtonLocator);
    }

    public SystemDialogComponent(AppiumDriver driver) {
        this.driver = driver;
    }

    /*public boolean isAnrDialogVisible() {
        return !driver.findElements(anrMessageLocator).isEmpty()
                && anrDialogText().getText().contains("isn't responding");
    }

    public boolean isCrashDialogVisible() {
        return !driver.findElements(crashDialogTextLocator).isEmpty()
                && crashDialogText().getText().contains("keeps stopping");
    }*/

    public boolean isCrashDialogVisible() {
        if (driver.findElements(crashDialogTextLocator).isEmpty()) return false;
        return crashDialogText().getText().contains("keeps stopping");
    }

    public boolean isAnrDialogVisible() {
        if (driver.findElements(anrMessageLocator).isEmpty()) return false;
        return anrDialogText().getText().contains("isn't responding");
    }

    public void handleDialog() {
        if (!driver.findElements(systemDialogLocator).isEmpty()) {
            System.out.println("DEBUG Dialog page source: " + driver.getPageSource());
        }

        if (isCrashDialogVisible()) {
            System.out.println("Crash dialog detected.");
            crashCloseButton().click();

            // Restart app after crash
            restartApp();

            return;
        }

        if (isAnrDialogVisible()) {
            System.out.println("ANR dialog detected.");
            // Prefer Wait
            if (!driver.findElements(anrWaitButtonLocator).isEmpty()) {
                anrWaitButton().click(); // Let the app recover
            } else {
                anrCloseButton().click(); // Android decided to kill it
                restartApp();
            }
        }
    }

    private void restartApp() {
        ((InteractsWithApps) driver).terminateApp(APP_PACKAGE);
        ((InteractsWithApps) driver).activateApp(APP_PACKAGE);
    }
}
