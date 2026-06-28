package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import models.CheckoutData;
import models.PaymentData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PaymentPage extends BasePage {
    public static final String INVALID_VALUE_ERROR = "Value looks invalid.";

    private final By checkoutHeaderLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/enterPaymentTitleTV");
    private final By fullNameErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/nameErrorIV");
    private final By cardNumberErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cardNumberErrorIV");
    private final By expirationDateErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/expirationDateIV");
    private final By securityCodeErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/securityCodeIV");

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

    private final CheckoutSection billingSection;
    private final By billingSectionLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/checkoutInfoCL");

    public PaymentPage(AppiumDriver driver) {
        super(driver);
        billingSection = new CheckoutSection(driver);
    }

    public CheckoutSection billingSection() {
        return billingSection;
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

    // No error message for empty card number
    public String getCardNumberErrorMessage() {
        return cardNumberErrorMessage().getText();
    }

    public String getExpirationDateErrorMessage() {
        return expirationDateErrorMessage().getText();
    }

    public String getSecurityCodeErrorMessage() {
        return securityCodeErrorMessage().getText();
    }

    public String getErrorMessage(String field) {
        switch (field) {
            case "fullName": return getFullNameErrorMessage();
            case "expirationDate": return getExpirationDateErrorMessage();
            case "securityCode": return getSecurityCodeErrorMessage();
            default: return ""; // cardNumber has no error message
        }
    }

    public boolean isErrorSymbolVisible(String field) {
        switch (field) {
            case "fullName": return isFullNameErrorSymbolVisible();
            case "cardNumber": return isCardNumberErrorSymbolVisible();
            case "expirationDate": return isExpirationDateErrorSymbolVisible();
            case "securityCode": return isSecurityCodeErrorSymbolVisible();
            default: throw new IllegalArgumentException("Unknown field: " + field);
        }
    }

    public void uncheckBillingAddress() {
        if (isBillingAddressChecked()) {
            billingAddressCheckbox().click();
        }
    }

    public void checkBillingAddress() {
        if (!isBillingAddressChecked()) {
            billingAddressCheckbox().click();
        }
    }

    public boolean isBillingSectionVisible() {
        return !driver.findElements(billingSectionLocator).isEmpty();
    }

    public void fillBillingAddress(CheckoutData data) {
        billingSection().fill(data);
    }

    private boolean isBillingAddressChecked() {
        return Boolean.parseBoolean(billingAddressCheckbox().getAttribute("checked"));
    }

    private boolean isFullNameErrorSymbolVisible() {
        return !driver.findElements(fullNameErrorSymbolLocator).isEmpty();
    }

    private boolean isCardNumberErrorSymbolVisible() {
        return !driver.findElements(cardNumberErrorSymbolLocator).isEmpty();
    }

    private boolean isExpirationDateErrorSymbolVisible() {
        return !driver.findElements(expirationDateErrorSymbolLocator).isEmpty();
    }

    private boolean isSecurityCodeErrorSymbolVisible() {
        return !driver.findElements(securityCodeErrorSymbolLocator).isEmpty();
    }
}
