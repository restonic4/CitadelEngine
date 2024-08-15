package me.restonic4.engine.util;

import me.restonic4.engine.util.debug.DebugLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.lwjgl.stb.STBImage.*;

public class FileManager {
    public static final String RESOURCES_PATH = "resources/";

    public static String toResources(String filePath) {
        filePath = filePath.replace("\\", "/"); // Replaces \ to /

        // Checks if you already got the prefix
        if (!filePath.startsWith(RESOURCES_PATH)) {
            filePath = RESOURCES_PATH + filePath;
        }

        return filePath;
    }

    public static boolean isFileInJar(String filePath) {
        InputStream is = Utils.class.getResourceAsStream("/" + filePath);

        if (is != null) {
            try (InputStream inputStream = is) {
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        return false;
    }

    public static boolean isFileInSystem(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }

    public static File getFile(String filePath) {
        if (isFileInJar(filePath)) {
            return new File(filePath);
        }
        else if (isFileInSystem(filePath)) {
            return new File(filePath);
        }

        throw new RuntimeException("Error getting file from jar/system: " + filePath);
    }

    public static String readFile(String filePath) {
        if (isFileInJar(filePath)) {
            return readFileFromJar(filePath);
        }
        else if (isFileInSystem(filePath)) {
            return readFileFromSystem(filePath);
        }

        throw new RuntimeException("Error reading file from jar/system: " + filePath);
    }

    public static String readFileFromJar(String filePath) {
        InputStream is = Utils.class.getResourceAsStream("/" + filePath);

        if (is != null) {
            try (InputStream inputStream = is) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Error reading file from jar: " + filePath, e);
            }
        } else {
            throw new IllegalStateException("Error reading file from jar: " + filePath);
        }
    }

    public static String readFileFromSystem(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }

    public static String removeResourcesPrefix(String filePath) {
        String normalizedFilePath = filePath.replace("\\", "/");
        String normalizedResourcesPath = RESOURCES_PATH.replace("\\", "/");

        if (normalizedFilePath.startsWith(normalizedResourcesPath)) {
            return normalizedFilePath.substring(normalizedResourcesPath.length());
        }

        return filePath;
    }

    public static ByteBuffer getTexture(String filepath, IntBuffer width, IntBuffer height, IntBuffer channels) {
        try {
            System.out.println("wait: " + isFileInJar(filepath));
            if (isFileInJar(filepath)) {
                InputStream is = Utils.class.getResourceAsStream("/" + filepath);

                if (is == null) {
                    throw new RuntimeException("Failed to load image: " + filepath + ". The input stream is null");
                }

                BufferedImage img = ImageIO.read(is);

                System.out.println(filepath);
                System.out.println(is);
                System.out.println(img);

                ByteBuffer scratch = ByteBuffer.allocateDirect(4 * img.getWidth() * img.getHeight());

                byte data[] = (byte[]) img.getRaster().getDataElements(0, 0, img.getWidth(), img.getHeight(), null);
                scratch.clear();
                scratch.put(data);
                scratch.rewind();

                return scratch;
            }
            else {
                return stbi_load(filepath, width, height, channels, 4);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractFileFromJar(String resourcePath) {
        try (InputStream inputStream = FileManager.class.getResourceAsStream("/" + resourcePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }

            // Temporal file
            Path tempFile = Files.createTempFile("model", ".tmp");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            DebugLogger.log("Temporal file created: " + resourcePath);

            return tempFile.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract file from JAR: " + resourcePath, e);
        }
    }
}
