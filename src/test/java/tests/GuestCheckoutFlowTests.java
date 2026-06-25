package tests;

import factories.CheckoutDataFactory;
import factories.PaymentDataFactory;
import models.CheckoutData;
import models.PaymentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static utils.PriceUtils.parsePrice;
import static utils.ProductSelectionHelper.getStableProductIndex;
import static utils.ProductSelectionHelper.getTwoStableProductIndices;

@Tag("e2e")
@Tag("guest")
public class GuestCheckoutFlowTests extends BaseTest {
    private CatalogPage catalogPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;
    private LoginPage loginPage;
    private CheckoutPage checkoutPage;
    private PaymentPage paymentPage;
    private ReviewOrderPage reviewOrderPage;
    private OrderCompletePage orderCompletePage;

    @BeforeEach
    public void setUpPages() {
        catalogPage = new CatalogPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);
        loginPage = new LoginPage(driver);
        checkoutPage = new CheckoutPage(driver);
        paymentPage = new PaymentPage(driver);
        reviewOrderPage = new ReviewOrderPage(driver);
        orderCompletePage = new OrderCompletePage(driver);
    }

    @Test
    @Tag("regression")
    public void guestCanCompleteFullCheckoutFlow() {
        // 1) Catalog
        assertTrue(catalogPage.isAtCatalogPage());

        int index = getStableProductIndex();
        catalogPage.scrollToProduct(index);

        String expectedName = catalogPage.getProductName(index);
        String expectedPrice = catalogPage.getProductPrice(index);

        catalogPage.openProductDetails(index);

        // 2) Product Details
        assertTrue(productDetailsPage.isAtProductDetailsPage());
        assertEquals(expectedName, productDetailsPage.getTitle());
        assertEquals(expectedPrice, productDetailsPage.getPrice());

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        // 3) Cart
        assertTrue(cartPage.isCartNotEmpty());
        assertTrue(cartPage.containsProduct(expectedName));
        assertEquals(expectedPrice, cartPage.getItemPrice(0));

        // 4) Proceed → Login
        cartPage.proceedToCheckout();
        assertTrue(loginPage.isAtLoginPage());
        loginPage.loginWithValidCredentials();

        // 5) Checkout Page
        assertTrue(checkoutPage.isAtCheckoutPage());
        CheckoutData checkoutData = CheckoutDataFactory.createValidCheckoutData();
        checkoutPage.fillShippingAddress(checkoutData);
        checkoutPage.goToPayment();

        // 6) Payment Page
        assertTrue(paymentPage.isAtPaymentPage());
        PaymentData paymentData = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(paymentData);
        paymentPage.reviewOrder();

        // 7) Review Order Page
        assertTrue(reviewOrderPage.isAtReviewOrderPage());
        assertAll("Review order validation",
                () -> assertEquals(1, reviewOrderPage.getProductsCount()),
                () -> assertEquals(expectedName, reviewOrderPage.getProductName(0)),
                () -> assertEquals(expectedPrice, reviewOrderPage.getProductPrice(0))
        );

        reviewOrderPage.placeOrder();

        // 8) Order Complete Page
        assertTrue(orderCompletePage.isAtOrderCompletePage());
        assertTrue(orderCompletePage.getHeaderText().contains("Checkout Complete"));
        assertTrue(orderCompletePage.getThankYouMessage().contains("Thank you"));
    }

    @Test
    @Tag("regression")
    public void guestCanCompleteCheckoutFlowWithMultipleProducts() {

        // 1) Catalog
        assertTrue(catalogPage.isAtCatalogPage());

        int[] indices = getTwoStableProductIndices();
        int first = indices[0];
        int second = indices[1];

        // --- First product ---
        catalogPage.scrollToProduct(first);
        String firstName = catalogPage.getProductName(first);
        String firstPrice = catalogPage.getProductPrice(first);
        catalogPage.openProductDetails(first);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        assertEquals(firstName, productDetailsPage.getTitle());

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openMenu();

        // Back to catalog
        MenuComponent menu = new MenuComponent(driver);
        menu.openCatalog();
        assertTrue(catalogPage.isAtCatalogPage());

        // --- Second product ---
        catalogPage.scrollToProduct(second);
        String secondName = catalogPage.getProductName(second);
        String secondPrice = catalogPage.getProductPrice(second);
        catalogPage.openProductDetails(second);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        assertEquals(secondName, productDetailsPage.getTitle());

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        // 2) Cart
        assertTrue(cartPage.isCartNotEmpty());
        assertAll("Cart content validation",
                () -> assertEquals(2, cartPage.getItemCount()),
                () -> assertTrue(cartPage.containsProduct(firstName)),
                () -> assertTrue(cartPage.containsProduct(secondName))
        );

        // 3) Proceed → Login
        cartPage.proceedToCheckout();
        assertTrue(loginPage.isAtLoginPage());
        loginPage.loginWithValidCredentials();

        // 4) Checkout Page
        assertTrue(checkoutPage.isAtCheckoutPage());
        CheckoutData checkoutData = CheckoutDataFactory.createValidCheckoutData();
        checkoutPage.fillShippingAddress(checkoutData);
        checkoutPage.goToPayment();

        // 5) Payment Page
        assertTrue(paymentPage.isAtPaymentPage());
        PaymentData paymentData = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(paymentData);
        paymentPage.reviewOrder();

        // 6) Review Order Page
        assertTrue(reviewOrderPage.isAtReviewOrderPage());
        assertEquals(2, reviewOrderPage.getProductsCount());

        // Validate both products
        assertAll("Review order validation",
                () -> assertTrue(reviewOrderPage.getAllProductNames().contains(firstName)),
                () -> assertTrue(reviewOrderPage.getAllProductNames().contains(secondName)),
                () -> assertTrue(reviewOrderPage.getAllProductPrices().contains(firstPrice)),
                () -> assertTrue(reviewOrderPage.getAllProductPrices().contains(secondPrice))
        );

        // Validate total price
        BigDecimal deliveryPrice = parsePrice(reviewOrderPage.getDeliveryPrice());
        BigDecimal expectedTotal = parsePrice(firstPrice).add(parsePrice(secondPrice)).add(deliveryPrice);
        BigDecimal actualTotal = parsePrice(reviewOrderPage.getTotalPrice());
        assertEquals(expectedTotal, actualTotal);

        reviewOrderPage.placeOrder();

        // 7) Order Complete Page
        assertTrue(orderCompletePage.isAtOrderCompletePage());
        assertTrue(orderCompletePage.getHeaderText().contains("Checkout Complete"));
    }
}
