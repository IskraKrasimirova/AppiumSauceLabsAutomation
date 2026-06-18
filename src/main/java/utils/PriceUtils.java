package utils;

import java.math.BigDecimal;

public class PriceUtils {
    public static BigDecimal parsePrice(String price) {
        String number = price.replaceAll("[^0-9.]", "");
        return new BigDecimal(number);
    }
}
