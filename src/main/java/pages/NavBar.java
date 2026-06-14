package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

public class NavBar {
    private final AppiumDriver driver;

    private WebElement menuButton() {
        return driver.findElement(AppiumBy.accessibilityId("View menu"));
    }

    private WebElement appName() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/mTvTitle"));
    }

    private WebElement sortButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/sortIV"));
    }

    private WebElement cartButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartIV"));
    }

    public NavBar(AppiumDriver driver) {
        this.driver = driver;
    }

    public MenuComponent menu() {
        return new MenuComponent(driver);
    }

    public void openMenu() {
        menuButton().click();
    }
}
