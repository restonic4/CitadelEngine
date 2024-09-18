package com.restonic4.citadel.networking.packets;

import io.netty.channel.ChannelHandlerContext;
import com.restonic4.citadel.networking.PacketType;
import com.restonic4.citadel.registries.built_in.managers.PacketDataTypes;
import com.restonic4.citadel.registries.built_in.types.Packet;
import com.restonic4.citadel.world.SceneManager;
import com.restonic4.game.core.scenes.WorldScene;
import org.joml.Vector3f;

public class TestPacket extends Packet {
    public TestPacket(PacketType packetType) {
        super(packetType);
    }

    @Override
    public void execute(ChannelHandlerContext ctx) {
        if (SceneManager.getCurrentScene() instanceof WorldScene worldScene) {
            Vector3f[] vecs = this.getPacketData().getData(PacketDataTypes.VECTOR3F);
            Vector3f vector3f = vecs[0];

            if (worldScene.test2 != null) {
                worldScene.test2.transform.setPosition(vector3f);
            }
        }
    }
}
