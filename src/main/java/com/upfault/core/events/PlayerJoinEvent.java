package com.upfault.core.events;

import com.upfault.core.CORE;
import com.upfault.core.utils.fastboard.FastBoard;
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

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerJoinEvent implements Listener {

	private final BossBar bossBar = Bukkit.createBossBar(ChatColor.AQUA + "" + ChatColor.BOLD + "GET RANKS AND BOOSTERS ON OUR STORE", BarColor.BLUE, BarStyle.SOLID);
	@EventHandler
	public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {

		Player player = event.getPlayer();
		World world = player.getWorld();
		Location location = new Location(world, -264.50, 18.00, 108.50, 90, 0);
		UUID playerUUID = player.getUniqueId();
		File playerFile = new File(CORE.instance.getDataFolder() + File.separator + "playerdata" + File.separator + playerUUID + ".yml");
		FileConfiguration config = CORE.instance.getConfig();

		if (!playerFile.exists()) {
			playerFile.getParentFile().mkdirs();
			try {
				playerFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(config.getBoolean("maintenance") && !player.isOp()) {
			event.joinMessage(null);
			event.getPlayer().kick(Component.text(ChatColor.RED + "Server is currently in maintenance, try again later."));
		}

		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		playerData.set("destroymode", false);
		playerData.set("buildmode", true);
		try {
			playerData.save(playerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FastBoard board = new FastBoard(player);

		CORE.boards.put(player.getUniqueId(), board);

		player.teleport(location);
		event.joinMessage(Component.text(player.getName() + ChatColor.GOLD + " has joined the server!"));
		player.setGameMode(GameMode.ADVENTURE);
		bossBar.addPlayer(player);
	}
}
