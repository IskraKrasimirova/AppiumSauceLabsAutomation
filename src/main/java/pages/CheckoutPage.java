package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import models.CheckoutData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CheckoutPage extends BasePage {
    private final By checkoutHeaderLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/checkoutTitleTV");
    private WebElement checkoutHeader() {
        return driver.findElement(checkoutHeaderLocator);
    }

    private WebElement shippingAddressText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/enterShippingAddressTV"));
    }

    private WebElement fullNameInput() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/fullNameET"));
    }

    private WebElement addressInput() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/address1ET"));
    }

    private WebElement address2Input() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/address2ET"));
    }

    private WebElement cityInput() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/cityET"));
    }

    private WebElement stateInput() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/stateET"));
    }

    private WebElement zipCodeInput() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/zipET"));
    }

    private WebElement countryInput() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/countryET"));
    }

    private WebElement paymentButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/paymentBtn"));
    }

    public CheckoutPage(AppiumDriver driver) {
        super(driver);
    }

    public NavBar navBar() {
        return new NavBar(driver);
    }

    public void fillShippingAddress(CheckoutData data){
        driverExt.enterText(fullNameInput(), data.fullName);
        driverExt.enterText(addressInput(), data.address);
        driverExt.enterText(address2Input(), data.address2);
        driverExt.enterText(cityInput(), data.city);
        driverExt.enterText(stateInput(), data.state);
        driverExt.enterText(zipCodeInput(), data.zipCode);
        driverExt.enterText(countryInput(), data.country);
    }

    public void goToPayment()
    {
        paymentButton().click();
    }

    public boolean isAtCheckoutPage() {
        driverExt.waitUntilVisible(checkoutHeaderLocator);

        return shippingAddressText().isDisplayed()
                && fullNameInput().isDisplayed()
                && addressInput().isDisplayed()
                && address2Input().isDisplayed()
                && cityInput().isDisplayed()
                && stateInput().isDisplayed()
                && zipCodeInput().isDisplayed()
                && countryInput().isDisplayed()
                && paymentButton().isDisplayed();
    }
}
