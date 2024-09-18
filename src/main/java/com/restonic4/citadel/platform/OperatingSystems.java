package com.restonic4.citadel.platform;

import com.restonic4.citadel.platform.operating_systems.*;

public enum OperatingSystems {
    WINDOWS(
            "windows",
            new String[]{
                "win"
            },
            new Windows()
    ),
    LINUX(
            "linux",
            new String[]{
                "linux", "unix"
            },
            new Linux()
    ),
    MAC(
            "mac",
            new String[]{
                "mac"
            },
            new Mac()
    ),
    SOLARIS(
            "solaris",
            new String[]{
                "solaris", "sunos"
            },
            new Solaris()
    ),
    UNKNOWN("unknown");

    private String id;
    private String[] keywords;
    private OperatingSystem layer;

    OperatingSystems(String id) {
        this.id = id;
        this.keywords = null;
        this.layer = new OperatingSystem();
    }

    OperatingSystems(String id, String[] keywords) {
        this.id = id;
        this.keywords = keywords;
        this.layer = new OperatingSystem();
    }

    <T extends OperatingSystem> OperatingSystems(String id, String[] keywords, T layer) {
        this.id = id;
        this.keywords = keywords;
        this.layer = layer;
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

    public OperatingSystem get() {
        return this.layer;
    }
}
