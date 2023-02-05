package com.upfault.core.utils;

import com.upfault.core.CORE;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;

public class LevelingSystem {

	private HashMap<Player, Integer> playerXp = new HashMap<>();
	private HashMap<Player, Integer> playerLevel = new HashMap<>();

	private int[] xpRequired = new int[100];

	private void calculateXpRequired() {
		for (int i = 1; i <= 100; i++) {
			xpRequired[i-1] = (int) (50 * Math.pow(i, 2) + 50 * i + 50);
		}
	}

	public void startTask() {
		Bukkit.getScheduler().runTaskTimer(CORE.instance, () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if(player == null) return;
				int currentXp = playerXp.getOrDefault(player, 0);
				int currentLevel = playerLevel.getOrDefault(player, 1);
				int requiredXp = xpRequired[currentLevel-1];
				int newXp = (int) (currentXp + requiredXp * 0.25);
				playerXp.put(player, newXp);
				updateXpBar(player);
				try {
					checkLevelUp(player);
					FileConfiguration config = Utilities.getPlayerConfig(player.getUniqueId());
					config.set("server_xp", playerXp.get(player));
					config.save(Utilities.getPlayerFile(player.getUniqueId()));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}, 0, 20 * 60 * 5);
	}

	private void updateXpBar(Player player) {
		int currentXp = playerXp.getOrDefault(player, 0);
		int currentLevel = playerLevel.getOrDefault(player, 1);
		int requiredXp = xpRequired[currentLevel-1];
		float progress = (float) currentXp / requiredXp;
		if(progress < 0.0f || progress > 1.0f || Float.isNaN(progress)) {
			return;
		}
		player.setExp(progress);
		player.setLevel(currentLevel);
	}

	private void checkLevelUp(Player player) throws IOException {
		int currentXp = playerXp.getOrDefault(player, 0);
		int currentLevel = playerLevel.getOrDefault(player, 1);
		int requiredXp = xpRequired[currentLevel-1];
		if (currentLevel >= 100) {
			// Player has reached maximum level, return from method
			return;
		}
		if (currentXp >= requiredXp) {
			FileConfiguration config = Utilities.getPlayerConfig(player.getUniqueId());
			config.set("server_level", currentLevel + 1);
			config.set("server_xp", currentXp);
			config.save(Utilities.getPlayerFile(player.getUniqueId()));
			config.save(Utilities.getPlayerFile(player.getUniqueId()));
			playerLevel.put(player, currentLevel + 1);
			checkLevelUp(player);
		}
	}


}
