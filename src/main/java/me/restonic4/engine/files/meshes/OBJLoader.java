package me.restonic4.engine.files.meshes;

import me.restonic4.engine.object.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class OBJLoader extends GenericMeshLoader {

    @Override
    public Mesh loadMesh(String data) {
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] textureArray = null;
        float[] normalsArray = null;

        // Divide el contenido del archivo en líneas
        String[] lines = data.split("\n");

        for (String line : lines) {
            String[] tokens = line.trim().split("\\s+"); // trim elimina espacios en blanco al inicio y al final
            if (tokens.length == 0) {
                continue; // Salta líneas vacías
            }
            switch (tokens[0]) {
                case "v": // Vertex
                    Vector3f vertex = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    vertices.add(vertex);
                    break;
                case "vt": // Texture
                    Vector2f texture = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    textures.add(texture);
                    break;
                case "vn": // Normals
                    Vector3f normal = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normals.add(normal);
                    break;
                case "f": // Face
                    if (textureArray == null) {
                        textureArray = new float[vertices.size() * 2];
                    }
                    if (normalsArray == null) {
                        normalsArray = new float[vertices.size() * 3];
                    }
                    processFace(tokens, indices, textures, normals, textureArray, normalsArray);
                    break;
                default:
                    // Ignora otros elementos
                    break;
            }
        }

        Vector3f[] verticesArray = vertices.toArray(new Vector3f[0]);
        int[] indicesArray = indices.stream().mapToInt(i -> i).toArray();

        return new Mesh(verticesArray, indicesArray);
    }

    private static void processFace(String[] tokens, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
        for (int i = 1; i < tokens.length; i++) {
            String[] vertexData = tokens[i].split("/");
            int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
            indices.add(currentVertexPointer);

            if (vertexData.length > 1 && !vertexData[1].isEmpty()) {
                Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
                textureArray[currentVertexPointer * 2] = currentTex.x;
                textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
            }

            if (vertexData.length > 2 && !vertexData[2].isEmpty()) {
                Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
                normalsArray[currentVertexPointer * 3] = currentNorm.x;
                normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
                normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
            }
        }
    }
}
