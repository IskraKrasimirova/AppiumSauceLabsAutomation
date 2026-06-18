package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CheckoutPage extends BasePage {
    private final By checkoutHeaderLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/checkoutTitleTV");
    private WebElement checkoutHeader() {
        return driver.findElement(checkoutHeaderLocator);
    }

    public CheckoutPage(AppiumDriver driver) {
        super(driver);
    }

    public NavBar navBar() {
        return new NavBar(driver);
    }

    public boolean isAtCheckoutPage() {
        driverExt.waitUntilVisible(driver.findElement(checkoutHeaderLocator));

        return checkoutHeader().isDisplayed();
    }
}
