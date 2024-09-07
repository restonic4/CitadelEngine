package me.restonic4.citadel.util;

public class ColorHelper {
    public static int hexToARGB(int hex) {
        int r = (hex >> 16) & 0xFF;
        int g = (hex >> 8) & 0xFF;
        int b = hex & 0xFF;

        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }
}
