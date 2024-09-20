package com.restonic4.game.core.world.sounds;

import com.restonic4.citadel.registries.AbstractRegistryInitializer;
import com.restonic4.citadel.registries.built_in.types.Sound;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.game.util.Constants;

public class Sounds extends AbstractRegistryInitializer {
    public static Sound GLASS;
    public static Sound TEMPLATE;

    public void register() {
        Logger.log("Registering sounds");

        GLASS = Registry.register(Registries.SOUND, new AssetLocation(Constants.VANILLA_NAMESPACE, "glass"), new Sound());
        TEMPLATE = Registry.register(Registries.MUSIC, new AssetLocation(Constants.VANILLA_NAMESPACE, "template"), new Sound(0.1f));
    }
}
