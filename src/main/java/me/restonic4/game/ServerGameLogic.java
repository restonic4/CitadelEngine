package me.restonic4.game;

import me.restonic4.citadel.core.IGameLogic;
import me.restonic4.citadel.networking.PacketType;
import me.restonic4.citadel.registries.RegistryManager;
import me.restonic4.citadel.registries.built_in.managers.Packets;
import me.restonic4.citadel.util.debug.DebugManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.util.debug.diagnosis.ProfilerManager;
import me.restonic4.citadel.util.math.RandomUtil;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.game.core.scenes.WorldScene;
import me.restonic4.game.core.world.sounds.Sounds;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerGameLogic implements IGameLogic {
    public void start() {
        Logger.log("Starting the game server logic");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                Packets.TEST.setData(new String[]{
                        String.valueOf(RandomUtil.random(-10, 10)),
                        String.valueOf(RandomUtil.random(-10, 10)),
                        String.valueOf(RandomUtil.random(-10, 10))
                });
                Packets.TEST.send(PacketType.SERVER_TO_ALL_CLIENTS);
            } catch (Exception e) {
                Logger.log(e.getMessage());
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
