package me.restonic4.engine;

import me.restonic4.engine.graph.Render;
import me.restonic4.engine.scene.Scene;

public interface IEngineLogic {

    void cleanup();

    void init(Window window, Scene scene, Render render);

    void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed);

    void update(Window window, Scene scene, long diffTimeMillis);
}