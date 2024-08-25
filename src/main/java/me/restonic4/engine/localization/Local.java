package me.restonic4.engine.localization;

public enum Local {
    EN_US,
    ES_ES;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
