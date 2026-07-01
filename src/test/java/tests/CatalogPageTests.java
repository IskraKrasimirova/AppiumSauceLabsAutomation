package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CatalogPage;
import pages.ProductDetailsPage;

import static org.junit.jupiter.api.Assertions.*;
import static utils.ProductSelectionHelper.getStableProductIndex;

@Tag("catalog")
@Tag("regression")
public class CatalogPageTests extends BaseTest {
    private CatalogPage catalogPage;

    @BeforeEach
    void setUpPage() {
        catalogPage = new CatalogPage(driver);
        assertTrue(catalogPage.isAtCatalogPage());
    }

    @Test
    void catalogShowsVisibleProducts() {
        int visibleCount = catalogPage.getVisibleProductCount();
        assertTrue(visibleCount > 0, "Catalog should show at least one visible product");
    }

    @Test
    void catalogScrollsToProductCorrectly() {
        int index = getStableProductIndex();
        assertDoesNotThrow(() -> catalogPage.scrollToProduct(index));
    }

    @Test
    void catalogDisplaysCorrectProductNameAndPrice() {
        int index = getStableProductIndex();
        catalogPage.scrollToProduct(index);

        String name = catalogPage.getProductName(index);
        String price = catalogPage.getProductPrice(index);

        assertAll("Catalog product validation",
                () -> assertFalse(name.isEmpty(), "Product name should not be empty"),
                () -> assertFalse(price.isEmpty(), "Product price should not be empty")
        );
    }

    @Test
    void catalogOpensProductDetailsPage() {
        int index = getStableProductIndex();
        catalogPage.scrollToProduct(index);
        String expectedName = catalogPage.getProductName(index);
        String expectedPrice = catalogPage.getProductPrice(index);

        catalogPage.openProductDetails(index);

        ProductDetailsPage productDetailsPage = new ProductDetailsPage(driver);
        assertTrue(productDetailsPage.isAtProductDetailsPage());
        assertAll("Product validation",
                () -> assertEquals(expectedName, productDetailsPage.getTitle()),
                () -> assertEquals(expectedPrice, productDetailsPage.getPrice()),
                () -> assertEquals(1, productDetailsPage.getQuantity())
        );
    }
}
