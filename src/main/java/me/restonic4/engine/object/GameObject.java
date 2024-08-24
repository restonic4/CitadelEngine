package me.restonic4.engine.object;

import me.restonic4.engine.SceneManager;
import me.restonic4.engine.util.Time;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    public Transform transform;

    private String name;
    private List<Component> components;
    private boolean isStatic;

    private Transform startingTransform;

    public GameObject(boolean isStatic) {
        this.name = "GameObject";
        this.transform = new Transform();
        this.startingTransform = new Transform();
        this.components = new ArrayList<>();
        this.isStatic = isStatic;
    }

    public GameObject(String name, boolean isStatic) {
        this.name = name;
        this.transform = new Transform();
        this.startingTransform = new Transform();
        this.components = new ArrayList<>();
        this.isStatic = isStatic;
    }

    public GameObject(String name, boolean isStatic, Transform transform) {
        this.name = name;
        this.transform = transform;
        this.startingTransform = transform.copy();
        this.components = new ArrayList<>();
        this.isStatic = isStatic;
    }

    public GameObject(String name, boolean isStatic, Transform transform, List<Component> components) {
        this.name = name;
        this.transform = transform;
        this.startingTransform = transform.copy();
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
        // Reset the transform if it changed and is static
        if (isStatic() && !this.transform.equals(this.startingTransform)) {
            this.transform.set(this.startingTransform);
        }

        for (Component component : components) {
            component.update();
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
