package me.restonic4.citadel.networking.packets;

import io.netty.channel.ChannelHandlerContext;
import me.restonic4.citadel.networking.PacketData;
import me.restonic4.citadel.networking.PacketType;
import me.restonic4.citadel.registries.built_in.managers.PacketDataTypes;
import me.restonic4.citadel.registries.built_in.types.Packet;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.game.core.scenes.WorldScene;

public class TestPacket extends Packet {
    public TestPacket(PacketType packetType) {
        super(packetType);
    }

    @Override
    public void execute(ChannelHandlerContext ctx) {
        if (SceneManager.getCurrentScene() instanceof WorldScene worldScene) {
            Integer[] coords = this.getPacketData().getData(PacketDataTypes.INTEGER);
            float x = coords[0];
            float y = coords[1];
            float z = coords[2];

            if (worldScene.test2 != null) {
                worldScene.test2.transform.setPosition(x, y, z);
            }
        }
    }
}
