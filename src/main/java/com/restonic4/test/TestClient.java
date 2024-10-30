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








        // Example 1

        /*
        RootNode playerData = new RootNode("playerData");

        ValueNode coins = new ValueNode("coins", 10);
        ValueNode wins = new ValueNode("wins", 0);
        ValueNode isNew = new ValueNode("isNew", true);

        RootNode inventory = new RootNode("inventory");

        ValueNode sword = new ValueNode("sword", 1);
        ValueNode bow = new ValueNode("bow", 2);
        ValueNode block = new ValueNode("block", 3);

        inventory.addChild(sword, bow, block);

        playerData.addChild(coins, wins, isNew, inventory);
        */

        // Example 2

        /*
        RootNode inventory = new RootNode("inventory");

        RootNode sword = new RootNode("sword");

        ValueNode swordSlotPosition = new ValueNode("slot", 0);
        ValueNode swordAmount = new ValueNode("amount", 14);

        sword.addChild(swordSlotPosition, swordAmount);

        inventory.addChild(sword);
         */

        // Example 3

        /*
        RootNode inventory = new RootNode("inventory");

        RootNode sword = new RootNode("sword");
        sword.addChild(
                new ValueNode("slot", 0),
                new ValueNode("amount", 14)
        );

        inventory.addChild(sword);
        */

        // Example 4

        RootNode playerData = new RootNode("playerData");

        ValueNode playerCoins = playerData.getValueNode("coins");







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
