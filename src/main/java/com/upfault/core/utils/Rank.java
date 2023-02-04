package com.upfault.core.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;

public enum Rank {

	DEFAULT("", ChatColor.GRAY, 0,(ArrayList<String>) Arrays.asList("core.help")),
	VIP("[VIP]", ChatColor.GREEN, 10, (ArrayList<String>) Arrays.asList("core.help")),
	MVP("[MVP]", ChatColor.BLUE, 20, (ArrayList<String>) Arrays.asList("core.help")),
	MOD("[MOD]", ChatColor.DARK_GREEN, 30, (ArrayList<String>) Arrays.asList("core.core", "core.help", "core.help")),
	ADMIN("[ADMIN]", ChatColor.RED, 40, (ArrayList<String>) Arrays.asList("core.core", "core.build", "core.destroy", "core.rank", "core.help")),
	OWNER("[OWNER]", ChatColor.RED, 50, (ArrayList<String>) Arrays.asList("core.core", "core.build", "core.destroy", "core.rank", "core.help"));

	private final String name;
	private final ChatColor color;
	private final ArrayList<String> permissions;
	private final int weight;
	Rank(String name, ChatColor color, int weight, ArrayList<String> permissions) {
		this.name = name;
		this.color = color;
		this.weight = weight;
		this.permissions = permissions;
	}

	public String getName() {
		return name;
	}

	public ChatColor getColor() {
		return color;
	}

	public ArrayList<String> getPermissions() {
		return permissions;
	}

	public int getWeight() {
		return weight;
	}
}

