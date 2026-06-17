package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {
    private WebElement cartHeader() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/productTV"));
    }

    private WebElement emptyCartHeader() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/noItemTitleTV"));
    }

    private WebElement emptyCartImage() {
        return driver.findElement(AppiumBy.xpath(
                "//android.widget.LinearLayout[@resource-id=\"com.saucelabs.mydemoapp.android:id/cartInfoLL\"]/android.widget.ImageView"
        ));
    }

    private WebElement emptyCartMessage() {
        return driver.findElement(AppiumBy.xpath("//*[contains(@text, 'Your cart is empty')]"));
    }

    private WebElement goShoppingButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/shoppingBt"));
    }

    private List<WebElement> cartItemImages() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/productIV"));
    }

    private List<WebElement> cartItemTitles() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/titleTV"));
    }

    private List<WebElement> cartItemPrices() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/priceTV"));
    }

    private List<WebElement> colorText() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/colorTitleTV"));
    }

    private List<WebElement> selectedColors() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/colorIV"));
    }

    private List<WebElement> cartItemRemoveButtons() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/removeBt"));
    }

    private List<WebElement> cartItemPlusButtons() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/plusIV"));
    }

    private List<WebElement> cartItemMinusButtons() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/minusIV"));
    }

    private List<WebElement> cartItemQuantities() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/noTV"));
    }

    private WebElement totalText() {
        return driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"Total:\"]"));
    }

    private WebElement numberOfItemsText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/itemsTV"));
    }

    private WebElement totalPrice() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/totalPriceTV"));
    }

    private WebElement ProceedToCheckoutButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartBt"));
    }

    public CartPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isCartEmpty(){
        driverExt.waitUntilVisible(emptyCartHeader());

        return emptyCartImage().isDisplayed()
                && emptyCartMessage().isDisplayed()
                && goShoppingButton().isDisplayed();
    }

    public boolean isCartNotEmpty() {
        driverExt.waitUntilVisible(cartHeader());

        return !cartItemTitles().isEmpty();
    }

    public boolean isAtCartPage() {
        return isCartEmpty() || isCartNotEmpty();
    }

    public int getItemCount() {
        return cartItemTitles().size();
    }

    public String getItemName(int index) {
        return cartItemTitles().get(index).getText();
    }

    public String getItemPrice(int index) {
        return cartItemPrices().get(index).getText();
    }

    public int getItemQuantity(int index) {
        return Integer.parseInt(cartItemQuantities().get(index).getText());
    }

    public void increaseQuantity(int index) {
        cartItemPlusButtons().get(index).click();
    }

    public void decreaseQuantity(int index) {
        cartItemMinusButtons().get(index).click();
    }

    public void removeItem(int index) {
        cartItemRemoveButtons().get(index).click();
    }

    public boolean containsProduct(String productName) {
        return cartItemTitles().stream()
                .anyMatch(e -> e.getText().equals(productName));
    }

    public String getTotalPrice() {
        return totalPrice().getText();
    }
}
