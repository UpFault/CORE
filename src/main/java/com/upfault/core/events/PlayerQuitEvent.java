package com.upfault.core.events;

import com.upfault.core.CORE;
import com.upfault.core.utils.Utilities;
import com.upfault.core.utils.fastboard.FastBoard;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerQuitEvent implements Listener {

	@EventHandler
	public void onLeave(org.bukkit.event.player.PlayerQuitEvent event) throws IOException {
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();
		FileConfiguration playerConfig = Utilities.getPlayerConfig(playerUUID);
		File playerFile = Utilities.getPlayerFile(playerUUID);

		FastBoard board = CORE.boards.remove(player.getUniqueId());

		if (board != null) {
			board.delete();
		}

		event.quitMessage(null);
		playerConfig.save(playerFile);
	}
}
