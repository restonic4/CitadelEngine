package com.restonic4.citadel.registries.built_in.types;

import io.netty.channel.ChannelHandlerContext;
import com.restonic4.citadel.networking.NetworkingManager;
import com.restonic4.citadel.networking.PacketData;
import com.restonic4.citadel.networking.PacketType;
import com.restonic4.citadel.registries.RegistryObject;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

public class Packet extends RegistryObject {
    private PacketType packetType;
    private PacketData packetData;

    public Packet(PacketType packetType) {
        if (packetType == PacketType.SERVER_TO_ALL_CLIENTS) {
            this.packetType = PacketType.SERVER_TO_CLIENT;
        }
        else {
            this.packetType = packetType;
        }
    }

    public void execute(ChannelHandlerContext ctx) {
        Logger.log(getMessage());
    };

    public void setData(PacketData data) {
        this.packetData = data;
    }

    public PacketData getPacketData() {
        return this.packetData;
    }

    public void send(PacketType packetType, PacketData packetData) {
        setData(packetData);
        send(packetType);
    }

    public void send(PacketType packetType, int clientId, PacketData packetData) {
        setData(packetData);
        send(packetType, clientId);
    }

    public void send(PacketType packetType) {
        NetworkingManager.sendPacket(packetType, getMessage());
    }

    public void send(PacketType packetType, int clientId) {
        NetworkingManager.sendPacket(packetType, clientId, getMessage());
    }

    public String getMessage() {
        String message = this.getAssetLocation().toString();
        return message + "|" + this.packetData + "\n";
    }

    public PacketType getPacketType() {
        return this.packetType;
    }
}
