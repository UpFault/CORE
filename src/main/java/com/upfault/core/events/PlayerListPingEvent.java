package com.upfault.core.events;

import com.upfault.core.CORE;
import com.upfault.core.utils.VersionCheck;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerListPingEvent implements Listener {

	@EventHandler
	public void changeMOTD(ServerListPingEvent event) {
		FileConfiguration config = CORE.instance.getConfig();
			if(config.getBoolean("maintenance")) {
				event.setMaxPlayers(0);
			}
		event.motd(Component.text(ChatColor.translateAlternateColorCodes('&', "                 &bCORE &c[1.8-1.19]&r\n                 &4&l[IN-DEV] v" + VersionCheck.getVersion())));
	}
}
