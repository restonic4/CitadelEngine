package me.restonic4.citadel.render;

import me.restonic4.citadel.world.object.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class Camera {
    protected float nearPlane = 0.01f;
    protected float farPlane = 100000.0f;

    protected Matrix4f projectionMatrix, viewMatrix;
    public Transform transform;

    public Camera(Transform transform) {
        this.transform = transform;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public abstract void adjustProjection();

    public Matrix4f getViewMatrix() {
        this.viewMatrix.identity();

        Vector3f front = new Vector3f(0, 0, -1).rotate(transform.getRotation());
        Vector3f up = new Vector3f(0, 1, 0).rotate(transform.getRotation());

        Vector3f center = new Vector3f(transform.getPosition()).add(front);

        this.viewMatrix.lookAt(transform.getPosition(), center, up);

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        adjustProjection();
        return this.projectionMatrix;
    }

    public void load() {
        getViewMatrix();
        getProjectionMatrix();
    }
}
