package com.restonic4.citadel.networking;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.CitadelSettings;

public class NetworkingManager {
    private static CitadelSettings settings = CitadelLauncher.getInstance().getSettings();
    private static MessageBus messageBus = new MessageBusImpl();

    public static MessageBus getMessageBus() {
        return messageBus;
    }

    public static void sendPacket(PacketType packetType, String message) {
        if (packetType == PacketType.SERVER_TO_ALL_CLIENTS) {
            messageBus.sendMessageToAllClients(message);
        } else if (packetType == PacketType.CLIENT_TO_SERVER) {
            messageBus.sendMessageToServer(message);
        }
    }

    public static void sendPacket(PacketType packetType, int clientId, String message) {
        if (packetType == PacketType.SERVER_TO_CLIENT) {
            if (clientId == -1) {
                messageBus.sendMessageToAllClients(message);
            } else {
                messageBus.sendMessageToClient(clientId, message);
            }
        } else if (packetType == PacketType.SERVER_TO_ALL_CLIENTS) {
            messageBus.sendMessageToAllClients(message);
        } else if (packetType == PacketType.CLIENT_TO_SERVER) {
            messageBus.sendMessageToServer(message);
        }
    }
}
