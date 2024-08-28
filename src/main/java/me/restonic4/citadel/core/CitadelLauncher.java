package me.restonic4.citadel.core;

import me.restonic4.citadel.util.debug.diagnosis.Logger;

public class CitadelLauncher {
    private IGameLogic iGameLogic;

    public CitadelLauncher(IGameLogic iGameLogic) {
        this.iGameLogic = iGameLogic;
    }

    public void launch() {
        Logger.log("Starting Citadel engine");

        Window.getInstance().run(this.iGameLogic);
    }
}
