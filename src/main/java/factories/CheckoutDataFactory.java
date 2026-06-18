package factories;

import models.CheckoutData;
import net.datafaker.Faker;

public class CheckoutDataFactory {
    private static final Faker faker = new Faker();

    public static CheckoutData createValidCheckoutData() {
        CheckoutData data = new CheckoutData();

        data.fullName = faker.name().fullName();
        data.address = faker.address().streetAddress();
        data.address2 = faker.address().secondaryAddress();
        data.city = faker.address().city();
        data.state = faker.address().state();
        data.zipCode = faker.address().zipCode();
        data.country = faker.address().country();

        return data;
    }
}
