package com.upfault.core.events;

import com.upfault.core.CORE;
import com.upfault.core.utils.FastBoard;
import com.upfault.core.utils.Utilities;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LeaveEvents implements Listener {

	@EventHandler
	public void onLeave(PlayerQuitEvent event) throws IOException {
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();
		FileConfiguration playerConfig = Utilities.getPlayerConfig(playerUUID);
		File playerFile = Utilities.getPlayerFile(playerUUID);

		event.quitMessage(Component.text(""));
		playerConfig.save(playerFile);
	}

	@EventHandler
	public void removeScoreboard(PlayerQuitEvent event) {

		FastBoard board = CORE.boards.remove(event.getPlayer().getUniqueId());

		if (board != null) {
			board.delete();
		}
	}
}
