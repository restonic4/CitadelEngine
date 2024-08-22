package me.restonic4.engine.object.components;

import me.restonic4.engine.object.Component;
import me.restonic4.engine.object.Mesh;
import org.joml.Vector4f;

public class ModelRendererComponent extends Component {
    private Mesh mesh;


    public ModelRendererComponent(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {

    }

    public Mesh getMesh() {
        return this.mesh;
    }
}
