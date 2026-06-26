package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import models.CheckoutData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CheckoutPage extends BasePage {
    public static final String FULL_NAME_ERROR_MESSAGE = "Please provide your full name.";
    public static final String ADDRESS_ERROR_MESSAGE = "Please provide your address.";
    public static final String CITY_ERROR_MESSAGE = "Please provide your city.";
    public static final String ZIP_CODE_ERROR_MESSAGE = "Please provide your zip";
    public static final String COUNTRY_ERROR_MESSAGE = "Please provide your";

    private final By checkoutHeaderLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/checkoutTitleTV");
    private final By fullNameErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/fullNameErrorIV");
    private final By addressErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/address1ErrorIV");
    private final By cityErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cityIV");
    private final By zipCodeErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/zipIV");
    private final By countryErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/countryIV");

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

    private WebElement fullNameErrorMessage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/fullNameErrorTV"));
    }

    private WebElement addressErrorMessage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/address1ErrorTV"));
    }

    private WebElement cityErrorMessage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/cityErrorTV"));
    }

    private WebElement zipCodeErrorMessage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/zipErrorTV"));
    }

    private WebElement countryErrorMessage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/countryErrorTV"));
    }

    public CheckoutPage(AppiumDriver driver) {
        super(driver);
    }

    public NavBar navBar() {
        return new NavBar(driver);
    }

    public void fillShippingAddress(CheckoutData data) {
        driverExt.enterText(fullNameInput(), data.fullName);
        driverExt.enterText(addressInput(), data.address);
        driverExt.enterText(address2Input(), data.address2);
        driverExt.enterText(cityInput(), data.city);
        driverExt.enterText(stateInput(), data.state);
        driverExt.enterText(zipCodeInput(), data.zipCode);
        driverExt.enterText(countryInput(), data.country);
    }

    public void goToPayment() {
        paymentButton().click();
    }

    public boolean isAtCheckoutPage() {
        driverExt.waitUntilVisible(checkoutHeaderLocator);

        return checkoutHeader().isDisplayed() && shippingAddressText().isDisplayed();
    }

    public String getFullNameErrorMessage() {
        return fullNameErrorMessage().getText();
    }

    public String getAddressErrorMessage() {
        return addressErrorMessage().getText();
    }

    public String getCityErrorMessage() {
        return cityErrorMessage().getText();
    }

    public String getZipErrorMessage() {
        return zipCodeErrorMessage().getText();
    }

    public String getCountryErrorMessage() {
        return countryErrorMessage().getText();
    }

    public String getErrorMessage(String field) {
        String errorMessage = "";

        switch (field) {
            case "fullName":
                errorMessage = getFullNameErrorMessage();
                break;
            case "address":
                errorMessage = getAddressErrorMessage();
                break;
            case "city":
                errorMessage = getCityErrorMessage();
                break;
            case "zipCode":
                errorMessage = getZipErrorMessage();
                break;
            case "country":
                errorMessage = getCountryErrorMessage();
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + field);
        }

        return errorMessage;
    }

    public boolean isErrorSymbolVisible(String field) {
        boolean visible;

        switch (field) {
            case "fullName":
                visible = isFullNameErrorSymbolVisible();
                break;
            case "address":
                visible = isAddressErrorSymbolVisible();
                break;
            case "city":
                visible = isCityErrorSymbolVisible();
                break;
            case "zipCode":
                visible = isZipCodeErrorSymbolVisible();
                break;
            case "country":
                visible = isCountryErrorSymbolVisible();
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + field);
        }

        return visible;
    }

    private boolean isFullNameErrorSymbolVisible() {
        return !driver.findElements(fullNameErrorSymbolLocator).isEmpty();
    }

    private boolean isAddressErrorSymbolVisible() {
        return !driver.findElements(addressErrorSymbolLocator).isEmpty();
    }

    private boolean isCityErrorSymbolVisible() {
        return !driver.findElements(cityErrorSymbolLocator).isEmpty();
    }

    private boolean isZipCodeErrorSymbolVisible() {
        return !driver.findElements(zipCodeErrorSymbolLocator).isEmpty();
    }

    private boolean isCountryErrorSymbolVisible() {
        return !driver.findElements(countryErrorSymbolLocator).isEmpty();
    }
}
