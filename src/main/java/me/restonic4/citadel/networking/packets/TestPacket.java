package me.restonic4.citadel.networking.packets;

import io.netty.channel.ChannelHandlerContext;
import me.restonic4.citadel.registries.built_in.types.Packet;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.game.core.scenes.WorldScene;

public class TestPacket extends Packet {
    @Override
    public void execute(ChannelHandlerContext ctx) {
        if (SceneManager.getCurrentScene() instanceof WorldScene worldScene) {
            String[] args = this.getData();
            float x = Float.parseFloat(args[0]);
            float y = Float.parseFloat(args[1]);
            float z = Float.parseFloat(args[2]);

            if (worldScene.test2 != null) {
                worldScene.test2.transform.setPosition(x, y, z);
            }
        }
    }
}
