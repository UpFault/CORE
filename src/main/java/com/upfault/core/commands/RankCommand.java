package com.upfault.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

		return false;
	}
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
			return Arrays.asList("promote", "demote");
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
			return Arrays.asList("default", "vip", "mvp", "mod", "admin", "owner");
		}
		return null;
	}
}