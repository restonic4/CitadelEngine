package me.restonic4.citadel.core;

import me.restonic4.citadel.events.types.CitadelLifecycleEvents;
import me.restonic4.citadel.localization.Localizer;
import me.restonic4.citadel.networking.Client;
import me.restonic4.citadel.networking.PacketType;
import me.restonic4.citadel.networking.Server;
import me.restonic4.citadel.platform.PlatformManager;
import me.restonic4.citadel.platform.operating_systems.OperatingSystem;
import me.restonic4.citadel.registries.RegistryManager;
import me.restonic4.citadel.registries.built_in.managers.*;
import me.restonic4.citadel.sound.SoundManager;
import me.restonic4.citadel.util.GradleUtil;
import me.restonic4.citadel.util.debug.DebugManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.util.math.RandomUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CitadelLauncher {
    private static CitadelLauncher instance;

    private CitadelSettings citadelSettings;
    Thread nettyThread = null;

    private CitadelLauncher(CitadelSettings citadelSettings) {
        this.citadelSettings = citadelSettings;
    }

    public static CitadelLauncher create(CitadelSettings citadelSettings) {
        if (CitadelLauncher.instance == null) {
            CitadelLauncher.instance = new CitadelLauncher(citadelSettings);
        }
        return CitadelLauncher.instance;
    }

    public static CitadelLauncher getInstance() {
        if (CitadelLauncher.instance == null) {
            throw new IllegalStateException("There no engine instanced");
        }

        return CitadelLauncher.instance;
    }

    public void launch() {
        CitadelLifecycleEvents.CITADEL_STARTING.invoker().onCitadelStarting(this);

        OperatingSystem operatingSystem = PlatformManager.getOperatingSystem().get();

        Logger.log("Starting Citadel engine");
        Logger.log("Platform: " + operatingSystem);
        Logger.log("Java locale: " + operatingSystem.getSystemLocale());

        GradleUtil.logInfo();

        SoundManager.getInstance().init();

        RegistryManager.registerBuiltInRegistrySet(new Sounds());
        RegistryManager.registerBuiltInRegistrySet(new ProfilerStats());
        RegistryManager.registerBuiltInRegistrySet(new Locales());
        RegistryManager.registerBuiltInRegistrySet(new KeyBinds());
        RegistryManager.registerBuiltInRegistrySet(new ImGuiScreens());
        RegistryManager.registerBuiltInRegistrySet(new Packets());
        RegistryManager.registerBuiltIn();

        Logger.log("Locale: " + Localizer.fromJavaLocale(operatingSystem.getSystemLocale()).getAssetLocation().getPath());

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (nettyThread != null) {
                Logger.log("Killing netty thread");
                nettyThread.interrupt();
            }

            Logger.getPersistentLogger().onCrash(thread, throwable);
        });

        if (!citadelSettings.isServerSide()) {
            Logger.log("Launching as client");

            Window window = Window.getInstance();
            window.init();

            nettyThread = new Thread(() -> {
                try {
                    Client client = new Client("localhost", 8080);
                    client.run();
                } catch(InterruptedException v) {
                    System.out.println(v);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            nettyThread.setName("Networking client side");
            nettyThread.start();

            this.citadelSettings.getiGameLogic().start();

            window.loop();
            window.cleanup();
        }
        else {
            Logger.log("Launching as server");

            nettyThread = new Thread(() -> {
                try {
                    DebugManager.setDebugMode(true);
                    Server server = new Server(8080);
                    server.run();
                } catch(InterruptedException v) {
                    System.out.println(v);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            nettyThread.setName("Networking server side");
            nettyThread.start();

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

        CitadelLifecycleEvents.CITADEL_STOPPED.invoker().onCitadelStopped(CitadelLauncher.getInstance());
    }

    public String getAppName() {
        return this.citadelSettings.getAppName();
    }

    public CitadelSettings getSettings() {
        return this.citadelSettings;
    }
}
