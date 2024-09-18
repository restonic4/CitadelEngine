package com.restonic4.citadel.world;

import com.restonic4.citadel.sound.SoundManager;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.world.object.GameObject;
import com.restonic4.citadel.render.cameras.Camera;
import com.restonic4.citadel.render.Renderer;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.object.components.LightComponent;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer(this);
    protected Camera camera;

    private List<GameObject> staticGameObjects = new ArrayList<>();
    private List<GameObject> dynamicGameObjects = new ArrayList<>();
    private List<LightComponent> lightComponents = new ArrayList<>();

    private boolean isActivated = false;

    public void init() {
        Logger.log("Initializing the scene " + this);
    }

    public void activate() {
        Logger.log("Trying to activate the scene " + this);

        if (isActivated) {
            Logger.log("The scene " + this + " was already activated");
            return;
        }

        activateGameObjects(staticGameObjects);
        activateGameObjects(dynamicGameObjects);

        isActivated = true;

        Logger.log("The scene " + this + " was activated");
    }

    private void activateGameObjects(List<GameObject> gameObjects) {
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);

            gameObject.start();
            renderer.add(gameObject);
        }
    }

    public void update() {
        for (int i = 0; i < this.staticGameObjects.size(); i++) {
            this.staticGameObjects.get(i).update();
        }

        for (int i = 0; i < this.dynamicGameObjects.size(); i++) {
            this.dynamicGameObjects.get(i).update();
        }

        this.renderer.render();
    };

    public void unload() {
        Logger.log("Unloading the scene");

        SoundManager.getInstance().reset();

        System.gc();
    }

    public void addGameObject(GameObject gameObject) {
        List<GameObject> targetList = gameObject.isStatic() ? staticGameObjects : dynamicGameObjects;
        targetList.add(gameObject);

        LightComponent lightComponent = gameObject.getComponent(LightComponent.class);
        if (lightComponent != null) {
            lightComponents.add(lightComponent);
        }

        if (isActivated) {
            gameObject.start();
            renderer.add(gameObject);
        }
    }

    public Camera getCamera() {
        return this.camera;
    }

    public Renderer getRenderer() {
        return this.renderer;
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

    public List<LightComponent> getLightComponents() {
        return this.lightComponents;
    }

    public List<LightComponent> getLightComponentsOfType(LightComponent.LightType lightType) {
        List<LightComponent> desiredLights = new ArrayList<>();

        for (int i = 0; i < this.lightComponents.size(); i++) {
            LightComponent lightComponent = this.lightComponents.get(i);

            if (lightComponent.getLightType() == lightType) {
                desiredLights.add(lightComponent);
            }
        }

        return desiredLights;
    }

    public Vector3f[] getLightPositions() {
        Vector3f[] positions = new Vector3f[getLightsAmount()];

        for (int i = 0; i < positions.length; i++) {
            positions[i] = this.lightComponents.get(i).gameObject.transform.getPosition();
        }

        return positions;
    }

    public Vector3f[] getLightColors() {
        Vector3f[] colors = new Vector3f[getLightsAmount()];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = this.lightComponents.get(i).getColor();
        }

        return colors;
    }

    public Vector4f[] getLightAttenuationFactors() {
        Vector4f[] attenuationFactors = new Vector4f[getLightsAmount()];

        for (int i = 0; i < attenuationFactors.length; i++) {
            attenuationFactors[i] = this.lightComponents.get(i).getLightType().getAttenuationFactorsComplete();
        }

        return attenuationFactors;
    }

    public int getLightsAmount() {
        return Math.min(this.lightComponents.size(), CitadelConstants.MAX_LIGHTS);

    }

    public int getGameObjectsAmount() {
        return this.staticGameObjects.size() + this.dynamicGameObjects.size();
    }

    public void cleanup() {
        this.renderer.cleanup();
    }
}
