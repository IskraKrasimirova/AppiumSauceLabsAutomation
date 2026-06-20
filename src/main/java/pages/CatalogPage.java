package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public class CatalogPage extends BasePage {
    private final By productsHeaderLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/productTV");
    private final By productContainerLocator = AppiumBy.xpath("//android.view.ViewGroup[.//android.widget.TextView[@resource-id='com.saucelabs.mydemoapp.android:id/titleTV']]");
    private final By productTitleLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/titleTV");
    private final By productPriceLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/priceTV");
    private final By productImageLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/productIV");
//    private final By productRatingLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/rattingV");

    private WebElement productsHeader() {
        return driver.findElement(productsHeaderLocator);
    }

    private WebElement productsList() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/productRV"));
    }

    private List<WebElement> productImages() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/productIV"));
    }

    private List<WebElement> productTitles() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/titleTV"));
    }

    private List<WebElement> productPrices() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/priceTV"));
    }

    public CatalogPage(AppiumDriver driver) {
        super(driver);
    }

    public NavBar navBar() {
        return new NavBar(driver);
    }

    public boolean isAtCatalogPage() {
        driverExt.waitUntilVisible(productsHeaderLocator);

        return productsHeader().isDisplayed() && productsList().isDisplayed();
    }

    public int getProductsCount() {
        return productImages().size();
    }

    public String getProductName(int index) {
        return productTitles().get(index).getText();
    }

    public String getProductPrice(int index) {
        return productPrices().get(index).getText();
    }

    public void openProductDetails(int index) {
        WebElement image = productImages().get(index);
        driverExt.waitUntilClickable(image);
        image.click();
    }

    public int getRandomProductIndex() {
        return new Random().nextInt(getProductsCount());
    }

    public void scrollToProduct(int index) {
        scrollUntilVisible(productImages().get(index), 10);
    }
}
