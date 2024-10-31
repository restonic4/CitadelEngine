package com.restonic4.citadel.core.nodex;

import com.restonic4.citadel.registries.built_in.types.NodeType;

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

    public void save() {
        Nodex.save(this);
    }
}
