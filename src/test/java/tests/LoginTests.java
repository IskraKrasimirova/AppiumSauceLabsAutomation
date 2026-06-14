package tests;

import org.junit.jupiter.api.Test;
import pages.CatalogPage;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTests extends BaseTest{
    @Test
    public void navigateToLoginPage() {

        CatalogPage catalog = new CatalogPage(driver);
        LoginPage login = new LoginPage(driver);

        assertTrue(catalog.isAtCatalogPage());

        catalog.navBar().openMenu();
        catalog.navBar().menu().openLogin();

        assertTrue(login.isAtLoginPage());
    }
}
