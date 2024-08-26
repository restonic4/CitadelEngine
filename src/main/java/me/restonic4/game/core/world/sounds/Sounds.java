package me.restonic4.game.core.world.sounds;

import me.restonic4.shared.SharedConstants;
import me.restonic4.engine.util.debug.diagnosis.Logger;
import me.restonic4.engine.registries.AssetLocation;
import me.restonic4.engine.registries.Registries;
import me.restonic4.engine.registries.Registry;

public class Sounds {
    public static Sound GLASS;
    public static Sound TEMPLATE;

    public static void register() {
        Logger.log("Registering sounds");

        GLASS = Registry.register(Registries.SOUND, new AssetLocation(SharedConstants.VANILLA_NAMESPACE, "glass"), new Sound(1));
        TEMPLATE = Registry.register(Registries.MUSIC, new AssetLocation(SharedConstants.VANILLA_NAMESPACE, "template"), new Sound(0.25f));
    }
}
