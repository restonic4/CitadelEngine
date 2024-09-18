package com.restonic4.citadel.util.math;

import java.util.List;
import java.util.Random;

public class RandomUtil {
    private static final Random RANDOM = new Random();

    public static float randomTiny() {
        return RANDOM.nextFloat();
    }

    public static int random(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value");
        }

        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public static <T> T getRandom(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("The list cannot be null or empty");
        }
        int index = RANDOM.nextInt(list.size());
        return list.get(index);
    }

    public static <T> T getRandom(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("The array cannot be null or empty");
        }
        int index = RANDOM.nextInt(array.length);
        return array[index];
    }
}
