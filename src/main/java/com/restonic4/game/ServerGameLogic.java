package com.restonic4.game;

import com.restonic4.citadel.core.GameLogic;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ServerGameLogic implements GameLogic {
    public void start() {
        Logger.log("Starting the game server logic");

        //ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        /*scheduler.scheduleAtFixedRate(() -> {
            try {
                Packets.TEST.send(PacketType.SERVER_TO_ALL_CLIENTS,
                        new PacketData(
                                new Vector3f(
                                        RandomUtil.random(-10, 10),
                                        RandomUtil.random(-10, 10),
                                        RandomUtil.random(-10, 10)
                                )
                        )
                );
            } catch (Exception e) {
                Logger.log(e.getMessage());
            }
        }, 0, 250, TimeUnit.MILLISECONDS);*/
    }

    @Override
    public void update() {

    }
}
