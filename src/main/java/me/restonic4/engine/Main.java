package me.restonic4.engine;

import me.restonic4.engine.util.debug.DebugManager;
import me.restonic4.engine.util.debug.diagnosis.Logger;

public class Main {
    public static void main(String[] args) {
        DebugManager.setDebugMode(false);

        Logger.log("Starting the engine");

        Window.getInstance().run();
    }
}
