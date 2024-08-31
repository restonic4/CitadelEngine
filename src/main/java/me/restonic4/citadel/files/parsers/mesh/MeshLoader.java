package me.restonic4.citadel.files.parsers.mesh;

import me.restonic4.citadel.exceptions.FileException;
import me.restonic4.citadel.files.FileManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.world.object.Mesh;

public class MeshLoader {
    public static Mesh loadMesh(String filePath) {
        if (!filePath.contains(".")) {
            throw new FileException("Invalid file path, no extesion found");
        }

        String resourcePath = FileManager.toResources(filePath);
        String fileExtension = resourcePath.split("\\.")[1].toLowerCase();

        return switch (fileExtension) {
            case "obj" -> OBJLoader.loadMesh(resourcePath);
            default -> throw new FileException("Mesh file type not supported");
        };
    }

    public static void extractPossibleNeededAssets(String modelPath) {
        String[] relatedExtensions = {".mtl", ".md5anim", ".png", ".jpg", ".tga", ".dds"};

        String baseFileName = modelPath.substring(0, modelPath.lastIndexOf('.'));

        Logger.logExtra("Extracting extra resources from " + modelPath);

        for (String extension : relatedExtensions) {
            String relatedResourcePath = baseFileName + extension;

            if (FileManager.isFileInJar(relatedResourcePath)) {
                FileManager.extractFileFromJar(relatedResourcePath);
            }
        }
    }
}
