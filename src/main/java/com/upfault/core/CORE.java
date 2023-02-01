package com.upfault.core;

import com.upfault.core.commands.main;
import com.upfault.core.events.ScoreboardEvent;
import com.upfault.core.utils.VersionCheck;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public final class CORE extends JavaPlugin {

	public static CORE instance;
	private FileConfiguration config;
	private File configFile;

	@Override
	public void onEnable() {
		// Plugin startup logic
		instance = this;
		VersionCheck.check();
		registerCommand();
		registerEvents();
		generateFiles();
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
		instance = null;
	}

	private void registerCommand() {
		Objects.requireNonNull(getCommand("core")).setExecutor(new main());
	}

	private void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new ScoreboardEvent(), this);
//		Bukkit.getPluginManager().registerEvents(new PlayerJoinSQLListener("localhost", "UPFAULT", "2bZ!e05gJ^"), this);
	}

	private void generateFiles() {
		configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			saveDefaultConfig();
		}
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	@Override
	public void saveDefaultConfig() {
		if (!configFile.exists()) {
			saveResource("config.yml", false);
		}
	}

	public @NotNull FileConfiguration getConfig() {
		return config;
	}
}
