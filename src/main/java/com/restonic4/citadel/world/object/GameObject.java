package com.restonic4.citadel.world.object;

import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.UniqueIdentifierManager;
import com.restonic4.citadel.world.object.components.CameraComponent;
import com.restonic4.citadel.world.object.components.LightComponent;
import com.restonic4.citadel.world.object.components.ModelRendererComponent;
import org.joml.Vector3f;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameObject extends Serializable {
    public Transform transform;

    private int id;

    private String name;
    private List<Component> components;
    private boolean isStatic;
    private boolean isInsideFrustum;

    private Transform startingTransform;

    public GameObject() {
        this.id = UniqueIdentifierManager.generateUID();
        this.transform = new Transform();
        this.components = new ArrayList<>();
    }

    public GameObject(boolean isStatic) {
        this.id = UniqueIdentifierManager.generateUID();
        this.name = "GameObject";
        this.transform = new Transform();
        this.components = new ArrayList<>();
        this.isStatic = isStatic;
    }

    public GameObject(String name, boolean isStatic) {
        this.id = UniqueIdentifierManager.generateUID();
        this.name = name;
        this.transform = new Transform();
        this.components = new ArrayList<>();
        this.isStatic = isStatic;
    }

    public GameObject(String name, boolean isStatic, Transform transform) {
        this.id = UniqueIdentifierManager.generateUID();
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.isStatic = isStatic;
    }

    public GameObject(String name, boolean isStatic, Transform transform, List<Component> components) {
        this.id = UniqueIdentifierManager.generateUID();
        this.name = name;
        this.transform = transform;
        this.components = components;
        this.isStatic = isStatic;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                try {
                    return componentClass.cast(component);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    throw new IllegalStateException("Error casting component");
                }
            }
        }

        return null;
    }

    public List<Component> getComponents() {
        return this.components;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i=0; i < components.size(); i++) {
            Component component = components.get(i);

            if (componentClass.isAssignableFrom(component.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component) {
        this.components.add(component);
        component.gameObject = this;
    }

    public void update() {
        if (this.startingTransform == null) {
            this.startingTransform = this.transform.copy();
        }

        // Reset the transform if it changed and is static
        if (isStatic() && !this.transform.equals(this.startingTransform)) {
            this.transform.set(this.startingTransform);
        }

        // Traditional for-loop, because GC explodes so badly lol
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update();
        }

    }

    public void start() {
        for (Component component : components) {
            component.start();
        }
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public void setStatic(boolean value) {
        this.isStatic = value;
    }

    public void setName(String name) {
        if (!name.isBlank() && !name.isEmpty()) {
            this.name = name;
        }
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public void setInsideFrustum(boolean value) {
        this.isInsideFrustum = value;
    }

    public boolean isInsideFrustum() {
        return this.isInsideFrustum;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        return obj instanceof GameObject gameObject && gameObject.getId() == this.id;
    }

    @Override
    public String serialize() {
        String data = StringBuilderHelper.concatenate(name, "!", isStatic ? "t" : "f", "!", transform.serialize(), "!");

        for (int i = 0; i < components.size(); i++) {
            data = StringBuilderHelper.concatenate(data, components.get(i).serialize());

            if (i < components.size() - 1) {
                data = StringBuilderHelper.concatenate(data, "_");
            }
        }

        return data;
    }

    @Override
    public Object deserialize(String data) {
        String[] splits = data.split("!");

        setName(splits[0]);
        isStatic = Objects.equals(splits[1], "t");
        transform = (Transform) new Transform().deserialize(splits[2]);

        String[] components = splits[3].split("_");

        for (int i = 0; i < components.length; i++) {
            String[] cSplits = components[i].split("%");

            String componentPrefix = cSplits[0];

            Component newComponent = switch (componentPrefix) {
                case "mr" -> (Component) new ModelRendererComponent().deserialize(cSplits[1]);
                case "l" -> (Component) new LightComponent().deserialize(cSplits[1]);
                case "c" -> (Component) new CameraComponent().deserialize(cSplits[1]);
                default -> null;
            };

            if (newComponent != null) {
                this.addComponent(newComponent);
            }
        }

        return this;
    }
}
