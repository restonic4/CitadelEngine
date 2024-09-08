package me.restonic4.citadel.render;

import me.restonic4.citadel.world.object.AABB;
import me.restonic4.citadel.world.object.GameObject;
import me.restonic4.citadel.world.object.components.ModelRendererComponent;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;

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

    // AABB bounding
    public boolean insideFrustum(Vector3f min, Vector3f max) {
        return frustumIntersection.testAab(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    // Sphere
    public void filter(List<GameObject> gameObjects, float meshBoundingRadius) {
        float boundingRadius;
        Vector3f pos;

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);

            float maxComponent = Math.max(gameObject.transform.getScale().x, Math.max(gameObject.transform.getScale().y, gameObject.transform.getScale().z));

            boundingRadius = maxComponent * meshBoundingRadius;

            pos = gameObject.transform.getPosition();
            gameObject.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
        }
    }
}
