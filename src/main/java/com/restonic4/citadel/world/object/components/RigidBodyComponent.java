package com.restonic4.citadel.world.object.components;

import com.restonic4.citadel.util.Time;
import com.restonic4.citadel.world.object.Component;
import org.joml.Vector3f;

public class RigidBodyComponent extends Component {
    private float mass;
    public Vector3f velocity;
    private Vector3f force;
    private Vector3f acceleration;

    public RigidBodyComponent(float mass) {
        this.mass = mass;
        this.velocity = new Vector3f();
        this.force = new Vector3f();
        this.acceleration = new Vector3f();
    }

    @Override
    public void update() {
        float deltaTime = (float) Time.getDeltaTime();

        // Calcular la aceleración
        acceleration.set(force).div(mass);

        // Actualizar la velocidad
        velocity.add(acceleration.mul(deltaTime, new Vector3f()));

        // Actualizar la posición
        gameObject.transform.addPosition(velocity.mul(deltaTime, new Vector3f()));

        // Restablecer fuerzas a cero para el próximo frame
        force.set(0, 0, 0);
        /*float deltaTime = (float) Time.getDeltaTime();

        acceleration.set(force).div(mass);

        velocity.add(acceleration.mul(deltaTime, new Vector3f()));

        gameObject.transform.addPosition(velocity.mul(deltaTime, new Vector3f()));

        force.set(0, 0, 0);*/
    }

    public void integrate(float deltaTime) {
        // F = ma -> a = F/m
        Vector3f tempAcc = new Vector3f(force).div(mass);
        acceleration.set(tempAcc);

        // Integrate velocity
        velocity.fma(deltaTime, acceleration);

        // Integrate position
        gameObject.transform.getPosition().fma(deltaTime, velocity);

        // Reset la force
        force.zero();
    }

    public void applyForce(Vector3f force) {
        this.force.add(force);
    }

    public Vector3f getVelocity() {
        return this.velocity;
    }

    public float getMass() {
        return this.mass;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity.set(velocity);
    }
}
