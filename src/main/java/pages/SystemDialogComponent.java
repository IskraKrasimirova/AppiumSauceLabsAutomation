package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SystemDialogComponent {
    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    private final AppiumDriver driver;

    // Crash and ANR dialog locators (App isn’t responding)
    private final By dialogTitleLocator = AppiumBy.id("android:id/alertTitle");
    private final By closeButtonLocator = AppiumBy.id("android:id/aerr_close");
    private final By waitButtonLocator = AppiumBy.id("android:id/aerr_wait");

    // Crash Dialog Text - My Demo App keeps stopping
    // ANR Dialog Text - Pixel Launcher isn’t responding or System UI isn't responding

    private WebElement dialogTitle() {
        return driver.findElement(dialogTitleLocator);
    }

    private WebElement closeButton() {
        return driver.findElement(closeButtonLocator);
    }

    private WebElement waitButton() {
        return driver.findElement(waitButtonLocator);
    }

    public SystemDialogComponent(AppiumDriver driver) {
        this.driver = driver;
    }

    public boolean isCrashDialogVisible() {
        return isDialogVisible("keeps stopping");
    }

    public boolean isAnrDialogVisible() {
        return isDialogVisible("isn't responding");
    }

    public void handleDialog() {
        if (isCrashDialogVisible()) {
            System.out.println("Crash dialog detected.");
            closeButton().click();

            // Restart app after crash
            restartApp();
            return;
        }

        if (isAnrDialogVisible()) {
            System.out.println("ANR dialog detected.");
            // Prefer Wait
            if (!driver.findElements(waitButtonLocator).isEmpty()) {
                waitButton().click();
                // Let the app recover
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {
                }
            } else {
                closeButton().click(); // Android decided to kill it
                restartApp();
            }
        }
    }

    private boolean isDialogVisible(String expectedText) {
        if (driver.findElements(dialogTitleLocator).isEmpty()) return false;
        return dialogTitle().getText().contains(expectedText);
    }

    private void restartApp() {
        ((InteractsWithApps) driver).terminateApp(APP_PACKAGE);
        ((InteractsWithApps) driver).activateApp(APP_PACKAGE);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }
}
