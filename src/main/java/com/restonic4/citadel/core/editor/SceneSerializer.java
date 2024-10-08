package com.restonic4.citadel.core.editor;

import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.Scene;
import com.restonic4.test.TestScene;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SceneSerializer {
    public void saveScene(Scene scene, String filePath) {
        String data = scene.serialize();

        try {
            Files.write(Paths.get(filePath), data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Logger.log("Scene saved at: " + filePath);
        } catch (IOException e) {
            Logger.logError(e);
        }
    }

    public Scene loadScene(String filePath) {
        return new TestScene();
    }
}
