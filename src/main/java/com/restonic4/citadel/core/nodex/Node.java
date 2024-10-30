package com.restonic4.citadel.core.nodex;

import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.types.NodeType;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Node {
    private String key;
    private NodeType type;

    public Node(String key, NodeType type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public NodeType getType() {
        return type;
    }

    public static void serialize(Node node, DataOutputStream out) throws IOException {
        out.writeUTF(node.getKey());
        out.writeUTF(node.getType().getAssetLocation().toString());
        node.getType().serialize(node, out);
    }

    public static Node deserialize(DataInputStream in) throws IOException {
        String key = in.readUTF();

        NodeType type = Registry.getRegistryObject(Registries.NODE_TYPE, new AssetLocation(in.readUTF()));

        Node node = new Node(key, type);
        node.getType().deserialize(node, in);

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
