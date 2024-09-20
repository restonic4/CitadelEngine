package com.restonic4.citadel.networking;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.restonic4.citadel.exceptions.NetworkException;
import com.restonic4.citadel.registries.built_in.types.Packet;
import org.jetbrains.annotations.NotNull;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    public MessageBus messageBus;

    public ClientHandler(MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {
        messageBus.registerServerHandler(ctx);
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) {
        String received = (String) msg;
        Packet packet = PacketFactory.createPacket(received);
        if (packet.getPacketType() == PacketType.SERVER_TO_CLIENT) {
            packet.execute(ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        throw new NetworkException("Connection lost with the server.");
    }
}
