package com.upfault.core.commands;

import com.upfault.core.CORE;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MaintenenceModeCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission("core.maintenance")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
			return true;
		}

		if (args.length != 1) {
			sender.sendMessage(ChatColor.RED + "Usage: /maintenance [on/off]");
			return true;
		}

		String mode = args[0];

		if (mode.equalsIgnoreCase("on")) {
			Bukkit.setWhitelist(true);
			sender.sendMessage(ChatColor.GREEN + "Maintenance mode enabled.");
			for(Player player : Bukkit.getOnlinePlayers()) {
				if(!player.isOp()) {
					player.kick(Component.text(ChatColor.RED + "Server is currently in maintenance, try again later."));
				}
			}
			CORE.instance.getConfig().set("maintenance", true);
			CORE.instance.saveConfig();
		} else if (mode.equalsIgnoreCase("off")) {
			Bukkit.setWhitelist(false);
			sender.sendMessage(ChatColor.GREEN + "Maintenance mode disabled.");
			CORE.instance.getConfig().set("maintenance", false);
			CORE.instance.saveConfig();
		} else {
			sender.sendMessage(ChatColor.RED + "Usage: /maintenance [on/off]");
			return true;
		}

		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		List<String> list = Arrays.asList("on", "off");
		return list;
	}
}
