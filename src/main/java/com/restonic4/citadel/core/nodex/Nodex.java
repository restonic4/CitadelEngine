package com.restonic4.citadel.core.nodex;

import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.managers.NodeTypes;
import com.restonic4.citadel.registries.built_in.types.NodeType;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.ConfigurableGZIPOutputStream;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

import java.io.*;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;

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

public abstract class Nodex {
    public static ValueNode getSavedValueNode(String key) {
        return (ValueNode) getSavedNode(key);
    }

    public static RootNode getSavedRootNode(String key) {
        return (RootNode) getSavedNode(key);
    }

    public static Node getSavedNode(String key) {
        String dirPath = FileManager.createDirectory(CitadelConstants.NODEX_DIRECTORY);
        String filePath = dirPath + "/" + key + ".ndx";

        try (DataInputStream in = new DataInputStream(new GZIPInputStream(new FileInputStream(filePath)))) {
            return deserialize(in);
        } catch (Exception exception) {
            Logger.logError(exception);
            return null;
        }
    }

    public static void save(Node node) {
        String dirPath = FileManager.createDirectory(CitadelConstants.NODEX_DIRECTORY);
        String filePath = dirPath + "/" + node.getKey() + ".ndx";

        try (DataOutputStream out = new DataOutputStream(new ConfigurableGZIPOutputStream(new FileOutputStream(filePath)).setCompressionLevel(Deflater.BEST_COMPRESSION))) {
            serialize(node, out);
        } catch (Exception exception) {
            Logger.logError(exception);
        }
    }

    public static void serialize(Node node, DataOutputStream out) throws IOException {
        String key = node.getKey();
        String assetLocation = node.getType().getAssetLocation().toString();

        out.writeUTF(key);
        out.writeUTF(assetLocation);
        node.getType().serialize(node, out);
    }

    public static Node deserialize(DataInputStream in) throws IOException {
        String key = in.readUTF();
        String assetLocation = in.readUTF();

        NodeType type = Registry.getRegistryObject(Registries.NODE_TYPE, new AssetLocation(assetLocation));

        Node node = null;

        if (type == NodeTypes.NODE) {
            node = new RootNode(key);
        } else {
            node = new ValueNode(key, type);
        }

        node.getType().deserialize(node, in);

        return node;
    }

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
