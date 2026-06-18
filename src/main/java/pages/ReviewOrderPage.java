package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ReviewOrderPage extends BasePage {
    private final By checkoutHeaderLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/checkoutTitleTV");

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
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/addressLL"));
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

    private WebElement paymentTotalBlock() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/totalLL"));
    }

    private WebElement numberOfItemsText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/itemNumberTV"));
    }

    private WebElement totalPriceText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/totalAmountTV"));
    }

    private WebElement placeOrderButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/paymentBtn"));
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
                && deliveryAddressBlock().isDisplayed()
                && paymentMethodText().isDisplayed()
                && paymentTotalBlock().isDisplayed()
                && placeOrderButton().isDisplayed();
    }

    public Integer getProductsCount() {
        return productImages().size();
    }

    public String getProductName(int index) {
        return productNames().get(index).getText();
    }

    public String getProductPrice(int index) {
        return productPrices().get(index).getText();
    }

    public String getDeliveryAddress() {
        return deliveryAddressBlock().getText();
    }

    public String getItemsCountText() {
        return numberOfItemsText().getText(); // e.g. "1 Items"
    }

    public Integer getTotalItemsCount() {
        return Integer.parseInt(numberOfItemsText().getText().replaceAll("\\D+", ""));
    }

    public String getTotalPrice() {
        return totalPriceText().getText();
    }

    public void placeOrder() {
        placeOrderButton().click();
    }
}
