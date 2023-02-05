package com.upfault.core.events;

import com.upfault.core.utils.Rank;
import com.upfault.core.utils.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

public class ChatEvent implements Listener {

	@EventHandler @SuppressWarnings("deprecation")
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		FileConfiguration config = Utilities.getPlayerConfig(player.getUniqueId());
		String rankValue = Objects.requireNonNull(config.getString("rank")).toUpperCase();

		Rank rank = Rank.DEFAULT;
		ChatColor rankColor = rank.getColor();

		for (Rank r : Rank.values()) {
			if (rankValue.equalsIgnoreCase(r.toString())) {
				rank = r;
				rankColor = rank.getColor();
				break;
			}
		}

		String formattedMessage;
		if (rank == Rank.DEFAULT) {
			formattedMessage = rankColor + player.getName() + ChatColor.WHITE + ": " + message;
		} else {
			String rankName = rank.getName();
			formattedMessage = rankColor + rankName + " " + player.getName() + ChatColor.WHITE + ": " + message;
		}

		event.setFormat(formattedMessage);
	}
}
