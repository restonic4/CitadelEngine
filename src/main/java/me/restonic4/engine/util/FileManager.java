package me.restonic4.engine.util;

import me.restonic4.engine.util.debug.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

    public static String extractFileFromJar(String resourcePath) {
        try (InputStream inputStream = FileManager.class.getResourceAsStream("/" + resourcePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }

            Path relativePath = Paths.get(resourcePath);
            String fileName = relativePath.getFileName().toString();

            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), Constants.APP_NAME);
            if (!Files.exists(tempDir)) {
                Files.createDirectory(tempDir);
            }

            Path tempFilePath = tempDir.resolve(relativePath);
            Files.createDirectories(tempFilePath.getParent());

            Files.copy(inputStream, tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            Logger.logExtra("Temporal file created: " + tempFilePath);

            return tempFilePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract file from JAR: " + resourcePath, e);
        }
    }

    public static String getMainDirectory() {
        String appDataDir = System.getenv("LOCALAPPDATA");
        if (appDataDir == null) {
            appDataDir = System.getenv("APPDATA");
        }

        if (appDataDir == null) {
            throw new RuntimeException("appDataDir is null. Could not create the directory");
        }

        appDataDir = appDataDir + "/" + Constants.APP_NAME;

        return appDataDir;
    }

    public static String createDirectory(String directory) {
        File appDir = new File(getMainDirectory());

        if (!appDir.exists()) {
            boolean created = appDir.mkdir();

            if (!created) {
                throw new RuntimeException("Could not create the APP directory: " + appDir.getAbsolutePath());
            }
        }

        File directoryFile = new File(appDir, directory);

        if (!directoryFile.exists()) {
            boolean created = directoryFile.mkdir();

            if (!created) {
                throw new RuntimeException("Could not create the directory: " + directoryFile.getAbsolutePath());
            }
        }

        return directoryFile.getAbsolutePath();
    }
}
