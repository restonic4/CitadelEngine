package me.restonic4.engine;

import me.restonic4.engine.util.debug.DebugManager;
import me.restonic4.engine.util.debug.Logger;
import me.restonic4.game.Game;

public class Main {
    public static void main(String[] args) {
        DebugManager.setDebugMode(true);

        Logger.log("Starting the game");

        Game game = Game.getInstance();
    }
}