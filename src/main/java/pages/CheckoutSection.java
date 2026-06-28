package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import models.CheckoutData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CheckoutSection extends  BasePage {
    public static final String FULL_NAME_ERROR_MESSAGE = "Please provide your full name.";
    public static final String ADDRESS_ERROR_MESSAGE = "Please provide your address.";
    public static final String CITY_ERROR_MESSAGE = "Please provide your city.";
    public static final String ZIP_CODE_ERROR_MESSAGE = "Please provide your zip";
    public static final String COUNTRY_ERROR_MESSAGE = "Please provide your";

    private final By fullNameInputLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/fullNameET");
    private final By addressInputLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/address1ET");
    private final By cityInputLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cityET");
    private final By zipCodeInputLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/zipET");
    private final By countryInputLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/countryET");
    private final By address2InputLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/address2ET");
    private final By stateInputLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/stateET");

    private final By fullNameErrorMessageLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/fullNameErrorTV");
    private final By addressErrorMessageLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/address1ErrorTV");
    private final By cityErrorMessageLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cityErrorTV");
    private final By zipCodeErrorMessageLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/zipErrorTV");
    private final By countryErrorMessageLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/countryErrorTV");

    private final By fullNameErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/fullNameErrorIV");
    private final By addressErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/address1ErrorIV");
    private final By cityErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cityIV");
    private final By zipCodeErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/zipIV");
    private final By countryErrorSymbolLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/countryIV");

    private WebElement fullNameInput() {
        return driver.findElement(fullNameInputLocator);
    }

    private WebElement addressInput() {
        return driver.findElement(addressInputLocator);
    }

    private WebElement address2Input() {
        return driver.findElement(address2InputLocator);
    }

    private WebElement cityInput() {
        return driver.findElement(cityInputLocator);
    }

    private WebElement stateInput() {
        return driver.findElement(stateInputLocator);
    }

    private WebElement zipCodeInput() {
        return driver.findElement(zipCodeInputLocator);
    }

    private WebElement countryInput() {
        return driver.findElement(countryInputLocator);
    }

    private WebElement fullNameErrorMessage() {
        return driver.findElement(fullNameErrorMessageLocator);
    }

    private WebElement addressErrorMessage() {
        return driver.findElement(addressErrorMessageLocator);
    }

    private WebElement cityErrorMessage() {
        return driver.findElement(cityErrorMessageLocator);
    }

    private WebElement zipCodeErrorMessage() {
        return driver.findElement(zipCodeErrorMessageLocator);
    }

    private WebElement countryErrorMessage() {
        return driver.findElement(countryErrorMessageLocator);
    }

    public CheckoutSection(AppiumDriver driver) {
        super(driver);
    }

    public void fill(CheckoutData data) {
        scrollUntilVisible(fullNameInputLocator, 5);
        driverExt.enterText(fullNameInput(), data.fullName);

        scrollUntilVisible(addressInputLocator, 5);
        driverExt.enterText(addressInput(), data.address);

        scrollUntilVisible(address2InputLocator, 5);
        driverExt.enterText(address2Input(), data.address2);

        scrollUntilVisible(cityInputLocator, 5);
        driverExt.enterText(cityInput(), data.city);

        scrollUntilVisible(stateInputLocator, 5);
        driverExt.enterText(stateInput(), data.state);

        scrollUntilVisible(zipCodeInputLocator, 5);
        driverExt.enterText(zipCodeInput(), data.zipCode);

        scrollUntilVisible(countryInputLocator, 5);
        driverExt.enterText(countryInput(), data.country);
    }

    public String getFullNameErrorMessage() {
        scrollUntilVisible(fullNameErrorMessageLocator, 5);
        return fullNameErrorMessage().getText();
    }

    public String getAddressErrorMessage() {
        scrollUntilVisible(addressErrorMessageLocator, 5);
        return addressErrorMessage().getText();
    }

    public String getCityErrorMessage() {
        scrollUntilVisible(cityErrorMessageLocator, 5);
        return cityErrorMessage().getText();
    }

    public String getZipErrorMessage() {
        scrollUntilVisible(zipCodeErrorMessageLocator, 5);
        return zipCodeErrorMessage().getText();
    }

    public String getCountryErrorMessage() {
        scrollUntilVisible(countryErrorMessageLocator, 5);
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
        scrollUntilVisible(fullNameErrorSymbolLocator, 5);
        return !driver.findElements(fullNameErrorSymbolLocator).isEmpty();
    }

    private boolean isAddressErrorSymbolVisible() {
        scrollUntilVisible(addressErrorSymbolLocator, 5);
        return !driver.findElements(addressErrorSymbolLocator).isEmpty();
    }

    private boolean isCityErrorSymbolVisible() {
        scrollUntilVisible(cityErrorSymbolLocator, 5);
        return !driver.findElements(cityErrorSymbolLocator).isEmpty();
    }

    private boolean isZipCodeErrorSymbolVisible() {
        scrollUntilVisible(zipCodeErrorSymbolLocator, 5);
        return !driver.findElements(zipCodeErrorSymbolLocator).isEmpty();
    }

    private boolean isCountryErrorSymbolVisible() {
        scrollUntilVisible(countryErrorSymbolLocator, 5);
        return !driver.findElements(countryErrorSymbolLocator).isEmpty();
    }
}
