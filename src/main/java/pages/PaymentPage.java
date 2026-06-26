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

    // Value looks invalid.
    private WebElement fullNameErrorMessage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/nameErrorTV"));
    }

    // No error message
    private WebElement cardNumberErrorMessage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/cardNumberErrorTV"));
    }

    private WebElement expirationDateErrorMessage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/expirationDateErrorTV"));
    }

    private WebElement securityCodeErrorMessage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/securityCodeErrorTV"));
    }

    private WebElement fullNameErrorSymbol() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/nameErrorIV"));
    }

    private WebElement cardNumberErrorSymbol() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/cardNumberErrorIV"));
    }

    private WebElement expirationDateErrorSymbol() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/expirationDateIV"));
    }

    private WebElement securityCodeErrorSymbol() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/securityCodeIV"));
    }

    public PaymentPage(AppiumDriver driver) {
        super(driver);
    }

    public void fillPaymentData(PaymentData data) {
        driverExt.enterText(fullNameInput(), data.fullName);
        driverExt.enterText(cardNumberInput(), data.cardNumber);
        driverExt.enterText(expirationDateInput(), data.expirationDate);
        driverExt.enterText(securityCodeInput(), data.securityCode);

        boolean isChecked = Boolean.parseBoolean(billingAddressCheckbox().getAttribute("checked"));
        if (data.billingSameAsShipping != isChecked) {
            billingAddressCheckbox().click();
        }
    }

    public void reviewOrder() {
        reviewOrderButton().click();
    }

    public boolean isAtPaymentPage() {
        driverExt.waitUntilVisible(checkoutHeaderLocator);

        return checkoutHeader().isDisplayed()
                && paymentMethodText().isDisplayed();
    }

    public String getFullNameErrorMessage() {
        return fullNameErrorMessage().getText();
    }

    public String getCardNumberErrorMessage() {
        return cardNumberErrorMessage().getText();
    }

    public String getExpirationDateErrorMessage() {
        return expirationDateErrorMessage().getText();
    }

    public String getSecurityCodeErrorMessage() {
        return securityCodeErrorMessage().getText();
    }
}
