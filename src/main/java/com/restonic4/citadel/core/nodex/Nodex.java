package com.restonic4.citadel.core.nodex;

import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.managers.NodeTypes;
import com.restonic4.citadel.registries.built_in.types.NodeType;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

import java.util.Map;

public abstract class Nodex {
    public static Node createRootNode() {
        Node rootNode = new Node("root", NodeTypes.STRING);
        rootNode.setValue("Nodex");

        return rootNode;
    }

    public static Node setValue(Node node, String name, Object value) {
        NodeType type = null;

        Map<AssetLocation, NodeType> registries = Registry.getRegistry(Registries.NODE_TYPE);
        for (NodeType nodeType : registries.values()) {
            if (value.getClass().getSimpleName().toLowerCase().equals(nodeType.getAssetLocation().getPath())) {
                type = nodeType;
            }
        }

        if (type == null) {
            throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
        }

        Node childNode = new Node(name, type);
        childNode.setValue(value);

        node.addChild(childNode);

        return childNode;
    }
}
