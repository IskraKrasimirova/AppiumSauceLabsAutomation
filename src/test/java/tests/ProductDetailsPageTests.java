package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.CatalogPage;
import pages.ProductDetailsPage;

import static org.junit.jupiter.api.Assertions.*;
import static utils.ProductSelectionHelper.getStableProductIndex;

@Tag("productDetails")
@Tag("regression")
public class ProductDetailsPageTests extends BaseTest {
    private CatalogPage catalogPage;
    private ProductDetailsPage productDetailsPage;
    private String productName;
    private String productPrice;

    @BeforeEach
    void setUpPages() {
        catalogPage = new CatalogPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);

        assertTrue(catalogPage.isAtCatalogPage(), "Catalog page is not visible");

        int index = getStableProductIndex();
        catalogPage.scrollToProduct(index);
        productName = catalogPage.getProductName(index);
        productPrice = catalogPage.getProductPrice(index);
        catalogPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
    }

    @Test
    void productDetailsDisplaysCorrectNameAndPrice() {
        assertAll("Product details validation",
                () -> assertEquals(productName, productDetailsPage.getTitle()),
                () -> assertEquals(productPrice, productDetailsPage.getPrice()),
                () -> assertEquals(1, productDetailsPage.getQuantity())
        );
    }

    @Test
    void productDetailsQuantityCanIncrease() {
        int initialQty = productDetailsPage.getQuantity();

        productDetailsPage.increaseQuantity();
        productDetailsPage.increaseQuantity();

        assertEquals(initialQty + 2, productDetailsPage.getQuantity());
    }

    @Test
    void productDetailsQuantityCanDecrease() {
        productDetailsPage.increaseQuantity(); // ensure > 1
        int initialQty = productDetailsPage.getQuantity();

        productDetailsPage.decreaseQuantity();

        assertEquals(initialQty - 1, productDetailsPage.getQuantity());
    }

    @Test
    void productDetailsAllowsColorSelection() {
        assertDoesNotThrow(() -> productDetailsPage.selectColor());
    }

    @Test
    void productDetailsAllowsAddingToCart() {
        String itemName = productDetailsPage.getTitle();
        String itemPrice = productDetailsPage.getPrice();
        Integer itemQuantity = productDetailsPage.getQuantity();

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();

        CartPage cartPage = new CartPage(driver);
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());

        String cartName = cartPage.getItemName(0);
        String cartPrice = cartPage.getItemPrice(0);
        int cartQty = cartPage.getItemQuantity(0);

        assertAll("Cart item validation",
                () -> assertEquals(itemName, cartName),
                () -> assertEquals(itemPrice, cartPrice),
                () -> assertEquals(itemQuantity, cartQty)
        );
    }
}
