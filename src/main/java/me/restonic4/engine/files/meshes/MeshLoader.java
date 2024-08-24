package me.restonic4.engine.files.meshes;

import me.restonic4.engine.exceptions.FileException;
import me.restonic4.engine.object.Mesh;
import me.restonic4.engine.files.FileManager;

public class MeshLoader {
    public static Mesh loadMesh(String filepath) {
        String desiredPath = FileManager.toResources(filepath);
        GenericMeshLoader loader = getDesiredLoader(desiredPath);
        String data = FileManager.readFile(desiredPath);

        return loader.loadMesh(data);
    }

    public static <T extends GenericMeshLoader> T getDesiredLoader(String filePath) {
        if (filePath.endsWith(".obj")) {
            return (T) new OBJLoader();
        }

        throw new FileException("Unknown model file type: " + filePath);
    }
}
