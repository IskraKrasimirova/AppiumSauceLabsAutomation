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

    public static CheckoutData createWithMissingField(String fieldName) {
        CheckoutData data = createValidCheckoutData();

        switch (fieldName) {
            case "fullName":
                data.fullName = "";
                break;
            case "address":
                data.address = "";
                break;
            case "city":
                data.city = "";
                break;
            case "zipCode":
                data.zipCode = "";
                break;
            case "country":
                data.country = "";
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + fieldName);
        }

        return data;
    }

    public static CheckoutData createWithCustomValue(String field, String value){
        CheckoutData data = createValidCheckoutData();

        switch (field) {
            case "fullName":
                data.fullName = value;
                break;
            case "address":
                data.address = value;
                break;
            case "city":
                data.city = value;
                break;
            case "zipCode":
                data.zipCode = value;
                break;
            case "country":
                data.country = value;
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + field);
        }

        return data;
    }
}
