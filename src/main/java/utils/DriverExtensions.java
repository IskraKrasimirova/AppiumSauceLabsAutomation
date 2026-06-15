package utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DriverExtensions {
    private final AppiumDriver driver;

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

    private WebDriverWait createWait(int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        return wait;
    }

    private WebDriverWait createWait() {
        return createWait(10); // default timeout
    }
}
