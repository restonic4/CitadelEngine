package me.restonic4.game.core.world.terrain;

import me.restonic4.engine.graph.Model;
import me.restonic4.engine.util.math.PerlinNoise;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class TerrainGenerator {
    private final PerlinNoise perlinNoise;
    private final int width;
    private final int depth;
    private final float scale;

    public TerrainGenerator(int seed, int width, int depth, float scale) {
        this.perlinNoise = new PerlinNoise(seed, 256);
        this.width = width;
        this.depth = depth;
        this.scale = scale;
    }

    public float[][] generateHeightMap() {
        float[][] heightMap = new float[width][depth];
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                float height = perlinNoise.noise(x * scale, z * scale);
                heightMap[x][z] = height;
            }
        }
        return heightMap;
    }
}
