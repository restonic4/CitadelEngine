package me.restonic4.game;

import me.restonic4.citadel.core.IGameLogic;
import me.restonic4.citadel.events.EventResult;
import me.restonic4.citadel.events.types.WindowEvents;
import me.restonic4.citadel.sound.SoundManager;
import me.restonic4.citadel.util.debug.DebugManager;
import me.restonic4.citadel.util.debug.diagnosis.ProfilerManager;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.registries.RegistryManager;
import me.restonic4.game.core.scenes.WorldScene;
import me.restonic4.game.core.world.sounds.Sounds;

public class Game implements IGameLogic {
    public void start() {
        Logger.log("Starting the game");

        DebugManager.setDebugMode(true);
        ProfilerManager.setEnabled(false);

        SoundManager.getInstance().init();

        RegistryManager.registerRegistrySet(new Sounds());
        RegistryManager.registerCustom();

        SceneManager.loadScene(new WorldScene());
    }
}
