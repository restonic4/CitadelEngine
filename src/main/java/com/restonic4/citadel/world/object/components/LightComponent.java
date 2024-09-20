package com.restonic4.citadel.world.object.components;

import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.object.Component;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class LightComponent extends Component {
    private LightType lightType;
    private Vector3f color;

    public LightComponent(LightType lightType, Vector3f color) {
        this.lightType = lightType;
        this.color = color;
    }

    public LightComponent(LightType lightType) {
        this.lightType = lightType;
        this.color = new Vector3f(1, 1, 1);
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {

    }

    public Vector3f getColor() {
        return color;
    }

    public LightType getLightType() {
        return this.lightType;
    }

    public Vector3f getDirection() {
        if (lightType == LightType.DIRECTIONAL) {
            return this.gameObject.transform.getPosition();
        }
        else {
            Quaternionf rotation = this.gameObject.transform.getRotation();

            Vector3f forward = new Vector3f(0, 0, -1);
            Matrix4f rotationMatrix = new Matrix4f().rotate(rotation);

            return rotationMatrix.transformDirection(forward);
        }
    }

    public enum LightType {
        DIRECTIONAL(0, new Vector3f(1.0f, 0.09f, 0.032f)),
        POINT(1, new Vector3f(1.0f, 0.09f, 0.032f));

        private final Vector3f originalAttenuationFactors;
        private Vector3f attenuationFactors;
        private final int id;

        private Vector4f completeData = new Vector4f();

        LightType(int id, Vector3f attenuationFactors) {
            this.originalAttenuationFactors = attenuationFactors;
            this.attenuationFactors = new Vector3f(attenuationFactors);
            this.id = id;
        }

        public Vector3f getAttenuationFactors() {
            return attenuationFactors;
        }

        public int getId() {
            return this.id;
        }

        public Vector4f getAttenuationFactorsComplete() {
            completeData.setComponent(0, id);
            completeData.setComponent(1, attenuationFactors.x);
            completeData.setComponent(2, attenuationFactors.y);
            completeData.setComponent(3, attenuationFactors.z);

            return completeData;
        }

        public void setAttenuationFactors(Vector3f factors) {
            this.attenuationFactors = factors;
        }

        public void setToInfiniteRange() {
            this.attenuationFactors.setComponent(0, 1);
            this.attenuationFactors.setComponent(1, 0);
            this.attenuationFactors.setComponent(2, 0);
        }

        public void adjustAttenuationByRange(float range) {
            if (range < 0) {
                Logger.log("Range must be a positive value");
                return;
            }

            if (range == 0) {
                this.attenuationFactors.setComponent(0, 100);
                this.attenuationFactors.setComponent(1, 100);
                this.attenuationFactors.setComponent(2, 100);
            } else if (range == 1) {
                this.attenuationFactors.setComponent(0, this.originalAttenuationFactors.x);
                this.attenuationFactors.setComponent(1, this.originalAttenuationFactors.y);
                this.attenuationFactors.setComponent(2, this.originalAttenuationFactors.z);
            } else {
                this.attenuationFactors.setComponent(0, this.originalAttenuationFactors.x / range);
                this.attenuationFactors.setComponent(1, this.originalAttenuationFactors.y / range);
                this.attenuationFactors.setComponent(2, this.originalAttenuationFactors.z / (range * range));
            }
        }
    }

}
