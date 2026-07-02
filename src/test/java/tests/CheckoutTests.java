package tests;

import factories.CheckoutDataFactory;
import models.CheckoutData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("checkout")
public class CheckoutTests extends BaseTest {
    private CatalogPage catalogPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private LoginPage loginPage;

    @BeforeEach
    public void setUpCheckout() {
        catalogPage = new CatalogPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        loginPage = new LoginPage(driver);

        // Navigate to checkout
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
    }

    @Test
    @Tag("validation")
    @Tag("regression")
    public void checkoutShowsAllErrorMessagesWhenFieldsAreEmpty() {
        checkoutPage.goToPayment();

        assertAll("All validation errors",
                () -> assertEquals(CheckoutSection.FULL_NAME_ERROR_MESSAGE, checkoutPage.checkoutSection().getFullNameErrorMessage()),
                () -> assertEquals(CheckoutSection.ADDRESS_ERROR_MESSAGE, checkoutPage.checkoutSection().getAddressErrorMessage()),
                () -> assertEquals(CheckoutSection.CITY_ERROR_MESSAGE, checkoutPage.checkoutSection().getCityErrorMessage()),
                () -> assertEquals(CheckoutSection.ZIP_CODE_ERROR_MESSAGE, checkoutPage.checkoutSection().getZipErrorMessage()),
                () -> assertEquals(CheckoutSection.COUNTRY_ERROR_MESSAGE, checkoutPage.checkoutSection().getCountryErrorMessage())
        );
    }

    @ParameterizedTest(name = "Missing field: {0}")
    @MethodSource("mandatoryFieldsAndErrorMessages")
    @Tag("validation")
    @Tag("regression")
    public void checkoutShowsErrorMessageForMissingMandatoryField(String fieldName, String expectedError) {
        CheckoutData data = CheckoutDataFactory.createWithMissingField(fieldName);

        checkoutPage.fillShippingAddress(data);
        checkoutPage.goToPayment();

        assertEquals(expectedError, checkoutPage.checkoutSection().getErrorMessage(fieldName));
        assertTrue(checkoutPage.checkoutSection().isErrorSymbolVisible(fieldName));
    }

    private static Stream<Arguments> mandatoryFieldsAndErrorMessages() {
        return Stream.of(
                Arguments.of("fullName", CheckoutSection.FULL_NAME_ERROR_MESSAGE),
                Arguments.of("address", CheckoutSection.ADDRESS_ERROR_MESSAGE),
                Arguments.of("city", CheckoutSection.CITY_ERROR_MESSAGE),
                Arguments.of("zipCode", CheckoutSection.ZIP_CODE_ERROR_MESSAGE),
                Arguments.of("country", CheckoutSection.COUNTRY_ERROR_MESSAGE)
        );
    }

    @Test
    @Tag("smoke")
    public void checkoutWithValidDataNavigatesToPaymentPage() {
        CheckoutData data = CheckoutDataFactory.createValidCheckoutData();

        checkoutPage.fillShippingAddress(data);
        checkoutPage.goToPayment();

        PaymentPage paymentPage = new PaymentPage(driver);
        assertTrue(paymentPage.isAtPaymentPage());
    }

    @ParameterizedTest(name = "Field {0} accepts value: \"{1}\"")
    @MethodSource("validWeirdValues")
    @Tag("regression")
    @Tag("validation")
    public void checkoutAcceptsAnyNonEmptyValue(String field, String value) {

        CheckoutData data = CheckoutDataFactory.createWithCustomValue(field, value);

        checkoutPage.fillShippingAddress(data);
        checkoutPage.goToPayment();

        PaymentPage paymentPage = new PaymentPage(driver);
        assertTrue(paymentPage.isAtPaymentPage());
    }

    private static Stream<Arguments> validWeirdValues() {
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
