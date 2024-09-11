package me.restonic4.citadel.util;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class ArrayHelper {
    private static final Map<Integer, Object[]> arrayPool = new HashMap<>();

    public static <T> T[] nullifyWithFixedSize(T[] array, int size) {
        @SuppressWarnings("unchecked")
        T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), size);

        System.arraycopy(array, 0, newArray, 0, Math.min(array.length, size));

        return newArray;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] nullifyWithFixedSize_GC_Optimized(T[] array, int size) {
        Class<?> componentType = array.getClass().getComponentType();

        Object[] newArray = arrayPool.get(size);
        if (newArray == null || !componentType.isInstance(newArray)) {
            newArray = (T[]) Array.newInstance(componentType, size);
        }

        System.arraycopy(array, 0, newArray, 0, Math.min(array.length, size));

        arrayPool.put(size, newArray);

        return (T[]) newArray;
    }
}
