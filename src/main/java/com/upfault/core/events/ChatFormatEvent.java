package com.upfault.core.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormatEvent implements Listener {
	@EventHandler @SuppressWarnings("deprecation")
	public void onPlayerChat(AsyncPlayerChatEvent event) {

		Player player = event.getPlayer();
		String message = event.getMessage();

		event.setFormat(player.getName() + ChatColor.WHITE + ": " + message);
	}
}
