package me.restonic4.engine.world;

import me.restonic4.engine.world.object.GameObject;
import me.restonic4.engine.render.Camera;
import me.restonic4.engine.render.Renderer;
import me.restonic4.engine.util.debug.diagnosis.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    protected Camera camera;

    private List<GameObject> staticGameObjects = new ArrayList<>();
    private List<GameObject> dynamicGameObjects = new ArrayList<>();

    private boolean isActivated = false;

    public void init() {
        Logger.log("Initializing the scene");
    }

    public void activate() {
        if (isActivated) {
            return;
        }

        activateGameObjects(staticGameObjects);
        activateGameObjects(dynamicGameObjects);

        isActivated = true;
    }

    private void activateGameObjects(List<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            renderer.add(gameObject);
        }
    }

    public void update() {
        for (GameObject gameObjects : this.staticGameObjects) {
            gameObjects.update();
        }

        for (GameObject gameObjects : this.dynamicGameObjects) {
            gameObjects.update();
        }

        this.renderer.render();
    };

    public void unload() {
        Logger.log("Unloading the scene");
    }

    public void addGameObject(GameObject gameObject) {
        List<GameObject> targetList = gameObject.isStatic() ? staticGameObjects : dynamicGameObjects;
        targetList.add(gameObject);

        if (isActivated) {
            gameObject.start();
            renderer.add(gameObject);
        }
    }

    public Camera getCamera() {
        return this.camera;
    }

    public List<GameObject> getGameObjects() {
        List<GameObject> allGameObjects = new ArrayList<>();

        allGameObjects.addAll(staticGameObjects);
        allGameObjects.addAll(dynamicGameObjects);

        return allGameObjects;
    }

    public List<GameObject> getStaticGameObjects() {
        return this.staticGameObjects;
    }

    public List<GameObject> getDynamicGameObjects() {
        return this.dynamicGameObjects;
    }

    public int getGameObjectsAmount() {
        return this.staticGameObjects.size() + this.dynamicGameObjects.size();
    }
}
