package com.restonic4.citadel.core.nodex;

import com.restonic4.citadel.registries.built_in.managers.NodeTypes;

import java.util.HashMap;
import java.util.Map;

public class RootNode extends Node {
    private Map<String, Node> children;

    public RootNode(String key) {
        super(key, NodeTypes.NODE);
        this.children = new HashMap<>();
    }

    public Map<String, Node> getChildren() {
        return children;
    }

    public Node getNode(String key) {
        return children.get(key);
    }

    public ValueNode getValueNode(String key) {
        if (children.get(key) instanceof ValueNode valueNode) {
            return valueNode;
        }

        return null;
    }

    public RootNode getRootNode(String key) {
        if (children.get(key) instanceof RootNode rootNode) {
            return rootNode;
        }

        return null;
    }

    public void addChild(Node node) {
        this.children.put(node.getKey(), node);
    }

    public void addChildren(Node... nodes) {
        addChild(nodes);
    }

    public void addChild(Node... nodes) {
        for (Node node: nodes) {
            this.children.put(node.getKey(), node);
        }
    }
}
