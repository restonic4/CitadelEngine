package com.restonic4.citadel.registries.built_in.types;

import com.restonic4.citadel.registries.RegistryObject;

public class NodeType<T> extends RegistryObject {
    private final Class<T> typeClass;

    public NodeType(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    public Class<T> getTypeClass() {
        return typeClass;
    }

    // Should override this
    public String serialize(Object object) {
        return object.toString();
    }

    public T deserialize(String value) {
        return null;
    }
}
