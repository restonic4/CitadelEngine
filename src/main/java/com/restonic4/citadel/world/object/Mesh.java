package com.restonic4.citadel.world.object;

import com.restonic4.citadel.render.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.Serial;
import java.io.Serializable;

public class Mesh {
    private Vector3f[] vertices;
    private int[] indices;
    private Vector4f[] verticesColors;
    private Vector4f tint;
    private Texture texture;
    private Vector2f[] uvs;
    private Vector3f[] normals;

    public Mesh(Vector3f[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
        this.tint = new Vector4f(1, 1, 1, 1);
        this.texture = null;
        this.uvs = new Vector2f[vertices.length];
        this.normals = new Vector3f[vertices.length];
    }

    public Mesh(Vector3f[] vertices, int[] indices, Vector4f tint) {
        this.vertices = vertices;
        this.indices = indices;
        this.tint = tint;
        this.texture = null;
        this.uvs = new Vector2f[vertices.length];
        this.normals = new Vector3f[vertices.length];
    }

    public Mesh(Vector3f[] vertices, int[] indices, Vector4f[] verticesColors) {
        this.vertices = vertices;
        this.indices = indices;
        this.verticesColors = verticesColors;
        this.tint = new Vector4f(1, 1, 1, 1);
        this.texture = null;
        this.uvs = new Vector2f[vertices.length];
        this.normals = new Vector3f[vertices.length];
    }

    public Mesh(Vector3f[] vertices, int[] indices, Texture texture, Vector2f[] uvs) {
        this.vertices = vertices;
        this.indices = indices;
        this.tint = new Vector4f(1, 1, 1, 1);
        this.texture = texture;
        this.uvs = uvs;
        this.normals = new Vector3f[vertices.length];
    }

    public Mesh(Mesh mesh) {
        this.vertices = mesh.vertices;
        this.indices = mesh.indices;
        this.verticesColors = mesh.verticesColors;
        this.tint = mesh.tint;
        this.texture = mesh.texture;
        this.uvs = mesh.uvs;
        this.normals = mesh.normals;
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

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2f[] getUVs() {
        return this.uvs;
    }

    public Vector3f[] getNormals() {
        return this.normals;
    }

    public void setTint(Vector4f vector4f) {
        this.tint = vector4f;
    }

    public void setVerticesColors(Vector4f[] verticesColors) {
        this.verticesColors = verticesColors;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setUVs(Vector2f[] uvs) {
        this.uvs = uvs;
    }

    public void setNormals(Vector3f[] normals) {
        this.normals = normals;
    }
}
