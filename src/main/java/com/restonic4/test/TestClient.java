package com.restonic4.test;

import com.restonic4.citadel.core.GameLogic;
import com.restonic4.citadel.core.editor.SceneSerializer;
import com.restonic4.citadel.files.save.Node;
import com.restonic4.citadel.files.save.NodeType;
import com.restonic4.citadel.util.StringHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.SceneManager;

import java.io.DataOutputStream;

public class TestClient implements GameLogic {
    @Override
    public void start() {
        SceneManager.loadScene(new TestScene());

        Node node = new Node("main", NodeType.INTEGER);
        node.setValue(1);

        node.addChild(node);

        try {
            Node.saveToCompressedFile(node, "node.txt");
        } catch (Exception e) {
            Logger.logError(e);
        }
    }

    @Override
    public void update() {

    }
}
