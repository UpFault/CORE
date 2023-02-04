package com.upfault.core.commands;

import com.upfault.core.CORE;
import com.upfault.core.utils.VersionCheck;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You need to be a player to use this command!");
			return true;
		}

			try {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.RED + "Error: No argument provided. Type /help for a list of commands.");
					return true;
				}
				switch (args[0]) {
					case "info":
						Player player = (Player) sender;
						PluginDescriptionFile pdf = CORE.getInstance().getDescription();
						String serverIP = Bukkit.getIp();
						if (serverIP.equals("")) {
							serverIP = "localhost";
						}

						String message = ChatColor.YELLOW + "Plugin Information:\n" +
								ChatColor.GREEN + "Name: " + ChatColor.WHITE + "" + pdf.getName() + "\n" +
								ChatColor.GREEN + "Current Version: " + ChatColor.WHITE + "" + VersionCheck.getVersion() + "\n" +
								ChatColor.GREEN + "Description: " + ChatColor.WHITE + "" + pdf.getDescription() + "\n" +
								ChatColor.GREEN + "Authors: " + ChatColor.WHITE + "" + String.join(", ", pdf.getAuthors()) + "\n" +
								ChatColor.YELLOW + "Server Information:\n" +
								ChatColor.GREEN + "Server IP: " + ChatColor.WHITE + "" + serverIP + "\n" +
								ChatColor.GREEN + "Server Version: " + ChatColor.WHITE + "" + Bukkit.getBukkitVersion() + "\n" +
								ChatColor.GREEN + "Online Players: " + ChatColor.WHITE + "" + Bukkit.getOnlinePlayers().size() + "\n" +
								ChatColor.GREEN + "Max Players: " + ChatColor.WHITE + "" + Bukkit.getMaxPlayers();

						player.sendMessage(message);
						break;
					default:
						sender.sendMessage(ChatColor.RED + "Error: Unrecognized argument. Type /help for a list of commands.");
						break;
				}
			} catch (Exception e) {
				Bukkit.getLogger().warning(e.getMessage());
			}
		return false;
	}
}
