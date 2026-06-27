package tests;

import factories.CheckoutDataFactory;
import factories.PaymentDataFactory;
import models.CheckoutData;
import models.PaymentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("payment")
public class PaymentTests extends BaseTest{
    private CatalogPage catalogPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private LoginPage loginPage;
    private PaymentPage paymentPage;
    private ReviewOrderPage reviewOrderPage;

    @BeforeEach
    public void setUpPayment() {
        catalogPage = new CatalogPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        loginPage = new LoginPage(driver);
        paymentPage = new PaymentPage(driver);
        reviewOrderPage = new ReviewOrderPage(driver);

        // Navigate to payment
        assertTrue(catalogPage.isAtCatalogPage());
        catalogPage.scrollToProduct(0);
        catalogPage.openProductDetails(0);

        assertTrue(productDetailsPage.isAtProductDetailsPage());
        productDetailsPage.selectColor();
        productDetailsPage.addToCart();
        productDetailsPage.navBar().openCart();

        assertTrue(cartPage.isCartNotEmpty());
        cartPage.proceedToCheckout();

        // User should log in first and then go to checkout
        assertTrue(loginPage.isAtLoginPage());
        loginPage.loginWithValidCredentials();

        assertTrue(checkoutPage.isAtCheckoutPage());
        CheckoutData data = CheckoutDataFactory.createValidCheckoutData();
        checkoutPage.fillShippingAddress(data);
        checkoutPage.goToPayment();
        assertTrue(paymentPage.isAtPaymentPage());
    }

    @Test
    @Tag("regression")
    @Tag("validation")
    public void paymentShowsAllErrorsWhenFieldsAreEmpty() {
        paymentPage.reviewOrder();

        assertAll("All payment validation errors",
                () -> assertEquals(PaymentPage.INVALID_VALUE_ERROR, paymentPage.getFullNameErrorMessage()),
                () -> assertTrue(paymentPage.isErrorSymbolVisible("cardNumber")),
                () -> assertEquals(PaymentPage.INVALID_VALUE_ERROR, paymentPage.getExpirationDateErrorMessage()),
                () -> assertEquals(PaymentPage.INVALID_VALUE_ERROR, paymentPage.getSecurityCodeErrorMessage())
        );
    }

    @ParameterizedTest(name = "Missing field: {0}")
    @MethodSource("mandatoryPaymentFields")
    @Tag("regression")
    @Tag("validation")
    public void paymentShowsErrorForMissingField(String fieldName, boolean hasTextError) {

        PaymentData data = PaymentDataFactory.createWithMissingField(fieldName);

        paymentPage.fillPaymentData(data);
        paymentPage.reviewOrder();

        if (hasTextError) {
            assertEquals(PaymentPage.INVALID_VALUE_ERROR, paymentPage.getErrorMessage(fieldName));
        }

        assertTrue(paymentPage.isErrorSymbolVisible(fieldName));
    }

    private static Stream<Arguments> mandatoryPaymentFields() {
        return Stream.of(
                Arguments.of("fullName", true),
                Arguments.of("cardNumber", false),
                Arguments.of("expirationDate", true),
                Arguments.of("securityCode", true)
        );
    }

    @Test
    @Tag("smoke")
    public void paymentWithValidDataNavigatesToReviewOrder() {
        PaymentData data = PaymentDataFactory.createValidPaymentData();

        paymentPage.fillPaymentData(data);
        paymentPage.reviewOrder();

        assertTrue(reviewOrderPage.isAtReviewOrderPage());
    }

    // Inconsistent validation especially for fields cardNumber and expirationDate
    @ParameterizedTest(name = "Field {0} accepts value: \"{1}\"")
    @MethodSource("validWeirdValues")
    @Tag("regression")
    @Tag("validation")
    public void paymentAcceptsSomeNonEmptyValue(String field, String value) {
        PaymentData data = PaymentDataFactory.createWithCustomValue(field, value);

        paymentPage.fillPaymentData(data);
        paymentPage.reviewOrder();

        assertTrue(reviewOrderPage.isAtReviewOrderPage());
    }

