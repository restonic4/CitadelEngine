package com.restonic4.citadel.networking;

import com.restonic4.citadel.registries.built_in.managers.Packets;
import com.restonic4.citadel.util.Time;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.restonic4.citadel.registries.built_in.types.Packet;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import org.jetbrains.annotations.NotNull;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static int nextClientId = 1; // Simple ID generator
    private final MessageBus messageBus;
    private int clientId;

    public ServerHandler(MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {
        clientId = nextClientId++;
        messageBus.registerHandler(clientId, ctx);
        Logger.log("Client connected with ID: " + clientId);

        Packets.CONNECTION.send(PacketType.SERVER_TO_CLIENT, clientId, new PacketData(
                clientId, Time.getRunningTime()
        ));
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) {
        String received = (String) msg;
        Packet packet = PacketFactory.createPacket(received);
        if (packet.getPacketType() == PacketType.CLIENT_TO_SERVER) {
            packet.execute(ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Logger.log("Connection lost with client " + clientId + ": " + cause.getMessage());
        messageBus.unregisterHandler(clientId);
        ctx.close();
    }
}
