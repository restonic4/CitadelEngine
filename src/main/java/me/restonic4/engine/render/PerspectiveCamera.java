package me.restonic4.engine.render;

import me.restonic4.engine.Window;
import me.restonic4.engine.world.object.Transform;

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
