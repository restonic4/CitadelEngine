package com.restonic4.citadel.physics.bounding;

import org.joml.Vector3f;

public class BoundingSphere {
    public Vector3f center;
    public float radius;

    private final float originalRadius;

    public BoundingSphere(Vector3f center, float radius) {
        this.center = center;
        this.radius = radius;
        this.originalRadius = radius;
    }

    public boolean intersects(BoundingSphere other) {
        float distanceSquared = center.distanceSquared(other.center);
        float radiusSum = this.radius + other.radius;
        return distanceSquared <= (radiusSum * radiusSum);
    }

    public boolean intersects(AABB aabb) {
        return aabb.intersects(this);
    }

    public boolean intersects(OBB obb) {
       return obb.intersects(this);
    }

    public void update(Vector3f newCenter, float scale) {
        this.center.set(newCenter);

        this.radius = originalRadius * scale;
    }
}
