package tests;

import drivers.DriverFactory;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.SystemDialogComponent;
import utils.ScreenshotOnFailureExtension;
import utils.ScreenshotUtils;

@ExtendWith({ScreenshotOnFailureExtension.class, AllureJunit5.class})
public abstract class BaseTest {
    protected AppiumDriver driver;
    private SystemDialogComponent systemDialog;

    @BeforeEach
    public void setUp() {
        driver = DriverFactory.getDriver();
        ScreenshotUtils.setDriver(driver);
        systemDialog = new SystemDialogComponent(driver);

        systemDialog.handleDialog();
    }

    @AfterEach
    public void tearDown() {
        systemDialog.handleDialog();
        DriverFactory.closeDriver();
    }
}
