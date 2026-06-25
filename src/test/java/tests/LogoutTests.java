package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CatalogPage;
import pages.LoginPage;
import pages.LogoutComponent;

import static org.junit.jupiter.api.Assertions.*;

@Tag("logout")
public class LogoutTests extends BaseTest {
    private CatalogPage catalogPage;
    private LoginPage loginPage;
    private LogoutComponent logoutModal;

    @BeforeEach
    public void setUpTest() {
        catalogPage = new CatalogPage(driver);
        loginPage = new LoginPage(driver);

        assertTrue(catalogPage.isAtCatalogPage());

        catalogPage.navBar().openMenu();
        catalogPage.navBar().menu().openLogin();

        assertTrue(loginPage.isAtLoginPage());
        loginPage.loginWithValidCredentials();

        assertTrue(catalogPage.isAtCatalogPage());
    }

    @Test
    @Tag("regression")
    public void loggedInUserCanLogoutSuccessfully(){
        catalogPage.navBar().openMenu();
        catalogPage.navBar().menu().openLogout();

        logoutModal = new LogoutComponent(driver);
        assertTrue(logoutModal.isVisible());

        logoutModal.confirmLogout();

        assertTrue(loginPage.isAtLoginPage());
        loginPage.navBar().openMenu();
        assertFalse(loginPage.navBar().menu().isUserLoggedIn());
    }

    @Test
    @Tag("regression")
    public void loggedInUserCanCancelLogout() {
        catalogPage.navBar().openMenu();
        catalogPage.navBar().menu().openLogout();

        logoutModal = new LogoutComponent(driver);
        assertTrue(logoutModal.isVisible());

        logoutModal.cancelLogout();

        assertTrue(catalogPage.isAtCatalogPage());
        catalogPage.navBar().openMenu();
        assertTrue(catalogPage.navBar().menu().isUserLoggedIn());
    }

    @Test
    @Tag("regression")
    public void logoutModalHasCorrectTexts() {
        catalogPage.navBar().openMenu();
        catalogPage.navBar().menu().openLogout();

        logoutModal = new LogoutComponent(driver);
        assertTrue(logoutModal.isVisible());

        assertAll("Logout modal text validation",
                () -> assertEquals("Log Out", logoutModal.getModalTitle()),
                () -> assertEquals("Are you sure you want to logout", logoutModal.getModalMessage()),
                () -> assertEquals("CANCEL", logoutModal.getCancelButtonText()),
                () -> assertEquals("LOGOUT", logoutModal.getLogoutButtonText())
        );
    }
}
