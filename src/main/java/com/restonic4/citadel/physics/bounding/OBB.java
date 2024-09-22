package com.restonic4.citadel.physics.bounding;

import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class OBB {
    private Vector3f center;
    private Vector3f halfSizes;
    private Matrix3f orientation;

    private final Vector3f originalHalfSizes;

    public OBB(Vector3f center, Vector3f halfSizes, Quaternionf rotation) {
        this.center = center;
        this.halfSizes = halfSizes;
        this.originalHalfSizes = new Vector3f(halfSizes);
        this.orientation = new Matrix3f().set(rotation);
    }

    public OBB(Vector3f center, Vector3f[] vertices, Quaternionf rotation) {
        this.center = center;
        this.orientation = new Matrix3f().set(rotation);
        this.halfSizes = calculateHalfSizes(vertices);
        this.originalHalfSizes = new Vector3f(halfSizes);
    }

    public Vector3f calculateHalfSizes(Vector3f[] vertices) {
        Vector3f min = new Vector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        Vector3f max = new Vector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

        for (Vector3f vertex : vertices) {
            min.min(vertex);
            max.max(vertex);
        }

        return new Vector3f(
                (max.x - min.x) / 2.0f,
                (max.y - min.y) / 2.0f,
                (max.z - min.z) / 2.0f
        );
    }

    public Vector3f getCenter() {
        return this.center;
    }

    public Vector3f getHalfSizes() {
        return this.halfSizes;
    }

    public Matrix3f getOrientation() {
        return this.orientation;
    }

    public Vector3f[] getCorners() {
        Vector3f[] corners = new Vector3f[8];

        Vector3f[] signs = {
                new Vector3f(-1, -1, -1),
                new Vector3f(1, -1, -1),
                new Vector3f(-1, 1, -1),
                new Vector3f(1, 1, -1),
                new Vector3f(-1, -1, 1),
                new Vector3f(1, -1, 1),
                new Vector3f(-1, 1, 1),
                new Vector3f(1, 1, 1)
        };

        for (int i = 0; i < 8; i++) {
            Vector3f corner = new Vector3f(signs[i]).mul(halfSizes);
            orientation.transform(corner);
            corners[i] = new Vector3f(center).add(corner);
        }

        return corners;
    }

    public boolean intersects(OBB other) {
        return testOBBvsOBB(this, other);
    }

    private boolean testOBBvsOBB(OBB obb1, OBB obb2) {
        Vector3f[] axes1 = {
                new Vector3f(obb1.orientation.m00(), obb1.orientation.m01(), obb1.orientation.m02()),
                new Vector3f(obb1.orientation.m10(), obb1.orientation.m11(), obb1.orientation.m12()),
                new Vector3f(obb1.orientation.m20(), obb1.orientation.m21(), obb1.orientation.m22())
        };

        Vector3f[] axes2 = {
                new Vector3f(obb2.orientation.m00(), obb2.orientation.m01(), obb2.orientation.m02()),
                new Vector3f(obb2.orientation.m10(), obb2.orientation.m11(), obb2.orientation.m12()),
                new Vector3f(obb2.orientation.m20(), obb2.orientation.m21(), obb2.orientation.m22())
        };

        Vector3f translation = new Vector3f(obb2.center).sub(obb1.center);

        for (int i = 0; i < 3; i++) {
            if (!testAxis(axes1[i], obb1, obb2, translation)) return false;
        }

        for (int i = 0; i < 3; i++) {
            if (!testAxis(axes2[i], obb1, obb2, translation)) return false;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Vector3f axis = new Vector3f();
                axes1[i].cross(axes2[j], axis);
                if (axis.lengthSquared() > 1e-6) {
                    if (!testAxis(axis.normalize(), obb1, obb2, translation)) return false;
                }
            }
        }

        return true;
    }

    private boolean testAxis(Vector3f axis, OBB obb1, OBB obb2, Vector3f translation) {
        float projectionCenter = Math.abs(translation.dot(axis));

        float projection1 = obb1.halfSizes.x * Math.abs(axis.dot(new Vector3f(obb1.orientation.m00(), obb1.orientation.m01(), obb1.orientation.m02()))) +
                obb1.halfSizes.y * Math.abs(axis.dot(new Vector3f(obb1.orientation.m10(), obb1.orientation.m11(), obb1.orientation.m12()))) +
                obb1.halfSizes.z * Math.abs(axis.dot(new Vector3f(obb1.orientation.m20(), obb1.orientation.m21(), obb1.orientation.m22())));

        float projection2 = obb2.halfSizes.x * Math.abs(axis.dot(new Vector3f(obb2.orientation.m00(), obb2.orientation.m01(), obb2.orientation.m02()))) +
                obb2.halfSizes.y * Math.abs(axis.dot(new Vector3f(obb2.orientation.m10(), obb2.orientation.m11(), obb2.orientation.m12()))) +
                obb2.halfSizes.z * Math.abs(axis.dot(new Vector3f(obb2.orientation.m20(), obb2.orientation.m21(), obb2.orientation.m22())));

        return projectionCenter <= projection1 + projection2;
    }

    public boolean intersects(AABB aabb) {
        return testOBBvsAABB(this, aabb);
    }

    private boolean testOBBvsAABB(OBB obb, AABB aabb) {
        Vector3f[] axes = {
                new Vector3f(obb.orientation.m00(), obb.orientation.m01(), obb.orientation.m02()),
                new Vector3f(obb.orientation.m10(), obb.orientation.m11(), obb.orientation.m12()),
                new Vector3f(obb.orientation.m20(), obb.orientation.m21(), obb.orientation.m22())
        };

        for (Vector3f axis : axes) {
            if (!overlapOnAxis(axis, obb, aabb)) {
                return false;
            }
        }

        Vector3f[] aabbAxes = {
                new Vector3f(1, 0, 0),
                new Vector3f(0, 1, 0),
                new Vector3f(0, 0, 1)
        };

        for (Vector3f axis : aabbAxes) {
            if (!overlapOnAxis(axis, obb, aabb)) {
                return false;
            }
        }

        for (Vector3f axis1 : axes) {
            for (Vector3f axis2 : aabbAxes) {
                Vector3f cross = new Vector3f();
                axis1.cross(axis2, cross);
                if (cross.lengthSquared() > 1e-6) {
                    if (!overlapOnAxis(cross.normalize(), obb, aabb)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean overlapOnAxis(Vector3f axis, OBB obb, AABB aabb) {
        float obbProjectionMin = Float.POSITIVE_INFINITY;
        float obbProjectionMax = Float.NEGATIVE_INFINITY;

        for (Vector3f corner : obb.getCorners()) {
            float projection = corner.dot(axis);
            obbProjectionMin = Math.min(obbProjectionMin, projection);
            obbProjectionMax = Math.max(obbProjectionMax, projection);
        }

        float aabbMin = aabb.getMin().dot(axis);
        float aabbMax = aabb.getMax().dot(axis);

        return !(obbProjectionMax < aabbMin || aabbMax < obbProjectionMin);
    }

    public boolean intersects(BoundingSphere sphere) {
        return testOBBvsBoundingSphere(this, sphere);
    }

    private boolean testOBBvsBoundingSphere(OBB obb, BoundingSphere boundingSphere) {
        Vector3f[] axes = {
                new Vector3f(obb.orientation.m00(), obb.orientation.m01(), obb.orientation.m02()),
                new Vector3f(obb.orientation.m10(), obb.orientation.m11(), obb.orientation.m12()),
                new Vector3f(obb.orientation.m20(), obb.orientation.m21(), obb.orientation.m22())
        };

        Vector3f closestPoint = new Vector3f(boundingSphere.center);

        for (Vector3f axis : axes) {
            float projection = closestPoint.dot(axis);
            float obbMin = obb.getCenter().dot(axis) - obb.getHalfSizes().dot(axis);
            float obbMax = obb.getCenter().dot(axis) + obb.getHalfSizes().dot(axis);

            if (projection < obbMin) {
                projection = obbMin;
            } else if (projection > obbMax) {
                projection = obbMax;
            }

            closestPoint.add(axis.mul(projection - closestPoint.dot(axis), new Vector3f()));
        }

        float distanceSquared = closestPoint.distanceSquared(boundingSphere.center);
        float radiusSum = boundingSphere.radius;

        return distanceSquared <= radiusSum * radiusSum;
    }

    public void update(Vector3f newCenter, Quaternionf newRotation, Vector3f newScale) {
        this.center.set(newCenter);

        this.orientation.set(newRotation);

        this.halfSizes.set(originalHalfSizes).mul(newScale);
    }
}
