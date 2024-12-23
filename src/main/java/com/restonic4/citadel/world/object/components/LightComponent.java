package com.restonic4.citadel.world.object.components;

import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.render.gui.ImGuiHelper;
import com.restonic4.citadel.util.helpers.StringBuilderHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.util.history.commands.ChangeLightColorHistoryCommand;
import com.restonic4.citadel.util.history.commands.ChangeLightTypeHistoryCommand;
import com.restonic4.citadel.world.object.Component;
import imgui.ImGui;
import imgui.flag.ImGuiColorEditFlags;
import imgui.type.ImInt;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class LightComponent extends Component {
    private LightType lightType;
    private Vector3f color;

    LightType[] lightTypes = LightType.values();
    String[] comboOptions = new String[lightTypes.length];

    private float[] colorValues = new float[4];
    private ImInt currentOption = new ImInt(0);

    public LightComponent() {}

    public LightComponent(LightType lightType, Vector3f color) {
        this.lightType = lightType;
        this.color = color;
    }

    public LightComponent(LightType lightType) {
        this.lightType = lightType;
        this.color = new Vector3f(1, 1, 1);
    }

    private void initColorValues() {
        colorValues[0] = color.x;
        colorValues[1] = color.y;
        colorValues[2] = color.z;
        colorValues[3] = 1.0f;
    }

    @Override
    public void start() {
        for (int i = 0; i < lightTypes.length; i++) {
            comboOptions[i] = lightTypes[i].name();
        }

        initColorValues();
    }

    @Override
    public void update() {

    }

    @Override
    public void renderEditorUI() {
        syncLightType();
        syncColor();

        ImGuiHelper.renderPropertyRow("Type", () -> {
            if (ImGui.combo(StringBuilderHelper.concatenate("##lightType", this.getId()), currentOption, comboOptions)) {
                LevelEditor.getHistoryCommandManager().executeCommand(new ChangeLightTypeHistoryCommand(this, lightTypes[currentOption.get()]));
            }
        });

        ImGuiHelper.renderPropertyRow("Color", () -> {
            if (ImGui.colorEdit4(StringBuilderHelper.concatenate("##colorPicker", this.getId()), colorValues, ImGuiColorEditFlags.AlphaPreviewHalf)) {
                LevelEditor.getHistoryCommandManager().executeCommand(new ChangeLightColorHistoryCommand(this, new Vector3f(colorValues[0], colorValues[1], colorValues[2])));
            }
        });
    }

    public void syncLightType() {
        for (int i = 0; i < lightTypes.length; i++) {
            if (lightTypes[i] == lightType) {
                currentOption.set(i);
                break;
            }
        }
    }

    public void syncColor() {
        colorValues[0] = color.x;
        colorValues[1] = color.y;
        colorValues[2] = color.z;
        colorValues[3] = 1;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setLightType(LightType lightType) {
        this.lightType = lightType;
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

    @Override
    public String serialize() {
        return StringBuilderHelper.concatenate(getSerializerID(), "%", lightType, ",", color.x, "-", color.y, "-", color.z);
    }

    @Override
    public Object deserialize(String data) {
        String[] splits = data.split(",");

        String[] color = splits[1].split("-");

        this.lightType = LightType.valueOf(splits[0]);
        this.color = new Vector3f(Float.parseFloat(color[0]), Float.parseFloat(color[1]), Float.parseFloat(color[2]));

        return this;
    }
}
