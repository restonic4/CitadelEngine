package me.restonic4.engine.platform;

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

    public String getId() {
        return this.id;
    }
}
