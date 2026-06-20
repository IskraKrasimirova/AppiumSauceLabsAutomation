package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static utils.PriceUtils.parsePrice;
import static utils.ProductSelectionHelper.getStableProductIndex;
import static utils.ProductSelectionHelper.getTwoStableProductIndices;

@Tag("guest")
@Tag("cart")
public class GuestCartTests extends BaseTest {
    private CatalogPage catalogPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;

    @BeforeEach
    public void setUpPages() {
        catalogPage = new CatalogPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);
    }

    @Test
    @Tag("smoke")
    public void guestCanAddProductToCart() {
        assertTrue(catalogPage.isAtCatalogPage());

        int index = getStableProductIndex(catalogPage.getProductsCount());
        catalogPage.scrollToProduct(index);
        String productName = catalogPage.getProductName(index);
        String productPrice = catalogPage.getProductPrice(index);
        catalogPage.openProductDetails(index);

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
        assertTrue(catalogPage.isAtCatalogPage());

        int[] indices = getTwoStableProductIndices();
        int first = indices[0];
        int second = indices[1];

        // First product
        catalogPage.scrollToProduct(first);
        String firstName = catalogPage.getProductName(first);
        String firstPrice = catalogPage.getProductPrice(first);
        catalogPage.openProductDetails(first);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        assertEquals(firstName, productDetailsPage.getTitle());

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openMenu();

        new MenuComponent(driver).openCatalog();
        assertTrue(catalogPage.isAtCatalogPage());

        // Second product
        catalogPage.scrollToProduct(second);
        String secondName = catalogPage.getProductName(second);
        String secondPrice = catalogPage.getProductPrice(second);
        catalogPage.openProductDetails(second);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        assertEquals(secondName, productDetailsPage.getTitle());

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());
        assertEquals(2, cartPage.getItemCount());
        assertTrue(cartPage.containsProduct(firstName));
        assertTrue(cartPage.containsProduct(secondName));

        BigDecimal cartTotalPrice = parsePrice(cartPage.getTotalPrice());
        BigDecimal expectedTotalPrice = parsePrice(firstPrice).add(parsePrice(secondPrice));

        assertEquals(expectedTotalPrice, cartTotalPrice);
    }

    @Test
    @Tag("regression")
    public void guestCanIncreaseAndDecreaseQuantityOfProductInCart() {
        assertTrue(catalogPage.isAtCatalogPage());

        int index = getStableProductIndex(catalogPage.getProductsCount());
        catalogPage.scrollToProduct(index);
        String itemName = catalogPage.getProductName(index);
        catalogPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        assertEquals(itemName, productDetailsPage.getTitle());

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
        assertTrue(catalogPage.isAtCatalogPage());

        int index = getStableProductIndex(catalogPage.getProductsCount());
        catalogPage.scrollToProduct(index);
        String itemName = catalogPage.getProductName(index);
        catalogPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());

        String name = productDetailsPage.getTitle();
        assertEquals(itemName, name);

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());
        assertEquals(1, cartPage.getItemCount());
        assertTrue(cartPage.containsProduct(name));

        cartPage.removeItem(0);

        assertTrue(cartPage.isCartEmpty());
    }

    @Test
    @Tag("regression")
    public void guestCanIncreaseQuantityFromProductDetailsPage() {
        assertTrue(catalogPage.isAtCatalogPage());

        int index = getStableProductIndex(catalogPage.getProductsCount());
        catalogPage.scrollToProduct(index);
        String itemName = catalogPage.getProductName(index);
        catalogPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        assertEquals(itemName, productDetailsPage.getTitle());

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
        assertTrue(catalogPage.isAtCatalogPage());

        int index = getStableProductIndex(catalogPage.getProductsCount());
        catalogPage.scrollToProduct(index);
        String itemName = catalogPage.getProductName(index);
        catalogPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        assertEquals(itemName, productDetailsPage.getTitle());

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());
        assertTrue(cartPage.containsProduct(itemName));

        cartPage.proceedToCheckout();

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isAtLoginPage());
    }

    @Test
    @Tag("regression")
    public void cartPersistsAfterLogin() {
        assertTrue(catalogPage.isAtCatalogPage());

        int index = getStableProductIndex(catalogPage.getProductsCount());
        catalogPage.scrollToProduct(index);
        String itemName = catalogPage.getProductName(index);
        catalogPage.openProductDetails(index);
        assertTrue(productDetailsPage.isAtProductDetailsPage());

        String productName = productDetailsPage.getTitle();
        assertEquals(itemName, productName);

        String productPrice = productDetailsPage.getPrice();
        Integer productQuantity = productDetailsPage.getQuantity();
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());
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
}
