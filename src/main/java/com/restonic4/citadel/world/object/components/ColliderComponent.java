package com.restonic4.citadel.world.object.components;

import com.restonic4.citadel.physics.bounding.AABB;
import com.restonic4.citadel.physics.bounding.BoundingSphere;
import com.restonic4.citadel.physics.bounding.OBB;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.world.object.Component;
import com.restonic4.citadel.world.object.GameObject;
import org.joml.Vector3f;

public class ColliderComponent extends Component {
    private ColliderType colliderType;

    private AABB aabb;
    private BoundingSphere boundingSphere;
    private OBB obb;

    public ColliderComponent() {}

    public ColliderComponent(ColliderType colliderType) {
        this.colliderType = colliderType;
    }

    @Override
    public void start() {
        updateCollider();
    }

    @Override
    public void update() {
        if (gameObject.transform.isDirty()) {
            updateCollider();
        }
    }

    private void updateCollider() {
        Vector3f position = gameObject.transform.getPosition();
        Vector3f scale = gameObject.transform.getScale();

        switch (colliderType) {
            case BOX:
                if (aabb == null) {
                    aabb = new AABB();
                }
                aabb.update(gameObject.getComponent(ModelRendererComponent.class).getMesh().getVertices());
                break;

            case SPHERE:
                if (boundingSphere == null) {
                    boundingSphere = new BoundingSphere(position, scale.x);
                } else {
                    boundingSphere.update(position, scale.x);
                }
                break;

            case COMPLEX_BOX:
                if (obb == null) {
                    obb = new OBB(position, gameObject.getComponent(ModelRendererComponent.class).getMesh().getVertices(), gameObject.transform.getRotation());
                } else {
                    obb.update(position, gameObject.transform.getRotation(), scale);
                }
                break;
        }
    }

    public ColliderType getColliderType() {
        return colliderType;
    }

    public Object getCollider() {
        switch (colliderType) {
            case BOX:
                return getAABB();
            case SPHERE:
                return getBoundingSphere();
            case COMPLEX_BOX:
                return getOBB();
            default:
                throw new IllegalStateException("Unexpected value: " + colliderType);
        }
    }

    public AABB getAABB() {
        return aabb;
    }

    public BoundingSphere getBoundingSphere() {
        return boundingSphere;
    }

    public OBB getOBB() {
        return obb;
    }

    public boolean intersects(ColliderComponent other) {
        if (other == null) return false;

        switch (this.colliderType) {
            case BOX:
                if (other.colliderType == ColliderType.BOX) {
                    return this.aabb.intersects(other.aabb);
                } else if (other.colliderType == ColliderType.SPHERE) {
                    return this.aabb.intersects(other.boundingSphere);
                } else if (other.colliderType == ColliderType.COMPLEX_BOX) {
                    return this.aabb.intersects(other.obb);
                }
                break;

            case SPHERE:
                if (other.colliderType == ColliderType.BOX) {
                    return other.boundingSphere.intersects(this.aabb);
                } else if (other.colliderType == ColliderType.SPHERE) {
                    return this.boundingSphere.intersects(other.boundingSphere);
                } else if (other.colliderType == ColliderType.COMPLEX_BOX) {
                    return other.obb.intersects(this.boundingSphere);
                }
                break;

            case COMPLEX_BOX:
                if (other.colliderType == ColliderType.BOX) {
                    return this.obb.intersects(other.aabb);
                } else if (other.colliderType == ColliderType.SPHERE) {
                    return this.obb.intersects(other.boundingSphere);
                } else if (other.colliderType == ColliderType.COMPLEX_BOX) {
                    return this.obb.intersects(other.obb);
                }
                break;
        }

        return false;
    }

    @Override
    public String serialize() {
        return StringBuilderHelper.concatenate(getSerializerID(), "%", getId());
    }

    @Override
    public Object deserialize(String data) {
        return null;
    }

    public enum ColliderType {
        BOX,
        SPHERE,
        COMPLEX_BOX;
    }
}
