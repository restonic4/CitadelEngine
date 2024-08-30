package me.restonic4.citadel.files;

import me.restonic4.citadel.exceptions.FileException;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.world.object.Mesh;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class MeshLoader {
    //private static int defaultMeshFlags = (aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_CalcTangentSpace | aiProcess_LimitBoneWeights | aiProcess_GenBoundingBoxes);
    private static int defaultMeshFlags = aiProcess_Triangulate | aiProcess_JoinIdenticalVertices;

    public static Mesh loadMesh(String filePath) {
        String resourcePath = FileManager.toResources(filePath);

        if (FileManager.isFileInJar(resourcePath)) {
            extractPossibleNeededAssets(resourcePath);
            resourcePath = FileManager.extractFileFromJar(resourcePath);
        }

        AIScene aiScene = aiImportFile(resourcePath, defaultMeshFlags);
        if (aiScene == null) {
            throw new FileException("Error loading model [modelPath: " + resourcePath + "]");
        }

        List<Vector3f> verticesList = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();
        List<Vector4f> colorsList = new ArrayList<>();

        for (int i = 0; i < aiScene.mNumMeshes(); i++) {
            AIMesh mesh = AIMesh.create(aiScene.mMeshes().get(i));

            for (AIVector3D vector3f : mesh.mVertices()) {
                Logger.log("x:" + vector3f.x() + ", y:" + vector3f.y() + ", z:" + vector3f.z());
            }

            Logger.log("ola " + mesh.mNumVertices());

            // Read vertex
            for (int v = 0; v < mesh.mNumVertices(); v++) {
                verticesList.add(new Vector3f(mesh.mVertices().get(v).x(), mesh.mVertices().get(v).y(), mesh.mVertices().get(v).z()));

                // Read vertex colors
                if (mesh.mColors(0) != null) {
                    colorsList.add(new Vector4f(mesh.mColors(0).get(v).r(), mesh.mColors(0).get(v).g(), mesh.mColors(0).get(v).b(), mesh.mColors(0).get(v).a()));
                } else {
                    // Si no hay colores, agregar un color por defecto
                    colorsList.add(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
                }
            }

            // Read indices
            for (int f = 0; f < mesh.mNumFaces(); f++) {
                IntBuffer faceIndices = mesh.mFaces().get(f).mIndices();
                while (faceIndices.remaining() > 0) {
                    indicesList.add(faceIndices.get());
                }
            }
        }

        Vector3f[] verticesArray = verticesList.toArray(new Vector3f[0]);
        int[] indicesArray = indicesList.stream().mapToInt(i -> i).toArray();
        Vector4f[] verticesColors = colorsList.toArray(new Vector4f[0]);

        Assimp.aiReleaseImport(aiScene);

        return new Mesh(verticesArray, indicesArray, verticesColors);
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
