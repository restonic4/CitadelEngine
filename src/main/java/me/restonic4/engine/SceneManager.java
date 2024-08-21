package me.restonic4.engine;

import me.restonic4.engine.util.debug.Logger;

public class SceneManager {
    private static SceneManager instance;

    private Scene currentScene;

    public static SceneManager getInstance() {
        if (SceneManager.instance == null) {
            SceneManager.instance = new SceneManager();
        }
        return SceneManager.instance;
    }

    public Scene getCurrentScene() {
        return this.currentScene;
    }

    private void setScene(Scene scene) {
        this.currentScene = scene;
    }

    public void loadScene(Scene scene) {
        Logger.log("Loading the scene: " + scene);

        setScene(scene);

        scene.init();
        scene.activate();
    }

    public void unLoadScene(Scene scene) {
        scene.unload();

        if (this.currentScene == scene) {
            this.currentScene = null;
        }
    }

    public void unLoadCurrentScene() {
        if (this.currentScene != null) {
            unLoadScene(this.currentScene);
        }
    }
}