package tests;

import factories.CheckoutDataFactory;
import factories.PaymentDataFactory;
import models.CheckoutData;
import models.PaymentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.*;

import static org.junit.jupiter.api.Assertions.*;
import static utils.ProductSelectionHelper.getStableProductIndex;

@Tag("order")
@Tag("regression")
public class OrderCompletePageTests extends BaseTest {
    private CatalogPage catalogPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;
    private LoginPage loginPage;
    private CheckoutPage checkoutPage;
    private PaymentPage paymentPage;
    private ReviewOrderPage reviewOrderPage;
    private OrderCompletePage orderCompletePage;

    @BeforeEach
    public void navigateToOrderCompletePage() {
        catalogPage = new CatalogPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);
        loginPage = new LoginPage(driver);
        checkoutPage = new CheckoutPage(driver);
        paymentPage = new PaymentPage(driver);
        reviewOrderPage = new ReviewOrderPage(driver);
        orderCompletePage = new OrderCompletePage(driver);

        assertTrue(catalogPage.isAtCatalogPage(), "Catalog page is not visible");

        // Add one product
        int index = getStableProductIndex();
        catalogPage.scrollToProduct(index);
        catalogPage.openProductDetails(index);
        assertTrue(productDetailsPage.isAtProductDetailsPage());

        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();
        assertTrue(cartPage.isCartNotEmpty());

        // Checkout
        cartPage.proceedToCheckout();
        assertTrue(loginPage.isAtLoginPage());
        loginPage.loginWithValidCredentials();

        CheckoutData checkoutData = CheckoutDataFactory.createValidCheckoutData();
        checkoutPage.fillShippingAddress(checkoutData);
        checkoutPage.goToPayment();
        assertTrue(paymentPage.isAtPaymentPage());

        PaymentData paymentData = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(paymentData);
        paymentPage.reviewOrder();
        assertTrue(reviewOrderPage.isAtReviewOrderPage());

        // Place order
        reviewOrderPage.placeOrder();
        assertTrue(orderCompletePage.isAtOrderCompletePage());
    }

    @Test
    public void orderCompletePageDisplaysCorrectTexts() {
        String headerText = orderCompletePage.getHeaderText();
        String thankYouMessage = orderCompletePage.getThankYouMessage();
        String descriptionText = orderCompletePage.getOrderDescriptionMessage();

        assertAll("Order Complete Page text validation",
                () -> assertEquals("Checkout Complete", headerText),
                () -> assertEquals("Thank you for your order", thankYouMessage),
                () -> assertFalse(descriptionText.isEmpty(), "Order description should not be empty"),
                () -> assertTrue(descriptionText.contains("order"), "Order description should contain keyword 'order'")
        );
    }

    @Test
    public void continueShoppingButtonNavigatesBackToCatalog() {
        orderCompletePage.continueShopping();
        assertTrue(catalogPage.isAtCatalogPage());

        catalogPage.navBar().openCart();
        assertTrue(cartPage.isCartEmpty());
    }

    @Test
    public void orderCompletePageLayoutIsStable() {
        assertAll("Order Complete Page layout stability",
                () -> assertTrue(orderCompletePage.getHeaderText().startsWith("Checkout")),
                () -> assertTrue(orderCompletePage.getThankYouMessage().startsWith("Thank")),
                () -> assertTrue(orderCompletePage.getOrderDescriptionMessage().length() > 10)
        );
    }
}
