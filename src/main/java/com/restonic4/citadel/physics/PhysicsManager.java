package com.restonic4.citadel.physics;

import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.SceneManager;
import com.restonic4.citadel.world.object.components.ColliderComponent;
import com.restonic4.citadel.world.object.components.RigidBodyComponent;
import org.joml.Vector3f;

import java.util.List;

public class PhysicsManager {
    public static void update() {
        Scene currentScene = SceneManager.getCurrentScene();
        List<RigidBodyComponent> rigidBodies = currentScene.getRigidBodyComponents();
        List<ColliderComponent> colliders = currentScene.getColliderComponents();

        for (int i = 0; i < rigidBodies.size(); i++) {
            rigidBodies.get(i).update();
        }

        checkCollisions(colliders);
    }

    private static void checkCollisions(List<ColliderComponent> colliders) {
        for (int i = 0; i < colliders.size(); i++) {
            for (int j = i + 1; j < colliders.size(); j++) {
                ColliderComponent colliderA = colliders.get(i);
                ColliderComponent colliderB = colliders.get(j);

                if (colliderA.intersects(colliderB)) {
                    resolveCollision(colliderA, colliderB);
                }
            }
        }
    }

    private static void resolveCollision(ColliderComponent colliderA, ColliderComponent colliderB) {
        RigidBodyComponent rbA = colliderA.gameObject.getComponent(RigidBodyComponent.class);
        RigidBodyComponent rbB = colliderB.gameObject.getComponent(RigidBodyComponent.class);

        if (rbA != null && rbB != null) {
            resolveDynamicCollision(colliderA, colliderB);
        } else if (rbA != null || rbB != null) {
            resolveStaticCollision(colliderA, colliderB);
        } else {
            resolveDoubleStaticCollision(colliderA, colliderB);
        }
    }

    private static void resolveDoubleStaticCollision(ColliderComponent colliderA, ColliderComponent colliderB) {

    }

    private static void resolveStaticCollision(ColliderComponent colliderA, ColliderComponent colliderB) {
        RigidBodyComponent physicObject;

        RigidBodyComponent rbA = colliderA.gameObject.getComponent(RigidBodyComponent.class);
        RigidBodyComponent rbB = colliderB.gameObject.getComponent(RigidBodyComponent.class);

        if (rbA != null) {
            physicObject = rbA;
        }
        else {
            physicObject = rbB;
        }

        float massA = physicObject.getMass();
        float massB = 1;

        Vector3f startVelocityA = physicObject.getVelocity();
        Vector3f startVelocityB = physicObject.getVelocity().negate();

        Vector3f finalAVelocity = new Vector3f(startVelocityB).mul(2 * massB).add(new Vector3f(startVelocityA).mul(massA - massB)).div(massA + massB);

        physicObject.setVelocity(finalAVelocity);
    }

    private static void resolveDynamicCollision(ColliderComponent colliderA, ColliderComponent colliderB) {
        RigidBodyComponent rbA = colliderA.gameObject.getComponent(RigidBodyComponent.class);
        RigidBodyComponent rbB = colliderB.gameObject.getComponent(RigidBodyComponent.class);

        float massA = rbA.getMass();
        float massB = rbB.getMass();

        Vector3f startVelocityA = rbA.getVelocity();
        Vector3f startVelocityB = rbB.getVelocity();

        Vector3f finalAVelocity = new Vector3f(startVelocityB).mul(2 * massB).add(new Vector3f(startVelocityA).mul(massA - massB)).div(massA + massB);
        Vector3f finalBVelocity = new Vector3f(startVelocityA).mul(2 * massA).add(new Vector3f(startVelocityB).mul(massB - massA)).div(massA + massB);

        rbA.setVelocity(finalAVelocity);
        rbB.setVelocity(finalBVelocity);
    }
}
