package com.restonic4.game;

import com.restonic4.citadel.core.IGameLogic;
import com.restonic4.citadel.registries.RegistryManager;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.game.core.world.sounds.Sounds;

public class SharedGameLogic implements IGameLogic {
    public void start() {
        Logger.log("Starting the game shared logic");

        RegistryManager.registerRegistrySet(new Sounds());
        RegistryManager.registerCustom();
    }

    @Override
    public void update() {

    }
}
