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

    private WebElement validUsernameText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/username1TV"));
    }

    private WebElement validPasswordText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/password1TV"));
    }

    private WebElement lockedUsernameText() {
        return driver.findElement(AppiumBy.id("com.saucelabs.mydemoapp.android:id/username2TV"));
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

    public String getValidUsername() {
        return validUsernameText().getText();
    }

    public String getValidPassword() {
        return validPasswordText().getText();
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

    public String getLockedUsernameText() {
        return lockedUsernameText().getText();
    }

    public boolean isLockedUserErrorVisible() {
        try {
            return passwordErrorText().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getLockedUserErrorMessage() {
        return passwordErrorText().getText();
    }
}
