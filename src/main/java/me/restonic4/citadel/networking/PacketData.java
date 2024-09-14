package me.restonic4.citadel.networking;

import me.restonic4.citadel.exceptions.NetworkException;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.PacketDataType;
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

            PacketDataType<?> packetDataType = findPacketDataType(tClass);
            if (packetDataType == null) {
                throw new NetworkException("Illegal data found on packet. " + tClass.toString() + " is not allowed!");
            }

            data[i] = packetDataType.getKey() + "_" + object.toString();
        }
    }

    public PacketData(String[] rawData) {
        this.data = rawData;
    }

    private PacketDataType<?> findPacketDataType(Class<?> tClass) {
        for (PacketDataType<?> packetDataType : Registry.getRegistry(Registries.PACKET_DATA_TYPE).values()) {
            if (packetDataType.getTypeClass().equals(tClass)) {
                return packetDataType;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] getData(PacketDataType<T> packetDataType) {
        List<T> resultList = new ArrayList<>();

        for (String datum : data) {
            String[] parts = trimEnd(datum).split("_", 3);

            if (parts[0].equals(packetDataType.getKey())) {
                String value = parts[1];
                Logger.log(value);
                T convertedValue = packetDataType.parse(value);
                if (convertedValue != null) {
                    resultList.add(convertedValue);
                }
            }
        }

        return resultList.toArray((T[]) java.lang.reflect.Array.newInstance(packetDataType.getTypeClass(), resultList.size()));
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
}
