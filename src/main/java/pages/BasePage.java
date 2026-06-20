package pages;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import utils.DriverExtensions;

public abstract class BasePage {
    protected AppiumDriver driver;
    protected DriverExtensions driverExt;

    protected BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.driverExt = new DriverExtensions(driver);
    }

    public void scrollDown() {
        ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", ImmutableMap.of(
                "left", 100,
                "top", 1200,
                "width", 800,
                "height", 800,
                "direction", "up",
                "percent", 0.50
        ));
    }

    public void scrollUntilVisible(WebElement element, int maxScrolls) {
        int scrollCount = 0;

        while (scrollCount < maxScrolls) {
            try {
                if (element.isDisplayed()) return;
            } catch (Exception ignored) {}

            ((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", ImmutableMap.of(
                    "left", 100,
                    "top", 300,
                    "width", 800,
                    "height", 1200,
                    "direction", "down",
                    "percent", 0.40
            ));

            scrollCount++;
        }

        throw new RuntimeException("Element not visible after scrolling: " + element);
    }

    public void scrollUntilVisible(By locator, int maxScrolls) {
        int scrollCount = 0;

        while (scrollCount < maxScrolls) {
            try {
                WebElement element = driver.findElement(locator);
                if (element.isDisplayed()) return;
            } catch (Exception ignored) {}

            ((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", ImmutableMap.of(
                    "left", 100,
                    "top", 300,
                    "width", 800,
                    "height", 1200,
                    "direction", "down",
                    "percent", 0.40
            ));

            scrollCount++;
        }

        throw new RuntimeException("Element not visible after scrolling: " + locator);
    }
}
