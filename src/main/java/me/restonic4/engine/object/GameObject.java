package me.restonic4.engine.object;

import me.restonic4.engine.util.Time;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    public Transform transform;
    private List<Component> components;

    public GameObject(String name) {
        this.name = name;
        this.transform = new Transform();
        this.components = new ArrayList<>();
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
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
        for (Component component : components) {
            component.update();
        }
    }

    public void start() {
        for (Component component : components) {
            component.start();
        }
    }

    public String getName() {
        return this.name;
    }
}
