package com.restonic4.test;

import com.restonic4.citadel.core.GameLogic;
import com.restonic4.citadel.core.editor.SceneSerializer;
import com.restonic4.citadel.util.StringHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.SceneManager;

public class TestClient implements GameLogic {
    @Override
    public void start() {
        SceneManager.loadScene(new TestScene());
        try {
            SceneSerializer sceneSerializer = new SceneSerializer();
            sceneSerializer.saveScene(SceneManager.getCurrentScene(), "sceneTest");

            Scene scene = sceneSerializer.loadScene("sceneTest");
        }
        catch (Exception exception) {
            Logger.logError(exception);
        }
    }

    @Override
    public void update() {

    }
}
