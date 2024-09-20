package com.restonic4.citadel.registries.built_in.types;

import com.restonic4.citadel.registries.RegistryObject;

public class PacketDataType<T> extends RegistryObject {
    private final Class<T> typeClass;
    private final String key;

    public PacketDataType(Class<T> typeClass, String key) {
        this.typeClass = typeClass;
        this.key = key;
    }

    public Class<T> getTypeClass() {
        return typeClass;
    }

    public String getKey() {
        return key;
    }

    // Should override this
    public T parse(String value) {
        return null;
    }

    public String parseToString(Object object) {
        return object.toString();
    }
}
