package com.upfault.core.commands;

import com.upfault.core.utils.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class BuildAndDestroyModeCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can use this command.");
			return false;
		}

		Player player = (Player) sender;
		FileConfiguration config = Utilities.getPlayerConfig(player.getUniqueId());
		File file = Utilities.getPlayerFile(player.getUniqueId());

		try {
			switch (command.getName()) {
				case "destroy":
					boolean destroyMode = Objects.equals(config.get("destroymode"), true);
					config.set("destroymode", !destroyMode);
					config.save(file);
					player.sendMessage((destroyMode ? ChatColor.RED + "Destroy mode disabled." : ChatColor.GREEN +"Destroy mode enabled."));
					break;
				case "build":
					boolean buildMode = Objects.equals(config.get("buildmode"), true);
					config.set("buildmode", !buildMode);
					config.save(file);
					player.sendMessage((buildMode ? ChatColor.RED + "Build mode disabled." : ChatColor.GREEN +"Build mode enabled."));
					break;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return false;
	}
}
