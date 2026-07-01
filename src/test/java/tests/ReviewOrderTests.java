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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static utils.PriceUtils.parsePrice;
import static utils.ProductSelectionHelper.getStableProductIndex;
import static utils.ProductSelectionHelper.getTwoStableProductIndices;

@Tag("order")
@Tag("e2e")
public class ReviewOrderTests extends BaseTest {
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
    @Tag("smoke")
    public void reviewOrderPageLoadsCorrectly() {
        // 1) Add product
        assertTrue(catalogPage.isAtCatalogPage());

        int index = getStableProductIndex();
        catalogPage.scrollToProduct(index);
        catalogPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        // 2) Checkout
        assertTrue(cartPage.isCartNotEmpty());
        cartPage.proceedToCheckout();

        assertTrue(loginPage.isAtLoginPage());
        loginPage.loginWithValidCredentials();

        // 3) Shipping
        assertTrue(checkoutPage.isAtCheckoutPage());
        CheckoutData checkoutData = CheckoutDataFactory.createValidCheckoutData();
        checkoutPage.fillShippingAddress(checkoutData);
        checkoutPage.goToPayment();

        // 4) Payment
        assertTrue(paymentPage.isAtPaymentPage());
        PaymentData paymentData = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(paymentData);
        paymentPage.reviewOrder();

        // 5) Review Order Page
        assertTrue(reviewOrderPage.isAtReviewOrderPage());
        assertAll("Order validation",
                () -> assertTrue(reviewOrderPage.getProductsCount() > 0),
                () -> assertTrue(reviewOrderPage.isDeliveryAddressVisible()),
                () -> assertTrue(reviewOrderPage.isPaymentDetailsVisible()),
                () -> assertNotNull(reviewOrderPage.getDeliveryInfo()),
                () -> assertNotNull(reviewOrderPage.getDeliveryPrice()),
                () -> assertNotNull(reviewOrderPage.getItemsCountText()),
                () -> assertNotNull(reviewOrderPage.getTotalPrice()),
                () -> assertTrue(reviewOrderPage.isPlaceOrderButtonVisible())
        );
    }

    @Test
    @Tag("regression")
    public void reviewOrderPageDisplaysCorrectProducts() {
        assertTrue(catalogPage.isAtCatalogPage());

        // Select two stable products
        int[] indices = getTwoStableProductIndices();
        int first = indices[0];
        int second = indices[1];

        // --- First product ---
        catalogPage.scrollToProduct(first);
        String firstName = catalogPage.getProductName(first);
        String firstPrice = catalogPage.getProductPrice(first);
        catalogPage.openProductDetails(first);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();

        // Back to catalog
        productDetailsPage.navBar().openMenu();
        new MenuComponent(driver).openCatalog();
        assertTrue(catalogPage.isAtCatalogPage());

        // --- Second product ---
        catalogPage.scrollToProduct(second);
        String secondName = catalogPage.getProductName(second);
        String secondPrice = catalogPage.getProductPrice(second);
        catalogPage.openProductDetails(second);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        // Cart
        assertTrue(cartPage.isCartNotEmpty());
        assertEquals(2, cartPage.getItemCount());

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

        // Review Order Page
        assertTrue(reviewOrderPage.isAtReviewOrderPage());

        // Validate product count
        assertEquals(2, reviewOrderPage.getProductsCount());
        assertEquals(2, reviewOrderPage.getTotalItemsCount());

        // Validate products names and prices
        List<String> names = reviewOrderPage.getAllProductNames();
        List<String> prices = reviewOrderPage.getAllProductPrices();

        assertAll("Product names and prices validation",
                () -> assertTrue(names.contains(firstName)),
                () -> assertTrue(names.contains(secondName)),
                () -> assertTrue(prices.contains(firstPrice)),
                () -> assertTrue(prices.contains(secondPrice))
        );
    }

