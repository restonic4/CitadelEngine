package me.restonic4.engine.object.loaders;

import me.restonic4.engine.object.Mesh;

public class GenericModelLoader {
    public Mesh loadMesh(String data) {
        throw new IllegalStateException("This class is meant to be extended, not used!");
    }
}
