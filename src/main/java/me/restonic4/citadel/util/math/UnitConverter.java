package me.restonic4.citadel.util.math;

public class UnitConverter {
    public static float bytesToKilobytes(int bytes) {
        return bytes / 1024.0f;
    }

    public static float kilobytesToMegabytes(float kilobytes) {
        return kilobytes / 1024.0f;
    }

    public static float megabytesToGigabytes(float megabytes) {
        return megabytes / 1024.0f;
    }

    public static int kilobytesToBytes(float kilobytes) {
        return (int) (kilobytes * 1024);
    }

    public static float megabytesToKilobytes(float megabytes) {
        return megabytes * 1024.0f;
    }

    public static float gigabytesToMegabytes(float gigabytes) {
        return gigabytes * 1024.0f;
    }

    public static float bytesToMegabytes(int bytes) {
        return bytes / (1024.0f * 1024.0f);
    }
}
