package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CatalogPage;
import pages.LoginPage;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.*;

@Tag("login")
public class LoginTests extends BaseTest {
    private CatalogPage catalogPage;
    private LoginPage loginPage;

    @BeforeEach
    public void setUpTest() {
        catalogPage = new CatalogPage(driver);
        loginPage = new LoginPage(driver);

        assertTrue(catalogPage.isAtCatalogPage());

        catalogPage.navBar().openMenu();
        catalogPage.navBar().menu().openLogin();

        assertTrue(loginPage.isAtLoginPage());
    }

    @Test
    @Tag("smoke")
    public void userCanLoginSuccessfullyWithValidCredentials() {
        loginPage.loginWithValidCredentials();

        assertTrue(catalogPage.isAtCatalogPage());
        catalogPage.navBar().openMenu();
        assertTrue(catalogPage.navBar().menu().isUserLoggedIn());
        catalogPage.navBar().closeMenu();
        assertTrue(catalogPage.isAtCatalogPage());
    }

    @ParameterizedTest
    @MethodSource("invalidLoginData")
    @Tag("regression")
    public void userCannotLoginWithInvalidCredentials(
            String username,
            String password,
            boolean expectUsernameError,
            boolean expectPasswordError) {
        loginPage.login(username, password);

        assertEquals(expectUsernameError, loginPage.isUsernameErrorVisible());
        assertEquals(expectPasswordError, loginPage.isPasswordErrorVisible());

        if (expectUsernameError) {
            assertEquals("Username is required", loginPage.getUsernameErrorMessage());
        }

        if (expectPasswordError) {
            assertEquals("Enter Password", loginPage.getPasswordErrorMessage());
        }

        assertTrue(loginPage.isAtLoginPage());
        loginPage.navBar().openMenu();
        assertFalse(loginPage.navBar().menu().isUserLoggedIn());
        loginPage.navBar().closeMenu();
    }

    private static Stream<Arguments> invalidLoginData() {
        return Stream.of(
                Arguments.of("", "", true, false),
                Arguments.of("iskra@iskra.com", "", false, true),
                Arguments.of("a", "", false, true),
                Arguments.of("1", "", false, true),
                Arguments.of("@", "", false, true),
                Arguments.of("!@#$%^&*()_+=<>?|-;:", "", false, true)
        );
    }

    @ParameterizedTest
    @MethodSource("userCredentials")
    @Tag("regression")
    public void userCanLoginWithAnyCredentials(String username, String password) {
        loginPage.login(username, password);

        assertTrue(catalogPage.isAtCatalogPage());
        catalogPage.navBar().openMenu();
        assertTrue(catalogPage.navBar().menu().isUserLoggedIn());
        catalogPage.navBar().closeMenu();
        assertTrue(catalogPage.isAtCatalogPage());
    }

    private static Stream<Arguments> userCredentials() {
        return Stream.of(
                Arguments.of("a", "a"),
                Arguments.of("123", "123"),
                Arguments.of("iskra", "password"),
                Arguments.of("!@#$%", "!@#$%"),
                Arguments.of("test@user.com", "whatever"),
                Arguments.of("bod@example.com", "Pass123")
        );
    }

    @Test
    @Tag("regression")
    public void userCannotLoginWhenLockedOut() {
        var lockedUsername = loginPage.getLockedUsernameText();
        var validPassword = loginPage.getValidPassword();

        loginPage.login(lockedUsername, validPassword);

        assertTrue(loginPage.isLockedUserErrorVisible());
        assertEquals("Sorry this user has been locked out.", loginPage.getLockedUserErrorMessage());

        assertTrue(loginPage.isAtLoginPage());
        loginPage.navBar().openMenu();
        assertFalse(loginPage.navBar().menu().isUserLoggedIn());
        loginPage.navBar().closeMenu();
    }
}
