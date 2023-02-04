package com.upfault.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You need to be a player to use this command!");
			return true;
		}

		Player player = (Player) sender;

		if (args.length < 2) {
			player.sendMessage("Usage: /rank {promote|demote|view} player");
			return false;
		}

		String action = args[0];
		String targetPlayer = args[1];

		switch (action) {
			case "promote":
				player.sendMessage("Promoting " + targetPlayer + "'s rank");
				break;
			case "demote":
				player.sendMessage("Demoting " + targetPlayer + "'s rank");
				break;
			case "view":
				player.sendMessage("Viewing " + targetPlayer + "'s rank");
				break;
			default:
				player.sendMessage("Usage: /rank {promote|demote|view} player");
				return false;
		}

		return true;
	}
}

