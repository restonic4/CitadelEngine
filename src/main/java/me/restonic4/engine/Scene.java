package me.restonic4.engine;

import me.restonic4.engine.util.debug.Logger;

public abstract class Scene {
    public void init() {
        Logger.log("Initializing the scene");
    }

    public abstract void update();
}
