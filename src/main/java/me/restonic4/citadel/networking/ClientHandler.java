package me.restonic4.citadel.networking;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import me.restonic4.citadel.exceptions.NetworkException;
import me.restonic4.citadel.registries.built_in.types.Packet;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.game.core.scenes.WorldScene;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    public MessageBus messageBus;

    public ClientHandler(MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        messageBus.registerServerHandler(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String received = (String) msg;
        Packet packet = PacketFactory.createPacket(received);
        packet.execute(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        throw new NetworkException("Connection lost with the server.");
    }
}
