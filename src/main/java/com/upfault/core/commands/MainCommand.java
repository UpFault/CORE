package com.upfault.core.commands;

import com.upfault.core.CORE;
import com.upfault.core.utils.VersionCheck;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {
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
								ChatColor.GREEN + "GitHub: " + ChatColor.WHITE + "https://github.com/UpFault/CORE" + "\n" +
								ChatColor.YELLOW + "Server Information:\n" +
								ChatColor.GREEN + "Server IP: " + ChatColor.WHITE + "" + serverIP + "\n" +
								ChatColor.GREEN + "Server Version: " + ChatColor.WHITE + "" + Bukkit.getBukkitVersion() + "\n" +
								ChatColor.GREEN + "Online Players: " + ChatColor.WHITE + "" + Bukkit.getOnlinePlayers().size() + "\n" +
								ChatColor.GREEN + "Max Players: " + ChatColor.WHITE + "" + Bukkit.getMaxPlayers();

						player.sendMessage(message);
						break;
					case "reload":
						CORE.instance.reloadConfig();
						File ranksFile = new File(CORE.instance.getDataFolder(), "ranks.yml");
						File animationsFile = new File(CORE.instance.getDataFolder(), "animations.yml");
						FileConfiguration ranksConfig = YamlConfiguration.loadConfiguration(ranksFile);
						FileConfiguration animationsConfig = YamlConfiguration.loadConfiguration(animationsFile);

						try {
							ranksConfig.save(ranksFile);
							animationsConfig.save(animationsFile);
						} catch (IOException e) {
							e.printStackTrace();
						}

						sender.sendMessage(ChatColor.YELLOW + "Your plugin config's has been reloaded.");
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

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		List<String> list = Arrays.asList("info", "reload");
		return list;
	}
}
