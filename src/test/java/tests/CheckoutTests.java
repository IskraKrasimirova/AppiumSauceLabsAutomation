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
        super.setUp();

        catalogPage = new CatalogPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        loginPage = new LoginPage(driver);

        // Navigate to checkout
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
    }

    @Test
    @Tag("validation")
    @Tag("regression")
    public void checkoutShowsAllErrorMessagesWhenFieldsAreEmpty() {
        checkoutPage.goToPayment();

        assertAll("All validation errors",
                () -> assertEquals(CheckoutPage.FULL_NAME_ERROR_MESSAGE, checkoutPage.getFullNameErrorMessage()),
                () -> assertEquals(CheckoutPage.ADDRESS_ERROR_MESSAGE, checkoutPage.getAddressErrorMessage()),
                () -> assertEquals(CheckoutPage.CITY_ERROR_MESSAGE, checkoutPage.getCityErrorMessage()),
                () -> assertEquals(CheckoutPage.ZIP_CODE_ERROR_MESSAGE, checkoutPage.getZipErrorMessage()),
                () -> assertEquals(CheckoutPage.COUNTRY_ERROR_MESSAGE, checkoutPage.getCountryErrorMessage())
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

        assertEquals(expectedError, checkoutPage.getErrorMessage(fieldName));
        assertTrue(checkoutPage.isErrorSymbolVisible(fieldName));
    }

    private static Stream<Arguments> mandatoryFieldsAndErrorMessages() {
        return Stream.of(
                Arguments.of("fullName", CheckoutPage.FULL_NAME_ERROR_MESSAGE),
                Arguments.of("address", CheckoutPage.ADDRESS_ERROR_MESSAGE),
                Arguments.of("city", CheckoutPage.CITY_ERROR_MESSAGE),
                Arguments.of("zipCode", CheckoutPage.ZIP_CODE_ERROR_MESSAGE),
                Arguments.of("country", CheckoutPage.COUNTRY_ERROR_MESSAGE)
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
}
