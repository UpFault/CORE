package com.upfault.core.events;

import com.upfault.core.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class HubProtectionEvent implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		World hub = Bukkit.getWorld("hub");
		if (event.getPlayer().getWorld() != hub) return;

		FileConfiguration config = Utilities.getPlayerConfig(event.getPlayer().getUniqueId());
		if (!config.getBoolean("destroymode")) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "Hey! You cannot do that here.");
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		World hub = Bukkit.getWorld("hub");
		if (event.getPlayer().getWorld() != hub) return;
		Block block = event.getClickedBlock();
		if (block == null || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		FileConfiguration config = Utilities.getPlayerConfig(event.getPlayer().getUniqueId());
		if (!config.getBoolean("buildmode")) event.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(@NotNull BlockPlaceEvent event) {
		World hub = Bukkit.getWorld("hub");
		if (event.getPlayer().getWorld() != hub) return;

		FileConfiguration config = Utilities.getPlayerConfig(event.getPlayer().getUniqueId());
		if (!config.getBoolean("buildmode")) event.setCancelled(true);
	}

	@EventHandler
	public void onDecrease(FoodLevelChangeEvent foodLevelChangeEvent) {
		World hub = Bukkit.getWorld("hub");
		Player player = (Player) foodLevelChangeEvent.getEntity();
		if (player.getWorld() != hub) return;
		foodLevelChangeEvent.setCancelled(true);
		player.setFoodLevel(20);
		player.setSaturation(20);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		World hub = Bukkit.getWorld("hub");
		if (!(event.getEntity() instanceof Player)) return;

		Player player = (Player) event.getEntity();
		if (player.getWorld() != hub) return;

		if (event.getCause() != EntityDamageEvent.DamageCause.CUSTOM) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent event) {
		World hub = Bukkit.getWorld("hub");
		if (event.getEntity().getWorld() != hub) return;
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
			event.setCancelled(true);
		}
	}
}

