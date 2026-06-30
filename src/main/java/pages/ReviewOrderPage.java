package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ReviewOrderPage extends BasePage {
    private final By checkoutHeaderLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/checkoutTitleTV");
    private final By deliveryPriceLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/amountTV");
    private final By deliveryAddressBlockLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/addressLL");
    private final By paymentDetailsLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/billingLL");
    private final By billingAddressLocator = AppiumBy.xpath("//android.widget.TextView[@text=\"Billing Address\"]");
    private final By billingAddressMessageLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/billingAddressTV");
    private final By paymentFullNamedLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cardHolderTV");
    private final By paymentCardNumberLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cardNumberTV");
    private final By paymentExpirationDateLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/expirationDateTV");

    private WebElement checkoutHeader() {
        return driver.findElement(checkoutHeaderLocator);
    }

    private WebElement reviewOrderText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/enterShippingAddressTV"));
    }

    private List<WebElement> productDetailsBlock() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/infoCL"));
    }

    private List<WebElement> productNames() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/titleTV"));
    }

    private List<WebElement> productPrices() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/priceTV"));
    }

    private List<WebElement> productImages() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/productIV"));
    }

    private WebElement deliveryAddressBlock() {
        return driver.findElement(deliveryAddressBlockLocator);
    }

    private WebElement paymentDetailsBlock() {
        return driver.findElement(paymentDetailsLocator);
    }

    private WebElement deliveryAddressText() {
        return driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"Deliver Address\"]"));
    }

    private WebElement fullNameText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/fullNameTV"));
    }

    private WebElement addressText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/addressTV"));
    }

    private WebElement address2Text() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/address2TV"));
    }

    private WebElement cityAndStateText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/cityTV"));
    }

    private WebElement countryAndZipCodeText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/countryTV"));
    }

    private WebElement paymentMethodText() {
        return driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"Payment Method\"]"));
    }

    private WebElement paymentFullNamedText() {
        return driver.findElement(paymentFullNamedLocator);
    }

    private WebElement paymentCardNumberText() {
        return driver.findElement(paymentCardNumberLocator);
    }

    private WebElement paymentExpirationDateText() {
        return driver.findElement(paymentExpirationDateLocator);
    }

    private WebElement billingAddressMessageText() {
        return driver.findElement(billingAddressMessageLocator);
    }

    private WebElement deliveryInfoText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/dhlTV"));
    }

    private WebElement deliveryPriceText() {
        return driver.findElement(deliveryPriceLocator);
    }

    private WebElement paymentTotalBlock() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/totalLL"));
    } // com.saucelabs.mydemoapp.android:id/paymentCV

    private WebElement numberOfItemsText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/itemNumberTV"));
    }

    private WebElement totalPriceText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/totalAmountTV"));
    }

    private WebElement placeOrderButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/paymentBtn"));
    }

    private WebElement billingAddressText() {
        return driver.findElement(billingAddressLocator);
    }

    private WebElement billingFullNameText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/billFullnameTV"));
    }

    private WebElement billingAddressLineText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/billaddressTV"));
    }

    private WebElement billingCityAndStateText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/billingCityAndStateTV"));
    }

    private WebElement billingZipCodeAndCountryText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/billingZipAndCountryTV"));
    }

    public ReviewOrderPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isAtReviewOrderPage() {
        driverExt.waitUntilVisible(checkoutHeaderLocator);

        return checkoutHeader().isDisplayed()
                && reviewOrderText().isDisplayed()
                && !productDetailsBlock().isEmpty()
                && !productImages().isEmpty()
                && paymentTotalBlock().isDisplayed();
    }

    public Integer getProductsCount() {
        return productImages().size();
    }

    public List<String> getAllProductNames() {
        return productNames()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<String> getAllProductPrices() {
        return productPrices()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public String getProductName(int index) {
        return productNames().get(index).getText();
    }

    public String getProductPrice(int index) {
        return productPrices().get(index).getText();
    }

    public String getItemsCountText() {
        return numberOfItemsText().getText(); // e.g. "1 Items"
    }

    public Integer getTotalItemsCount() {
        return Integer.parseInt(numberOfItemsText().getText().replaceAll("\\D+", ""));
    }

    public String getDeliveryPrice() {
        scrollUntilVisible(deliveryPriceLocator, 5);
        return deliveryPriceText().getText();
    }

    public String getDeliveryInfo() {
        scrollUntilVisible(deliveryPriceLocator, 5);
        return deliveryInfoText().getText();
    }

    public String getTotalPrice() {
        return totalPriceText().getText();
    }

    public void placeOrder() {
        placeOrderButton().click();
    }

    public boolean isPlaceOrderButtonVisible() {
        return placeOrderButton().isDisplayed();
    }

    public String getFullName() {
        scrollUntilVisible(deliveryAddressBlockLocator, 5);
        return fullNameText().getText();
    }

    public String getAddressLine() {
        scrollUntilVisible(deliveryAddressBlockLocator, 5);
        return addressText().getText();
    }

    public String getCityAndState() {
        scrollUntilVisible(deliveryAddressBlockLocator, 5);
        return cityAndStateText().getText();
    }

    public String getCountryAndZip() {
        scrollUntilVisible(deliveryAddressBlockLocator, 5);
        return countryAndZipCodeText().getText();
    }

    public boolean isDeliveryAddressVisible() {
        scrollUntilVisible(deliveryAddressBlockLocator, 5);
        return deliveryAddressBlock().isDisplayed();
    }

    public boolean isDeliveryAddressTextVisible() {
        scrollUntilVisible(deliveryAddressBlockLocator, 5);
        return deliveryAddressText().isDisplayed();
    }

    public boolean isPaymentDetailsVisible() {
        scrollUntilVisible(paymentDetailsLocator, 5);
        return paymentDetailsBlock().isDisplayed();
    }

    public boolean isBillingAddressMessageVisible() {
        scrollUntilVisible(billingAddressMessageLocator, 5);
        return billingAddressMessageText().isDisplayed();
    } // Billing address is the same as shopping address

    public boolean isBillingAddressTextVisible() {
        scrollUntilVisible(billingAddressLocator, 5);
        return billingAddressText().isDisplayed();
    }

    public String getBillingFullName() {
        scrollUntilVisible(billingAddressLocator, 5);
        return billingFullNameText().getText();
    }

    public String getBillingAddress() {
        scrollUntilVisible(billingAddressLocator, 5);
        return billingAddressLineText().getText();
    }

    public String getBillingCityAndState() {
        scrollUntilVisible(billingAddressLocator, 5);
        return billingCityAndStateText().getText();
    }

    public String getBillingZipCodeAndCountry() {
        scrollUntilVisible(billingAddressLocator, 5);
        return billingZipCodeAndCountryText().getText();
    }

    public boolean isPaymentMethodTextVisible() {
        scrollUntilVisible(paymentDetailsLocator, 5);
        return paymentMethodText().isDisplayed();
    }

    public String getCardHolderName() {
        scrollUntilVisible(paymentFullNamedLocator, 5);
        return paymentFullNamedText().getText();
    }

    public String getCardNumber() {
        scrollUntilVisible(paymentCardNumberLocator, 5);
        return paymentCardNumberText().getText();
    }

    public String getExpirationDate() {
        scrollUntilVisible(paymentExpirationDateLocator, 5);
        return paymentExpirationDateText().getText();
    }
}
