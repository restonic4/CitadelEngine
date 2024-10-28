package com.restonic4.citadel.world.object;

import com.restonic4.citadel.files.Serializable;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.UniqueIdentifierManager;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.SceneManager;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Transform implements Serializable {
    private int id;
    private Transform parent;

    private Vector3f position;
    private Vector3f scale;
    private Quaternionf rotation;

    private Vector3f cleanPosition;
    private Vector3f cleanScale;
    private Quaternionf cleanRotation;

    // Tells to the render system if it needs to update something
    private boolean dirty = true;

    ///////////////////////////////////////////////
    //               CONSTRUCTORS                //
    ///////////////////////////////////////////////

    public Transform() {
        create(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
    }

    public Transform(Vector3f position) {
        create(position, new Vector3f(1, 1, 1));
    }

    public Transform(Vector3f position, Vector3f scale) {
        create(position, scale);
    }

    ///////////////////////////////////////////////
    //                  GETTERS                  //
    ///////////////////////////////////////////////

    public Vector3f getPosition() {
        return this.position;
    }

    public Vector3f getScale() {
        return this.scale;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    ///////////////////////////////////////////////
    //             POSITION SETTERS              //
    ///////////////////////////////////////////////

    public void setPosition(Vector3f vector3f) {
        this.position = vector3f;

        setDirty();
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);

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

    public void addPosition(Vector3f vector3f) {
        this.position.x += vector3f.x;
        this.position.y += vector3f.y;
        this.position.z += vector3f.z;

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

    ///////////////////////////////////////////////
    //          LOCAL POSITION SETTERS           //
    ///////////////////////////////////////////////

    public void addLocalPosition(float x, float y, float z) {
        Vector3f localMovement = new Vector3f(x, y, z);
        Vector3f globalMovement = localMovement.rotate(rotation);

        this.position.add(globalMovement);

        setDirty();
    }

    public void addLocalPositionX(float x) {
        addLocalPosition(x, 0, 0);
    }

    public void addLocalPositionY(float y) {
        addLocalPosition(0, y, 0);
    }

    public void addLocalPositionZ(float z) {
        addLocalPosition(0, 0, z);
    }

    ///////////////////////////////////////////////
    //               SCALE SETTERS               //
    ///////////////////////////////////////////////

    public void setScale(float x, float y, float z) {
        this.scale.x = x;
        this.scale.y = y;
        this.scale.z = z;

        setDirty();
    }

    public void setScale(Vector3f vector3f) {
        this.scale = vector3f;

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

    ///////////////////////////////////////////////
    //             ROTATION SETTERS              //
    ///////////////////////////////////////////////

    public void setRotation(float x, float y, float z, float angle) {
        this.rotation.fromAxisAngleRad(x, y, z, angle);
    }

    public void setRotationEuler(float pitch, float yaw, float roll) {
        this.rotation.rotationXYZ(pitch, yaw, roll);

        setDirty();
    }

    public void addRotationEuler(float pitch, float yaw, float roll) {
        Quaternionf additionalRotation = new Quaternionf().rotationXYZ(pitch, yaw, roll);

        this.rotation.mul(additionalRotation);

        setDirty();
    }

    public void setRotation(Quaternionf quaternion) {
        this.rotation.set(quaternion);

        setDirty();
    }

    public void addRotationQuaternion(Quaternionf quaternion) {
        this.rotation.mul(quaternion);

        setDirty();
    }

    ///////////////////////////////////////////////
    //          LOCAL ROTATION SETTERS           //
    ///////////////////////////////////////////////

    public void addLocalRotationEuler(float pitch, float yaw, float roll) {
        Quaternionf localRotation = new Quaternionf().rotationXYZ(pitch, yaw, roll);

        this.rotation.mul(localRotation);

        setDirty();
    }

    ///////////////////////////////////////////////
    //                   OTHER                   //
    ///////////////////////////////////////////////

    public Vector3f getCleanPosition() {
        return this.cleanPosition;
    }

    public Vector3f getCleanScale() {
        return this.cleanScale;
    }

    public Quaternionf getCleanRotation() {
        return this.cleanRotation;
    }

    public Transform getParent() {
        return this.parent;
    }

    public void setParent(Transform parent) {
        Scene scene = SceneManager.getCurrentScene();
        if (scene != null) {
            if (this.parent == scene.getTransform() && parent != scene.getTransform()) {
                scene.removeRootTransform(this);
            }
            else if (this.parent != scene.getTransform() &&  parent == scene.getTransform()) {
                scene.addRootTransform(this);
            }
        }

        this.parent = parent;
    }

    public List<GameObject> getChildren() {
        Scene scene = SceneManager.getCurrentScene();
        if (scene != null) {
            return scene.getTransformChildren(this);
        }

        return new ArrayList<>();
    }

    public boolean hasChildren() {
        Scene scene = SceneManager.getCurrentScene();
        if (scene != null) {
            return scene.hasTransformChildren(this);
        }

        return false;
    }

    public void create(Vector3f position, Vector3f scale) {
        this.id = UniqueIdentifierManager.generateUID();

        this.position = position;
        this.scale = scale;
        this.rotation = new Quaternionf();

        this.cleanPosition = new Vector3f(position);
        this.cleanScale = new Vector3f(scale);
        this.cleanRotation = new Quaternionf();
    }

    public Transform copy() {
        Transform newTransform = new Transform();

        newTransform.setPosition(this.getPosition());
        newTransform.setScale(this.getScale());
        newTransform.setRotation(this.getRotation());

        newTransform.setDirty();

        return newTransform;
    }

    public void set(Transform transform) {
        this.position = transform.position;
        this.scale = transform.scale;
        this.rotation = transform.rotation;

        this.setDirty();
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty() {
        this.dirty = true;
    }

    public void setClean() {
        this.cleanPosition.set(this.position);
        this.cleanScale.set(this.scale);
        this.cleanRotation.set(this.rotation);

        this.dirty = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Transform transform) {
            return transform.id == this.id;
        } else {
            return false;
        }
    }

    public boolean equalsComponents(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Transform transform) {
            return transform.position.equals(this.position) && transform.scale.equals(this.scale) && transform.rotation.equals(this.rotation);
        } else {
            return false;
        }
    }

    @Override
    public String serialize() {
        return StringBuilderHelper.concatenate(
                position.x, ",", position.y, ",", position.z,
                "_",
                scale.x, ",", scale.y, ",", scale.z,
                "_",
                rotation.x, ",",  rotation.y, ",",  rotation.z, ",",  rotation.w
        );
    }

    @Override
    public Object deserialize(String data) {
        String[] splits = data.split("_");

        String[] position = splits[0].split(",");
        String[] scale = splits[1].split(",");
        String[] rotation = splits[2].split(",");

        this.position = new Vector3f(Float.parseFloat(position[0]), Float.parseFloat(position[1]), Float.parseFloat(position[2]));
        this.scale = new Vector3f(Float.parseFloat(scale[0]), Float.parseFloat(scale[1]), Float.parseFloat(scale[2]));
        this.rotation = new Quaternionf(Float.parseFloat(rotation[0]), Float.parseFloat(rotation[1]), Float.parseFloat(rotation[2]), Float.parseFloat(rotation[3]));

        return this;
    }
}
