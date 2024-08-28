package me.restonic4.citadel.render;

import me.restonic4.citadel.world.object.Transform;

public class OrthographicCamera extends Camera {
    public OrthographicCamera(Transform transform) {
        super(transform);
    }

    public void adjustProjection() {
        this.projectionMatrix.identity();

        projectionMatrix.ortho(0.0f, 1920, 0.0f, 1080, nearPlane, farPlane);
    }
}