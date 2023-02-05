package com.upfault.core.events;

import com.upfault.core.CORE;
import com.upfault.core.utils.Rank;
import com.upfault.core.utils.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JoinEvents implements Listener {

	private final BossBar bossBar = Bukkit.createBossBar(ChatColor.AQUA + "" + ChatColor.BOLD + "GET RANKS AND BOOSTERS ON OUR STORE", BarColor.BLUE, BarStyle.SOLID);

	@EventHandler
	public void onJoin(PlayerJoinEvent event) throws IOException {
		Player player = event.getPlayer();
		World world = player.getWorld();
		Location location = new Location(world, -264.50, 18.00, 108.50, 90, 0);
		File playerDataFile = new File(CORE.getInstance().getDataFolder() + "/playerdata", player.getUniqueId() + ".yml");
		FileConfiguration config = Utilities.getPlayerConfig(player.getUniqueId());
		String rankValue = config.getString("rank");
		Rank rank = Rank.valueOf(rankValue);

		if(!playerDataFile.exists()) {
			generatePlayerData(player);
		}


		Utilities.setWorldProperties(world);
		player.teleport(location);
		player.setGameMode(GameMode.ADVENTURE);
		if(Rank.valueOf(rankValue) == Rank.DEFAULT) {
			player.setPlayerListName(rank.getColor() + player.getDisplayName());
			event.setJoinMessage(rank.getColor() + player.getDisplayName() + ChatColor.GOLD + " has joined the server!");
		} else {
			player.setPlayerListName(Rank.valueOf(rankValue).getColor() + Rank.valueOf(rankValue).getName() + " " + player.getDisplayName());
			event.setJoinMessage(Rank.valueOf(rankValue).getColor() + Rank.valueOf(rankValue).getName() + " " + player.getDisplayName() + ChatColor.GOLD + " has joined the server!");
		}		bossBar.addPlayer(player);
	}

	public void generatePlayerData(Player player) {

		File playerDataDirectory = new File(CORE.getInstance().getDataFolder() + "/playerdata");
		File playerDataFile = new File(CORE.getInstance().getDataFolder() + "/playerdata", player.getUniqueId() + ".yml");

		if (!playerDataDirectory.exists()) {
			playerDataDirectory.mkdirs();
		}

		if (!playerDataFile.exists()) {
			try {
				playerDataFile.createNewFile();
			} catch (IOException e) {
				Bukkit.getLogger().warning(e.getMessage());
				return;
			}
		}

		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);
		playerData.set("server_level", 0);
		playerData.set("server_xp", 0);
		playerData.set("rank", "DEFAULT");
		playerData.set("friends", Collections.emptyList());
		playerData.set("buildmode", false);
		playerData.set("destroymode", false);

		try {
			playerData.save(playerDataFile);
		} catch (IOException e) {
			Bukkit.getLogger().warning(e.getMessage());
		}
	}

	@EventHandler
	public void setRank(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		FileConfiguration config = Utilities.getPlayerConfig(player.getUniqueId());
		String rankValue = config.getString("rank");

		for (Rank rank : Rank.values()) {
			assert rankValue != null;
			if (rankValue.equals(rank.toString())) {
				String rankName = rank.getName();
				ChatColor rankColor = rank.getColor();
				List<String> permissions = rank.getPermissions();

				for (String permission : permissions) {
					player.addAttachment(CORE.instance, permission, true);
				}

				Scoreboard scoreboard = player.getScoreboard();
				Team team = scoreboard.getTeam(rankName);
				if (team == null) {
					team = scoreboard.registerNewTeam(rankName);
					team.prefix(Component.text(rankColor + ""));
					team.setAllowFriendlyFire(false);
					team.setCanSeeFriendlyInvisibles(true);
				}
				team.addEntry(player.getName());
				break;
			}
		}
	}

	public static void updatePlayerTeam(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		String playerName = player.getName();
		FileConfiguration config = Utilities.getPlayerConfig(player.getUniqueId());
		String rankValue = config.getString("rank");
		Rank rank = Rank.valueOf(rankValue);

		Team currentTeam = scoreboard.getEntryTeam(playerName);
		if (currentTeam != null) {
			if (!currentTeam.getName().equals(rank.toString())) {
				currentTeam.removeEntry(playerName);
				Team team = scoreboard.getTeam(rank.toString());
				if (team == null) {
					if (rank == Rank.DEFAULT) {
						team = scoreboard.registerNewTeam(rank.toString());
						team.setPrefix(rank.getColor() + "");
					} else {
						team = scoreboard.registerNewTeam(rank.toString());
						team.setPrefix(rank.getColor() + rank.getName() + " ");
					}
				}
				team.addEntry(playerName);
			}
		} else {
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
			team.addEntry(playerName);
		}
	}

}
