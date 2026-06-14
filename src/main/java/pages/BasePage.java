package pages;

import io.appium.java_client.AppiumDriver;

public abstract class BasePage {
    protected AppiumDriver driver;

    protected BasePage(AppiumDriver driver) {
        this.driver = driver;
    }
}
