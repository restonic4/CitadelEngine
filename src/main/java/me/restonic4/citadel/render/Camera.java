package me.restonic4.citadel.render;

import me.restonic4.citadel.world.object.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class Camera {
    protected Matrix4f projectionMatrix, viewMatrix;
    public Transform transform;

    private boolean isSimulated;
    protected Matrix4f fakeProjectionMatrix, fakeViewMatrix;

    // Cache

    private Vector3f front = new Vector3f(0, 0, -1);
    private Vector3f up = new Vector3f(0, 1, 0);
    private Vector3f center = new Vector3f();

    public Camera(Transform transform) {
        this.transform = transform;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.isSimulated = false;
        adjustProjection();
    }

    public abstract void adjustProjection();

    public Matrix4f getViewMatrix() {
        this.viewMatrix.identity();

        front.set(0, 0, -1).rotate(transform.getRotation());
        up.set(0, 1, 0).rotate(transform.getRotation());

        center.set(transform.getPosition()).add(front);

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

    public void setSimulated(boolean value) {
        if (value) {
            this.fakeViewMatrix = new Matrix4f(viewMatrix);
            this.fakeProjectionMatrix = new Matrix4f(projectionMatrix);
        }

        this.isSimulated = value;
    }

    public boolean isSimulated() {
        return this.isSimulated;
    }
}
