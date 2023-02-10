package com.upfault.core.utils;

import com.upfault.core.CORE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class VersionCheck {
    private static final String versionUrl = "https://raw.githubusercontent.com/UpFault/CORE/master/version.txt";
    private static final PluginDescriptionFile pdf = CORE.getInstance().getDescription();
    private static final Logger logger = Bukkit.getLogger();
    private static String latestVersion;

    public static void check() {
        String currentVersion = pdf.getVersion();
        try {
            latestVersion = getLatestVersion();
            if (!currentVersion.equals(latestVersion)) {
                logger.warning("[CORE] Your plugin is outdated! Current version: " + currentVersion + " Latest version: " + latestVersion);
            }
        } catch (Exception e) {
            logger.warning("[CORE] Failed to check for updates: " + e.getMessage());
        }
    }

    public static String getVersion() {
        String currentVersion = pdf.getVersion();
        try {
            if (latestVersion == null) {
                latestVersion = getLatestVersion();
            }
            if (!currentVersion.equals(latestVersion)) {
                return ChatColor.RED + "" + currentVersion + "->" + ChatColor.GREEN + "" + latestVersion;
            } else {
                return currentVersion;
            }
        } catch (Exception e) {
            logger.warning("[CORE] Failed to check for updates: " + e.getMessage());
        }
        return null;
    }

    private static String getLatestVersion() throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(versionUrl).openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String latestVersion = in.readLine();
        in.close();
        return latestVersion;
    }
}
