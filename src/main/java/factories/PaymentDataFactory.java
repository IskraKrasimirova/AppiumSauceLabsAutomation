package factories;

import models.PaymentData;
import net.datafaker.Faker;

public class PaymentDataFactory {
    private static final Faker faker = new Faker();

    public static PaymentData createValidPaymentData() {
        PaymentData data = new PaymentData();

        data.fullName = faker.name().fullName();
        data.cardNumber = faker.finance().creditCard();
        data.expirationDate = faker.business().creditCardExpiry(); // format MM/YY
        data.securityCode = faker.number().digits(3);
        data.billingSameAsShipping = true;

        return data;
    }
}
