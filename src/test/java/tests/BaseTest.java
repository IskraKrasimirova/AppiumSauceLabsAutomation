package tests;

import drivers.DriverFactory;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import reporting.ExtentReportExtension;
import utils.ScreenshotUtils;

@ExtendWith(ExtentReportExtension.class)
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
