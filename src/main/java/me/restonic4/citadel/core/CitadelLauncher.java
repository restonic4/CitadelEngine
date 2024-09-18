package me.restonic4.citadel.core;

import me.restonic4.citadel.api.ModLoader;
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

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CitadelLauncher {
    private static CitadelLauncher instance;

    private final CitadelSettings citadelSettings;
    private final ModLoader modLoader;

    Thread nettyThread = null;
    boolean shouldEnd = false;

    private CitadelLauncher(CitadelSettings citadelSettings) {
        this.citadelSettings = citadelSettings;
        this.modLoader = new ModLoader();
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

        RegistryManager.registerBuiltInRegistrySet(new Shaders());
        RegistryManager.registerBuiltInRegistrySet(new Sounds());
        RegistryManager.registerBuiltInRegistrySet(new ProfilerStats());
        RegistryManager.registerBuiltInRegistrySet(new Locales());
        RegistryManager.registerBuiltInRegistrySet(new KeyBinds());
        RegistryManager.registerBuiltInRegistrySet(new ImGuiScreens());
        RegistryManager.registerBuiltInRegistrySet(new PacketDataTypes());
        RegistryManager.registerBuiltInRegistrySet(new Packets());
        RegistryManager.registerBuiltInRegistrySet(new FrameBuffers());
        RegistryManager.registerBuiltIn();

        this.citadelSettings.getSharedGameLogic().start();

        Logger.log("Locale: " + Localizer.fromJavaLocale(operatingSystem.getSystemLocale()).getAssetLocation().getPath());

        if (operatingSystem.isAppRunning("idea64.exe")) {
            Logger.log("You coding huh?");
        }

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            killNetworkThreads();
            shouldEnd = true;
            Logger.getPersistentLogger().onCrash(thread, throwable);
            throwable.printStackTrace();
            //throw new RuntimeException(throwable.getMessage());
        });

        if (!citadelSettings.isServerSide()) { // Client side
            Logger.log("Launching as client");

            Window window = Window.getInstance();
            window.init();

            nettyThread = new Thread(() -> {
                try {
                    Client client = new Client("localhost", 8080);
                    client.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            nettyThread.setName("Networking client side");
            nettyThread.start();

            this.citadelSettings.getClientGameLogic().start();

            window.setCursorLocked(true);

            window.loop();
            window.cleanup();
        }
        else { // Server side
            Logger.log("Launching as server");

            nettyThread = new Thread(() -> {
                try {
                    DebugManager.setDebugMode(true);
                    Server server = new Server(8080);
                    server.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            nettyThread.setName("Networking server side");
            nettyThread.start();

            // Java arguments
            for (int i = 0; i < citadelSettings.getArgs().length; i++) {
                if (Objects.equals(citadelSettings.getArgs()[i], "citadelConsole")) {
                    Thread renderThread = new Thread(() -> {
                        try {
                            Window.getInstance().initGuiOnly();
                        } catch (Exception e) {
                            killNetworkThreads();
                            System.exit(0);
                            throw new RuntimeException(e);
                        }
                    });
                    renderThread.setName("Server console rendering");
                    renderThread.start();

                    ImGuiScreens.SERVER_CONSOLE.show();
                }
            }

            this.citadelSettings.getServerGameLogic().start();
        }

        this.modLoader.loadMods();

        while (!shouldEnd) {
            this.modLoader.update();
            this.citadelSettings.getSharedGameLogic().update();
            this.citadelSettings.getServerGameLogic().update();
        }

        CitadelLifecycleEvents.CITADEL_STOPPED.invoker().onCitadelStopped(CitadelLauncher.getInstance());
    }

    private void killNetworkThreads() {
        if (nettyThread != null) {
            Logger.log("Killing netty thread");
            nettyThread.interrupt();
        }
    }

    public String getAppName() {
        return this.citadelSettings.getAppName();
    }

    public CitadelSettings getSettings() {
        return this.citadelSettings;
    }
}
