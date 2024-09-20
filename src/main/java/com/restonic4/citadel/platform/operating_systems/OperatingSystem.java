package com.restonic4.citadel.platform.operating_systems;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;

public class OperatingSystem {
    public Locale getSystemLocale() {
        return Locale.getDefault();
    }

    public boolean isAppRunning(String appName) {
        try {
            Process process = Runtime.getRuntime().exec("tasklist");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(appName.toLowerCase())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
