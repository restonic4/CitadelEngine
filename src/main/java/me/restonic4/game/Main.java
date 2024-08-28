package me.restonic4.game;

import me.restonic4.citadel.CitadelLauncher;
import me.restonic4.citadel.util.debug.DebugManager;

public class Main {
    public static void main(String[] args) {
        DebugManager.setDebugMode(true);

        CitadelLauncher citadelLauncher = new CitadelLauncher(new Game());
        citadelLauncher.launch();
    }
}
