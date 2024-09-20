package com.restonic4.citadel.networking;

import io.netty.channel.ChannelHandlerContext;

public interface MessageBus {
    void registerHandler(int clientId, ChannelHandlerContext ctx);
    void registerServerHandler(ChannelHandlerContext ctx);
    void unregisterHandler(int clientId);
    void sendMessageToClient(int clientId, String message);
    void sendMessageToAllClients(String message);
    void sendMessageToServer(String message);
}