    @Test
    @Tag("regression")
    public void reviewOrderPageDisplaysCorrectDeliveryAddress() {
        // 1) Add product
        assertTrue(catalogPage.isAtCatalogPage());

        int index = getStableProductIndex();
        catalogPage.scrollToProduct(index);
        catalogPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        // 2) Checkout
        assertTrue(cartPage.isCartNotEmpty());
        cartPage.proceedToCheckout();

        assertTrue(loginPage.isAtLoginPage());
        loginPage.loginWithValidCredentials();

        // 3) Shipping
        assertTrue(checkoutPage.isAtCheckoutPage());
        CheckoutData checkoutData = CheckoutDataFactory.createValidCheckoutData();
        checkoutPage.fillShippingAddress(checkoutData);
        checkoutPage.goToPayment();

        // 4) Payment
        assertTrue(paymentPage.isAtPaymentPage());
        PaymentData paymentData = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(paymentData);
        paymentPage.reviewOrder();

        // 5) Review Order Page
        assertTrue(reviewOrderPage.isAtReviewOrderPage());
        assertAll("Order validation",
                () -> assertTrue(reviewOrderPage.isDeliveryAddressVisible()),
                () -> assertTrue(reviewOrderPage.isDeliveryAddressTextVisible()),
                () -> assertTrue(reviewOrderPage.isPaymentDetailsVisible()),
                () -> assertTrue(reviewOrderPage.isBillingAddressMessageVisible())
        );

        // Actual UI values
        String actualFullName = reviewOrderPage.getFullName();
        String actualAddress1 = reviewOrderPage.getAddressLine();
        String actualCityAndState = reviewOrderPage.getCityAndState();
        String actualCountryAndZipCode = reviewOrderPage.getCountryAndZip();

        // Expected values (assembled from model)
        String expectedFullName = checkoutData.fullName;
        String expectedAddress1 = checkoutData.address;
        String expectedCityAndState = checkoutData.city + ", " + checkoutData.state;
        String expectedCountryAndZipCode = checkoutData.country + ", " + checkoutData.zipCode;

        assertAll("Delivery Address validation",
                () -> assertEquals(expectedFullName, actualFullName),
                () -> assertEquals(expectedAddress1, actualAddress1),
                () -> assertEquals(expectedCityAndState, actualCityAndState),
                () -> assertEquals(expectedCountryAndZipCode, actualCountryAndZipCode)
        );
    }

    @Test
    @Tag("regression")
    public void reviewOrderPageDisplaysCorrectDeliveryAndBillingAddressesWhenAreDifferent() {
        // 1) Add product
        assertTrue(catalogPage.isAtCatalogPage());

        int index = getStableProductIndex();
        catalogPage.scrollToProduct(index);
        catalogPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        // 2) Checkout
        assertTrue(cartPage.isCartNotEmpty());
        cartPage.proceedToCheckout();
        assertTrue(loginPage.isAtLoginPage());
        loginPage.loginWithValidCredentials();

        // 3) Shipping address
        CheckoutData shippingData = CheckoutDataFactory.createValidCheckoutData();
        checkoutPage.fillShippingAddress(shippingData);
        checkoutPage.goToPayment();

        // 4) Payment
        assertTrue(paymentPage.isAtPaymentPage());
        PaymentData paymentData = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(paymentData);
        paymentPage.uncheckBillingAddress(); // Uncheck billing same as shipping

        // 5) Billing address
        CheckoutData billingData = CheckoutDataFactory.createValidCheckoutData();
        paymentPage.fillBillingAddress(billingData);
        paymentPage.reviewOrder();

        // 6) Review Order Page
        assertTrue(reviewOrderPage.isAtReviewOrderPage());

        // Delivery Address (should match shippingData, without address2)
        assertTrue(reviewOrderPage.isDeliveryAddressVisible());
        assertTrue(reviewOrderPage.isDeliveryAddressTextVisible());

        String expectedFullName = shippingData.fullName;
        String expectedAddress = shippingData.address;
        String expectedCityAndState = shippingData.city + ", " + shippingData.state;
        String expectedCountryAndZipCode = shippingData.country + ", " + shippingData.zipCode;

        assertAll("Delivery Address validation",
                () -> assertEquals(expectedFullName, reviewOrderPage.getFullName()),
                () -> assertEquals(expectedAddress, reviewOrderPage.getAddressLine()),
                () -> assertEquals(expectedCityAndState, reviewOrderPage.getCityAndState()),
                () -> assertEquals(expectedCountryAndZipCode, reviewOrderPage.getCountryAndZip())
        );

        // Billing Address (should match billingData, including address2)
        assertTrue(reviewOrderPage.isBillingAddressTextVisible());
        String expectedBillingFullName = billingData.fullName;
        String expectedBillingAddress = billingData.address + "," + billingData.address2;
        String expectedBillingCityAndState = billingData.city + "," + billingData.state;
        String expectedBillingZipCodeAndCountry = billingData.zipCode + "," + billingData.country;

        assertAll("Billing Address validation",
                () -> assertEquals(expectedBillingFullName, reviewOrderPage.getBillingFullName()),
                () -> assertEquals(expectedBillingAddress, reviewOrderPage.getBillingAddress()),
                () -> assertEquals(expectedBillingCityAndState, reviewOrderPage.getBillingCityAndState()),
                () -> assertEquals(expectedBillingZipCodeAndCountry, reviewOrderPage.getBillingZipCodeAndCountry())
        );
    }

