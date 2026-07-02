package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CatalogPage extends BasePage {
    private final By productsHeaderLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/productTV");

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
        int attempts = 2; // 1 normal run + 1 retry

        for (int i = 0; i < attempts; i++) {
            try {
                // explicit wait: 20–25 sec in CI
                driverExt.waitUntilVisible(productsHeaderLocator);

                return productsHeader().isDisplayed() &&
                        productsList().isDisplayed();
            } catch (Exception e) {
                // only in failure → 2 sec sleep
                if (i < attempts - 1) {
                    try { // CI‑specific stabilization pattern
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }

        return false;
    }

    /*public boolean isAtCatalogPage() {
        driverExt.waitUntilVisible(productsHeaderLocator);

        return productsHeader().isDisplayed() && productsList().isDisplayed();
    }*/

    /*public boolean isAtCatalogPage() {
        System.out.println("DEBUG: Checking if Catalog page is visible...");

        try {
            driverExt.waitUntilVisible(productsHeaderLocator);
            var title = driver.findElement(productsHeaderLocator);
            System.out.println("DEBUG: Title text = " + title.getText());
            return true;
        } catch (Exception e) {
            System.out.println("DEBUG: Catalog page NOT visible. Exception: " + e);
            return false;
        }
    }*/

    public int getVisibleProductCount() {
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

    public void scrollToProduct(int index) {
        scrollUntilVisible(productImages().get(index), 10);
    }
}
