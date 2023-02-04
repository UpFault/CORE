package com.upfault.core.utils;

import com.upfault.core.CORE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionCheck {
    public static void check() {
        PluginDescriptionFile pdf = CORE.getInstance().getDescription();
        String currentVersion = pdf.getVersion();
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("https://raw.githubusercontent.com/UpFault/CORE/master/version.txt").openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String latestVersion = in.readLine();
            in.close();
            if (!currentVersion.equals(latestVersion)) {
                Bukkit.getLogger().warning("[CORE] Your plugin is outdated! Current version: " + currentVersion + " Latest version: " + latestVersion);
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[CORE] Failed to check for updates: " + e.getMessage());
        }
    }

    public static String getVersion() {
        PluginDescriptionFile pdf = CORE.getInstance().getDescription();
        String currentVersion = pdf.getVersion();
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("https://raw.githubusercontent.com/UpFault/CORE/master/version.txt").openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String latestVersion = in.readLine();
            in.close();
            if (!currentVersion.equals(latestVersion)) {
                return ChatColor.RED + "" + currentVersion + "->" + ChatColor.GREEN + "" + latestVersion;
            } else {
                return currentVersion;
            }
        } catch (Exception e) {
//            Bukkit.getLogger().warning("Failed to check for updates: " + e.getMessage());
        }
        return null;
    }
}
