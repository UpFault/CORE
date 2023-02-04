package com.upfault.core.events;

import com.upfault.core.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class HubGuard implements Listener {
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		FileConfiguration config = Utilities.getPlayerConfig(event.getPlayer().getUniqueId());
		World hub = Bukkit.getWorld("hub");

		if (event.getPlayer().getWorld() != hub) return;

		if (!config.getBoolean("destroymode")) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "Hey! You cannot do that here.");
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		FileConfiguration config = Utilities.getPlayerConfig(event.getPlayer().getUniqueId());
		World hub = Bukkit.getWorld("hub");

		if (event.getPlayer().getWorld() != hub) return;
		if (event.getClickedBlock() == null) return;

		Block block = event.getClickedBlock();
		if (!block.getType().name().contains("DOOR")) return;
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		if (!config.getBoolean("buildmode")) event.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(@NotNull BlockPlaceEvent event) {
		FileConfiguration config = Utilities.getPlayerConfig(event.getPlayer().getUniqueId());
		World hub = Bukkit.getWorld("hub");

		if (event.getPlayer().getWorld() != hub) return;

		if (!config.getBoolean("buildmode")) event.setCancelled(true);
	}

	@EventHandler
	public void onDecrease(FoodLevelChangeEvent foodLevelChangeEvent) {
		Player player = (Player) foodLevelChangeEvent.getEntity();
		player.setFoodLevel(20);
		player.setSaturation(20);
	}

	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event) {
		World hub = Bukkit.getWorld("hub");

		if(event.getEntity().getWorld() == hub) {
			if(event.getEntity() instanceof Monster || event.getEntity() instanceof Animals) {
				event.setCancelled(true);
			}
		}
	}
}

