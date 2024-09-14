package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.PacketDataType;
import me.restonic4.citadel.util.CitadelConstants;
import me.restonic4.citadel.util.StringBuilderHelper;
import org.joml.*;

import java.math.BigDecimal;
import java.math.BigInteger;

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

    public static PacketDataType<Vector2i> VECTOR2I;
    public static PacketDataType<Vector3i> VECTOR3I;
    public static PacketDataType<Vector4i> VECTOR4I;

    public static PacketDataType<Vector2f> VECTOR2F;
    public static PacketDataType<Vector3f> VECTOR3F;
    public static PacketDataType<Vector4f> VECTOR4F;

    public static PacketDataType<Quaternionf> QUATERNIONF;

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

        VECTOR2I = (PacketDataType<Vector2i>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "vector2i"), new PacketDataType<>(Vector2i.class, "v2i") {
            @Override
            public Vector2i parse(String value) {
                String[] parts = value.split(";");
                int x, y;

                x = INTEGER.parse(parts[0]);
                y = INTEGER.parse(parts[1]);

                return new Vector2i(x, y);
            }

            @Override
            public String parseToString(Object object) {
                Vector2i vector2i = (Vector2i) object;
                return StringBuilderHelper.concatenate(vector2i.x, ";", vector2i.y);
            }
        });

        VECTOR3I = (PacketDataType<Vector3i>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "vector3i"), new PacketDataType<>(Vector3i.class, "v3i") {
            @Override
            public Vector3i parse(String value) {
                String[] parts = value.split(";");
                int x, y, z;

                x = INTEGER.parse(parts[0]);
                y = INTEGER.parse(parts[1]);
                z = INTEGER.parse(parts[2]);

                return new Vector3i(x, y, z);
            }

            @Override
            public String parseToString(Object object) {
                Vector3i vector3i = (Vector3i) object;
                return StringBuilderHelper.concatenate(vector3i.x, ";", vector3i.y, ";", vector3i.z);
            }
        });

        VECTOR4I = (PacketDataType<Vector4i>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "vector4i"), new PacketDataType<>(Vector4i.class, "v4i") {
            @Override
            public Vector4i parse(String value) {
                String[] parts = value.split(";");
                int x, y, z, w;

                x = INTEGER.parse(parts[0]);
                y = INTEGER.parse(parts[1]);
                z = INTEGER.parse(parts[2]);
                w = INTEGER.parse(parts[3]);

                return new Vector4i(x, y, z, w);
            }

            @Override
            public String parseToString(Object object) {
                Vector4i vector4i = (Vector4i) object;
                return StringBuilderHelper.concatenate(vector4i.x, ";", vector4i.y, ";", vector4i.z, ";", vector4i.w);
            }
        });

        VECTOR2F = (PacketDataType<Vector2f>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "vector2f"), new PacketDataType<>(Vector2f.class, "v2f") {
            @Override
            public Vector2f parse(String value) {
                String[] parts = value.split(";");
                float x, y;

                x = FLOAT.parse(parts[0]);
                y = FLOAT.parse(parts[1]);

                return new Vector2f(x, y);
            }

            @Override
            public String parseToString(Object object) {
                Vector2f vector2f = (Vector2f) object;
                return StringBuilderHelper.concatenate(vector2f.x, ";", vector2f.y);
            }
        });

        VECTOR3F = (PacketDataType<Vector3f>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "vector3f"), new PacketDataType<>(Vector3f.class, "v3f") {
            @Override
            public Vector3f parse(String value) {
                String[] parts = value.split(";");
                float x, y, z;

                x = FLOAT.parse(parts[0]);
                y = FLOAT.parse(parts[1]);
                z = FLOAT.parse(parts[2]);

                return new Vector3f(x, y, z);
            }

            @Override
            public String parseToString(Object object) {
                Vector3f vector3f = (Vector3f) object;
                return StringBuilderHelper.concatenate(vector3f.x, ";", vector3f.y, ";", vector3f.z);
            }
        });

        VECTOR4F = (PacketDataType<Vector4f>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "vector4f"), new PacketDataType<>(Vector4f.class, "v4f") {
            @Override
            public Vector4f parse(String value) {
                String[] parts = value.split(";");
                float x, y, z, w;

                x = FLOAT.parse(parts[0]);
                y = FLOAT.parse(parts[1]);
                z = FLOAT.parse(parts[2]);
                w = FLOAT.parse(parts[3]);

                return new Vector4f(x, y, z, w);
            }

            @Override
            public String parseToString(Object object) {
                Vector4f vector4f = (Vector4f) object;
                return StringBuilderHelper.concatenate(vector4f.x, ";", vector4f.y, ";", vector4f.z, ";", vector4f.w);
            }
        });

        QUATERNIONF = (PacketDataType<Quaternionf>) Registry.register(Registries.PACKET_DATA_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "quaternionf"), new PacketDataType<>(Quaternionf.class, "qf") {
            @Override
            public Quaternionf parse(String value) {
                String[] parts = value.split(";");

                float x = Float.parseFloat(parts[0]);
                float y = Float.parseFloat(parts[1]);
                float z = Float.parseFloat(parts[2]);
                float w = Float.parseFloat(parts[3]);

                return new Quaternionf(x, y, z, w);
            }

            @Override
            public String parseToString(Object object) {
                Quaternionf quaternion = (Quaternionf) object;
                return StringBuilderHelper.concatenate(quaternion.x, ";", quaternion.y, ";", quaternion.z, ";", quaternion.w);
            }
        });
    }
}
