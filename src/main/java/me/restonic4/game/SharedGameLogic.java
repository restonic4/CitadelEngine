package me.restonic4.game;

import me.restonic4.citadel.core.IGameLogic;
import me.restonic4.citadel.registries.RegistryManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.game.core.world.sounds.Sounds;

public class SharedGameLogic implements IGameLogic {
    public void start() {
        Logger.log("Starting the game shared logic");

        RegistryManager.registerRegistrySet(new Sounds());
        RegistryManager.registerCustom();
    }
}
