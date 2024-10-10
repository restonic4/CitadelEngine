package com.restonic4.citadel.util.history.commands;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.registries.built_in.managers.ImGuiScreens;
import com.restonic4.citadel.render.gui.guis.editor.EditorAssetsImGui;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.util.history.HistoryCommand;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteFileHistoryCommand implements HistoryCommand {
    private final String filePath;
    private final boolean isFile;
    private String data;
    private final DirectoryData directoryData;

    public DeleteFileHistoryCommand(String filePath) {
        this.filePath = filePath;
        Path path = Paths.get(filePath);
        this.isFile = Files.isRegularFile(path);
        this.directoryData = isFile ? null : new DirectoryData(path.getFileName().toString());

        if (isFile) {
            this.data = readFileContent(path);
        } else {
            getFilesInDirectory(path, directoryData);
        }
    }

    private String readFileContent(Path path) {
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            Logger.logError(e);
            return null;
        }
    }

    private void getFilesInDirectory(Path directoryPath, DirectoryData dirData) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    DirectoryData subDirData = new DirectoryData(path.getFileName().toString());
                    dirData.addSubDirectory(subDirData);
                    getFilesInDirectory(path, subDirData);
                } else {
                    String content = readFileContent(path);
                    dirData.addFile(new FileData(path.getFileName().toString(), content));
                }
            }
        } catch (IOException e) {
            Logger.logError(e);
        }
    }

    @Override
    public void execute() {
        FileManager.deleteFileOrFolder(filePath);
        ImGuiScreens.EDITOR_ASSETS.reload();
    }

    @Override
    public void undo() {
        Path path = Paths.get(filePath);
        try {
            if (isFile) {
                Files.write(path, data.getBytes(), StandardOpenOption.CREATE_NEW);
            } else {
                Files.createDirectories(path);
                restoreDirectory(path, directoryData);
            }
        } catch (IOException e) {
            Logger.logError(e);
        }

        ImGuiScreens.EDITOR_ASSETS.reload();
    }

    private void restoreDirectory(Path parentPath, DirectoryData dirData) {
        // Restaurar archivos
        for (FileData fileData : dirData.getFiles()) {
            Path newFilePath = parentPath.resolve(fileData.getName());
            try {
                Files.write(newFilePath, fileData.getContent().getBytes(), StandardOpenOption.CREATE_NEW);
            } catch (IOException e) {
                Logger.logError(e);
            }
        }

        // Restaurar subdirectorios
        for (DirectoryData subDirData : dirData.getSubDirectories()) {
            Path newDirPath = parentPath.resolve(subDirData.getName());
            try {
                Files.createDirectories(newDirPath);
                restoreDirectory(newDirPath, subDirData);
            } catch (IOException e) {
                Logger.logError(e);
            }
        }
    }

    private static class FileData {
        private final String name;
        private final String content;

        public FileData(String name, String content) {
            this.name = name;
            this.content = content;
        }

        public String getName() {
            return name;
        }

        public String getContent() {
            return content;
        }
    }

    private static class DirectoryData {
        private final String name;
        private final List<FileData> files;
        private final List<DirectoryData> subDirectories;

        public DirectoryData(String name) {
            this.name = name;
            this.files = new ArrayList<>();
            this.subDirectories = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public List<FileData> getFiles() {
            return files;
        }

        public List<DirectoryData> getSubDirectories() {
            return subDirectories;
        }

        public void addFile(FileData fileData) {
            files.add(fileData);
        }

        public void addSubDirectory(DirectoryData dirData) {
            subDirectories.add(dirData);
        }
    }
}
