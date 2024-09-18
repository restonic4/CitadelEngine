package com.restonic4.citadel.platform;

import com.restonic4.citadel.exceptions.UnknownPlatformException;

import java.util.Locale;

public abstract class PlatformManager {
    private static OperatingSystems operatingSystem = getOperatingSystem();

    public static OperatingSystems getOperatingSystem() {
        if (operatingSystem != null) {
            return operatingSystem;
        }

        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        for (OperatingSystems os : OperatingSystems.values()) {
            if (os.matches(osName)) {
                return os;
            }
        }

        return OperatingSystems.UNKNOWN;
    }

    public static String getEndOfLine() {
        if (operatingSystem.isWindows()) {
            return "\r\n";
        }
        else if (operatingSystem.isLinux() || operatingSystem.isMac() || operatingSystem.isSolaris()) {
            return "\n";
        }

        throw new UnknownPlatformException();
    }
}

