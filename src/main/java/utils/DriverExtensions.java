package utils;

import config.ConfigReader;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DriverExtensions {
    private final AppiumDriver driver;
    private final boolean isCi = ConfigReader.getSettings().IsCi;

    public DriverExtensions(AppiumDriver driver) {
        this.driver = driver;
    }

    public void enterText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    public void waitUntilClickable(WebElement element) {
        createWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitUntilClickable(WebElement element, int timeoutSeconds) {
        createWait(timeoutSeconds).until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitUntilVisible(WebElement element) {
        createWait().until(ExpectedConditions.visibilityOf(element));
    }

    public void waitUntilVisible(WebElement element, int timeoutSeconds) {
        createWait(timeoutSeconds).until(ExpectedConditions.visibilityOf(element));
    }

    public void waitUntilVisible(By locator) {
        createWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitUntilVisible(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitUntilVisibleAndReturn(By locator) {
        WebDriverWait wait = createWait();
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebDriverWait createWait(int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        return wait;
    }

    private WebDriverWait createWait() {
        int timeout = isCi ? 25 : 10;
        return createWait(timeout);
    }
}
