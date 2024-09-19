package com.restonic4.citadel.core;

import com.restonic4.citadel.api.ModLoader;
import com.restonic4.citadel.events.types.CitadelLifecycleEvents;
import com.restonic4.citadel.localization.Localizer;
import com.restonic4.citadel.networking.Client;
import com.restonic4.citadel.networking.Server;
import com.restonic4.citadel.platform.PlatformManager;
import com.restonic4.citadel.platform.operating_systems.OperatingSystem;
import com.restonic4.citadel.registries.RegistryManager;
import com.restonic4.citadel.registries.built_in.managers.*;
import com.restonic4.citadel.sound.SoundManager;
import com.restonic4.citadel.util.ArrayHelper;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.GradleUtil;
import com.restonic4.citadel.util.debug.DebugManager;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

import java.util.Objects;

// TODO: This class is a chaos
public class CitadelLauncher {
    private static CitadelLauncher instance;

    private final CitadelSettings citadelSettings;
    private final ModLoader modLoader;

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

        logUsefulData();
        handleCrashes();
        startRegistries();

        startDesiredEnvironment();

        while (!shouldEnd) {
            this.modLoader.update();
            this.citadelSettings.getSharedGameLogic().update();

            if (citadelSettings.isServerSide()) {
                this.citadelSettings.getServerGameLogic().update();
            }
            else {
                this.citadelSettings.getClientGameLogic().update();
            }
        }

        CitadelLifecycleEvents.CITADEL_STOPPED.invoker().onCitadelStopped(CitadelLauncher.getInstance());
    }

    private void startDesiredEnvironment() {
        this.citadelSettings.getSharedGameLogic().start();

        if (!citadelSettings.isServerSide()) {
            Logger.log("Launching as client");
            startClient();
        }
        else {
            Logger.log("Launching as server");
            startServer();
        }

        this.modLoader.loadMods();
    }

    private void startClient() {
        ThreadManager.startThread(CitadelConstants.CLIENT_SIDE_THREAD_NAME, () -> {
            Client client = new Client("localhost", 8080);
            client.run();
        });

        this.citadelSettings.getClientGameLogic().start();

        ThreadManager.startThread(CitadelConstants.CLIENT_SIDE_RENDER_THREAD_NAME, () -> {
            Window window = Window.getInstance();
            window.init();

            window.setCursorLocked(true);

            window.loop();
            window.cleanup();
        });
    }

    private void startServer() {
        ThreadManager.startThread(CitadelConstants.SERVER_SIDE_THREAD_NAME, () -> {
            DebugManager.setDebugMode(true);
            Server server = new Server(8080);
            server.run();
        });

        // Java arguments
        ArrayHelper.runIfFound(citadelSettings.getArgs(), "citadelConsole", () -> {
            ThreadManager.startThread("Server console rendering", () -> {
                try {
                    Window.getInstance().initGuiOnly();
                } catch (Exception e) {
                    killNetworkThreads();
                    System.exit(0);
                    throw new RuntimeException(e);
                }
            });

            ImGuiScreens.SERVER_CONSOLE.show();
        });

        this.citadelSettings.getServerGameLogic().start();
    }

    private void logUsefulData() {
        OperatingSystem operatingSystem = PlatformManager.getOperatingSystem().get();

        Logger.log("Starting Citadel engine");
        Logger.log("Platform: " + operatingSystem);
        Logger.log("Java locale: " + operatingSystem.getSystemLocale());
        Logger.log("Locale: " + Localizer.fromJavaLocale(operatingSystem.getSystemLocale()).getAssetLocation().getPath());

        GradleUtil.logInfo();

        if (operatingSystem.isAppRunning("idea64.exe")) {
            Logger.log("You coding huh?");
        }
    }

    private void startRegistries() {
        // Only init the sound engine on the client
        // Required before registries
        if (!citadelSettings.isServerSide()) {
            SoundManager.getInstance().init();
        }

        // Loading the registry sets
        RegistryManager.registerBuiltInRegistrySet(new Shaders());
        RegistryManager.registerBuiltInRegistrySet(new Sounds());
        RegistryManager.registerBuiltInRegistrySet(new ProfilerStats());
        RegistryManager.registerBuiltInRegistrySet(new Locales());
        RegistryManager.registerBuiltInRegistrySet(new KeyBinds());
        RegistryManager.registerBuiltInRegistrySet(new ImGuiScreens());
        RegistryManager.registerBuiltInRegistrySet(new PacketDataTypes());
        RegistryManager.registerBuiltInRegistrySet(new Packets());
        RegistryManager.registerBuiltInRegistrySet(new FrameBuffers());

        // Register all
        RegistryManager.registerBuiltIn();
    }

    private void handleCrashes() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            killNetworkThreads();
            shouldEnd = true;
            Logger.getPersistentLogger().onCrash(thread, throwable);
            throwable.printStackTrace();
            //throw new RuntimeException(throwable.getMessage());
        });
    }

    private void killNetworkThreads() {
        Thread clientThread = ThreadManager.findThreadByName(CitadelConstants.CLIENT_SIDE_THREAD_NAME);
        if (clientThread != null) {
            ThreadManager.stopThread(clientThread);
        }

        Thread serverThread = ThreadManager.findThreadByName(CitadelConstants.SERVER_SIDE_THREAD_NAME);
        if (serverThread != null) {
            ThreadManager.stopThread(serverThread);
        }
    }

    public String getAppName() {
        return this.citadelSettings.getAppName();
    }

    public CitadelSettings getSettings() {
        return this.citadelSettings;
    }
}
