package com.upfault.core.events;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreboardEvent implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("lobby", "dummy", ChatColor.AQUA + " " + ChatColor.BOLD + "SERVER NAME");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		int rank = getPlayerRank(player);
		int level = getPlayerLevel(player);
		int friends = getPlayerFriends(player);
		int lobby = Bukkit.getServer().getOnlinePlayers().size();
		String date = getDate();
		String serverIP = Bukkit.getIp();

		if (serverIP.equals("")) {
			serverIP = "localhost";
		}

		Score dateandlobbyid = objective.getScore(ChatColor.GRAY + date + " " + ChatColor.DARK_GRAY + "D172C");
		dateandlobbyid.setScore(10);

		Score spacer4 = objective.getScore("    ");
		spacer4.setScore(9);

		Score rankScore = objective.getScore("Rank: " + rank);
		rankScore.setScore(8);

		Score levelScore = objective.getScore("Level: " + ChatColor.GREEN + level);
		levelScore.setScore(7);

		Score spacer3 = objective.getScore("   ");
		spacer3.setScore(6);

		Score lobbyPlayers = objective.getScore("Lobby: " + ChatColor.GREEN + lobby);
		lobbyPlayers.setScore(5);

		Score spacer2 = objective.getScore("  ");
		spacer2.setScore(4);

		Score friendsScore = objective.getScore("Friends: " + ChatColor.GREEN + friends);
		friendsScore.setScore(3);

		Score spacer1 = objective.getScore(" ");
		spacer1.setScore(2);

		Score dateScore = objective.getScore(ChatColor.YELLOW + serverIP);
		dateScore.setScore(1);

		player.setScoreboard(board);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	private int getPlayerRank(Player player) {
		// Code to retrieve player rank
		return 0;
	}

	private int getPlayerLevel(Player player) {
		// Code to retrieve player level
		return 0;
	}

	private int getPlayerFriends(Player player) {
		// Code to retrieve player friends
		return 0;
	}

	private String getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		return formatter.format(date);
	}
}
