package com.restonic4.citadel.registries.built_in.managers;

import com.restonic4.citadel.registries.AbstractRegistryInitializer;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.types.NodeType;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.StringBuilderHelper;
import org.joml.*;

public class NodeTypes extends AbstractRegistryInitializer {
    public static NodeType<String> STRING;
    public static NodeType<Integer> INTEGER;
    public static NodeType<Float> FLOAT;
    public static NodeType<Boolean> BOOLEAN;
    public static NodeType<Double> DOUBLE;
    public static NodeType<Long> LONG;
    public static NodeType<Short> SHORT;
    public static NodeType<Byte> BYTE;
    public static NodeType<Character> CHARACTER;

    public static NodeType<Vector2i> VECTOR2I;
    public static NodeType<Vector3i> VECTOR3I;
    public static NodeType<Vector4i> VECTOR4I;

    public static NodeType<Vector2f> VECTOR2F;
    public static NodeType<Vector3f> VECTOR3F;
    public static NodeType<Vector4f> VECTOR4F;

    public static NodeType<Quaternionf> QUATERNIONF;

    @SuppressWarnings("unchecked")
    public void register() {
        STRING = (NodeType<String>) Registry.register(Registries.NODE_TYPE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "string"), new NodeType<>(String.class) {
            @Override
            public String serialize(Object object) {
                return super.serialize(object);
            }

            @Override
            public String deserialize(String value) {
                return value;
            }
        });
    }
}
