package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import models.CheckoutData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CheckoutPage extends BasePage {
    private final CheckoutSection checkoutSection;
    private final By checkoutHeaderLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/checkoutTitleTV");

    private WebElement checkoutHeader() {
        return driver.findElement(checkoutHeaderLocator);
    }

    private WebElement shippingAddressText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/enterShippingAddressTV"));
    }

    private WebElement paymentButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/paymentBtn"));
    }

    public CheckoutPage(AppiumDriver driver) {
        super(driver);
        checkoutSection = new CheckoutSection(driver);
    }

    public CheckoutSection checkoutSection() {
        return checkoutSection;
    }

    public NavBar navBar() {
        return new NavBar(driver);
    }

    public void fillShippingAddress(CheckoutData data) {
        checkoutSection().fill(data);
    }

    public void goToPayment() {
        paymentButton().click();
    }

    public boolean isAtCheckoutPage() {
        driverExt.waitUntilVisible(checkoutHeaderLocator);

        return checkoutHeader().isDisplayed() && shippingAddressText().isDisplayed();
    }
}
