package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {

    private WebElement loginHeader() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/loginTV"));
    }

    private WebElement usernameInput() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/nameET"));
    }

    private WebElement passwordInput() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/passwordET"));
    }

    private WebElement loginButton() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/loginBtn"));
    }

    private WebElement usernamesText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/savedNamesTV"));
    }

    private WebElement passwordText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/savedPasswordTV"));
    }

    private String getValidUsername() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/username1TV")).getText();
    }

    private String getValidPassword() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/password1TV")).getText();
    }

    private WebElement usernameErrorText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/nameErrorTV"));
    }

    private WebElement usernameErrorImage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/usernameErrorIV"));
    }

    private WebElement passwordErrorText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/passwordErrorTV"));
    }

    private WebElement passwordErrorImage() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/passwordErrorIV"));
    }

    public LoginPage(AppiumDriver driver) {
        super(driver);
    }

    public NavBar navBar() {
        return new NavBar(driver);
    }

    public void login(String username, String password) {
        driverExt.enterText(usernameInput(), username);
        driverExt.enterText(passwordInput(), password);

        loginButton().click();
    }

    public void loginWithValidCredentials() {
        login(getValidUsername(), getValidPassword());
    }

    public boolean isAtLoginPage() {
        driverExt.waitUntilVisible(loginHeader());

        return usernameInput().isDisplayed()
                && passwordInput().isDisplayed()
                && loginButton().isDisplayed()
                && usernamesText().isDisplayed()
                && passwordText().isDisplayed();
    }

    public boolean isUsernameErrorVisible() {
        try {
            return usernameErrorText().isDisplayed() && usernameErrorImage().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPasswordErrorVisible() {
        try {
            return passwordErrorText().isDisplayed() && passwordErrorImage().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameErrorMessage() {
        return usernameErrorText().getText();
    }

    public String getPasswordErrorMessage() {
        return passwordErrorText().getText();
    }
}
