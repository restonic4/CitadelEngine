package me.restonic4.game;

import me.restonic4.citadel.IGameLogic;
import me.restonic4.citadel.sound.SoundManager;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.registries.RegistryManager;
import me.restonic4.game.core.scenes.WorldScene;

public class Game implements IGameLogic {
    public void start() {
        Logger.log("Starting the game");

        SoundManager.getInstance().init();

        RegistryManager.registerAll();

        SceneManager.loadScene(new WorldScene());
    }
}