    @Test
    @Tag("regression")
    public void reviewOrderPageDisplaysCorrectPaymentDetails() {
        // 1) Add product
        assertTrue(catalogPage.isAtCatalogPage());

        int index = getStableProductIndex();
        catalogPage.scrollToProduct(index);
        catalogPage.openProductDetails(index);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        // 2) Checkout
        assertTrue(cartPage.isCartNotEmpty());
        cartPage.proceedToCheckout();

        assertTrue(loginPage.isAtLoginPage());
        loginPage.loginWithValidCredentials();

        // 3) Shipping
        CheckoutData shippingData = CheckoutDataFactory.createValidCheckoutData();
        checkoutPage.fillShippingAddress(shippingData);
        checkoutPage.goToPayment();

        // 4) Payment
        assertTrue(paymentPage.isAtPaymentPage());
        PaymentData paymentData = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(paymentData);
        paymentPage.reviewOrder();

        // 5) Review Order Page
        assertTrue(reviewOrderPage.isAtReviewOrderPage());
        assertTrue(reviewOrderPage.isPaymentMethodTextVisible());

        // Validate payment details
        String expectedCardNumber = paymentData.cardNumber.replace("-", "");
        String actualCardNumber = reviewOrderPage.getCardNumber().replace(" ", "");

        assertAll("Payment section validation",
                () -> assertEquals(paymentData.fullName, reviewOrderPage.getCardHolderName()),
                () -> assertEquals(expectedCardNumber, actualCardNumber),
                () -> assertEquals("Exp: " + paymentData.expirationDate, reviewOrderPage.getExpirationDate()),
                () -> assertTrue(reviewOrderPage.isBillingAddressMessageVisible())
                // Validate billing message (only when billing = shipping address)
        );
    }

