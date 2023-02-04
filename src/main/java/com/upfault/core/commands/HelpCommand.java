package com.upfault.core.commands;

import com.upfault.core.CORE;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HelpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You need to be a player to use this command!");
			return true;
		}

		StringBuilder helpMessage = new StringBuilder(ChatColor.YELLOW + "Core Command Help:\n");
		Map<String, Map<String, Object>> commands = CORE.instance.getDescription().getCommands();
		for (Map.Entry<String, Map<String, Object>> entry : commands.entrySet()) {
			String cmdName = entry.getKey();
			Map<String, Object> cmdData = entry.getValue();
			String description = (String) cmdData.get("description");
			helpMessage.append(ChatColor.GREEN)
					.append("/").append(cmdName)
					.append(ChatColor.WHITE).append(" - ").append(description).append("\n");
			sender.sendMessage(helpMessage.toString());
		}
		return false;
	}
}