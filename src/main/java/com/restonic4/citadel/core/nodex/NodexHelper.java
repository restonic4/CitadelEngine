package com.restonic4.citadel.core.nodex;

import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.managers.NodeTypes;
import com.restonic4.citadel.registries.built_in.types.NodeType;

import java.util.Map;

public abstract class NodexHelper {
    public static NodeType getDesiredNodeType(Object value) {
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

        return type;
    }
}
