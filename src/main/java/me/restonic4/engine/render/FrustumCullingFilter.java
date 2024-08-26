package me.restonic4.engine.render;

import me.restonic4.engine.world.object.GameObject;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class FrustumCullingFilter {
    private static FrustumCullingFilter instance;

    private final Matrix4f prjViewMatrix;

    private FrustumIntersection frustumIntersection;

    public FrustumCullingFilter() {
        prjViewMatrix = new Matrix4f();
        frustumIntersection = new FrustumIntersection();
    }


    public static FrustumCullingFilter getInstance() {
        if (FrustumCullingFilter.instance == null) {
            FrustumCullingFilter.instance = new FrustumCullingFilter();
        }
        return FrustumCullingFilter.instance;
    }

    public void updateFrustum(Matrix4f projMatrix, Matrix4f viewMatrix) {
        // Calculate projection view matrix
        prjViewMatrix.set(projMatrix);
        prjViewMatrix.mul(viewMatrix);

        // Update frustum intersection class
        frustumIntersection.set(prjViewMatrix);
    }

    // Sphere bounding
    public boolean insideFrustum(float x0, float y0, float z0, float boundingRadius) {
        return frustumIntersection.testSphere(x0, y0, z0, boundingRadius);
    }

    public void filter(List<GameObject> gameItems, float meshBoundingRadius) {
        float boundingRadius;
        Vector3f pos;
        for (GameObject gameObject : gameItems) {
            float maxComponent = Math.max(gameObject.transform.getScale().x, Math.max(gameObject.transform.getScale().y, gameObject.transform.getScale().z));

            boundingRadius = maxComponent * meshBoundingRadius;
            pos = gameObject.transform.getPosition();
            gameObject.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
        }
    }
}
