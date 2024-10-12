package com.restonic4.citadel.world.object.components;

import com.restonic4.citadel.core.Window;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.world.object.Component;
import com.restonic4.citadel.world.object.Mesh;
import com.restonic4.citadel.world.object.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CameraComponent extends Component {
    private CameraType cameraType;
    protected Transform transform;

    protected Matrix4f projectionMatrix, viewMatrix;

    private boolean isSimulated;
    private Matrix4f fakeProjectionMatrix, fakeViewMatrix;

    // Cache

    private final Vector3f front = new Vector3f(0, 0, -1);
    private final Vector3f up = new Vector3f(0, 1, 0);
    private final Vector3f center = new Vector3f();

    public CameraComponent() {}

    public CameraComponent(CameraType cameraType) {
        this.cameraType = cameraType;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.isSimulated = false;

        adjustProjection();
    }

    public CameraComponent(CameraType cameraType, Transform transform) {
        this.cameraType = cameraType;
        this.transform = transform;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.isSimulated = false;

        adjustProjection();
    }

    @Override
    public void start() {
        syncTransformWithGameObject();

        getViewMatrix();
        getProjectionMatrix();
    }

    @Override
    public void update() {}

    private void syncTransformWithGameObject() {
        if (gameObject != null) {
            this.transform = gameObject.transform;
        }
    }

    public void adjustProjection() {
        if (this.cameraType == CameraType.ORTHOGRAPHIC) {
            adjustOrthographic();
        } else if (this.cameraType == CameraType.PERSPECTIVE) {
            adjustPerspective();
        }
    }

    public void adjustOrthographic() {
        this.projectionMatrix.identity();

        projectionMatrix.ortho(0.0f, Window.getInstance().getWidth(), 0.0f, Window.getInstance().getHeight(), CitadelConstants.CAMERA_NEAR_PLANE, CitadelConstants.CAMERA_FAR_PLANE);
    }

    public void adjustPerspective() {
        this.projectionMatrix.identity();

        projectionMatrix.perspective(CitadelConstants.CAMERA_FOV, Window.getInstance().getAspectRatio(), CitadelConstants.CAMERA_NEAR_PLANE, CitadelConstants.CAMERA_FAR_PLANE);
    }

    public Matrix4f getViewMatrix() {
        this.viewMatrix.identity();

        front.set(0, 0, -1).rotate(transform.getRotation());
        up.set(0, 1, 0).rotate(transform.getRotation());

        center.set(transform.getPosition()).add(front);

        this.viewMatrix.lookAt(transform.getPosition(), center, up);

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        adjustProjection();
        return this.projectionMatrix;
    }

    public Matrix4f getFakeProjectionMatrix() {
        return fakeProjectionMatrix;
    }

    public Matrix4f getFakeViewMatrix() {
        return fakeViewMatrix;
    }

    // TODO: Optimize this, this will probably create memory leaks
    public Vector3f[] getFrustumCorners() {
        Matrix4f invProjView = new Matrix4f();
        getProjectionMatrix().mul(getViewMatrix(), invProjView).invert();

        Vector3f[] frustumCorners = new Vector3f[8];
        frustumCorners[0] = new Vector3f(-1, -1, -1); // Near-bottom-left
        frustumCorners[1] = new Vector3f(1, -1, -1);  // Near-bottom-right
        frustumCorners[2] = new Vector3f(1, 1, -1);   // Near-top-right
        frustumCorners[3] = new Vector3f(-1, 1, -1);  // Near-top-left

        frustumCorners[4] = new Vector3f(-1, -1, 1);  // Far-bottom-left
        frustumCorners[5] = new Vector3f(1, -1, 1);   // Far-bottom-right
        frustumCorners[6] = new Vector3f(1, 1, 1);    // Far-top-right
        frustumCorners[7] = new Vector3f(-1, 1, 1);   // Far-top-left

        for (int i = 0; i < 8; i++) {
            frustumCorners[i].mulProject(invProjView);
        }

        return frustumCorners;
    }

    public void setSimulated(boolean value) {
        if (value) {
            this.fakeViewMatrix = new Matrix4f(viewMatrix);
            this.fakeProjectionMatrix = new Matrix4f(projectionMatrix);
        }

        this.isSimulated = value;
    }

    public boolean isSimulated() {
        return this.isSimulated;
    }

    public enum CameraType {
        PERSPECTIVE, ORTHOGRAPHIC
    }

    @Override
    public String serialize() {
        return StringBuilderHelper.concatenate("c%", cameraType);
    }

    @Override
    public Object deserialize(String data) {
        String[] splits = data.split(",");

        this.cameraType = CameraComponent.CameraType.valueOf(splits[0]);

        return this;
    }
}
