package com.restonic4.citadel.api;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.CitadelSettings;
import com.restonic4.citadel.events.types.CitadelLifecycleEvents;
import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ModLoader {
    private final List<Mod> mods;

    public ModLoader() {
        this.mods = new ArrayList<>();
    }

    /**
     * Injects all the mods into the engine.
     */
    public void loadMods() {
        Logger.log("Loading mods");

        CitadelSettings citadelSettings = CitadelLauncher.getInstance().getSettings();

        if (!citadelSettings.isThirdPartyNamespaceRegistrationAllowed()) {
            Logger.log("Skipping mod injection, it's disabled");
            return;
        }

        File modsFolder = new File(FileManager.getOrCreateDirectory(CitadelConstants.MODS_DIRECTORY));

        File[] modFiles = modsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (modFiles == null) {
            Logger.log("Couldn't find any mod");
            return;
        }

        try {
            for (File modFile : modFiles) {
                URL[] urls = { modFile.toURI().toURL() };
                URLClassLoader classLoader = new URLClassLoader(urls, this.getClass().getClassLoader());

                // Look for mod.properties.json
                InputStream configStream = classLoader.getResourceAsStream(CitadelConstants.MOD_PROPERTIES_FILE_NAME);
                if (configStream == null) {
                    Logger.log("No " + CitadelConstants.MOD_PROPERTIES_FILE_NAME + " found in " + modFile.getName());
                    continue;
                }

                // Load properties
                Properties properties = new Properties();
                properties.load(configStream);

                String mainClass = properties.getProperty(CitadelConstants.MOD_PROPERTIES_MAIN_CLASS);
                if (mainClass == null || mainClass.isEmpty()) {
                    Logger.log("No " + CitadelConstants.MOD_PROPERTIES_MAIN_CLASS + " defined in " + CitadelConstants.MOD_PROPERTIES_FILE_NAME + " for " + modFile.getName());
                    continue;
                }

                // Load main class
                Class<?> modClass = Class.forName(mainClass, true, classLoader);
                Mod mod = (Mod) modClass.getDeclaredConstructor().newInstance();

                mods.add(mod);

                mod.onStart();

                CitadelLifecycleEvents.MOD_LOADED.invoker().onModLoaded(mod);
            }
        } catch (Exception exception) {
            Logger.log("Couldn't load mods: " + exception.getMessage());
        }

        // Subscribe to end mods
        CitadelLifecycleEvents.CITADEL_STOPPING.register((citadelLauncher, window) -> {
            for (Mod mod : mods) {
                mod.onStop();

                CitadelLifecycleEvents.MOD_UNLOADED.invoker().onModUnloaded(mod);
            }
        });
    }

    /**
     * Calls the onUpdate() of every mod.
     */
    public void update() {
        for (int i = 0; i < mods.size(); i++) {
            mods.get(i).onUpdate();
        }
    }
}
