package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

public class MenuComponent {
    private final AppiumDriver driver;

    private WebElement menuButton() {
        return driver.findElement(AppiumBy.accessibilityId("View menu"));
    }

    private WebElement loginMenuItem() {
        return driver.findElement(AppiumBy.accessibilityId("Login Menu Item"));
    }

    private WebElement catalogMenuItem() {
        return driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Catalog\")"));
    }

    private WebElement logoutMenuItem() {
        return driver.findElement(AppiumBy.accessibilityId("Logout Menu Item"));
    }

    public MenuComponent(AppiumDriver driver) {
        this.driver = driver;
    }

    public void openCatalog() {

        catalogMenuItem().click();
    }

    public void openLogin() {

        loginMenuItem().click();
    }

    public void logout() {
        logoutMenuItem().click();
    }

    public boolean isUserLoggedIn(){
        try {
            return logoutMenuItem().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
