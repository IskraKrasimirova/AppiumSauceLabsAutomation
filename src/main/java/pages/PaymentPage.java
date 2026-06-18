package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import models.PaymentData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PaymentPage extends BasePage {
    private final By checkoutHeaderLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/enterPaymentTitleTV");

    private WebElement checkoutHeader() {
        return driver.findElement(checkoutHeaderLocator);
    }

    private WebElement paymentMethodText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/enterPaymentMethodTV"));
    }

    private WebElement paymentInfoText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/paymentDetailsTV"));
    }

    private WebElement cardText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/cardTV"));
    }

    private WebElement cardVisaImage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/visaIV"));
    }

    private WebElement mastercardImage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/mastercardIV"));
    }

    private WebElement fullNameInput() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/nameET"));
    }

    private WebElement cardNumberInput() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/cardNumberET"));
    }

    private WebElement expirationDateInput() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/expirationDateET"));
    }

    private WebElement securityCodeInput() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/securityCodeET"));
    }

    private WebElement billingAddressCheckbox() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/billingAddressCB"));
    }

    private WebElement reviewOrderButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/paymentBtn"));
    }

    public PaymentPage(AppiumDriver driver) {
        super(driver);
    }

    public void fillPaymentData(PaymentData data){
        driverExt.enterText(fullNameInput(), data.fullName);
        driverExt.enterText(cardNumberInput(), data.cardNumber);
        driverExt.enterText(expirationDateInput(), data.expirationDate);
        driverExt.enterText(securityCodeInput(), data.securityCode);

        boolean isChecked = Boolean.parseBoolean(billingAddressCheckbox().getAttribute("checked"));
        if (data.billingSameAsShipping != isChecked) {
            billingAddressCheckbox().click();
        }
    }

    public void reviewOrder(){
        reviewOrderButton().click();
    }

    public boolean isAtPaymentPage() {
        driverExt.waitUntilVisible(checkoutHeaderLocator);

        return paymentMethodText().isDisplayed()
                && paymentInfoText().isDisplayed()
                && cardText().isDisplayed()
                && cardVisaImage().isDisplayed()
                && mastercardImage().isDisplayed()
                && fullNameInput().isDisplayed()
                && reviewOrderButton().isDisplayed();
    }
}
