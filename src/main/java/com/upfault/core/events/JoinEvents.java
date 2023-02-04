package com.upfault.core.events;

import com.upfault.core.CORE;
import com.upfault.core.utils.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class JoinEvents implements Listener {

	private final BossBar bossBar = Bukkit.createBossBar(ChatColor.AQUA + "" + ChatColor.BOLD + "GET RANKS AND BOOSTERS ON OUR STORE", BarColor.BLUE, BarStyle.SOLID);

	@EventHandler @SuppressWarnings("deprecation")
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		Location location = new Location(world, -264.50, 18.00, 108.50, 90, 0);

		generatePlayerData(player);

		Utilities.setWorldProperties(world);
		player.teleport(location);

		event.joinMessage(Component.text(player.getDisplayName() + " has joined the server!"));

		bossBar.addPlayer(player);
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
}
