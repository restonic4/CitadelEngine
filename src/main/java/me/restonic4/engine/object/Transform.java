package me.restonic4.engine.object;

import org.joml.Vector3f;

public class Transform {
    public Vector3f position;
    public Vector3f scale;

    public Transform() {
        create(new Vector3f(), new Vector3f());
    }

    public Transform(Vector3f position) {
        create(position, new Vector3f());
    }

    public Transform(Vector3f position, Vector3f scale) {
        create(position, scale);
    }

    public void create(Vector3f position, Vector3f scale) {
        this.position = position;
        this.scale = scale;
    }
}
