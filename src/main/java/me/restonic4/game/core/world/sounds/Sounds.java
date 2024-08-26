package me.restonic4.game.core.world.sounds;

import me.restonic4.shared.SharedConstants;
import me.restonic4.engine.util.debug.diagnosis.Logger;
import me.restonic4.engine.registries.AssetLocation;
import me.restonic4.engine.registries.Registries;
import me.restonic4.engine.registries.Registry;

public class Sounds {
    public static Sound GLASS;

    public static void register() {
        Logger.log("Registering sounds");

        GLASS = Registry.register(Registries.SOUND, new AssetLocation(SharedConstants.VANILLA_NAMESPACE, "glass"), new Sound());
    }
}
