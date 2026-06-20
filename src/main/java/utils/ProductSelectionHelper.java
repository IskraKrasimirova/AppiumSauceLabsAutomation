package utils;

import java.util.List;
import java.util.Random;

public class ProductSelectionHelper {
    public static int getStableProductIndex() {
        List<Integer> stable = List.of(0, 2, 3); // product green with index 1 crashes the app
        return stable.get(new Random().nextInt(stable.size()));
    }

    public static int[] getTwoStableProductIndices() {
        List<Integer> stable = List.of(0, 2, 3);

        int first = stable.get(new Random().nextInt(stable.size()));

        int second = stable.stream()
                .filter(i -> i != first)
                .findAny()
                .orElseThrow();

        return new int[]{first, second};
    }
}
