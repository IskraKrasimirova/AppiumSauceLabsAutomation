package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.CatalogPage;
import pages.LoginPage;
import pages.ProductDetailsPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GuestCartTests extends BaseTest {
    private CatalogPage productsPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;
    //private LoginPage loginPage;

    @BeforeEach
    public void setUpPages() {
        productsPage = new CatalogPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);
        //loginPage = new LoginPage(driver);
    }

    @Test
    public void guestCanAddProductToCart() {
        assertTrue(productsPage.isAtCatalogPage());

        int index = productsPage.getRandomProductIndex();
        productsPage.scrollToProduct(index);
        String productName = productsPage.getProductName(index);
        String productPrice = productsPage.getProductPrice(index);
        productsPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        String name = productDetailsPage.getTitle();
        String price = productDetailsPage.getPrice();
        Integer quantity = productDetailsPage.getQuantity();

        assertEquals(name, productName);
        assertEquals(price, productPrice);

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());

        String cartProductName = cartPage.getItemName(0);
        String cartProductPrice = cartPage.getItemPrice(0);
        Integer cartProductQuantity = cartPage.getItemQuantity(0);
        String totalPrice = cartPage.getTotalPrice();

        assertEquals(name, cartProductName);
        assertEquals(price, cartProductPrice);
        assertEquals(quantity, cartProductQuantity);
        assertEquals(price, totalPrice);
    }
}
