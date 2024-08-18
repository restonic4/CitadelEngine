package me.restonic4.game.core;

import me.restonic4.shared.SharedConstants;
import me.restonic4.game.core.exceptions.AssetLocationException;
import me.restonic4.game.core.registries.RegistryKey;

//This is used to locate assets
public class AssetLocation {
    private String namespace;
    private String path;
    private RegistryKey<?> registryKey;

    public AssetLocation(String namespace, String path) {
        setUp(namespace, path);
    }

    public AssetLocation(String compressed) {
        if (!compressed.contains(":")) {
            throw new AssetLocationException("Illegal compressed asset location. It should be like: \"namespace:path\"");
        }

        String[] parts = compressed.split(":");

        if (parts.length < 2) {
            throw new AssetLocationException("Illegal compressed asset location. It is missing a part. It should be like: \"namespace:path\"");
        }

        setUp(parts[0], parts[1]);
    }

    private void setUp(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;

        if (!isValidNamespace(namespace)) {
            throw new AssetLocationException("Illegal namespace character in: " + this);
        }

        if (!isValidNPath(namespace)) {
            throw new AssetLocationException("Illegal path character in: " + this);
        }
    }

    public static boolean isValidNamespace(String string) {
        return isValidString(string, SharedConstants.VALID_NAMESPACE_CHARS);
    }

    public static boolean isValidNPath(String string) {
        return isValidString(string, SharedConstants.VALID_PATH_CHARS);
    }

    private static boolean isValidString(String string, String validChars) {
        for (int i = 0; i < string.length(); ++i) {
            if (validChars.indexOf(string.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return this.namespace + ":" + this.path;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    public void setRegistryKey(RegistryKey<?> registryKey) {
        this.registryKey = registryKey;
    }

    public RegistryKey<?> getRegistryKey() {
        return this.registryKey;
    }
}
