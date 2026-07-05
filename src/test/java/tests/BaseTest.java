package tests;

import drivers.DriverFactory;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.ScreenshotOnFailureExtension;
import utils.ScreenshotUtils;

@ExtendWith({AllureJunit5.class, ScreenshotOnFailureExtension.class})
public abstract class BaseTest {
    protected AppiumDriver driver;

    @BeforeEach
    public void setUp() {
        driver = DriverFactory.getDriver();
        ScreenshotUtils.setDriver(driver);
    }

    @AfterEach
    public void tearDown() {
        DriverFactory.closeDriver();
    }
}
