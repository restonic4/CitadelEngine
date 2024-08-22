package me.restonic4.engine.object;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Mesh {
    private Vector3f[] vertices;
    private int[] indices;
    private Vector4f color;

    public Mesh(Vector3f[] vertices, int[] indices, Vector4f color) {
        this.vertices = vertices;
        this.indices = indices;
        this.color = color;
    }

    public Vector3f[] getVertices() {
        return this.vertices;
    }

    public int[] getIndices() {
        return this.indices;
    }

    public Vector4f getColor() {
        return this.color;
    }

    public void setColor(Vector4f vector4f) {
        this.color = vector4f;
    }
}
