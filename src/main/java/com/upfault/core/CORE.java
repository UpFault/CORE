package com.upfault.core;

import com.upfault.core.commands.BuildAndDestroyModeCommand;
import com.upfault.core.commands.HelpCommand;
import com.upfault.core.commands.MainCommand;
import com.upfault.core.commands.RankCommand;
import com.upfault.core.events.*;
import com.upfault.core.utils.VersionCheck;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public final class CORE extends JavaPlugin {

	@Getter
	public static CORE instance;

	@Override
	public void onEnable() {
		instance = this;
		VersionCheck.check();
		registerCommands();
		registerListeners();
		generateConfig();
	}

	@Override
	public void onDisable() {
		saveConfig();
		instance = null;
	}

	private void registerCommands() {
		Objects.requireNonNull(getCommand("core")).setExecutor(new MainCommand());
		Objects.requireNonNull(getCommand("build")).setExecutor(new BuildAndDestroyModeCommand());
		Objects.requireNonNull(getCommand("destroy")).setExecutor(new BuildAndDestroyModeCommand());
		Objects.requireNonNull(getCommand("rank")).setExecutor(new RankCommand());
		Objects.requireNonNull(getCommand("help")).setExecutor(new HelpCommand());
	}

	private void registerListeners() {
		ArrayList<Listener> listeners = new ArrayList<>(Arrays.asList(
				new HubGuard(),
				new HurtEvents(),
				new JoinEvents(),
				new LeaveEvents(),
				new MoveEvent(),
				new ScoreboardEvent()
		));
		for (Listener listener : listeners) {
			Bukkit.getPluginManager().registerEvents(listener, this);
		}
	}

	private void generateConfig() {
		File configFile = new File(getDataFolder(), "config.yml");
		File configDirectory = configFile.getParentFile();

		if (!configFile.exists()) {
			if (!configDirectory.exists() && !configDirectory.mkdirs()) {
				getLogger().severe("Failed to create config directory");
				return;
			}
			saveResource("config.yml", false);
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		YamlConfiguration defaultConfig = new YamlConfiguration();

//		SET DEFAULT VALUES

		defaultConfig.set("database.host", "localhost");
		defaultConfig.set("database.port", 3306);
		defaultConfig.set("database.database", "db");
		defaultConfig.set("database.user", "admin");
		defaultConfig.set("database.password", "12345");

		config.addDefaults(defaultConfig);
		config.options().copyDefaults(true);

		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
