package com.restonic4.citadel.networking.packets;

import com.restonic4.citadel.networking.PacketData;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import io.netty.channel.ChannelHandlerContext;
import com.restonic4.citadel.networking.PacketType;
import com.restonic4.citadel.registries.built_in.managers.PacketDataTypes;
import com.restonic4.citadel.registries.built_in.types.Packet;

public class ConnectionPacket extends Packet {
    public ConnectionPacket(PacketType packetType) {
        super(packetType);
    }

    @Override
    public void execute(ChannelHandlerContext ctx) {
        PacketData packetData = this.getPacketData();
        Integer[] ints = packetData.getData(PacketDataTypes.INTEGER);
        Double[] doubles = packetData.getData(PacketDataTypes.DOUBLE);

        Logger.log(StringBuilderHelper.concatenate("Connected to the server, received data: {\"client_id\": ", ints[0], ", \"time_alive\": ", doubles[0]));
    }
}
