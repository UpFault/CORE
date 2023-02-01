package com.upfault.core.utils;

public enum Rank {
	ADMIN("§cADMIN", 75),
	MOD("§2MOD", 50),
	MVP("§9MVP", 25),
	VIP("§aVIP", 10),
	DEFAULT("§7NONE", 0);

	private final String name;
	/**
	 * @weight A higher weight indicates that the factor has a greater impact on the final ranking, whereas a lower weight indicates that it has a lesser impact.
	 */
	private final int weight;

	Rank(String name, int weight) {
		this.name = name;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public int getWeight() {
		return weight;
	}
}