    @Test
    @Tag("regression")
    public void reviewOrderPageDisplaysCorrectTotals() {
        assertTrue(catalogPage.isAtCatalogPage());

        // Select two stable products
        int[] indices = getTwoStableProductIndices();
        int first = indices[0];
        int second = indices[1];

        // --- First product ---
        catalogPage.scrollToProduct(first);
        String firstPriceText = catalogPage.getProductPrice(first);
        BigDecimal firstPrice = parsePrice(firstPriceText);
        catalogPage.openProductDetails(first);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();

        // Back to catalog
        productDetailsPage.navBar().openMenu();
        new MenuComponent(driver).openCatalog();
        assertTrue(catalogPage.isAtCatalogPage());

        // --- Second product ---
        catalogPage.scrollToProduct(second);
        String secondPriceText = catalogPage.getProductPrice(second);
        BigDecimal secondPrice = parsePrice(secondPriceText);
        catalogPage.openProductDetails(second);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        // Cart
        assertTrue(cartPage.isCartNotEmpty());
        assertEquals(2, cartPage.getItemCount());

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

        // Review Order Page
        assertTrue(reviewOrderPage.isAtReviewOrderPage());

        // Delivery price
        BigDecimal deliveryPrice = parsePrice(reviewOrderPage.getDeliveryPrice());
        BigDecimal expectedTotalPrice = firstPrice.add(secondPrice).add(deliveryPrice);
        // Total price from UI
        BigDecimal actualTotalPrice = parsePrice(reviewOrderPage.getTotalPrice());

        assertAll("Totals validation",
                () -> assertEquals(new BigDecimal("5.99"), deliveryPrice),
                () -> assertEquals(expectedTotalPrice, actualTotalPrice),
                () -> assertEquals(2, reviewOrderPage.getTotalItemsCount()),
                () -> assertEquals("DHL Standard Delivery", reviewOrderPage.getDeliveryInfo())
        );
    }

    @Test
    @Tag("regression")
    public void userCanPlaceOrderSuccessfully() {
        assertTrue(catalogPage.isAtCatalogPage());

        // Select two stable products
        int[] indices = getTwoStableProductIndices();
        int first = indices[0];
        int second = indices[1];

        // --- First product ---
        catalogPage.scrollToProduct(first);
        String firstName = catalogPage.getProductName(first);
        String firstPriceText = catalogPage.getProductPrice(first);
        catalogPage.openProductDetails(first);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();

        // Back to catalog
        productDetailsPage.navBar().openMenu();
        new MenuComponent(driver).openCatalog();
        assertTrue(catalogPage.isAtCatalogPage());

        // --- Second product ---
        catalogPage.scrollToProduct(second);
        String secondName = catalogPage.getProductName(second);
        String secondPriceText = catalogPage.getProductPrice(second);
        catalogPage.openProductDetails(second);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        // Cart
        assertTrue(cartPage.isCartNotEmpty());
        assertAll("Cart content validation",
                () -> assertEquals(2, cartPage.getItemCount()),
                () -> assertTrue(cartPage.containsProduct(firstName)),
                () -> assertTrue(cartPage.containsProduct(secondName))
        );

        // Checkout
        cartPage.proceedToCheckout();
        assertTrue(loginPage.isAtLoginPage());
        loginPage.loginWithValidCredentials();

        CheckoutData checkoutData = CheckoutDataFactory.createValidCheckoutData();
        checkoutPage.fillShippingAddress(checkoutData);
        checkoutPage.goToPayment();

        PaymentData paymentData = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(paymentData);
        paymentPage.reviewOrder();

        // Review Order Page
        assertTrue(reviewOrderPage.isAtReviewOrderPage());

        // Validate product names before placing order
        List<String> productNames = reviewOrderPage.getAllProductNames();
        List<String> productPrices = reviewOrderPage.getAllProductPrices();

        BigDecimal expectedFirstPrice = parsePrice(firstPriceText);
        BigDecimal expectedSecondPrice = parsePrice(secondPriceText);

        BigDecimal actualFirstPrice = parsePrice(productPrices.get(0));
        BigDecimal actualSecondPrice = parsePrice(productPrices.get(1));

        assertAll("Product names and prices validation",
                () -> assertTrue(productNames.contains(firstName), "First product name mismatch"),
                () -> assertTrue(productNames.contains(secondName), "Second product name mismatch"),
                () -> assertEquals(expectedFirstPrice, actualFirstPrice, "First product price mismatch"),
                () -> assertEquals(expectedSecondPrice, actualSecondPrice, "Second product price mismatch")
        );

        // Place order
        reviewOrderPage.placeOrder();

        // Order Complete Page
        assertTrue(orderCompletePage.isAtOrderCompletePage());
    }
}
