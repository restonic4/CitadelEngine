package me.restonic4.game;

import me.restonic4.engine.SceneManager;
import me.restonic4.engine.Window;
import me.restonic4.game.core.scenes.WorldScene;

public class Game {
    private static Game instance = null;

    public static Game getInstance() {
        if (Game.instance == null) {
            Game.instance = new Game();
        }
        return Game.instance;
    }

    public void start() {
        SceneManager.getInstance().loadScene(new WorldScene());
    }
}
