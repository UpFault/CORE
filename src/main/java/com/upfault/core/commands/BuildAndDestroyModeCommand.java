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
			sender.sendMessage("You need to be a player to use this command!");
			return true;
		}

		Player player = (Player) sender;
		FileConfiguration config = Utilities.getPlayerConfig(player.getUniqueId());
		File file = Utilities.getPlayerFile(player.getUniqueId());

		try {
			switch (command.getName()) {
				case "destroy":
					toggleMode(config, file, player, "destroymode", ChatColor.RED + "Destroy mode disabled.", ChatColor.GREEN + "Destroy mode enabled.");
					break;
				case "build":
					toggleMode(config, file, player, "buildmode", ChatColor.RED + "Build mode disabled.", ChatColor.GREEN + "Build mode enabled.");
					break;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return false;
	}

	private void toggleMode(FileConfiguration config, File file, Player player, String key, String disableMessage, String enableMessage) throws IOException {
		boolean mode = Objects.equals(config.get(key), true);
		config.set(key, !mode);
		config.save(file);
		player.sendMessage(mode ? disableMessage : enableMessage);
	}
}
