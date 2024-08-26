package me.restonic4.engine.world;

import me.restonic4.engine.util.debug.diagnosis.Logger;

public abstract class SceneManager {
    private static Scene currentScene;

    public static Scene getCurrentScene() {
        return currentScene;
    }

    private static void setScene(Scene scene) {
        currentScene = scene;
    }

    public static void loadScene(Scene scene) {
        Logger.log("Loading the scene: " + scene);

        setScene(scene);

        scene.init();
        scene.activate();
    }

    public static void unLoadScene(Scene scene) {
        scene.unload();

        if (currentScene == scene) {
            currentScene = null;
        }
    }

    public static void unLoadCurrentScene() {
        if (currentScene != null) {
            unLoadScene(currentScene);
        }
    }
}