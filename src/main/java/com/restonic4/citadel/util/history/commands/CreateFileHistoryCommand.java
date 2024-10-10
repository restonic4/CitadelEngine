package com.restonic4.citadel.util.history.commands;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.registries.built_in.managers.ImGuiScreens;
import com.restonic4.citadel.render.gui.guis.editor.EditorAssetsImGui;
import com.restonic4.citadel.util.history.HistoryCommand;
import com.restonic4.citadel.world.object.GameObject;

import java.io.IOException;
import java.nio.file.Path;

public class CreateFileHistoryCommand implements HistoryCommand {
    private final String currentDir;
    private final String name;
    private final boolean isFile;

    public CreateFileHistoryCommand(String currentDir, String name, boolean isFile) {
        this.currentDir = currentDir;
        this.name = name;
        this.isFile = isFile;
    }

    @Override
    public void execute() {
        try {
            if (isFile) {
                FileManager.createFileInDirectory(currentDir, name);
            }
            else {
                FileManager.createFolderInDirectory(currentDir, name);
            }

            ImGuiScreens.EDITOR_ASSETS.reload();
        }
        catch (IOException e) {
            CitadelLauncher.getInstance().handleError(e.getMessage());
        }
    }

    @Override
    public void undo() {
        FileManager.deleteFileOrFolder(currentDir + "/" + name);
       ImGuiScreens.EDITOR_ASSETS.reload();
    }
}
