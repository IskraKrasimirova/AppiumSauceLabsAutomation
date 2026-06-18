package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class OrderCompletePage extends BasePage {
    private final By checkoutHeaderLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/completeTV");
    private WebElement checkoutHeader() {
        return driver.findElement(checkoutHeaderLocator);
    }

    private WebElement thankYouMessageText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/thankYouTV"));
    }

    private WebElement orderMessageText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/orderTV"));
    }

    private WebElement continueShoppingButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/shoopingBt"));
    }

    public OrderCompletePage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isAtOrderCompletePage() {
        driverExt.waitUntilVisible(checkoutHeaderLocator);

        return checkoutHeader().isDisplayed()
                && thankYouMessageText().isDisplayed()
                && orderMessageText().isDisplayed()
                && continueShoppingButton().isDisplayed();
    }

    public String getHeaderText() {
        return checkoutHeader().getText();
    }

    public String getThankYouMessage() {
        return thankYouMessageText().getText();
    }

    public String getOrderDescriptionMessage() {
        return orderMessageText().getText();
    }

    public void continueShopping() {
        continueShoppingButton().click();
    }
}
