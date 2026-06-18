package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.*;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("guest")
@Tag("cart")
public class GuestCartTests extends BaseTest {
    private CatalogPage productsPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;
   // private LoginPage loginPage;

    @BeforeEach
    public void setUpPages() {
        productsPage = new CatalogPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);
        //loginPage = new LoginPage(driver);
    }

    @Test
    @Tag("smoke")
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

    @Test
    @Tag("regression")
    public void guestCanAddMultipleProductsToCart() {
        assertTrue(productsPage.isAtCatalogPage());

        int totalItems = productsPage.getProductsCount();
        int first = new Random().nextInt(totalItems);
        int second = (first + 1) % totalItems;

        // First product
        productsPage.scrollToProduct(first);
        productsPage.openProductDetails(first);
        assertTrue(productDetailsPage.isAtProductDetailsPage());
        String firstProductName = productDetailsPage.getTitle();
        String firstProductPrice = productDetailsPage.getPrice();
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openMenu();
        MenuComponent menu = new MenuComponent(driver);
        menu.openCatalog();
        assertTrue(productsPage.isAtCatalogPage());

        // Second product
        productsPage.scrollToProduct(second);
        productsPage.openProductDetails(second);
        assertTrue(productDetailsPage.isAtProductDetailsPage());
        String secondProductName = productDetailsPage.getTitle();
        String secondProductPrice = productDetailsPage.getPrice();
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());
        assertEquals(2, cartPage.getItemCount());
        assertTrue(cartPage.containsProduct(firstProductName));
        assertTrue(cartPage.containsProduct(secondProductName));
        BigDecimal cartTotalPrice = parsePrice(cartPage.getTotalPrice());
        BigDecimal expectedTotalPrice = parsePrice(firstProductPrice).add(parsePrice(secondProductPrice));
        assertEquals(expectedTotalPrice, cartTotalPrice);
    }

    @Test
    @Tag("regression")
    public void guestCanIncreaseAndDecreaseQuantityOfProductInCart() {
        assertTrue(productsPage.isAtCatalogPage());

        int index = productsPage.getRandomProductIndex();
        productsPage.scrollToProduct(index);
        productsPage.openProductDetails(index);
        assertTrue(productDetailsPage.isAtProductDetailsPage());

        BigDecimal price = parsePrice(productDetailsPage.getPrice());
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());
        assertEquals(1, cartPage.getItemCount());

        int initialQty = cartPage.getItemQuantity(0);
        cartPage.increaseQuantity(0);
        int increasedQty = cartPage.getItemQuantity(0);

        assertEquals(initialQty + 1, increasedQty);

        BigDecimal expectedPriceAfterIncrease = price.multiply(BigDecimal.valueOf(increasedQty));
        BigDecimal actualPriceAfterIncrease = parsePrice(cartPage.getTotalPrice());
        assertEquals(expectedPriceAfterIncrease, actualPriceAfterIncrease);

        cartPage.decreaseQuantity(0);
        int decreasedQty = cartPage.getItemQuantity(0);
        assertEquals(initialQty, decreasedQty);

        BigDecimal expectedPriceAfterDecrease = price.multiply(BigDecimal.valueOf(decreasedQty));
        BigDecimal actualPriceAfterDecrease = parsePrice(cartPage.getTotalPrice());
        assertEquals(expectedPriceAfterDecrease, actualPriceAfterDecrease);
    }

    @Test
    @Tag("regression")
    public void guestCanRemoveItemFromCart() {
        assertTrue(productsPage.isAtCatalogPage());

        int index = productsPage.getRandomProductIndex();
        productsPage.scrollToProduct(index);
        productsPage.openProductDetails(index);
        assertTrue(productDetailsPage.isAtProductDetailsPage());

        String productName = productDetailsPage.getTitle();
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());
        assertEquals(1, cartPage.getItemCount());
        assertTrue(cartPage.containsProduct(productName));

        cartPage.removeItem(0);

        assertTrue(cartPage.isCartEmpty());
    }

    @Test
    @Tag("regression")
    public void guestCanIncreaseQuantityFromProductDetailsPage() {
        assertTrue(productsPage.isAtCatalogPage());

        int index = productsPage.getRandomProductIndex();
        productsPage.scrollToProduct(index);
        productsPage.openProductDetails(index);
        assertTrue(productDetailsPage.isAtProductDetailsPage());

        BigDecimal price = parsePrice(productDetailsPage.getPrice());
        int initialQty = productDetailsPage.getQuantity();
        productDetailsPage.increaseQuantity();
        int increasedQty = productDetailsPage.getQuantity();

        assertEquals(initialQty + 1, increasedQty);

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());
        assertEquals(increasedQty, cartPage.getItemQuantity(0));

        BigDecimal expectedTotalPrice = price.multiply(BigDecimal.valueOf(increasedQty));
        BigDecimal actualTotalPrice = parsePrice(cartPage.getTotalPrice());
        assertEquals(expectedTotalPrice, actualTotalPrice);
    }

    @Test
    @Tag("regression")
    public void guestIsRedirectedToLoginWhenProceedingToCheckout() {
        assertTrue(productsPage.isAtCatalogPage());

        int index = productsPage.getRandomProductIndex();
        productsPage.scrollToProduct(index);
        productsPage.openProductDetails(index);
        assertTrue(productDetailsPage.isAtProductDetailsPage());

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());

        cartPage.proceedToCheckout();

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isAtLoginPage());
    }

    @Test
    @Tag("regression")
    public void cartPersistsAfterLogin() {
        assertTrue(productsPage.isAtCatalogPage());

        int index = productsPage.getRandomProductIndex();
        productsPage.scrollToProduct(index);
        productsPage.openProductDetails(index);
        assertTrue(productDetailsPage.isAtProductDetailsPage());

        String productName = productDetailsPage.getTitle();
        String productPrice = productDetailsPage.getPrice();
        Integer productQuantity = productDetailsPage.getQuantity();
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.containsProduct(productName));

        // Proceed to checkout → redirect to login
        cartPage.proceedToCheckout();
        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isAtLoginPage());

        loginPage.loginWithValidCredentials();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        assertTrue(checkoutPage.isAtCheckoutPage());
        checkoutPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());
        assertTrue(cartPage.containsProduct(productName));

        String price = cartPage.getItemPrice(0);
        Integer quantity = cartPage.getItemQuantity(0);
        String totalPrice = cartPage.getTotalPrice();
        Integer itemsInCart = cartPage.getNumberOfItemsInCart();

        assertEquals(productQuantity, quantity);
        assertEquals(quantity, itemsInCart);
        assertEquals(productPrice, price);
        assertEquals(productPrice, totalPrice);
    }


    private BigDecimal parsePrice(String price) {
        return new BigDecimal(price.replace("$ ", "").trim());
    }
}
