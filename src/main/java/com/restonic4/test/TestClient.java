package com.restonic4.test;

import com.restonic4.citadel.core.GameLogic;
import com.restonic4.citadel.core.nodex.Node;
import com.restonic4.citadel.registries.built_in.managers.NodeTypes;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.SceneManager;

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

        Node node = new Node("main", NodeTypes.STRING);
        node.setValue("hola xd");

        node.addChild(node);

        try {
            Node.saveToCompressedFile(node, "node");
        } catch (Exception e) {
            Logger.logError(e);
        }
    }

    @Override
    public void update() {

    }
}
