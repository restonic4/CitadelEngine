package me.restonic4.engine;

import me.restonic4.engine.util.debug.DebugManager;
import me.restonic4.engine.util.debug.Logger;
import me.restonic4.game.Game;

public class Main {
    public static void main(String[] args) {
        DebugManager.setDebugMode(false);

        Logger.log("Starting the game");

        Window.getInstance().run();
    }
}
