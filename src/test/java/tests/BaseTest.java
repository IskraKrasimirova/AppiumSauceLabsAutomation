package tests;

import drivers.DriverFactory;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {
    protected AppiumDriver driver;

    @BeforeEach
    public void setUp() {

        driver = DriverFactory.getDriver();
    }

    @AfterEach
    public void tearDown() {
        DriverFactory.closeDriver();
    }
}
