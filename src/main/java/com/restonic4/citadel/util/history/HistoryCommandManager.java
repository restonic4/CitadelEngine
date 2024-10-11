package com.restonic4.citadel.util.history;

import com.restonic4.citadel.core.editor.LevelEditor;

import java.util.Stack;

public class HistoryCommandManager {
    private Stack<HistoryCommand> undoStack = new Stack<>();
    private Stack<HistoryCommand> redoStack = new Stack<>();

    public void executeCommand(HistoryCommand command) {
        command.execute();
        if (!LevelEditor.isIsPlaying()) {
            undoStack.push(command);
            redoStack.clear();
        }
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            HistoryCommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            HistoryCommand command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }
}
