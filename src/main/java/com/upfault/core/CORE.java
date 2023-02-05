package com.upfault.core;

import com.upfault.core.commands.BuildAndDestroyModeCommand;
import com.upfault.core.commands.HelpCommand;
import com.upfault.core.commands.MainCommand;
import com.upfault.core.commands.RankCommand;
import com.upfault.core.events.*;
import com.upfault.core.utils.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
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
		registerRanks();
		clear();
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
			for (Player player : players) {
				updateScoreboardForPlayer(player);
				JoinEvents.updatePlayerTeam(player);
			}
		}, 0, 20);
		new LevelingSystem().startTask();
	}

	@Override
	public void onDisable() {
		saveConfig();
		HandlerList.unregisterAll(this);
		instance = null;
	}

	private void registerCommands() {
		Objects.requireNonNull(getCommand("core")).setExecutor(new MainCommand());
		Objects.requireNonNull(getCommand("build")).setExecutor(new BuildAndDestroyModeCommand());
		Objects.requireNonNull(getCommand("destroy")).setExecutor(new BuildAndDestroyModeCommand());
		Objects.requireNonNull(getCommand("rank")).setExecutor(new RankCommand());
		Objects.requireNonNull(getCommand("rank")).setTabCompleter(new RankCommand());
		Objects.requireNonNull(getCommand("help")).setExecutor(new HelpCommand());
	}

	private void registerListeners() {
		ArrayList<Listener> listeners = new ArrayList<>(Arrays.asList(
				new HubGuard(),
				new HurtEvents(),
				new JoinEvents(),
				new LeaveEvents(),
				new MoveEvent(),
				new ChatEvent()
		));
		for (Listener listener : listeners) {
			Bukkit.getPluginManager().registerEvents(listener, this);
		}
	}

	private void registerRanks() {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		for (Rank rank : Rank.values()) {
			Team team = scoreboard.getTeam(rank.toString());
			if (team == null) {
				if (rank == Rank.DEFAULT) {
					team = scoreboard.registerNewTeam(rank.toString());
					team.setPrefix(rank.getColor() + "");
					team.setColor(rank.getColor());
				} else {
					team = scoreboard.registerNewTeam(rank.toString());
					team.setPrefix(rank.getColor() + rank.getName() + " ");
					team.setColor(rank.getColor());
				}
			}
		}

	}

	private void clear() {
		World hub = Bukkit.getWorld("hub");
		assert hub != null;
		for (LivingEntity entity : hub.getLivingEntities()) {
			if (!(entity instanceof Player)) {
				entity.setHealth(0);
			}
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

	private void updateScoreboardForPlayer(Player player) {
		FileConfiguration config = Utilities.getPlayerConfig(player.getUniqueId());
		String rankName = "NONE";
		ChatColor rankColor = ChatColor.GRAY;

		for (Rank rank : Rank.values()) {
			if (config.get("rank").toString().equals(rank.toString())) {
				rankName = rank.getName().replaceAll("\\[|\\]", "");
				if (rank == Rank.DEFAULT) {
					rankName = "NONE";
				}
				rankColor = rank.getColor();
				break;
			}
		}

		String serverIP = Bukkit.getIp();
		if (serverIP.equals("")) {
			serverIP = "localhost";
		}

		FastBoard board = boards.get(player.getUniqueId());
		if (board == null) {
			board = new FastBoard(player);
			boards.put(player.getUniqueId(), board);
		}

		board.updateTitle(ChatColor.AQUA + "" + ChatColor.BOLD + "CORE");
		board.updateLines(
				ChatColor.GRAY + Utilities.getDate() + " " + ChatColor.DARK_GRAY + "D12C",
				"",
				"Rank: " + rankColor + rankName,
				"Level: " + ChatColor.GREEN + 0,
				"",
				"Lobby: " + ChatColor.GREEN + 0,
				"",
				"Friends: " + ChatColor.GREEN + 0,
				"",
				ChatColor.AQUA + serverIP
		);
	}

}
