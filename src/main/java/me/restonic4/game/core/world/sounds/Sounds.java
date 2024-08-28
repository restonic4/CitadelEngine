package me.restonic4.game.core.world.sounds;

import me.restonic4.citadel.util.CitadelConstants;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.game.util.Constants;

public class Sounds {
    public static Sound GLASS;
    public static Sound TEMPLATE;

    public static void register() {
        Logger.log("Registering sounds");

        GLASS = Registry.register(Registries.SOUND, new AssetLocation(Constants.VANILLA_NAMESPACE, "glass"), new Sound(1));
        TEMPLATE = Registry.register(Registries.MUSIC, new AssetLocation(Constants.VANILLA_NAMESPACE, "template"), new Sound(0.1f));
    }
}
