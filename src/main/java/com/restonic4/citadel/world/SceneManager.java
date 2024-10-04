package com.restonic4.citadel.world;

import com.restonic4.citadel.events.types.SceneEvents;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

import static org.lwjgl.opengl.GL11.glFinish;

public abstract class SceneManager {
    private static Scene currentScene;
    public static int changes = 0;

    public static Scene getCurrentScene() {
        return currentScene;
    }

    private static void setScene(Scene scene) {
        changes++;
        currentScene = scene;
    }

    public static void loadScene(Scene scene) {
        Logger.log("Loading the scene: " + scene);

        if (currentScene != null) {
            currentScene.unload();
        }

        // Makes sure OpenGL finishes his things, so it doesn't break, Spoiler: It breaks at the start of the engine and if the scene changes every frame, cool I guess
        glFinish();

        setScene(scene);

        scene.init();
        scene.activate();

        SceneEvents.LOADED.invoker().onSceneLoaded(scene);
    }

    public static void unLoadScene(Scene scene) {
        scene.unload();

        if (currentScene == scene) {
            currentScene = null;
        }

        SceneEvents.UNLOADED.invoker().onSceneUnLoaded(scene);
    }

    public static void unLoadCurrentScene() {
        if (currentScene != null) {
            currentScene.cleanup();
            unLoadScene(currentScene);
        }
    }
}