package com.restonic4.citadel.networking;

import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.types.Packet;

import java.util.Arrays;

public class PacketFactory {
    public static Packet createPacket(String data) {
        String[] parts = data.split("\\|");
        String rawAssetLocation = parts[0];
        String[] packetData = Arrays.copyOfRange(parts, 1, parts.length);

        AssetLocation assetLocation = new AssetLocation(rawAssetLocation);

        Packet packet = Registry.getRegistryObject(Registries.PACKET, assetLocation);
        packet.setData(new PacketData(packetData));
        return packet;
    }
}
