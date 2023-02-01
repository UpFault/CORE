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

public class main implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You need to be a player to use this command!");
			return true;
		}
		if (command.getName().equals("core"))
			switch (args[0]) {
				case "help":
//                display helpful info
					break;
				case "info":
					Player player = (Player) sender;
					PluginDescriptionFile pdf = CORE.instance.getDescription();
					String serverIP = Bukkit.getIp();

					if (serverIP.equals("")) {
						serverIP = "localhost";
					}

					player.sendMessage(ChatColor.YELLOW + "Plugin Information:");
					player.sendMessage(ChatColor.GREEN + "Name: " + ChatColor.WHITE + "" + pdf.getName());
					player.sendMessage(ChatColor.GREEN + "Current Version: " + ChatColor.WHITE + "" + VersionCheck.getVersion());
					player.sendMessage(ChatColor.GREEN + "Description: " + ChatColor.WHITE + "" + pdf.getDescription());
					player.sendMessage(ChatColor.GREEN + "Authors: " + ChatColor.WHITE + "" + String.join(", ", pdf.getAuthors()));
					player.sendMessage(ChatColor.YELLOW + "Server Information:");
					player.sendMessage(ChatColor.GREEN + "Server IP: " + ChatColor.WHITE + "" + serverIP);
					player.sendMessage(ChatColor.GREEN + "Server Version: " + ChatColor.WHITE + "" + Bukkit.getBukkitVersion());
					player.sendMessage(ChatColor.GREEN + "Online Players: " + ChatColor.WHITE + "" + Bukkit.getOnlinePlayers().size());
					player.sendMessage(ChatColor.GREEN + "Max Players: " + ChatColor.WHITE + "" + Bukkit.getMaxPlayers());
					break;
			}

		return false;
	}
}
