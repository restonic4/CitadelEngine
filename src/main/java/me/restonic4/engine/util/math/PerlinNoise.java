package me.restonic4.engine.util.math;

import java.util.Random;

public class PerlinNoise {
    private final int[] permutation;
    private final int repeat;

    public PerlinNoise(int seed, int repeat) {
        this.repeat = repeat;
        permutation = new int[512];
        for (int i = 0; i < 256; i++) {
            permutation[i] = i;
        }
        Random random = new Random(seed);
        for (int i = 0; i < 256; i++) {
            int swap = i + random.nextInt(256 - i);
            int temp = permutation[i];
            permutation[i] = permutation[swap];
            permutation[swap] = temp;
            permutation[i + 256] = permutation[i];
        }
    }

    public float noise(float x, float y) {
        x /= repeat;
        y /= repeat;

        int xi = (int) x & 255;
        int yi = (int) y & 255;

        float xf = x - (int) x;
        float yf = y - (int) y;

        float u = fade(xf);
        float v = fade(yf);

        int aa = permutation[permutation[xi] + yi];
        int ab = permutation[permutation[xi] + yi + 1];
        int ba = permutation[permutation[xi + 1] + yi];
        int bb = permutation[permutation[xi + 1] + yi + 1];

        float x1 = lerp(u, grad(aa, xf, yf), grad(ba, xf - 1, yf));
        float x2 = lerp(u, grad(ab, xf, yf - 1), grad(bb, xf - 1, yf - 1));
        return lerp(v, x1, x2);
    }

    private float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    private float grad(int hash, float x, float y) {
        return switch (hash & 3) {
            case 0 -> x + y;
            case 1 -> -x + y;
            case 2 -> x - y;
            case 3 -> -x - y;
            default -> 0;
        };
    }
}
