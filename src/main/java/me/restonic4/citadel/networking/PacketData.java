package me.restonic4.citadel.networking;

import me.restonic4.citadel.exceptions.NetworkException;
import me.restonic4.citadel.util.StringBuilderHelper;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

import java.util.ArrayList;
import java.util.List;

public class PacketData {
    private String[] data;

    public PacketData(Object... objects) {
        data = new String[objects.length];

        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            Class<?> tClass = object.getClass();

            AllowedPacketData allowedPacketData = findAllowedPacketData(tClass);
            if (allowedPacketData == null) {
                throw new NetworkException("Illegal data found on packet. " + tClass.toString() + " is not allowed!");
            }

            data[i] = allowedPacketData.getKey() + "_" + object.toString();
        }
    }

    public PacketData(String[] rawData) {
        this.data = rawData;
    }

    private AllowedPacketData findAllowedPacketData(Class<?> tClass) {
        for (int i = 0; i < AllowedPacketData.values().length; i++) {
            AllowedPacketData allowed = AllowedPacketData.values()[i];
            if (allowed.getStoredClass().equals(tClass)) {
                return allowed;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] getData(AllowedPacketData allowedPacketData) {
        List<T> resultList = new ArrayList<>();

        for (String datum : data) {
            String[] parts = datum.split("_", 2);

            if (parts[0].equals(allowedPacketData.getKey())) {
                String value = parts[1];

                T convertedValue = (T) convertToType(allowedPacketData, value);
                if (convertedValue != null) {
                    resultList.add(convertedValue);
                }
            }
        }

        return resultList.toArray((T[]) java.lang.reflect.Array.newInstance(allowedPacketData.getStoredClass(), resultList.size()));
    }

    private String trimEnd(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        char lastChar = value.charAt(value.length() - 1);
        if (lastChar == ' ' || lastChar == '\n' || lastChar == '\t') {
            return value.substring(0, value.length() - 1);
        }

        return value;
    }

    private Object convertToType(AllowedPacketData allowedPacketData, String value) {
        value = trimEnd(value);
        switch (allowedPacketData) {
            case STRING:
                return value;
            case INTEGER:
                return Integer.parseInt(value);
            case FLOAT:
                return Float.parseFloat(value);
            case BOOLEAN:
                return Boolean.parseBoolean(value);
            case DOUBLE:
                return Double.parseDouble(value);
            case LONG:
                return Long.parseLong(value);
            case SHORT:
                return Short.parseShort(value);
            case BYTE:
                return Byte.parseByte(value);
            case CHARACTER:
                return value.charAt(0);
            default:
                return null;
        }
    }

    public String[] getRawData() {
        return this.data;
    }

    @Override
    public String toString() {
        if (data.length == 0) {
            return null;
        }

        String string = data[0];

        for (int i = 1; i < data.length; i++) {
            string = StringBuilderHelper.concatenate(string, "|", data[i]);
        }

        return string;
    }

    public enum AllowedPacketData {
        STRING(String.class, "s"),
        INTEGER(Integer.class, "i"),
        FLOAT(Float.class, "f"),
        BOOLEAN(Boolean.class, "b"),
        DOUBLE(Double.class, "d"),
        LONG(Long.class, "l"),
        SHORT(Short.class, "sh"),
        BYTE(Byte.class, "by"),
        CHARACTER(Character.class, "c");

        private final Class<?> tClass;
        private final String key;

        private AllowedPacketData(Class<?> tClass, String key) {
            this.tClass = tClass;
            this.key = key;
        }

        private Class<?> getStoredClass() {
            return this.tClass;
        }

        private String getKey() {
            return this.key;
        }
    }
}
