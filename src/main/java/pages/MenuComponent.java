package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.DriverExtensions;

public class MenuComponent {
    private final AppiumDriver driver;
    private final DriverExtensions driverExt;

    private final By loginMenuItemLocator = AppiumBy.accessibilityId("Login Menu Item");
    private final By catalogMenuItemLocator = AppiumBy.androidUIAutomator("new UiSelector().text(\"Catalog\")");
    private final By logoutMenuItemLocator = AppiumBy.accessibilityId("Logout Menu Item");


    private WebElement loginMenuItem() {
        return driver.findElement(loginMenuItemLocator);
    }

    private WebElement catalogMenuItem() {
        return driver.findElement(catalogMenuItemLocator);
    }

    private WebElement logoutMenuItem() {
        return driver.findElement(logoutMenuItemLocator);
    }

    public MenuComponent(AppiumDriver driver) {
        this.driver = driver;
        this.driverExt = new DriverExtensions(driver);
    }

    public void openCatalog() {
        driverExt.waitUntilClickable(catalogMenuItemLocator);
        catalogMenuItem().click();
    }

    public void openLogin() {
        driverExt.waitUntilClickable(loginMenuItemLocator);
        loginMenuItem().click();
    }

    public void openLogout() {
        driverExt.waitUntilClickable(logoutMenuItemLocator);
        logoutMenuItem().click();
    }

    public boolean isUserLoggedIn() {
        try {
            return logoutMenuItem().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
