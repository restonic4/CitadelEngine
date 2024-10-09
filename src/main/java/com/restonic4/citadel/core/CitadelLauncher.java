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
import com.restonic4.citadel.sound.SoundSource;
import com.restonic4.citadel.util.ArrayHelper;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.GradleUtil;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.debug.DebugManager;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import org.joml.Vector3f;

import java.util.Objects;

public class CitadelLauncher {
    private static CitadelLauncher instance;

    private final CitadelSettings citadelSettings;
    private final ModLoader modLoader;

    private static volatile boolean shouldEnd = false;

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
        logConsoleBranding();

        applyArguments(citadelSettings.getArgs());

        CitadelLifecycleEvents.CITADEL_STARTING.invoker().onCitadelStarting(this);

        handleCrashes();
        startRegistries();
        logUsefulData();

        startDesiredEnvironment();

        while (!shouldEnd) {
            this.modLoader.update();
            this.citadelSettings.getSharedGameLogic().update();

            if (citadelSettings.isServerSide()) {
                this.citadelSettings.getServerGameLogic().update();
            }
        }

        killNetworkThreads();

        CitadelLifecycleEvents.CITADEL_STOPPED.invoker().onCitadelStopped(CitadelLauncher.getInstance());
    }

    private void applyArguments(String[] args) {
        ArrayHelper.runIfFound(citadelSettings.getArgs(), "server", () -> {
            citadelSettings.setServerSide(true);
        });
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
        ThreadManager.startThread(CitadelConstants.CLIENT_SIDE_RENDER_THREAD_NAME, true, () -> {
            Window window = Window.getInstance();
            window.init();

            this.citadelSettings.getClientGameLogic().start();

            window.loop();
            window.cleanup();

            finishApp();
        });
    }

    public void connectClientToServer(String ip, int port) {
        ThreadManager.startThread(CitadelConstants.CLIENT_SIDE_THREAD_NAME, false, () -> {
            Client client = new Client(ip, port);
            client.run();
        });
    }

    private void startServer() {
        ThreadManager.startThread(CitadelConstants.SERVER_SIDE_THREAD_NAME, false, () -> {
            DebugManager.setDebugMode(true);
            Server server = new Server(citadelSettings.getServerPort());
            server.run();
        });

        // Java arguments
        ArrayHelper.runIfFound(citadelSettings.getArgs(), "citadelConsole", () -> {
            ThreadManager.startThread("Server console rendering", true, () -> {
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

    private void logConsoleBranding() {
        Logger.log(StringBuilderHelper.concatenate(
                "//----------------------------------------------------------------------------------------------------------------\\\\", PlatformManager.getEndOfLine(),
                "||   .d8888b.  d8b 888                  888          888      8888888888                   d8b                    ||", PlatformManager.getEndOfLine(),
                "||  d88P  Y88b Y8P 888                  888          888      888                          Y8P                    ||", PlatformManager.getEndOfLine(),
                "||  888    888     888                  888          888      888                                                 ||", PlatformManager.getEndOfLine(),
                "||  888        888 888888  8888b.   .d88888  .d88b.  888      8888888    88888b.   .d88b.  888 88888b.   .d88b.   ||", PlatformManager.getEndOfLine(),
                "||  888        888 888        \"88b d88\" 888 d8P  Y8b 888      888        888 \"88b d88P\"88b 888 888 \"88b d8P  Y8b  ||", PlatformManager.getEndOfLine(),
                "||  888    888 888 888    .d888888 888  888 88888888 888      888        888  888 888  888 888 888  888 88888888  ||", PlatformManager.getEndOfLine(),
                "||  Y88b  d88P 888 Y88b.  888  888 Y88b 888 Y8b.     888      888        888  888 Y88b 888 888 888  888 Y8b.      ||", PlatformManager.getEndOfLine(),
                "||   \"Y8888P\"  888  \"Y888 \"Y888888  \"Y88888  \"Y8888  888      8888888888 888  888  \"Y88888 888 888  888  \"Y8888   ||", PlatformManager.getEndOfLine(),
                "\\\\------------------------------------------------------------------------------------888-------------------------//", PlatformManager.getEndOfLine(),
                "                                                                              Y8b d88P                       ", PlatformManager.getEndOfLine(),
                "                                                                               \"Y88P\"                        "
        ));

        GradleUtil.logInfo();
    }

    private void logUsefulData() {
        OperatingSystem operatingSystem = PlatformManager.getOperatingSystem().get();

        Logger.log("Starting Citadel engine");
        Logger.log("Platform: " + operatingSystem);
        Logger.log("Java locale: " + operatingSystem.getSystemLocale());
        Logger.log("Locale: " + Localizer.fromJavaLocale(operatingSystem.getSystemLocale()).getAssetLocation().getPath());

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
        RegistryManager.registerBuiltInRegistrySet(new Icons());

        // Register all
        RegistryManager.registerBuiltIn();
    }

    private void handleCrashes() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            killNetworkThreads();
            finishApp();
            ThreadManager.stopAllThreads();
            ThreadManager.shutdownExecutor();
            Logger.getPersistentLogger().onCrash(thread, throwable);
            throwable.printStackTrace();
            //throw new RuntimeException(throwable.getMessage());
        });
    }

    private void killNetworkThreads() {
        Logger.log("Killing networking threads");

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

    public static synchronized void finishApp() {
        Logger.log("Finishing engine");
        shouldEnd = true;
    }

    public void handleError(String message) {
        Logger.logError(message);

        if (citadelSettings.isEditorMode()) {
            SoundSource soundSource = Sounds.CRASH.createSource(false, false);
            soundSource.setPosition(new Vector3f(0, 0, 0));
            soundSource.play();
        }
        else {
            throw new RuntimeException("Critical error");
        }
    }
}
