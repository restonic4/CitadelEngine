package me.restonic4.engine.object.loaders;

import me.restonic4.engine.object.Mesh;

public class ModelLoader {
    public static Mesh loadMesh(String filepath) {
        GenericModelLoader loader = getDesiredLoader(filepath);
        return loader.loadMesh(filepath);
    }

    public static <T extends GenericModelLoader> T getDesiredLoader(String filePath) {
        if (filePath.endsWith(".obj")) {
            return (T) new OBJLoader();
        }

        throw new RuntimeException("Unknown model file type: " + filePath);
    }
}
