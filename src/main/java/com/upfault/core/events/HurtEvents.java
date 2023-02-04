package com.upfault.core.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class HurtEvents implements Listener {
	@EventHandler
	public void onPlayerHurt(EntityDamageEvent event) {
		if(event.getEntity().getWorld() == Bukkit.getWorld("hub")) event.setCancelled(true);
	}
}
