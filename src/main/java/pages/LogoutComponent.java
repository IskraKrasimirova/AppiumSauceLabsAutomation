package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

public class LogoutComponent extends BasePage {
    private WebElement modalTitle() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/alertTitle"));
    }

    private WebElement modalMessage() {
        return driver.findElement(AppiumBy.id("android:id/message"));
    }

    private WebElement cancelButton() {
        return driver.findElement(AppiumBy.id("android:id/button2"));
    }

    private WebElement logoutButton() {
        return driver.findElement(AppiumBy.id("android:id/button1"));
    }

    public LogoutComponent(AppiumDriver driver) {
        super(driver);
    }

    public boolean isVisible() {
        try {
            return modalTitle().isDisplayed() && modalMessage().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void confirmLogout() {
        logoutButton().click();
    }

    public void cancelLogout() {
        cancelButton().click();
    }

    public String getModalTitle() {
        return modalTitle().getText();
    }

    public String getModalMessage() {
        return modalMessage().getText();
    }

    public String getCancelButtonText() {
        return cancelButton().getText();
    }

    public String getLogoutButtonText() {
        return logoutButton().getText();
    }
}
