package me.restonic4.engine.object;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Mesh {
    private Vector3f[] vertices;
    private int[] indices;
    private Vector4f[] verticesColors;
    private Vector4f tint;

    public Mesh(Vector3f[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
        this.tint = new Vector4f(1, 1, 1, 1);
    }

    public Mesh(Vector3f[] vertices, int[] indices, Vector4f tint) {
        this.vertices = vertices;
        this.indices = indices;
        this.tint = tint;
    }

    public Mesh(Vector3f[] vertices, int[] indices, Vector4f[] verticesColors) {
        this.vertices = vertices;
        this.indices = indices;
        this.verticesColors = verticesColors;
        this.tint = new Vector4f(1, 1, 1, 1);
    }

    public Vector3f[] getVertices() {
        return this.vertices;
    }

    public int[] getIndices() {
        return this.indices;
    }

    public Vector4f[] getVerticesColors() {
        return this.verticesColors;
    }

    public Vector4f getTint() {
        return this.tint;
    }

    public void setTint(Vector4f vector4f) {
        this.tint = vector4f;
    }

    public void setVerticesColors(Vector4f[] verticesColors) {
        this.verticesColors = verticesColors;
    }

    public Mesh copy() {
        Mesh mesh = new Mesh(this.vertices, this.indices, this.verticesColors);
        mesh.setTint(mesh.getTint());

        return mesh;
    }
}
