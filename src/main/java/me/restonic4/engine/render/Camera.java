package me.restonic4.engine.render;

import me.restonic4.engine.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private float fov = (float) Math.toRadians(70.0f);

    private float nearPlane = 0.01f;
    private float farPlane = 1000.0f;

    private Matrix4f projectionMatrix, viewMatrix;
    public Vector3f position;
    private Vector3f rotation;

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();

        //projectionMatrix.perspective(fov, Window.getInstance().getAspectRatio(), nearPlane, farPlane);
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, nearPlane, farPlane);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp);

        return this.viewMatrix;

        /*viewMatrix.identity();

        // Apply rotation
        viewMatrix.rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z));

        // Apply translation
        viewMatrix.translate(-position.x, -position.y, -position.z);

        return this.viewMatrix;*/

        /*Vector3f cameraFront = new Vector3f(1, 0, 0);
        Vector3f cameraUp = new Vector3f(0, 1, 0);
        Vector3f cameraRight = new Vector3f(0, 0, 1);

        Vector3f center = new Vector3f(position.x, position.y, position.z);
        center.add(cameraFront);

        this.viewMatrix.identity();

        this.viewMatrix = viewMatrix.lookAt(position, center, cameraUp);

        return this.viewMatrix;*/
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void setRotation(Vector3f rotation) {
        this.rotation.set(rotation);
    }

    public void addRotation(float x, float y, float z) {
        this.rotation.add(x, y, z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}
