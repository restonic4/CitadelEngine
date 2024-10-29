package com.restonic4.citadel.core.nodex;

import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.types.NodeType;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import org.joml.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Node {
    private String key;
    private NodeType type;
    private Object value;
    private Map<String, Node> children;

    public Node(String key, NodeType type) {
        this.key = key;
        this.type = type;
        this.children = new HashMap<>();
    }

    public String getKey() {
        return key;
    }

    public NodeType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public Map<String, Node> getChildren() {
        return children;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public void setValue(Vector2i value) {
        this.value = value;
    }

    public void setValue(Vector3i value) {
        this.value = value;
    }

    public void setValue(Vector4i value) {
        this.value = value;
    }

    public void setValue(Vector2f value) {
        this.value = value;
    }

    public void setValue(Vector3f value) {
        this.value = value;
    }

    public void setValue(Vector4f value) {
        this.value = value;
    }

    public void setValue(Quaternionf value) {
        this.value = value;
    }

    public void addChild(Node node) {
        this.children.put(node.getKey(), node);
    }

    public static void serialize(Node node, DataOutputStream out) throws IOException {
        out.writeUTF(node.getKey());
        out.writeUTF(node.getType().getAssetLocation().toString());
        node.getType().serialize(node, out);

        /*switch (node.getType()) {
            case STRING:
                out.writeUTF((String) node.getValue());
                break;
            case INTEGER:
                out.writeInt((Integer) node.getValue());
                break;
            case DOUBLE:
                out.writeDouble((Double) node.getValue());
                break;
            case BOOLEAN:
                out.writeBoolean((Boolean) node.getValue());
                break;
            case NODE:
                out.writeInt(node.getChildren().size());
                for (Node child : node.getChildren().values()) {
                    serialize(child, out);
                }
                break;
        }*/
    }

    public static Node deserialize(DataInputStream in) throws IOException {
        String key = in.readUTF();

        NodeType type = Registry.getRegistryObject(Registries.NODE_TYPE, new AssetLocation(in.readUTF()));

        Node node = new Node(key, type);
        node.getType().deserialize(node, in);

        /*switch (type) {
            case STRING:
                node.setValue(in.readUTF());
                break;
            case INTEGER:
                node.setValue(in.readInt());
                break;
            case DOUBLE:
                node.setValue(in.readDouble());
                break;
            case BOOLEAN:
                node.setValue(in.readBoolean());
                break;
            case NODE:
                int childrenCount = in.readInt();
                for (int i = 0; i < childrenCount; i++) {
                    node.addChild(deserialize(in));
                }
                break;
        }*/
        return node;
    }

    public static void saveToCompressedFile(Node root, String path) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(path.concat(".ndx"))))) {
            serialize(root, out);
        }
    }

    public static Node loadFromCompressedFile(String path) throws IOException {
        try (DataInputStream in = new DataInputStream(new GZIPInputStream(new FileInputStream(path.concat(".ndx"))))) {
            return deserialize(in);
        }
    }
}
