package com.restonic4.citadel.tools.discord;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.ThreadManager;
import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.platform.PlatformManager;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.helpers.StringBuilderHelper;
import com.restonic4.citadel.util.history.HistoryCommand;
import com.restonic4.citadel.world.SceneManager;
import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.*;

import java.time.Instant;

public class DiscordIntegration {
    private static Core core;
    private static Activity activity;

    public static void init() {
        ThreadManager.startThread("Discord RPC", false, DiscordIntegration::startActivity);
    }

    private static void startActivity() {
        try(CreateParams params = new CreateParams())
        {
            params.setClientID(CitadelConstants.DISCORD_APP_ID);
            params.setFlags(CreateParams.getDefaultFlags());

            try(Core newCore = new Core(params))
            {
                core = newCore;

                try(Activity newActivity = new Activity())
                {
                    activity = newActivity;

                    updateActivityDetails();

                    activity.timestamps().setStart(Instant.now());

                    core.activityManager().updateActivity(activity);
                }

                while(true)
                {
                    core.runCallbacks();
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void updateActivityDetails() {
        if (activity == null) {
            return;
        }

        activity.setDetails(getUpdatedActivityDetails());
        activity.setState(getUpdatedActivityState());
        activity.setType(ActivityType.PLAYING);

        if (core != null) {
            core.activityManager().updateActivity(activity);
        }
    }

    public static String getUpdatedActivityDetails() {
        return CitadelLauncher.getInstance().getAppName();
    }

    public static String getUpdatedActivityState() {
        return SceneManager.getCurrentScene().getName();
    }
}
