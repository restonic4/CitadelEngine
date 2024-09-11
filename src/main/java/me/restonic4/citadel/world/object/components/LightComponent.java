package me.restonic4.citadel.world.object.components;

import me.restonic4.citadel.util.StringBuilderHelper;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.world.object.Component;
import me.restonic4.citadel.world.object.Material;
import me.restonic4.citadel.world.object.Mesh;
import org.joml.Vector3f;

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

    public enum LightType {
        CUSTOM(new Vector3f(1.0f, 0.0f, 0.0f)),
        INFINITE(new Vector3f(1.0f, 0.0f, 0.0f)),
        POINT(new Vector3f(1.0f, 0.09f, 0.032f));

        private Vector3f attenuationFactors;

        LightType(Vector3f attenuationFactors) {
            this.attenuationFactors = attenuationFactors;
        }

        public Vector3f getAttenuationFactors() {
            return attenuationFactors;
        }

        public void setAttenuationFactors(Vector3f factors) {
            if (this != LightType.CUSTOM) {
                Logger.log(StringBuilderHelper.concatenate("This is not a custom light type (", this, "). You should use LightType.CUSTOM"));
            }

            this.attenuationFactors = factors;
        }
    }

}
