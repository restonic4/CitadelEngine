package com.restonic4.citadel.util;

public class FlagFactory {
    public static int createFlag(int id) {
        if (id < 0 || id > 31) {
            throw new IllegalArgumentException("ID must be between 0 and 31.");
        }
        return 1 << id;
    }

    public static boolean hasFlag(int flags, int flag) {
        return (flags & flag) != 0;
    }

    public static int addFlag(int flags, int flag) {
        return flags | flag;
    }

    public static int removeFlag(int flags, int flag) {
        return flags & ~flag;
    }

    public static String showFlags(int flags) {
        return String.format("Flags (binary): %s", Integer.toBinaryString(flags));
    }

    public static boolean isEmpty(int flags) {
        return flags == 0;
    }
}
