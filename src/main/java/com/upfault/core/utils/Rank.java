package com.upfault.core.utils;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Rank {

	DEFAULT("", ChatColor.GRAY, 0,Arrays.asList("core.help")),
	VIP("[VIP]", ChatColor.GREEN, 10, Arrays.asList("core.help")),
	MVP("[MVP]", ChatColor.AQUA, 20, Arrays.asList("core.help")),
	MOD("[MOD]", ChatColor.DARK_GREEN, 30, Arrays.asList("core.core", "core.help")),
	ADMIN("[ADMIN]", ChatColor.RED, 40, Arrays.asList("core.core", "core.build", "core.destroy", "core.rank", "core.help")),
	OWNER("[OWNER]", ChatColor.RED, 50, Arrays.asList("core.core", "core.build", "core.destroy", "core.rank", "core.help"));

	private final String name;
	private final ChatColor color;
	private final List<String> permissions;
	private final int weight;
	Rank(String name, ChatColor color, int weight, List<String> permissions) {
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

	public List<String> getPermissions() {
		return permissions;
	}

	public int getWeight() {
		return weight;
	}

	public int compare(Rank other) {
		return Integer.compare(this.weight, other.weight);
	}

	public static Rank compareRank(Rank currentRank, String action) {
		Map<Rank, Integer> rankWeight = new HashMap<>();
		for (Rank rank : Rank.values()) {
			rankWeight.put(rank, rank.getWeight());
		}

		int currentRankWeight = rankWeight.get(currentRank);
		if (action.equals("promote")) {
			currentRankWeight++;
		} else if (action.equals("demote")) {
			currentRankWeight--;
		}

		for (Map.Entry<Rank, Integer> entry : rankWeight.entrySet()) {
			if (entry.getValue() == currentRankWeight) {
				return entry.getKey();
			}
		}

		return currentRank;
	}
}

