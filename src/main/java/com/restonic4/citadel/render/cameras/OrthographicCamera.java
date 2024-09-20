package com.restonic4.citadel.render.cameras;

import com.restonic4.ClientSide;
import com.restonic4.citadel.core.Window;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.world.object.Transform;

@ClientSide
public class OrthographicCamera extends Camera {
    public OrthographicCamera(Transform transform) {
        super(transform);
    }

    public void adjustProjection() {
        this.projectionMatrix.identity();

        projectionMatrix.ortho(0.0f, Window.getInstance().getWidth(), 0.0f, Window.getInstance().getHeight(), CitadelConstants.CAMERA_NEAR_PLANE, CitadelConstants.CAMERA_FAR_PLANE);
    }
}
