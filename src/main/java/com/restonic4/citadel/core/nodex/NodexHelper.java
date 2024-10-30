package com.restonic4.citadel.core.nodex;

import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.managers.NodeTypes;
import com.restonic4.citadel.registries.built_in.types.NodeType;

import java.util.Map;

/**
 <h1>Examples:</h1>
 <br>
 <h2>Example 1: Simple player data</h2>
 <pre>{@code
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
 }</pre>
 <br>
 <h2>Example 2: Better inventory</h2>
 <pre>{@code
RootNode inventory = new RootNode("inventory");

RootNode sword = new RootNode("sword");

ValueNode swordSlotPosition = new ValueNode("slot", 0);
ValueNode swordAmount = new ValueNode("amount", 14);

sword.addChild(swordSlotPosition, swordAmount);

inventory.addChild(sword);
}</pre>
 <br>
 <h2>Example 3: Simplification</h2>
 <pre>{@code
RootNode inventory = new RootNode("inventory");

RootNode sword = new RootNode("sword");
sword.addChild(
new ValueNode("slot", 0),
new ValueNode("amount", 14)
);

inventory.addChild(sword);
}</pre>
 <br>
 <h2>Example 4: Getting a value</h2>
 <pre>{@code
RootNode playerData = new RootNode("playerData");

ValueNode playerCoins = playerData.getValueNode("coins");
}</pre>
 */

public abstract class NodexHelper {
    public static NodeType getDesiredNodeType(Object value) {
        NodeType type = null;

        Map<AssetLocation, NodeType> registries = Registry.getRegistry(Registries.NODE_TYPE);
        for (NodeType nodeType : registries.values()) {
            if (value.getClass().getSimpleName().toLowerCase().equals(nodeType.getAssetLocation().getPath())) {
                type = nodeType;
            }
        }

        if (type == null) {
            throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
        }

        return type;
    }
}
