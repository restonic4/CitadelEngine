package me.restonic4.engine.render;

import me.restonic4.engine.object.Transform;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class OrthographicCamera extends Camera {
    public OrthographicCamera(Transform transform) {
        super(transform);
    }

    public void adjustProjection() {
        this.projectionMatrix.identity();

        projectionMatrix.ortho(0.0f, 1920, 0.0f, 1080, nearPlane, farPlane);
    }
}
