package me.restonic4.game;

import me.restonic4.citadel.core.IGameLogic;
import me.restonic4.citadel.registries.built_in.managers.FrameBuffers;
import me.restonic4.citadel.render.FrameBufferManager;
import me.restonic4.citadel.render.FrustumRenderer;
import me.restonic4.citadel.util.debug.DebugManager;
import me.restonic4.citadel.util.debug.diagnosis.ProfilerManager;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.game.core.scenes.WorldScene;

import static org.lwjgl.opengl.GL11.glViewport;

public class ClientGameLogic implements IGameLogic {
    public void start() {
        Logger.log("Starting the game client logic");

        DebugManager.setDebugMode(true);
        ProfilerManager.setEnabled(false);

        SceneManager.loadScene(new WorldScene());
    }

    @Override
    public void update() {

    }
}
