package me.restonic4.engine.world.object;

public abstract class Component {
    public GameObject gameObject = null;

    public void start() {

    }

    public abstract void update();
}
