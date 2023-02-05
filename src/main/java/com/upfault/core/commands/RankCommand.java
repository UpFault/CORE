package com.upfault.core.commands;

import com.upfault.core.utils.Rank;
import com.upfault.core.utils.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You need to be a player to use this command!");
			return true;
		}

		Player player = (Player) sender;

		if (args.length < 2 || args.length > 3) {
			player.sendMessage("Usage: /rank {promote|demote|view} player rank");
			return false;
		}

		String action = args[0];
		String targetPlayerName = args[1];
		Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
		if (targetPlayer == null) {
			player.sendMessage(targetPlayerName + " is not a valid player!");
			return false;
		}

		String rank;
		if (args.length == 3) {
			rank = args[2];
		} else {
			String currentRank = Utilities.getPlayerConfig(targetPlayer.getUniqueId()).getString("rank");
			rank = String.valueOf(Rank.compareRank(Rank.valueOf(currentRank), action));
		}
		rank = rank.toUpperCase();

		FileConfiguration playerConfig = Utilities.getPlayerConfig(targetPlayer.getUniqueId());
		switch (action) {
			case "promote":
				playerConfig.set("rank", rank);
				player.playerListName(Component.text(Rank.valueOf(rank).getColor() + Rank.valueOf(rank).getName() + " " + player.getDisplayName()));
				try {
					playerConfig.save(Utilities.getPlayerFile(targetPlayer.getUniqueId()));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				player.sendMessage(ChatColor.GREEN + "Promoting " + ChatColor.YELLOW + targetPlayerName + ChatColor.GREEN + " to " + Rank.valueOf(rank).getColor() + rank + ChatColor.GREEN + " rank");
				break;
			case "demote":
				playerConfig.set("rank", rank);
				player.playerListName(Component.text(Rank.valueOf(rank).getColor() + Rank.valueOf(rank).getName() + " " + player.getDisplayName()));
				try {
					playerConfig.save(Utilities.getPlayerFile(targetPlayer.getUniqueId()));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				player.sendMessage(ChatColor.GREEN + "Demoting " + ChatColor.YELLOW + targetPlayerName + ChatColor.GREEN + " to " + Rank.valueOf(rank).getColor() + rank + ChatColor.GREEN + " rank");
				break;
			case "view":
				player.sendMessage(ChatColor.GREEN + "Viewing " + ChatColor.YELLOW + targetPlayerName + "'s" + ChatColor.GREEN + " rank: " + Rank.valueOf(playerConfig.getString("rank")).getColor() + playerConfig.getString("rank"));
				break;
			default:
				player.sendMessage("Usage: /rank {promote|demote|view} player rank");
				return false;
		}
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
			return Arrays.asList("promote", "demote", "view");
		}
		if (args.length == 2) {
			List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
			List<String> playerNames = new ArrayList<>();
			for (Player player : players) {
				playerNames.add(player.getName());
			}
			return playerNames;
		}
		if(args.length==3) {
			return Arrays.asList("DEFAULT", "VIP", "MVP", "MOD", "ADMIN", "OWNER");
		}
		return null;
	}
}