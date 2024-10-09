package com.restonic4.citadel.util.history.commands;

import com.restonic4.citadel.registries.built_in.managers.ImGuiScreens;
import com.restonic4.citadel.render.gui.guis.editor.EditorAssetsImGui;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.util.history.HistoryCommand;
import com.restonic4.citadel.world.object.GameObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class RenameFileHistoryCommand implements HistoryCommand {
    private final Path path;
    private final String newName;
    private String oldName;

    public RenameFileHistoryCommand(Path path, String newName) {
        this.path = path;
        this.newName = newName;
        this.oldName = path.getFileName().toString();
    }

    @Override
    public void execute() {
        this.oldName = path.getFileName().toString();
        Path newPath = path.resolveSibling(newName);

        try {
            Files.move(path, newPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Logger.logError(e);
        }

        ((EditorAssetsImGui) ImGuiScreens.EDITOR_ASSETS).reload();
    }

    @Override
    public void undo() {
        Path newPath = path.resolveSibling(newName);
        Path oldPath = path.resolveSibling(oldName);

        try {
            Files.move(newPath, oldPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Logger.logError(e);
        }

        ((EditorAssetsImGui) ImGuiScreens.EDITOR_ASSETS).reload();
    }
}
