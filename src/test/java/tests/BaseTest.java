package tests;

import com.google.common.collect.ImmutableMap;
import drivers.DriverFactory;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.JavascriptExecutor;

public abstract class BaseTest {
    protected AppiumDriver driver;

    @BeforeEach
    public void setUp() {

        driver = DriverFactory.getDriver();
        ((JavascriptExecutor) driver).executeScript("mobile: setOrientation",
                ImmutableMap.of("orientation", "PORTRAIT"));
    }

    @AfterEach
    public void tearDown() {
        DriverFactory.closeDriver();
    }
}
