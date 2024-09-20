package com.restonic4.game;

import com.restonic4.citadel.core.GameLogic;
import com.restonic4.citadel.util.debug.DebugManager;
import com.restonic4.citadel.util.debug.diagnosis.ProfilerManager;
import com.restonic4.citadel.world.SceneManager;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.game.core.scenes.WorldScene;

public class ClientGameLogic implements GameLogic {
    public void start() {
        Logger.log("Starting the game client logic");

        DebugManager.setDebugMode(true);
        ProfilerManager.setEnabled(false);

        Logger.log("ola1");

        SceneManager.loadScene(new WorldScene());
    }

    @Override
    public void update() {

    }
}
