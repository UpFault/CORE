package com.upfault.core.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		if (player.getLocation().getBlockY() <= 5) {
			Location location = new Location(world, -264.50, 18.00, 108.50, 90, 0);
			player.teleport(location);
		}
	}
}
