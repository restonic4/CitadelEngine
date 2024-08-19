package me.restonic4.engine;

import me.restonic4.engine.object.GameObject;
import me.restonic4.engine.render.Camera;
import me.restonic4.engine.render.Renderer;
import me.restonic4.engine.util.debug.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    protected List<GameObject> gameObjects = new ArrayList<>();

    private boolean isActivated = false;

    public void init() {
        Logger.log("Initializing the scene");
    }

    public void activate() {
        if (isActivated) {
            return;
        }

        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            this.renderer.add(gameObject);
        }

        isActivated = true;
    }

    public abstract void update();

    public void addGameObject(GameObject gameObject) {
        if (!isActivated) {
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.start();
            this.renderer.add(gameObject);
        }
    }

    public Camera getCamera() {
        return this.camera;
    }
}
