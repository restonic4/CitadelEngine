package me.restonic4.engine.render;

import me.restonic4.engine.Window;
import me.restonic4.engine.object.Transform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

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
