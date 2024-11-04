package com.restonic4.test;

import com.restonic4.citadel.core.GameLogic;
import com.restonic4.citadel.core.nodex.Nodex;
import com.restonic4.citadel.core.nodex.RootNode;
import com.restonic4.citadel.core.nodex.ValueNode;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.SceneManager;

public class TestClient implements GameLogic {
    @Override
    public void start() {
        SceneManager.loadScene(new TestScene());

        RootNode foundNode = Nodex.getSavedRootNode("playerData");
        Logger.log("yippie");

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

        Nodex.save(playerData);

        ValueNode waos = new ValueNode("waos", 629);
        waos.save();
        Nodex.backup(waos);

        ValueNode waosLoaded = Nodex.getSavedValueNode("waos");
        Logger.log("waos");
    }

    @Override
    public void update() {

    }
}
