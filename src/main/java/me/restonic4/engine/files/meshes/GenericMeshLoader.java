package me.restonic4.engine.files.meshes;

import me.restonic4.engine.object.Mesh;

public class GenericMeshLoader {
    public Mesh loadMesh(String data) {
        throw new IllegalStateException("This class is meant to be extended, not used!");
    }
}
