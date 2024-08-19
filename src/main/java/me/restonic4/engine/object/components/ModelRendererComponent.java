package me.restonic4.engine.object.components;

import me.restonic4.engine.object.Component;
import org.joml.Vector4f;

public class ModelRendererComponent extends Component {
    private Vector4f color;

    public ModelRendererComponent(Vector4f color) {
        this.color = color;
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {

    }

    public Vector4f getColor() {
        return this.color;
    }
}