    private static Stream<Arguments> validWeirdValues() {
        return Stream.of(
                Arguments.of("fullName", "a"),
                Arguments.of("fullName", "1"),
                Arguments.of("fullName", "@"),
                Arguments.of("fullName", "abc123!@#"),

                Arguments.of("cardNumber", "1"),
                Arguments.of("cardNumber", "abc--1@-/$&*'"),
                Arguments.of("cardNumber", "12345678901234567890"),

                Arguments.of("expirationDate", "1"),
                Arguments.of("expirationDate", "a@ 1.com-&*!?  B"),
                Arguments.of("expirationDate", "12/20"),
                Arguments.of("expirationDate", "12/ab"),
                Arguments.of("expirationDate", "03-20"),

                Arguments.of("securityCode", "1"),
                Arguments.of("securityCode", "00000"),
                Arguments.of("securityCode", "@"),
                Arguments.of("securityCode", "a-b--1 @#$%^&*()!  O")
        );
    }

    @ParameterizedTest(name = "Field {0} rejects value: \"{1}\"")
    @MethodSource("invalidValues")
    @Tag("regression")
    @Tag("validation")
    public void paymentRejectsInvalidValues(String field, String value) {
        PaymentData data = PaymentDataFactory.createWithCustomValue(field, value);

        paymentPage.fillPaymentData(data);
        paymentPage.reviewOrder();

        assertTrue(paymentPage.isErrorSymbolVisible(field));
    }

    private static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of("cardNumber", "a"),
                Arguments.of("cardNumber", "@"),
                Arguments.of("cardNumber", "abc--@$&"),

                Arguments.of("expirationDate", "a"),
                Arguments.of("expirationDate", "@"),
                Arguments.of("expirationDate", "33/20"),
                Arguments.of("expirationDate", "3/20"),

                Arguments.of("expirationDate", "3/2020"),
                Arguments.of("expirationDate", "3-2020"),
                Arguments.of("expirationDate", "ab-20"),
                Arguments.of("expirationDate", "ab/20")
        );
    }

    @Test
    @Tag("regression")
    @Tag("validation")
    public void paymentWithValidDataAndUncheckedBillingCheckboxWithEmptyBillingFieldsShowsErrors() {
        // Fill payment fields
        PaymentData data = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(data);

        // Uncheck checkbox
        paymentPage.uncheckBillingAddress();

        // Billing section should appear
        assertTrue(paymentPage.isBillingSectionVisible());

        // Try to continue without filling billing data
        paymentPage.reviewOrder();

        assertAll("Billing validation errors",
                () -> assertTrue(paymentPage.isBillingErrorVisible("billingFullName")),
                () -> assertTrue(paymentPage.isBillingErrorVisible("billingAddress")),
                () -> assertTrue(paymentPage.isBillingErrorVisible("billingCity")),
                () -> assertTrue(paymentPage.isBillingErrorVisible("billingZip")),
                () -> assertTrue(paymentPage.isBillingErrorVisible("billingCountry"))
        );
    }

    @Test
    @Tag("regression")
    @Tag("validation")
    public void paymentWithValidDataAndUncheckedBillingCheckboxAndBillingSectionFilledSuccessfullyNavigatesToReviewOrder() {
        // Fill payment fields
        PaymentData data = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(data);

        // Uncheck checkbox
        paymentPage.uncheckBillingAddress();

        assertTrue(paymentPage.isBillingSectionVisible());

        // Fill billing fields (with scroll)
        CheckoutData billingData = CheckoutDataFactory.createValidCheckoutData();
        paymentPage.fillShippingAddress(billingData);

        // Continue
        paymentPage.reviewOrder();

        assertTrue(reviewOrderPage.isAtReviewOrderPage());
    }
}
