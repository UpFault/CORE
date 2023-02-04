package com.upfault.core.events;

import com.upfault.core.utils.Utilities;
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

		playerConfig.save(playerFile);
	}
}
