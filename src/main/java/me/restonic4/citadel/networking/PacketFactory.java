package me.restonic4.citadel.networking;

import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.Packet;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

import java.util.Arrays;
import java.util.Map;

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
