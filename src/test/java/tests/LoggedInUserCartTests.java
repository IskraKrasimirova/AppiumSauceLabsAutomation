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

@Tag("loggedIn")
@Tag("cart")
public class LoggedInUserCartTests extends BaseTest {
    private CatalogPage catalogPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;
    private LoginPage loginPage;

    @BeforeEach
    public void setUpLoggedInUser() {
        catalogPage = new CatalogPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);
        loginPage = new LoginPage(driver);

        // Login first
        assertTrue(catalogPage.isAtCatalogPage());
        catalogPage.navBar().openMenu();
        catalogPage.navBar().menu().openLogin();

        assertTrue(loginPage.isAtLoginPage());
        loginPage.loginWithValidCredentials();

        assertTrue(catalogPage.isAtCatalogPage());
    }

    @Test
    @Tag("smoke")
    public void loggedInUserCanAddProductToCart() {
        int index = getStableProductIndex();
        catalogPage.scrollToProduct(index);

        String expectedName = catalogPage.getProductName(index);
        String expectedPrice = catalogPage.getProductPrice(index);

        catalogPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        assertEquals(expectedName, productDetailsPage.getTitle());
        assertEquals(expectedPrice, productDetailsPage.getPrice());

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());
        assertAll("Cart item validation",
                () -> assertEquals(expectedName, cartPage.getItemName(0)),
                () -> assertEquals(expectedPrice, cartPage.getItemPrice(0)),
                () -> assertEquals(1, cartPage.getItemQuantity(0)),
                () -> assertEquals(expectedPrice, cartPage.getTotalPrice())
        );
    }

    @Test
    @Tag("regression")
    public void loggedInUserCanAddMultipleProductsToCart() {
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
        assertAll("Cart content validation",
                () -> assertEquals(2, cartPage.getItemCount()),
                () -> assertTrue(cartPage.containsProduct(firstName)),
                () -> assertTrue(cartPage.containsProduct(secondName))
        );

        BigDecimal cartTotalPrice = parsePrice(cartPage.getTotalPrice());
        BigDecimal expectedTotalPrice = parsePrice(firstPrice).add(parsePrice(secondPrice));

        assertEquals(expectedTotalPrice, cartTotalPrice);
    }

    @Test
    @Tag("regression")
    public void loggedInUserCanIncreaseAndDecreaseQuantityOfProductInCart() {
        int index = getStableProductIndex();
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
    public void loggedInUserCanRemoveItemFromCart() {
        int index = getStableProductIndex();
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
    public void loggedInUserCanIncreaseQuantityFromProductDetailsPage() {
        int index = getStableProductIndex();
        catalogPage.scrollToProduct(index);
        String itemName = catalogPage.getProductName(index);
        catalogPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        assertEquals(itemName, productDetailsPage.getTitle());

        BigDecimal price = parsePrice(productDetailsPage.getPrice());
        int initialQty = productDetailsPage.getQuantity();
        // Increase 3 times
        productDetailsPage.increaseQuantity();
        productDetailsPage.increaseQuantity();
        productDetailsPage.increaseQuantity();

        int increasedQty = productDetailsPage.getQuantity();
        assertEquals(initialQty + 3, increasedQty);

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());

        BigDecimal expectedTotalPrice = price.multiply(BigDecimal.valueOf(increasedQty));
        BigDecimal actualTotalPrice = parsePrice(cartPage.getTotalPrice());

        assertAll("Cart validation",
                () -> assertEquals(increasedQty, cartPage.getItemQuantity(0)),
                () -> assertEquals(expectedTotalPrice, actualTotalPrice)
        );
    }

    @Test
    @Tag("regression")
    public void loggedInUserCartPersistsAcrossPages() {
        int index = getStableProductIndex();
        catalogPage.scrollToProduct(index);

        String itemName = catalogPage.getProductName(index);
        catalogPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        assertEquals(itemName, productDetailsPage.getTitle());

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();

        // Navigate around
        productDetailsPage.navBar().openMenu();
        new MenuComponent(driver).openCatalog();
        assertTrue(catalogPage.isAtCatalogPage());

        catalogPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());
        assertTrue(cartPage.containsProduct(itemName));
    }
}
