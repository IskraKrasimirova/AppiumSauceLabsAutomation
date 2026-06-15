package pages;

import io.appium.java_client.AppiumDriver;
import utils.DriverExtensions;

public abstract class BasePage {
    protected AppiumDriver driver;
    protected DriverExtensions driverExt;

    protected BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.driverExt = new DriverExtensions(driver);
    }
}
