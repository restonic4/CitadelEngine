package com.restonic4.test;

import com.restonic4.citadel.core.GameLogic;
import com.restonic4.citadel.core.nodex.Node;
import com.restonic4.citadel.core.nodex.Nodex;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.managers.Locales;
import com.restonic4.citadel.registries.built_in.managers.NodeTypes;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.SceneManager;
import org.joml.Vector3f;

public class TestClient implements GameLogic {
    @Override
    public void start() {
        SceneManager.loadScene(new TestScene());

        try {
            Node foundNode = Node.loadFromCompressedFile("node");
            Logger.log("yippie");
        } catch (Exception e) {
            Logger.logError(e);
        }

        //

        Node playerData = Nodex.createRootNode();

        Nodex.setValue(playerData, "coins", 10);
        Nodex.setValue(playerData, "level", 1.2f);
        Nodex.setValue(playerData, "name", "Aguacate");
        Nodex.setValue(playerData, "prefix", 'a');
        Nodex.setValue(playerData, "position", new Vector3f(15, 64, 20));

        Node inventory = Nodex.addChild(playerData, "inventory");
        Nodex.setValue(inventory, "espada", 10);

        try {
            Node.saveToCompressedFile(playerData, "node");
        } catch (Exception e) {
            Logger.logError(e);
        }

        Logger.log("Registry memory size: " + Registry.getApproximatedMemorySize());
        Logger.log(Locales.EN_US.getData().toString());
    }

    @Override
    public void update() {

    }
}
