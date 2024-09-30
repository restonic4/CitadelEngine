package com.restonic4.citadel.util;

public class UniqueIdentifierManager {
    private static int lastKnownID = 0;

    public static int generateUniqueID() {
        lastKnownID++;
        return lastKnownID;
    }

    public static int predictNextID() {
        return lastKnownID + 1;
    }
}
