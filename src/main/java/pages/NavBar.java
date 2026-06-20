package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;

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

    public void closeMenu() {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), 1000, 100));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
    }

    public void openCart() {
        cartButton().click();
    }
}
