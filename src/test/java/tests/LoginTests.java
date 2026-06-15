package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.CatalogPage;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public  void userCanLoginSuccessfullyWithValidCredentials() {
        loginPage.loginWithValidCredentials();

        assertTrue(catalogPage.isAtCatalogPage());
        catalogPage.navBar().openMenu();
        assertTrue(catalogPage.navBar().menu().isUserLoggedIn());
        catalogPage.navBar().closeMenu();
        assertTrue(catalogPage.isAtCatalogPage());
    }
}
