package com.restonic4.test;

import com.restonic4.citadel.core.GameLogic;
import com.restonic4.citadel.core.nodex.Node;
import com.restonic4.citadel.core.nodex.RootNode;
import com.restonic4.citadel.core.nodex.ValueNode;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.managers.Locales;
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

        RootNode playerData = new RootNode("playerData");

        ValueNode coins = new ValueNode("coins", 10);
        ValueNode wins = new ValueNode("wins", 0);
        ValueNode isNew = new ValueNode("isNew", true);

        RootNode inventory = new RootNode("inventory");

        RootNode sword = new RootNode("sword");
        sword.addChild(
                new ValueNode("slot", 0),
                new ValueNode("amount", 14)
        );

        inventory.addChild(sword);

        playerData.addChild(coins, wins, isNew, inventory);

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
