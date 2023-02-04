package com.upfault.core.events;

import com.upfault.core.utils.FastBoard;
import com.upfault.core.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardEvent implements Listener {

	public static FastBoard board;
	@EventHandler @SuppressWarnings("all")
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		String serverIP = Bukkit.getIp();
		serverIP = serverIP != null ? serverIP : "localhost";

		board = new FastBoard(player);

		board.updateTitle(ChatColor.AQUA + "" + ChatColor.BOLD + "CORE");
		board.updateLines(
				ChatColor.GRAY + Utilities.getDate() + " " + ChatColor.DARK_GRAY + "D12C",
				"",
				"Rank: " + "NONE",
				"Level: " + ChatColor.GREEN + 0,
				"",
				"Lobby: " + ChatColor.GREEN + 0,
				"",
				"Friends: " + ChatColor.GREEN + 0,
				"",
				ChatColor.YELLOW + serverIP
		);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		FastBoard board = (FastBoard) player.getScoreboard();
		board.delete();
	}
}

