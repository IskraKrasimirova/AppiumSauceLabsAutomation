package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public class CatalogPage extends BasePage {
    private final By productsHeaderLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/productTV");

    private WebElement productsHeader() {
        return driver.findElement(productsHeaderLocator);
    }

    private WebElement productsList() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/productRV"));
    }

    private List<WebElement> productItems() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/productIV"));
    }

    private List<WebElement> productTitles() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/titleTV"));
    }

    private List<WebElement> productPrices() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/priceTV"));
    }

    private List<WebElement> productRatings() {
        return driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/rattingV"));
    }

    public CatalogPage(AppiumDriver driver) {
        super(driver);
    }

    public NavBar navBar() {
        return new NavBar(driver);
    }

    public boolean isAtCatalogPage() {
        driverExt.waitUntilVisible(driver.findElement(productsHeaderLocator));

        return productsList().isDisplayed();
    }

    public int getProductsCount() {
        return productItems().size();
    }

    public String getProductName(int index) {
        return productTitles().get(index).getText();
    }

    public String getProductPrice(int index) {
        return productPrices().get(index).getText();
    }

    public void openProductDetails(int index) {
        productItems().get(index).click();
    }

    public String getRandomProductName() {
        int index = getRandomProductIndex();
        return getProductName(index);
    }

    public void scrollToProduct(int index) {
        scrollUntilVisible(productItems().get(index), 10);
    }

    public int getRandomProductIndex() {
        Random random = new Random();
        return random.nextInt(getProductsCount());
    }
}
