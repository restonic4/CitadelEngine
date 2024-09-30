package com.restonic4.test;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.CitadelSettings;

public class TestMain {
    public static void main(String[] args) {
        CitadelSettings citadelSettings = new CitadelSettings(new TestClient(), new TestServer(), new TestShared(), "TestGame", args);
        citadelSettings.setEditorMode(true);

        CitadelLauncher.create(citadelSettings).launch();
    }
}