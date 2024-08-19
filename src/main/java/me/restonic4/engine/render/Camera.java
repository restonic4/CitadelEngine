package me.restonic4.engine.render;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Camera {
    protected float nearPlane = 0.01f;
    protected float farPlane = 1000.0f;

    protected Matrix4f projectionMatrix, viewMatrix;
    public Vector3f position;
    public Vector3f rotation;

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public abstract void adjustProjection();

    public abstract Matrix4f getViewMatrix();

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void setRotation(Vector3f rotation) {
        this.rotation.set(rotation);
    }

    public void addRotation(float pitch, float yaw, float roll) {
        this.rotation.add(pitch, yaw, roll);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public double getPitch() {
        return rotation.x;
    }

    public double getYaw() {
        return rotation.y;
    }

    public double getRoll() {
        return rotation.z;
    }
}
