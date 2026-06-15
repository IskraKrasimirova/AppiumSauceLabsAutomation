package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

public class CatalogPage extends BasePage {
    private WebElement productsHeader() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/productTV"));
    }

    private WebElement productsList() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/productRV"));
    }

    public CatalogPage(AppiumDriver driver) {

        super(driver);
    }

    public NavBar navBar() {

        return new NavBar(driver);
    }

    public boolean isAtCatalogPage() {
        driverExt.waitUntilVisible(productsHeader());

        return productsList().isDisplayed();
    }
}
