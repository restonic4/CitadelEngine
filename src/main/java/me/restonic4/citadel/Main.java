package me.restonic4.citadel;

import me.restonic4.citadel.util.debug.DebugManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

public class Main {
    public static void main(String[] args) {
        DebugManager.setDebugMode(true);

        Logger.log("Starting the engine");

        Window.getInstance().run();
    }
}
