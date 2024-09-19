package com.restonic4.citadel.world.object.components;

import com.restonic4.citadel.world.object.Component;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

public class DebugComponent extends Component {
    @Override
    public void start() {
        Logger.log("Debug component added to the game object: " + gameObject.getName());
    }

    @Override
    public void update() {

    }
}