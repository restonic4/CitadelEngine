package com.restonic4.citadel.world;

import com.restonic4.citadel.events.EventResult;
import com.restonic4.citadel.events.types.SceneEvents;
import com.restonic4.citadel.physics.PhysicsManager;
import com.restonic4.citadel.sound.SoundManager;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.helpers.StringBuilderHelper;
import com.restonic4.citadel.world.object.GameObject;
import com.restonic4.citadel.render.Renderer;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.files.Serializable;
import com.restonic4.citadel.world.object.Transform;
import com.restonic4.citadel.world.object.components.CameraComponent;
import com.restonic4.citadel.world.object.components.ColliderComponent;
import com.restonic4.citadel.world.object.components.LightComponent;
import com.restonic4.citadel.world.object.components.RigidBodyComponent;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Scene implements Serializable {
    private String name = getClass().getSimpleName();

    private Renderer renderer = new Renderer(this);
    private CameraComponent mainCamera;
    private Transform transform = new Transform();

    private List<GameObject> allGameObjects = new ArrayList<>();
    private List<GameObject> staticGameObjects = new ArrayList<>();
    private List<GameObject> dynamicGameObjects = new ArrayList<>();
    private List<GameObject> rootGameObjects = new ArrayList<>();

    private HashMap<Transform, GameObject> transformToGameObjectMap = new HashMap<>();

    private List<LightComponent> lightComponents = new ArrayList<>();
    private List<ColliderComponent> colliderComponents = new ArrayList<>();
    private List<RigidBodyComponent> rigidBodyComponents = new ArrayList<>();

    private boolean isActivated = false;
    private boolean hasBeenDeserialized = false;
    private Path sceneFile;

    public Scene() {}

    public Scene(Scene currentScene) {
        super();

        this.name = currentScene.name;
        this.transform = currentScene.transform;
        this.mainCamera = currentScene.mainCamera;
        this.allGameObjects = currentScene.allGameObjects;
        this.staticGameObjects = currentScene.staticGameObjects;
        this.dynamicGameObjects = currentScene.dynamicGameObjects;
        this.rootGameObjects = currentScene.rootGameObjects;
        this.transformToGameObjectMap = currentScene.transformToGameObjectMap;
        this.lightComponents = currentScene.lightComponents;
        this.colliderComponents = currentScene.colliderComponents;
        this.rigidBodyComponents = currentScene.rigidBodyComponents;
        this.hasBeenDeserialized = currentScene.hasBeenDeserialized;
        this.sceneFile = currentScene.sceneFile;
    }

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
        if (!isActivated) {
            return;
        }

        PhysicsManager.update();

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

        isActivated = false;

        SoundManager.getInstance().reset();

        System.gc();
    }

    public void addGameObject(GameObject gameObject) {
        EventResult eventResult = SceneEvents.ADDING_OBJECT.invoker().onSceneAddingObject(this, gameObject);
        if (eventResult == EventResult.CANCELED) {
            return;
        }

        List<GameObject> targetList = gameObject.isStatic() ? staticGameObjects : dynamicGameObjects;
        targetList.add(gameObject);
        allGameObjects.add(gameObject);

        LightComponent lightComponent = gameObject.getComponent(LightComponent.class);
        if (lightComponent != null) {
            lightComponents.add(lightComponent);
        }

        ColliderComponent colliderComponent = gameObject.getComponent(ColliderComponent.class);
        if (colliderComponent != null) {
            colliderComponents.add(colliderComponent);
        }

        RigidBodyComponent rigidBodyComponent = gameObject.getComponent(RigidBodyComponent.class);
        if (rigidBodyComponent != null) {
            rigidBodyComponents.add(rigidBodyComponent);
        }

        CameraComponent cameraComponent = gameObject.getComponent(CameraComponent.class);
        if (cameraComponent != null) {
            mainCamera = cameraComponent;
        }

        if (gameObject.transform.getParent() == null) {
            gameObject.transform.setParent(transform);
        }

        if (gameObject.transform.getParent() == transform) {
            rootGameObjects.add(gameObject);
        }

        transformToGameObjectMap.put(gameObject.transform, gameObject);

        if (isActivated) {
            gameObject.start();
            renderer.add(gameObject);
        }
    }

    public void addRootTransform(Transform transform) {
        GameObject gameObject = transformToGameObjectMap.get(transform);

        if (gameObject != null && !rootGameObjects.contains(gameObject)) {
            rootGameObjects.add(gameObject);
        }
    }

    public void removeRootTransform(Transform transform) {
        GameObject gameObject = transformToGameObjectMap.get(transform);

        if (gameObject != null && rootGameObjects.contains(gameObject)) {
            rootGameObjects.remove(gameObject);
        }
    }

    public List<GameObject> getTransformChildren(Transform transform) {
        List<GameObject> children = new ArrayList<>();

        for (int i = 0; i < allGameObjects.size(); i++) {
            Transform foundTransform = allGameObjects.get(i).transform;

            if (foundTransform.getParent() == transform) {
                children.add(allGameObjects.get(i));
            }
        }

        return children;
    }

    public boolean hasTransformChildren(Transform transform) {
        for (int i = 0; i < allGameObjects.size(); i++) {
            Transform foundTransform = allGameObjects.get(i).transform;

            if (foundTransform.getParent() == transform) {
                return true;
            }
        }

        return false;
    }

    public CameraComponent getMainCamera() {
        return this.mainCamera;
    }

    public Renderer getRenderer() {
        return this.renderer;
    }

    public List<GameObject> getGameObjects() {
        return allGameObjects;
    }

    public List<GameObject> getRootGameObjects() {
        return this.rootGameObjects;
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

    public List<ColliderComponent> getColliderComponents() {
        return colliderComponents;
    }

    public List<RigidBodyComponent> getRigidBodyComponents() {
        return rigidBodyComponents;
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

    public Transform getTransform() {
        return this.transform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void markAsDeserialized(Path path) {
        this.hasBeenDeserialized = true;
        this.sceneFile = path;
    }

    public Path getScenePath() {
        return this.sceneFile;
    }

    public boolean hasBeenDeserialized() {
        return this.hasBeenDeserialized;
    }

    public boolean isActivated() {
        return this.isActivated;
    }

    public void cleanup() {
        this.renderer.cleanup();
    }

    private String serializeObjects(List<GameObject> gameObjects) {
        String data = "";

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);

            data = StringBuilderHelper.concatenate(data, gameObject.serialize());

            List<GameObject> children = gameObject.transform.getChildren();
            if (!children.isEmpty()) {
                data = StringBuilderHelper.concatenate(data, "{", serializeObjects(children), "}");
            }

            if (i < gameObjects.size() - 1) {
                data = StringBuilderHelper.concatenate(data, "&");
            }
        }

        return data;
    }

    @Override
    public String serialize() {
        return serializeObjects(rootGameObjects);
    }

    public void processObjectHierarchy(String data) {
        Stack<GameObject> parents = new Stack<>();
        StringBuilder currentObject = new StringBuilder();

        parents.push(null);

        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);

            if (c == '&') {
                if (currentObject.length() > 0) {
                    createAndAddGameObject(currentObject.toString(), parents.peek());
                    currentObject.setLength(0);
                }
            } else if (c == '{') {
                if (currentObject.length() > 0) {
                    GameObject newObject = createAndAddGameObject(currentObject.toString(), parents.peek());
                    parents.push(newObject);
                    currentObject.setLength(0);
                }
            } else if (c == '}') {
                if (currentObject.length() > 0) {
                    createAndAddGameObject(currentObject.toString(), parents.peek());
                    currentObject.setLength(0);
                }
                parents.pop();
            } else {
                currentObject.append(c);
            }
        }

        if (currentObject.length() > 0) {
            createAndAddGameObject(currentObject.toString(), parents.peek());
        }
    }

    public GameObject createAndAddGameObject(String objectData, GameObject parent) {
        GameObject newGameObject = new GameObject();
        newGameObject.deserialize(objectData);

        if (parent != null) {
            newGameObject.transform.setParent(parent.transform);
        }

        addGameObject(newGameObject);

        return newGameObject;
    }

    @Override
    public Object deserialize(String data) {
        processObjectHierarchy(data);

        return this;
    }
}
