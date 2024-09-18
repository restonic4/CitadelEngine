package com.restonic4.citadel.files.parsers.mesh;

import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.world.object.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.*;

/**
 * A utility class for loading and parsing OBJ files into Mesh objects.
 * <p>
 * This class parses the basic elements of an OBJ file such as vertices and face indices.
 * It currently supports only triangulated OBJ files.
 * </p>
 * <p>
 * OBJ documentation: <a href="https://www.martinreddy.net/gfx/3d/OBJ.spec">OBJ Spec</a>
 * <br>
 * If the website gets down in the future, a snapshot has been saved on the web archive
 * (Save date: 31/08/2024 20:00;
 * <a href="https://web.archive.org/web/20240731091045/http://www.martinreddy.net/gfx/3d/OBJ.spec">Web Archive Snapshot</a>)
 * </p>
 *
 * @author restonic4
 */
public class OBJLoader {
    public static Mesh loadMesh(String filePath) {
        String fileData = FileManager.readFile(filePath);

        Scanner scanner = new Scanner(fileData);

        List<Vector3f> vertexList = new ArrayList<>();
        List<Vector2f> uvList = new ArrayList<>();
        List<Vector3f> normalList = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();

        // Este map ayuda a identificar vértices únicos
        Map<String, Integer> uniqueVerticesMap = new HashMap<>();
        List<Vector3f> finalVertices = new ArrayList<>();
        List<Vector2f> finalUVs = new ArrayList<>();
        List<Vector3f> finalNormals = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.startsWith("v ")) { // Vertex
                parseVertex(vertexList, line);
            } else if (line.startsWith("vt ")) { // UVs
                parseUV(uvList, line);
            } else if (line.startsWith("vn ")) { // Normales
                parseNormal(normalList, line);
            } else if (line.startsWith("f ")) { // Indices
                parseFace(indicesList, line, vertexList, uvList, normalList, finalVertices, finalUVs, finalNormals, uniqueVerticesMap);
            }
        }

        scanner.close();

        Mesh mesh = new Mesh(finalVertices.toArray(new Vector3f[0]), indicesList.stream().mapToInt(i -> i).toArray());
        mesh.setUVs(finalUVs.toArray(new Vector2f[0]));
        mesh.setNormals(finalNormals.toArray(new Vector3f[0]));

        //Logger.log("Final vertex: " + finalVertices.size());

        return mesh;
    }

    public static void parseFace(List<Integer> indicesList, String line, List<Vector3f> vertexList, List<Vector2f> uvList, List<Vector3f> normalList, List<Vector3f> finalVertices, List<Vector2f> finalUVs, List<Vector3f> finalNormals, Map<String, Integer> uniqueVerticesMap) {
        String[] currentLine = line.split(" ");

        for (int i = 1; i <= 3; i++) {
            String[] vertexData = currentLine[i].split("/");

            int vertexIndex = Integer.parseInt(vertexData[0]) - 1;
            Vector3f vertex = vertexList.get(vertexIndex);

            Vector2f uv = null;
            if (vertexData.length > 1 && !vertexData[1].isEmpty()) {
                int uvIndex = Integer.parseInt(vertexData[1]) - 1;
                uv = uvList.get(uvIndex);
            }

            Vector3f normal = null;
            if (vertexData.length > 2 && !vertexData[2].isEmpty()) {
                int normalIndex = Integer.parseInt(vertexData[2]) - 1;
                normal = normalList.get(normalIndex);
            }

            String uniqueKey = vertexIndex + "/" + (uv != null ? uv.toString() : "null") + "/" + (normal != null ? normal.toString() : "null");

            if (uniqueVerticesMap.containsKey(uniqueKey)) {
                indicesList.add(uniqueVerticesMap.get(uniqueKey));
            } else {
                finalVertices.add(vertex);
                finalUVs.add(uv != null ? uv : new Vector2f(0, 0));
                finalNormals.add(normal != null ? normal : new Vector3f(0, 0, 0));

                int newIndex = finalVertices.size() - 1;
                uniqueVerticesMap.put(uniqueKey, newIndex);
                indicesList.add(newIndex);
            }
        }
    }

    public static void parseVertex(List<Vector3f> list, String line) {
        String[] currentLine = line.split(" ");
        Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
        list.add(vertex);
    }

    public static void parseUV(List<Vector2f> list, String line) {
        String[] currentLine = line.split(" ");
        Vector2f uv = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
        list.add(uv);
    }

    public static void parseNormal(List<Vector3f> list, String line) {
        String[] currentLine = line.split(" ");
        Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
        list.add(normal);
    }

    /*public static Mesh optimizeToRender(Mesh mesh) {
        if (mesh.getTexture() != null) {
            List<Vector3f> optimizedVertices = new ArrayList<>();
            List<Vector3f> optimizedNormals = new ArrayList<>();
            List<Integer> optimizedIndices = new ArrayList<>();

            Map<String, Integer> uniqueVerticesMap = new HashMap<>();

            Vector3f[] vertices = mesh.getVertices();
            Vector3f[] normals = mesh.getNormals();
            int[] indices = mesh.getIndices();

            for (int i = 0; i < indices.length; i++) {
                int vertexIndex = indices[i];
                Vector3f vertex = vertices[vertexIndex];
                Vector3f normal = normals != null ? normals[vertexIndex] : new Vector3f(0, 0, 0);

                String uniqueKey = vertex.toString() + "/" + (normal != null ? normal.toString() : "null");

                if (uniqueVerticesMap.containsKey(uniqueKey)) {
                    optimizedIndices.add(uniqueVerticesMap.get(uniqueKey));
                } else {
                    optimizedVertices.add(vertex);
                    optimizedNormals.add(normal);

                    int newIndex = optimizedVertices.size() - 1;
                    uniqueVerticesMap.put(uniqueKey, newIndex);
                    optimizedIndices.add(newIndex);
                }
            }

            Mesh optimizedMesh = new Mesh(
                    optimizedVertices.toArray(new Vector3f[0]),
                    optimizedIndices.stream().mapToInt(i -> i).toArray()
            );
            optimizedMesh.setNormals(optimizedNormals.toArray(new Vector3f[0]));

            return optimizedMesh;
        }

        return mesh;
    }*/
}
