package com.restonic4.citadel.util;

public class UniqueIdentifierManager {
    private static int lastKnownUID = 0;

    public static int generateUID() {
        lastKnownUID++;
        return lastKnownUID;
    }

    public static int predictNextUID() {
        return lastKnownUID + 1;
    }
}
