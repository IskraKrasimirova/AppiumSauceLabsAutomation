package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public class ProductDetailsPage extends BasePage{
    private WebElement productImage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/productIV"));
    }

    private WebElement productTitle() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/productTV"));
    }

    private WebElement productPrice() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/priceTV"));
    }

    private WebElement productRating() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/rattingV"));
    }

    private List<WebElement> colorOptions() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/aroundIV"));
    }

    private WebElement addToCartButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartBt"));
    }

    private WebElement quantityMinus() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/minusIV"));
    }

    private WebElement quantityPlus() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/plusIV"));
    }

    private WebElement quantityValue() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/noTV"));
    }

    public ProductDetailsPage(AppiumDriver driver) {
        super(driver);
    }

    public NavBar navBar() {
        return new NavBar(driver);
    }

    public boolean isAtProductDetailsPage() {
        driverExt.waitUntilVisible(productTitle());

        return productImage().isDisplayed()
                && productPrice().isDisplayed()
                && addToCartButton().isDisplayed();
    }

    public String getTitle() {
        return productTitle().getText();
    }

    public String getPrice() {
        return productPrice().getText();
    }

    public void increaseQuantity() {
        quantityPlus().click();
    }

    public void decreaseQuantity() {
        quantityMinus().click();
    }

    public int getQuantity() {
        return Integer.parseInt(quantityValue().getText());
    }

    public void addToCart() {
        addToCartButton().click();
    }

    public void selectColor() {
        List<WebElement> colors = colorOptions();

        if (colors.isEmpty()) {
            throw new RuntimeException("No colors available for this product");
        }

        int random = new Random().nextInt(colors.size());
        colors.get(random).click();
    }

    public String getSelectedColor() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/colorTitleTV")).getText();
    }

    // ---------- Scroll support ----------

    public void scrollToAddToCart() {
        scrollUntilVisible(addToCartButton(), 10);
    }

    public void scrollToQuantity() {
        scrollUntilVisible(quantityPlus(), 10);
    }
}
