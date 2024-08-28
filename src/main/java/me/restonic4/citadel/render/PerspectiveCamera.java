package me.restonic4.citadel.render;

import me.restonic4.citadel.core.Window;
import me.restonic4.citadel.world.object.Transform;

public class PerspectiveCamera extends Camera {
    protected float fov = (float) Math.toRadians(60);

    public PerspectiveCamera(Transform transform) {
        super(transform);
    }

    public void adjustProjection() {
        this.projectionMatrix.identity();

        projectionMatrix.perspective(fov, Window.getInstance().getAspectRatio(), nearPlane, farPlane);
    }
}
