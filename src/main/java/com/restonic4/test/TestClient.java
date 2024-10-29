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

        Node node = Nodex.createRootNode();
        Nodex.setValue(node, "coins", 10);
        Nodex.setValue(node, "level", 1.2f);
        Nodex.setValue(node, "name", "Aguacate");
        Nodex.setValue(node, "prefix", 'a');
        Nodex.setValue(node, "position", new Vector3f(15, 64, 20));
        Nodex.setValue(node, "position", new Node("caca", NodeTypes.BOOLEAN));

        try {
            Node.saveToCompressedFile(node, "node");
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
