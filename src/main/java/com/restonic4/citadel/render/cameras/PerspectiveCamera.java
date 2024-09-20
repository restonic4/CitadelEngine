package com.restonic4.citadel.render.cameras;

import com.restonic4.ClientSide;
import com.restonic4.citadel.core.Window;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.world.object.Transform;

@ClientSide
public class PerspectiveCamera extends Camera {
    public PerspectiveCamera(Transform transform) {
        super(transform);
    }

    public void adjustProjection() {
        this.projectionMatrix.identity();

        projectionMatrix.perspective(CitadelConstants.CAMERA_FOV, Window.getInstance().getAspectRatio(), CitadelConstants.CAMERA_NEAR_PLANE, CitadelConstants.CAMERA_FAR_PLANE);
    }
}
