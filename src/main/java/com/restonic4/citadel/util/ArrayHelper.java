package com.restonic4.citadel.util;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ArrayHelper {
    private static final Map<ArrayKey, Object[]> arrayPool = new HashMap<>();

    public static <T> T[] nullifyWithFixedSize(T[] array, int size) {
        @SuppressWarnings("unchecked")
        T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), size);

        System.arraycopy(array, 0, newArray, 0, Math.min(array.length, size));

        return newArray;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] nullifyWithFixedSize_GC_Optimized(T[] array, int size) {
        Class<?> componentType = array.getClass().getComponentType();

        ArrayKey key = new ArrayKey(componentType, size);

        Object[] newArray = arrayPool.get(key);
        if (newArray == null) {
            newArray = (T[]) Array.newInstance(componentType, size);
            arrayPool.put(key, newArray);
        }

        System.arraycopy(array, 0, newArray, 0, Math.min(array.length, size));

        return (T[]) newArray;
    }

    private static class ArrayKey {
        private final Class<?> type;
        private final int size;

        public ArrayKey(Class<?> type, int size) {
            this.type = type;
            this.size = size;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ArrayKey arrayKey = (ArrayKey) o;
            return size == arrayKey.size && type.equals(arrayKey.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, size);
        }
    }
}
