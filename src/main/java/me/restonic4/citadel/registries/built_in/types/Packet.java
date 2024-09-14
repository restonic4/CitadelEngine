package me.restonic4.citadel.registries.built_in.types;

import io.netty.channel.ChannelHandlerContext;
import me.restonic4.citadel.networking.NetworkingManager;
import me.restonic4.citadel.networking.PacketType;
import me.restonic4.citadel.registries.RegistryObject;
import me.restonic4.citadel.util.StringBuilderHelper;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

public class Packet extends RegistryObject {
    private PacketType packetType;
    private String[] data;

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

    public void setData(String[] data) {
        this.data = data;
    }

    public String[] getData() {
        return this.data;
    }

    public void send(PacketType packetType) {
        NetworkingManager.sendPacket(packetType, getMessage());
    }

    public void send(PacketType packetType, int clientId) {
        NetworkingManager.sendPacket(packetType, clientId, getMessage());
    }

    public String getMessage() {
        String message = this.getAssetLocation().toString();

        for (int i = 0; i < data.length; i++) {
            message = StringBuilderHelper.concatenate(message, "|", data[i]);
        }

        return message + "\n";
    }

    public PacketType getPacketType() {
        return this.packetType;
    }
}
