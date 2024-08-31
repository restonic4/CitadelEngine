package me.restonic4.citadel.files.parsers.mesh;

import me.restonic4.citadel.exceptions.FileException;
import me.restonic4.citadel.files.FileManager;
import me.restonic4.citadel.world.object.Mesh;
import org.joml.Vector3f;

import java.util.*;

public class OBJLoader {
    public static Mesh loadMesh(String filePath) {
        String fileData = FileManager.readFile(filePath);

        Scanner scanner = new Scanner(fileData);

        List<Vector3f> vertexList = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.startsWith("v ")) { // Vertex
                parseVertex(vertexList, line);
            } else if (line.startsWith("f ")) { // Indices
                parseIndices(indicesList, line);
            }
        }

        scanner.close();

        return new Mesh(convertVectorListToArray(vertexList), convertIntListToArray(indicesList));
    }

    public static void parseVertex(List<Vector3f> list, String line) {
        String[] currentLine = line.split(" ");

        // I am doing +1 because 0 is the keyword, v
        Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));

        list.add(vertex);
    }

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

    public static Vector3f[] convertVectorListToArray(List<Vector3f> list) {
        Vector3f[] array = new Vector3f[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    public static int[] convertIntListToArray(List<Integer> list) {
        int[] array = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }
}
