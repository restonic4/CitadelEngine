package me.restonic4.citadel.world.object.components;

import me.restonic4.citadel.world.object.Component;
import me.restonic4.citadel.world.object.Mesh;

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
