package me.restonic4.engine.object.components;

import me.restonic4.engine.object.Component;
import me.restonic4.engine.util.debug.Logger;

public class DebugComponent extends Component {
    @Override
    public void start() {
        Logger.log("Debug component added to the game object: " + gameObject.getName());
    }

    @Override
    public void update() {

    }
}
