package com.restonic4.citadel.registries;

import com.restonic4.citadel.util.StringHelper;

public class RegistryKey<T> {
    private final String key, rootDirectory, assetFileExtension;

    public RegistryKey(String key) {
        this.key = key;
        this.rootDirectory = null;
        this.assetFileExtension = null;
    }

    public RegistryKey(String key, String rootDirectory, String assetFileExtension) {
        this.key = key;
        this.rootDirectory = rootDirectory;
        this.assetFileExtension = assetFileExtension;
    }

    public String getKey() {
        return key;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public String getAssetFileExtension() {
        return assetFileExtension;
    }

    public long getMemorySize() {
        long size = 8;
        size += 3 * 8;

        size += StringHelper.getMemorySize(getKey());
        size += StringHelper.getMemorySize(getRootDirectory());
        size += StringHelper.getMemorySize(getAssetFileExtension());

        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistryKey<?> that = (RegistryKey<?>) o;
        return key.equals(that.key) && rootDirectory.equals(that.rootDirectory) && assetFileExtension.equals(that.assetFileExtension);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
