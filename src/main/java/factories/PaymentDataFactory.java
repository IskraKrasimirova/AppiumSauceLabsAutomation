package factories;

import models.PaymentData;
import net.datafaker.Faker;

import java.time.LocalDate;

public class PaymentDataFactory {
    private static final Faker faker = new Faker();

    public static PaymentData createValidPaymentData() {
        PaymentData data = new PaymentData();
        int month = faker.number().numberBetween(1, 12);
        int year = LocalDate.now().getYear() + faker.number().numberBetween(1, 5);

        String expDate = String.format("%02d/%02d", month, year % 100);

        data.fullName = faker.name().fullName();
        data.cardNumber = faker.finance().creditCard();
        //data.expirationDate = faker.business().creditCardExpiry(); // format MM/YY
        data.expirationDate = expDate;
        data.securityCode = faker.number().digits(3);
        data.billingSameAsShipping = true;

        return data;
    }
}
