package me.restonic4.game.core.world.sounds;

import me.restonic4.engine.util.Constants;
import me.restonic4.game.core.AssetLocation;
import me.restonic4.game.core.registries.Registries;
import me.restonic4.game.core.registries.Registry;

public class Sounds {
    public static Sound GLASS;

    public static void register() {
        System.out.println("Registering sounds");

        GLASS = Registry.register(Registries.SOUND, new AssetLocation(Constants.VANILLA_NAMESPACE, "glass"), new Sound());
    }
}
