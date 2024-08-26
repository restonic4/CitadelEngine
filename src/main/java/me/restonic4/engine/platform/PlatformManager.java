package me.restonic4.engine.platform;

import me.restonic4.engine.exceptions.UnknownPlatformException;
import me.restonic4.engine.util.debug.diagnosis.Logger;

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
                Logger.log("Playing on " + os.getId());
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

