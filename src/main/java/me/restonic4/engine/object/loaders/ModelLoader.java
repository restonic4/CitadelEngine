package me.restonic4.engine.object.loaders;

import me.restonic4.engine.object.Mesh;
import me.restonic4.engine.util.FileManager;
import me.restonic4.engine.util.debug.Logger;

public class ModelLoader {
    public static Mesh loadMesh(String filepath) {
        String desiredPath = FileManager.toResources(filepath);
        GenericModelLoader loader = getDesiredLoader(desiredPath);
        String data = FileManager.readFile(desiredPath);

        return loader.loadMesh(data);
    }

    public static <T extends GenericModelLoader> T getDesiredLoader(String filePath) {
        if (filePath.endsWith(".obj")) {
            return (T) new OBJLoader();
        }

        throw new RuntimeException("Unknown model file type: " + filePath);
    }
}
