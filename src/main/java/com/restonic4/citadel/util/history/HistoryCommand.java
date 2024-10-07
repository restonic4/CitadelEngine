package com.restonic4.citadel.util.history;

public interface HistoryCommand {
    void execute();
    void undo();
}
