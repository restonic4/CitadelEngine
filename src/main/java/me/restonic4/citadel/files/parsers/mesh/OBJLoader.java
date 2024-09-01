package me.restonic4.citadel.files.parsers.mesh;

import me.restonic4.citadel.exceptions.FileException;
import me.restonic4.citadel.files.FileManager;
import me.restonic4.citadel.world.object.Mesh;
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
    /**
     * Creates a Mesh from a file path
     * @param filePath Valid file path, you should check if it's valid before calling this method.
     * @return A new Mesh with the vertex and indices data.
     *
     * @author restonic4
     */
    public static Mesh loadMesh(String filePath) {
        String fileData = FileManager.readFile(filePath);

        Scanner scanner = new Scanner(fileData);

        List<Vector3f> vertexList = new ArrayList<>();
        List<Vector2f> uvList = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.startsWith("v ")) { // Vertex
                parseVertex(vertexList, line);
            } else if (line.startsWith("vt ")) { // UVs
                parseUV(uvList, line);
            } else if (line.startsWith("f ")) { // Indices
                parseIndices(indicesList, line);
            }
        }

        scanner.close();

        Mesh mesh = new Mesh(vertexList.toArray(new Vector3f[0]), indicesList.stream().mapToInt(i -> i).toArray());
        mesh.setUVs(uvList.toArray(new Vector2f[0]));

        return mesh;
    }

    /**
     * Parses the vertex data, which has a 'v' as an indicator to a Vector3f and adds it to the desired list.
     * @param list The list where the new Vector3f should be placed.
     * @param line The line of text to parse and translate to a Vector3f.
     *
     * @author restonic4
     */
    public static void parseVertex(List<Vector3f> list, String line) {
        String[] currentLine = line.split(" ");

        // I am doing +1 because 0 is the keyword, v
        Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));

        list.add(vertex);
    }

    /**
     * Parses the uv data, which has a 'vt' as an indicator to a Vector2f and adds it to the desired list.
     * @param list The list where the new Vector2f should be placed.
     * @param line The line of text to parse and translate to a Vector2f.
     *
     * @author restonic4
     */
    public static void parseUV(List<Vector2f> list, String line) {
        String[] currentLine = line.split(" ");

        // I am doing +1 because 0 is the keyword, vt
        Vector2f vertex = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));

        list.add(vertex);
    }

    /**
     * Parses the indices data, which has a 'f' as an indicator to an int and adds it to the desired list.
     * Example: 1/1/1, the first 1 is a vertex indices, so we should read all the /// in the line but only choose the first number.
     * @param list The list where the new int should be placed.
     * @param line The line of text to parse and translate to an int.
     *
     * @author restonic4
     */
    public static void parseIndices(List<Integer> list, String line) {
        String[] currentLine = line.split(" ");

        // The first white-space is f, that's why its 4
        if (currentLine.length != 4) {
            throw new FileException("This model is not triangulated");
        }

        // -1 because OBJ files adds +1, instead of starting at 0
        int first = Integer.parseInt(currentLine[1].split("/")[0]) - 1;
        int second = Integer.parseInt(currentLine[2].split("/")[0]) - 1;
        int third = Integer.parseInt(currentLine[3].split("/")[0]) - 1;

        list.add(first);
        list.add(second);
        list.add(third);
    }
}
