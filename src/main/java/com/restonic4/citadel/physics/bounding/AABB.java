package com.restonic4.citadel.physics.bounding;

import org.joml.Vector3f;

public class AABB {
    private Vector3f min;
    private Vector3f max;
    private Vector3f[] corners;

    public AABB() {
        this.min = new Vector3f();
        this.max = new Vector3f();
    }

    public AABB(Vector3f min, Vector3f max) {
        this.min = min;
        this.max = max;
    }

    public Vector3f getMin() {
        return this.min;
    }

    public Vector3f getMax() {
        return this.max;
    }

    public Vector3f[] getCorners() {
        if (this.corners == null) {
            this.corners = new Vector3f[]{
                    new Vector3f(this.getMin().x, this.getMin().y, this.getMin().z),
                    new Vector3f(this.getMax().x, this.getMin().y, this.getMin().z),
                    new Vector3f(this.getMin().x, this.getMax().y, this.getMin().z),
                    new Vector3f(this.getMax().x, this.getMax().y, this.getMin().z),
                    new Vector3f(this.getMin().x, this.getMin().y, this.getMax().z),
                    new Vector3f(this.getMax().x, this.getMin().y, this.getMax().z),
                    new Vector3f(this.getMin().x, this.getMax().y, this.getMax().z),
                    new Vector3f(this.getMax().x, this.getMax().y, this.getMax().z)
            };
        }

        return this.corners;
    }

    public boolean intersects(AABB other) {
        return (this.max.x > other.min.x && this.min.x < other.max.x) &&
                (this.max.y > other.min.y && this.min.y < other.max.y) &&
                (this.max.z > other.min.z && this.min.z < other.max.z);
    }

    public boolean intersects(BoundingSphere sphere) {
        Vector3f closestPoint = new Vector3f(
                Math.max(this.min.x, Math.min(sphere.center.x, this.max.x)),
                Math.max(this.min.y, Math.min(sphere.center.y, this.max.y)),
                Math.max(this.min.z, Math.min(sphere.center.z, this.max.z))
        );
        float distanceSquared = closestPoint.distanceSquared(sphere.center);
        return distanceSquared <= (sphere.radius * sphere.radius);
    }

    public boolean intersects(OBB obb) {
        return obb.intersects(this);
    }

    public void update(Vector3f[] vertices) {
        if (vertices == null || vertices.length == 0) {
            return;
        }

        this.min.set(vertices[0]);
        this.max.set(vertices[0]);

        for (int i = 0; i < vertices.length; i++) {
            Vector3f vertex = vertices[i];

            if (vertex.x < this.min.x) this.min.x = vertex.x;
            if (vertex.y < this.min.y) this.min.y = vertex.y;
            if (vertex.z < this.min.z) this.min.z = vertex.z;

            if (vertex.x > this.max.x) this.max.x = vertex.x;
            if (vertex.y > this.max.y) this.max.y = vertex.y;
            if (vertex.z > this.max.z) this.max.z = vertex.z;
        }

        this.corners = null;
    }
}
