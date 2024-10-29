package com.restonic4.citadel.registries;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.CitadelSettings;
import com.restonic4.citadel.exceptions.RegistryObjectException;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.helpers.StringBuilderHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Registry {
    private static final Map<RegistryKey<?>, Map<AssetLocation, ?>> registries = new HashMap<>();

    public static <T extends RegistryObject> T register(RegistryKey<T> registryKey, AssetLocation assetLocation, T object) {
        if (!isValidNamespace(assetLocation)) {
            String allowedNamespacesText = "[";

            String[] allowedNamespaces = CitadelLauncher.getInstance().getSettings().getAllowedNamespaces();
            if (allowedNamespaces != null) {
                for (int i = 0; i < allowedNamespaces.length; i++) {
                    allowedNamespacesText = StringBuilderHelper.concatenate(allowedNamespacesText, allowedNamespaces[i]);

                    if (i < allowedNamespaces.length - 1) {
                        allowedNamespacesText = StringBuilderHelper.concatenate(allowedNamespacesText, ", ");
                    }
                }
            }
            else {
                allowedNamespacesText = StringBuilderHelper.concatenate(allowedNamespacesText, "NONE");
            }

            allowedNamespacesText = StringBuilderHelper.concatenate(allowedNamespacesText, "]");

            throw new RegistryObjectException("The developer of this game disabled the ability to register new objects. Only the following namespaces are allowed: " + allowedNamespacesText);
        }

        Map<AssetLocation, T> registry = getOrCreateRegistry(registryKey);

        assetLocation.setRegistryKey(registryKey);

        if (registry.containsKey(assetLocation)) {
            throw new IllegalArgumentException("Duplicate asset location: " + assetLocation);
        }

        object.setAssetLocation(assetLocation);
        object.onPopulate();

        registry.put(assetLocation, object);

        return object;
    }

    public static <T extends RegistryObject> T getRegistryObject(RegistryKey<T> registryKey, AssetLocation assetLocation) {
        Map<AssetLocation, T> registry = getRegistry(registryKey);

        if (registry == null) return null;

        return registry.get(assetLocation);
    }

    @SuppressWarnings("unchecked")
    private static <T extends RegistryObject> Map<AssetLocation, T> getOrCreateRegistry(RegistryKey<T> registryKey) {
        return (Map<AssetLocation, T>) registries.computeIfAbsent(registryKey, k -> new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    public static <T extends RegistryObject> Map<AssetLocation, T> getRegistry(RegistryKey<T> registryKey) {
        return (Map<AssetLocation, T>) registries.get(registryKey);
    }

    public static <T extends RegistryObject> boolean isNamespaceLoaded(String id) {
        for (Map.Entry<RegistryKey<?>, Map<AssetLocation, ?>> data : registries.entrySet()) {
            Map<AssetLocation, ?> map = data.getValue();

            for (Map.Entry<AssetLocation, ?> registryData : map.entrySet()) {
                AssetLocation assetLocation = registryData.getKey();

                if (Objects.equals(assetLocation.getNamespace(), id)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Map<RegistryKey<?>, Map<AssetLocation, ?>> getRegistries() {
        return registries;
    }

    public static boolean isValidNamespace(AssetLocation assetLocation) {
        CitadelSettings citadelSettings = CitadelLauncher.getInstance().getSettings();

        if (citadelSettings.isThirdPartyNamespaceRegistrationAllowed()) {
            return true;
        }

        if (assetLocation.getNamespace() == CitadelConstants.REGISTRY_NAMESPACE) {
            return true;
        }

        String[] allowedNamespaces = citadelSettings.getAllowedNamespaces();
        if (allowedNamespaces == null) {
            return false;
        }

        for (int i = 0; i < allowedNamespaces.length; i++) {
            if (assetLocation.getNamespace() == allowedNamespaces[i]) {
                return true;
            }
        }

        return false;
    }

    public static long getApproximatedMemorySize() {
        long size = 36;

        for (Map.Entry<RegistryKey<?>, Map<AssetLocation, ?>> entry : registries.entrySet()) {
            size += entry.getKey().getMemorySize();

            Map<AssetLocation, ?> innerMap = entry.getValue();
            size += 32;
            size += 8;

            if (innerMap != null) {
                size += 36;

                for (Map.Entry<AssetLocation, ?> innerEntry : innerMap.entrySet()) {
                    size += innerEntry.getKey().getMemorySize();
                    size += 8;
                }
            }
        }

        return size;
    }
}
