package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import utils.DriverExtensions;

import java.time.Duration;
import java.util.Collections;

public class NavBar {
    private final AppiumDriver driver;
    private final DriverExtensions driverExt;

    private final By menuButtonLocator = AppiumBy.accessibilityId("View menu");
    private final By cartButtonLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartIV");

    private WebElement menuButton() {
        return driver.findElement(menuButtonLocator);
    }

    private WebElement appName() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/mTvTitle"));
    }

    private WebElement sortButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/sortIV"));
    }

    private WebElement cartButton() {
        return driver.findElement(cartButtonLocator);
    }

    public NavBar(AppiumDriver driver) {
        this.driver = driver;
        this.driverExt = new DriverExtensions(driver);
    }

    public MenuComponent menu() {
        return new MenuComponent(driver);
    }

    public void openMenu() {
        driverExt.waitUntilClickable(menuButtonLocator);
        menuButton().click();
    }

    public void closeMenu() {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), 1000, 100));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
    }

    public void openCart() {
        driverExt.waitUntilClickable(cartButtonLocator);
        cartButton().click();
    }
}
