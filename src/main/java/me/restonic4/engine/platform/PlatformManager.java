package me.restonic4.engine.platform;

import me.restonic4.engine.exceptions.UnknownPlatformException;
import me.restonic4.engine.util.debug.Logger;

import java.util.Locale;

public class PlatformManager {
    private static PlatformManager instance;

    private OperatingSystem operatingSystem = getOperatingSystem();

    public static PlatformManager getInstance() {
        if (PlatformManager.instance == null) {
            PlatformManager.instance = new PlatformManager();
        }
        return PlatformManager.instance;
    }

    public OperatingSystem getOperatingSystem() {
        if (this.operatingSystem != null) {
            return this.operatingSystem;
        }

        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        for (OperatingSystem os : OperatingSystem.values()) {
            if (os.matches(osName)) {
                Logger.log("Playing on " + os.id);
                return os;
            }
        }

        return OperatingSystem.UNKNOWN;
    }

    public String getEndOfLine() {
        if (operatingSystem.isWindows()) {
            return "\r\n";
        }
        else if (operatingSystem.isLinux() || operatingSystem.isMac() || operatingSystem.isSolaris()) {
            return "\n";
        }

        throw new UnknownPlatformException();
    }

    public enum OperatingSystem {
        WINDOWS("windows", new String[]{
                "win"
        }),
        LINUX("linux", new String[]{
                "linux", "unix"
        }),
        MAC("mac", new String[]{
                "mac"
        }),
        SOLARIS("solaris", new String[]{
                "solaris", "sunos"
        }),
        UNKNOWN("unknown");

        private String id;
        private String[] keywords;

        OperatingSystem(String id) {
            this.id = id;
            this.keywords = null;
        }

        OperatingSystem(String id, String[] keywords) {
            this.id = id;
            this.keywords = keywords;
        }

        public boolean matches(String osName) {
            for (String keyword : keywords) {
                if (osName.contains(keyword)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isWindows() {
            return this == WINDOWS;
        }

        public boolean isLinux() {
            return this == LINUX;
        }

        public boolean isMac() {
            return this == MAC;
        }

        public boolean isSolaris() {
            return this == SOLARIS;
        }
    }
}

