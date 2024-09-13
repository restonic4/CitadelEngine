package me.restonic4.citadel.networking;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import me.restonic4.citadel.util.StringBuilderHelper;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageBusImpl implements MessageBus {
    private ChannelHandlerContext serverContext;
    private final Map<Integer, ChannelHandlerContext> clientContexts = new ConcurrentHashMap<>();

    @Override
    public void registerHandler(int clientId, ChannelHandlerContext ctx) {
        clientContexts.put(clientId, ctx);
    }

    @Override
    public void registerServerHandler(ChannelHandlerContext ctx) {
        serverContext = ctx;
    }

    @Override
    public void unregisterHandler(int clientId) {
        clientContexts.remove(clientId);
    }

    @Override
    public void sendMessageToClient(int clientId, String message) {
        killStoppedContexts();
        ChannelHandlerContext ctx = clientContexts.get(clientId);
        if (ctx != null) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
        }
    }

    @Override
    public void sendMessageToAllClients(String message) {
        killStoppedContexts();
        for (ChannelHandlerContext ctx : clientContexts.values()) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
        }
    }

    @Override
    public void sendMessageToServer(String message) {
        killStoppedContexts();
        serverContext.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
    }

    private void killStoppedContexts() {
        Iterator<Map.Entry<Integer, ChannelHandlerContext>> iterator = clientContexts.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, ChannelHandlerContext> entry = iterator.next();
            ChannelHandlerContext ctx = entry.getValue();
            int clientId = entry.getKey();

            if (ctx.isRemoved()) {
                ctx.close();
                iterator.remove();
                Logger.log(StringBuilderHelper.concatenate("The client ", clientId, " lost the connection."));
            }
        }
    }
}
