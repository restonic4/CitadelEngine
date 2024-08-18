package me.restonic4.engine.platform;

import me.restonic4.engine.exceptions.UnknownPlatformException;

public class PlatformManager {
    // TODO: Implement isWindows, isLinux and isMac

    public static boolean isWindows() {
        return true;
    }

    public static boolean isLinux() {
        return false;
    }

    public static boolean isMac() {
        return false;
    }

    public static String getEndOfLine() {
        if (isWindows()) {
            return "\r\n";
        }
        else if (isLinux() || isMac()) {
            return "\n";
        }

        throw new UnknownPlatformException();
    }
}
