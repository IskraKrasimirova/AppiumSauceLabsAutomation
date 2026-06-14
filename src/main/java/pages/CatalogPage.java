package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CatalogPage extends BasePage {
    private WebElement productsHeader() {
        return driver.findElement(AppiumBy.accessibilityId("title"));
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(driver ->
                productsHeader().isDisplayed() &&
                productsList().isDisplayed()
        );

        return true;
    }
}
