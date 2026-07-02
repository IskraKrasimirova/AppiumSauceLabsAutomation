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
public class PaymentBillingTests extends BaseTest {
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
        assertTrue(catalogPage.isAtCatalogPage(), "Catalog page is not visible");
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
    public void paymentBillingWithValidDataSuccessfullyNavigatesToReviewOrder() {
        // Fill payment fields
        PaymentData data = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(data);

        // Uncheck checkbox
        paymentPage.uncheckBillingAddress();

        assertTrue(paymentPage.isBillingSectionVisible());

        // Fill billing fields (with scroll)
        CheckoutData billingData = CheckoutDataFactory.createValidCheckoutData();
        paymentPage.fillBillingAddress(billingData);

        // Continue
        paymentPage.reviewOrder();

        assertTrue(reviewOrderPage.isAtReviewOrderPage());
    }

    @Test
    @Tag("regression")
    @Tag("validation")
    public void paymentBillingShowsErrorsWhenFieldsAreEmpty() {
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
                () -> assertEquals(CheckoutSection.FULL_NAME_ERROR_MESSAGE, paymentPage.billingSection().getFullNameErrorMessage()),
                () -> assertEquals(CheckoutSection.ADDRESS_ERROR_MESSAGE, paymentPage.billingSection().getAddressErrorMessage()),
                () -> assertEquals(CheckoutSection.CITY_ERROR_MESSAGE, paymentPage.billingSection().getCityErrorMessage()),
                () -> assertEquals(CheckoutSection.ZIP_CODE_ERROR_MESSAGE, paymentPage.billingSection().getZipErrorMessage()),
                () -> assertEquals(CheckoutSection.COUNTRY_ERROR_MESSAGE, paymentPage.billingSection().getCountryErrorMessage())
        );
    }

    @ParameterizedTest(name = "Billing missing field: {0}")
    @MethodSource("billingMandatoryFields")
    @Tag("validation")
    @Tag("regression")
    public void paymentBillingShowsErrorForMissingMandatoryField(String fieldName, String expectedError) {
        PaymentData paymentData = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(paymentData);

        paymentPage.uncheckBillingAddress();
        assertTrue(paymentPage.isBillingSectionVisible());

        CheckoutData billingData = CheckoutDataFactory.createWithMissingField(fieldName);

        paymentPage.fillBillingAddress(billingData);
        paymentPage.reviewOrder();

        assertEquals(expectedError, paymentPage.billingSection().getErrorMessage(fieldName));
        assertTrue(paymentPage.billingSection().isErrorSymbolVisible(fieldName));
    }

    private static Stream<Arguments> billingMandatoryFields() {
        return Stream.of(
                Arguments.of("fullName", CheckoutSection.FULL_NAME_ERROR_MESSAGE),
                Arguments.of("address", CheckoutSection.ADDRESS_ERROR_MESSAGE),
                Arguments.of("city", CheckoutSection.CITY_ERROR_MESSAGE),
                Arguments.of("zipCode", CheckoutSection.ZIP_CODE_ERROR_MESSAGE),
                Arguments.of("country", CheckoutSection.COUNTRY_ERROR_MESSAGE)
        );
    }

    @ParameterizedTest(name = "Billing field {0} accepts value: \"{1}\"")
    @MethodSource("validBillingWeirdValues")
    @Tag("regression")
    @Tag("validation")
    public void paymentBillingAcceptsWeirdValues(String field, String value) {

        PaymentData paymentData = PaymentDataFactory.createValidPaymentData();
        paymentPage.fillPaymentData(paymentData);

        paymentPage.uncheckBillingAddress();
        assertTrue(paymentPage.isBillingSectionVisible());

        CheckoutData billingData = CheckoutDataFactory.createWithCustomValue(field, value);

        paymentPage.fillBillingAddress(billingData);
        paymentPage.reviewOrder();

        assertTrue(reviewOrderPage.isAtReviewOrderPage());
    }

    private static Stream<Arguments> validBillingWeirdValues() {
        return Stream.of(
                Arguments.of("fullName", "a"),
                Arguments.of("fullName", "1"),
                Arguments.of("fullName", "@"),
                Arguments.of("fullName", "abc123!@#"),

                Arguments.of("address", "a"),
                Arguments.of("address", "1"),
                Arguments.of("address", "@"),
                Arguments.of("address", "abc--1@-/$&*'"),

                Arguments.of("city", "a"),
                Arguments.of("city", "1"),
                Arguments.of("city", "@"),
                Arguments.of("city", "a@ 1.com-&*!?  B"),

                Arguments.of("zipCode", "1"),
                Arguments.of("zipCode", "00000"),
                Arguments.of("zipCode", "@"),
                Arguments.of("zipCode", "a-b--1 @#$%^&*()!  O"),

                Arguments.of("country", "a"),
                Arguments.of("country", "1"),
                Arguments.of("country", "@"),
                Arguments.of("country", "aB--  123-!?&%@#")
        );
    }
}
