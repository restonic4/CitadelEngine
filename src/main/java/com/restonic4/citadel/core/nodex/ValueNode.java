package com.restonic4.citadel.core.nodex;

import com.restonic4.citadel.registries.built_in.types.NodeType;
import org.joml.*;

public class ValueNode extends Node {
    private Object value;

    public ValueNode(String key, NodeType type) {
        super(key, type);
    }

    public ValueNode(String key, Object value) {
        super(key, Nodex.getDesiredNodeType(value));
        setValue(value);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String getString() {
        return (value instanceof String) ? (String) value : null;
    }

    public Integer getInteger() {
        return (value instanceof Integer) ? (Integer) value : null;
    }

    public Float getFloat() {
        return (value instanceof Float) ? (Float) value : null;
    }

    public Boolean getBoolean() {
        return (value instanceof Boolean) ? (Boolean) value : null;
    }

    public Double getDouble() {
        return (value instanceof Double) ? (Double) value : null;
    }

    public Long getLong() {
        return (value instanceof Long) ? (Long) value : null;
    }

    public Short getShort() {
        return (value instanceof Short) ? (Short) value : null;
    }

    public Byte getByte() {
        return (value instanceof Byte) ? (Byte) value : null;
    }

    public Character getCharacter() {
        return (value instanceof Character) ? (Character) value : null;
    }

    // JOML types
    public Vector2i getVector2i() {
        return (value instanceof Vector2i) ? (Vector2i) value : null;
    }

    public Vector3i getVector3i() {
        return (value instanceof Vector3i) ? (Vector3i) value : null;
    }

    public Vector4i getVector4i() {
        return (value instanceof Vector4i) ? (Vector4i) value : null;
    }

    public Vector2f getVector2f() {
        return (value instanceof Vector2f) ? (Vector2f) value : null;
    }

    public Vector3f getVector3f() {
        return (value instanceof Vector3f) ? (Vector3f) value : null;
    }

    public Vector4f getVector4f() {
        return (value instanceof Vector4f) ? (Vector4f) value : null;
    }

    public Quaternionf getQuaternionf() {
        return (value instanceof Quaternionf) ? (Quaternionf) value : null;
    }
}
