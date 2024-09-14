package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.PacketDataType;
import me.restonic4.citadel.util.CitadelConstants;

public class PacketDataTypes extends AbstractRegistryInitializer {
    public static PacketDataType<String> STRING;
    public static PacketDataType<Integer> INTEGER;
    public static PacketDataType<Float> FLOAT;
    public static PacketDataType<Boolean> BOOLEAN;
    public static PacketDataType<Double> DOUBLE;
    public static PacketDataType<Long> LONG;
    public static PacketDataType<Short> SHORT;
    public static PacketDataType<Byte> BYTE;
    public static PacketDataType<Character> CHARACTER;

    @SuppressWarnings("unchecked")
    public void register() {
        STRING = (PacketDataType<String>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "string"), new PacketDataType<>(String.class, "s") {
            @Override
            public String parse(String value) {
                return value;
            }
        });

        INTEGER = (PacketDataType<Integer>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "integer"), new PacketDataType<>(Integer.class, "i") {
            @Override
            public Integer parse(String value) {
                return Integer.parseInt(value);
            }
        });

        FLOAT = (PacketDataType<Float>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "float"), new PacketDataType<>(Float.class, "f") {
            @Override
            public Float parse(String value) {
                return Float.parseFloat(value);
            }
        });

        BOOLEAN = (PacketDataType<Boolean>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "boolean"), new PacketDataType<>(Boolean.class, "b") {
            @Override
            public Boolean parse(String value) {
                return Boolean.parseBoolean(value);
            }
        });

        DOUBLE = (PacketDataType<Double>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "double"), new PacketDataType<>(Double.class, "d") {
            @Override
            public Double parse(String value) {
                return Double.parseDouble(value);
            }
        });

        LONG = (PacketDataType<Long>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "long"), new PacketDataType<>(Long.class, "l") {
            @Override
            public Long parse(String value) {
                return Long.parseLong(value);
            }
        });

        SHORT = (PacketDataType<Short>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "short"), new PacketDataType<>(Short.class, "sh") {
            @Override
            public Short parse(String value) {
                return Short.parseShort(value);
            }
        });

        BYTE = (PacketDataType<Byte>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "byte"), new PacketDataType<>(Byte.class, "by") {
            @Override
            public Byte parse(String value) {
                return Byte.parseByte(value);
            }
        });

        CHARACTER = (PacketDataType<Character>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "character"), new PacketDataType<>(Character.class, "c") {
            @Override
            public Character parse(String value) {
                return value.charAt(0);
            }
        });
    }
}
