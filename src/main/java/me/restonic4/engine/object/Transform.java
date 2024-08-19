package me.restonic4.engine.object;

import org.joml.Vector3f;

public class Transform {
    private Vector3f position;
    private Vector3f scale;

    // Tells to the render system if it needs to update something
    private boolean dirty = true;

    public Transform() {
        create(new Vector3f(), new Vector3f());
    }

    public Transform(Vector3f position) {
        create(position, new Vector3f());
    }

    public Transform(Vector3f position, Vector3f scale) {
        create(position, scale);
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Vector3f getScale() {
        return this.scale;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;

        setDirty();
    }

    public void setPositionX(float x) {
        this.position.x = x;

        setDirty();
    }

    public void setPositionY(float y) {
        this.position.y = y;

        setDirty();
    }

    public void setPositionZ(float z) {
        this.position.z = z;

        setDirty();
    }

    public void addPosition(float x, float y, float z) {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;

        setDirty();
    }

    public void addPositionX(float x) {
        this.position.x += x;

        setDirty();
    }

    public void addPositionY(float y) {
        this.position.y += y;

        setDirty();
    }

    public void addPositionZ(float z) {
        this.position.z += z;

        setDirty();
    }

    public void setScale(float x, float y, float z) {
        this.scale.x = x;
        this.scale.y = y;
        this.scale.z = z;

        setDirty();
    }

    public void setScaleX(float x) {
        this.scale.x = x;

        setDirty();
    }

    public void setScaleY(float y) {
        this.scale.y = y;

        setDirty();
    }

    public void setScaleZ(float z) {
        this.scale.z = z;

        setDirty();
    }

    public void addScale(float x, float y, float z) {
        this.scale.x += x;
        this.scale.y += y;
        this.scale.z += z;

        setDirty();
    }

    public void addScaleX(float x) {
        this.scale.x += x;

        setDirty();
    }

    public void addScaleY(float y) {
        this.scale.y += y;

        setDirty();
    }

    public void addScaleZ(float z) {
        this.scale.z += z;

        setDirty();
    }

    public void create(Vector3f position, Vector3f scale) {
        this.position = position;
        this.scale = scale;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty() {
        this.dirty = true;
    }

    public void setClean() {
        this.dirty = false;
    }
}
