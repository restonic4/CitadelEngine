package me.restonic4.game;

import me.restonic4.engine.Window;

public class Game {
    private static Game instance = null;

    public Game() {
        Window window = Window.getInstance();
        window.run();
    }

    public static Game getInstance() {
        if (Game.instance == null) {
            Game.instance = new Game();
        }
        return Game.instance;
    }
}
