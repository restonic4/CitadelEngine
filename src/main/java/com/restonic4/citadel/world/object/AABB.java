package com.restonic4.citadel.world.object;

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

    public void calculateAABB(Vector3f[] vertices) {
        if (vertices == null || vertices.length == 0) {
            return;
        }

        // Initialize min and max to the first vertex
        Vector3f min = new Vector3f(vertices[0]);
        Vector3f max = new Vector3f(vertices[0]);

        for (int i = 1; i < vertices.length; i++) {
            Vector3f vertex = vertices[i];

            // Update min
            if (vertex.x < min.x) min.x = vertex.x;
            if (vertex.y < min.y) min.y = vertex.y;
            if (vertex.z < min.z) min.z = vertex.z;

            // Update max
            if (vertex.x > max.x) max.x = vertex.x;
            if (vertex.y > max.y) max.y = vertex.y;
            if (vertex.z > max.z) max.z = vertex.z;
        }

        // Set the calculated min and max to the AABB
        this.min = min;
        this.max = max;
    }
}
