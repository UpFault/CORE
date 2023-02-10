package com.upfault.core;

import com.upfault.core.commands.*;
import com.upfault.core.events.*;
import com.upfault.core.utils.Utilities;
import com.upfault.core.utils.VersionCheck;
import com.upfault.core.utils.fastboard.FastBoard;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.*;

public final class CORE extends JavaPlugin {
	@Getter
	public static CORE instance;
	public static final Map<UUID, FastBoard> boards = new HashMap<>();

	@Override
	public void onEnable() {
		instance = this;
		VersionCheck.check();
		registerCommands();
		registerListeners();
		generateConfig();
		getServer().getScheduler().runTaskTimer(this, () -> {
			for (FastBoard board : boards.values()) {
				updateLines(board);
			}
		}, 0, 20);
		int taskId = 1;
		final int[] animationIndex = {0};
		int finalTaskId = taskId;
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (FastBoard board : boards.values()) {
				String world = board.getPlayer().getWorld().getName();
				List<String> animationLines = Utilities.getAnimationsConfig().getStringList("animated-title." + world);
				if (getConfig().getBoolean("scoreboard." + world + ".animated-title")) {
					if (!animationLines.isEmpty()) {
						String line = animationLines.get(animationIndex[0] % animationLines.size());
						String translatedLine = ChatColor.translateAlternateColorCodes('&', line);
						board.updateTitle(translatedLine);
						animationIndex[0]++;
					}
				} else {
					updateTitle(board);
					Bukkit.getScheduler().cancelTask(finalTaskId);
				}
			}
		}, 0L, 20L);
	}

	@Override
	public void onDisable() {
		saveDefaultConfig();
		HandlerList.unregisterAll(this);
		instance = null;
	}

	@SuppressWarnings("all")
	private void registerCommands() {
		getCommand("core").setExecutor(new MainCommand());
		getCommand("core").setTabCompleter(new MainCommand());
		getCommand("build").setExecutor(new BuildAndDestroyModeCommand());
		getCommand("destroy").setExecutor(new BuildAndDestroyModeCommand());
		getCommand("rank").setExecutor(new RankCommand());
		getCommand("rank").setTabCompleter(new RankCommand());
		getCommand("help").setExecutor(new HelpCommand());
		getCommand("maintenance").setExecutor(new MaintenenceModeCommand());
		getCommand("maintenance").setTabCompleter(new MaintenenceModeCommand());
	}

	private void registerListeners() {
		ArrayList<Listener> listeners = new ArrayList<>(Arrays.asList(
				new HubProtectionEvent(),
				new PlayerJoinEvent(),
				new PlayerQuitEvent(),
				new PlayerFallProtectionEvent(),
				new ChatFormatEvent(),
				new InventoryInteractionEvent(),
				new CommandPreProcessEvent(),
				new PlayerDataEvent(),
				new PlayerListPingEvent()
		));
		for (Listener listener : listeners) {
			Bukkit.getPluginManager().registerEvents(listener, this);
		}
	}

	private void generateConfig() {
		String[] files = new String[]{"config.yml", "animations.yml", "ranks.yml", "language.yml"};
		File dataFolder = getDataFolder();
		for (String file : files) {
			File configFile = new File(dataFolder, file);
			File parentDir = configFile.getParentFile();
			if (!parentDir.exists() && !parentDir.mkdirs()) {
				getLogger().severe("Failed to create the directory for " + file);
				continue;
			}
			this.saveDefaultConfig(configFile, file);

			File databaseDir = new File(getDataFolder(), "database");
			if (!databaseDir.exists() && !databaseDir.mkdirs()) {
				getLogger().severe("Failed to create database directory");
			}
		}
	}

	private void saveDefaultConfig(File configFile, String defaultConfigFile) {
		if (!configFile.exists()) {
			saveResource(defaultConfigFile, false);
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new File(defaultConfigFile));
		boolean needsSaving = false;
		for (String key : defaultConfig.getKeys(true)) {
			if (!config.contains(key)) {
				config.set(key, defaultConfig.get(key));
				needsSaving = true;
			}
		}
		if (needsSaving) {
			try {
				config.save(configFile);
			} catch (Exception e) {
				getLogger().severe("Failed to save " + configFile.getName() + " to disk!");
			}
		}
	}

	private void updateLines(FastBoard board) {
		String world = board.getPlayer().getWorld().getName();
		List<String> lines = getConfig().getStringList("scoreboard." + world + ".lines");
		List<String> translatedLines = new ArrayList<>();
		for (String line : lines) {
			translatedLines.add(ChatColor.translateAlternateColorCodes('&', line));
		}
		board.updateLines(translatedLines);
	}

	private void updateTitle(FastBoard board) {
		UUID playerUUID = board.getPlayer().getUniqueId();
		FastBoard playerBoard = boards.get(playerUUID);
		if (playerBoard != null) {
			String world = playerBoard.getPlayer().getWorld().getName();
			String title = getConfig().getString("scoreboard." + world + ".title");
			assert title != null;
			board.updateTitle(ChatColor.translateAlternateColorCodes('&', title));
		}
	}

	private void registerTeams() {
		Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		if (mainScoreboard == null) {
			getLogger().severe("Failed to get scoreboard manager!");
			return;
		}
		List<String> rankNames = new ArrayList<>();
		List<String> rankColors = new ArrayList<>();
		FileConfiguration ranksConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "ranks.yml"));

		for (String rank : ranksConfig.getKeys(false)) {
			String rankName = ranksConfig.getString("Ranks." + rank + ".display");
			rankNames.add(rankName);
		}
		for (String rank : ranksConfig.getKeys(false)) {
			String rankColor = ranksConfig.getString("Ranks." + rank + ".color");
			rankColors.add(rankColor);
		}

		for (int i = 0; i < rankNames.size(); i++) {
			String rankName = rankNames.get(i);
			String rankColor = rankColors.get(i);

			Team team = mainScoreboard.getTeam(rankName);
			if (team == null) {
				team = mainScoreboard.registerNewTeam(rankName);
				Bukkit.getLogger().info("Registered " + team.getName() + " as a new team.");
			}

			team.setColor(ChatColor.valueOf(rankColor));
			team.setPrefix(rankColor + "[" + rankName + "]");
		}
	}
}
