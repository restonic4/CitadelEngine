package com.restonic4.citadel.registries.built_in.managers;

import com.restonic4.citadel.networking.PacketType;
import com.restonic4.citadel.networking.packets.TestPacket;
import com.restonic4.citadel.registries.AbstractRegistryInitializer;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.types.Packet;
import com.restonic4.citadel.util.CitadelConstants;

public class Packets extends AbstractRegistryInitializer {
    public static Packet DEFAULT;
    public static TestPacket TEST;

    @Override
    public void register() {
        DEFAULT = Registry.register(Registries.PACKET, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "default"), new Packet(PacketType.CLIENT_TO_SERVER));
        TEST = (TestPacket) Registry.register(Registries.PACKET, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "test"), new TestPacket(PacketType.SERVER_TO_CLIENT));}
}
