package com.upfault.core.utils;

import com.upfault.core.CORE;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Utilities {
	private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
	private static final CORE instance = CORE.getInstance();
	private static final String PLAYER_DATA_DIRECTORY = "/playerdata";
	private static final String ANIMATIONS_FILE = "animations.yml";
	private static final String RANKS_FILE = "ranks.yml";

	public static String getDate() {
		return formatter.format(new Date());
	}

	public static void setWorldProperties(World world) {
		world.setPVP(false);
		world.setDifficulty(Difficulty.PEACEFUL);
	}

	public static File getPlayerFile(UUID uuid) {
		return new File(instance.getDataFolder() + PLAYER_DATA_DIRECTORY, uuid + ".yml");
	}

	public static FileConfiguration getPlayerConfig(UUID uuid) {
		return YamlConfiguration.loadConfiguration(getPlayerFile(uuid));
	}

	public static File getAnimationsFile() {
		return new File(instance.getDataFolder(), ANIMATIONS_FILE);
	}

	public static FileConfiguration getAnimationsConfig() {
		return YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), ANIMATIONS_FILE));
	}

	public static File getRanksFile() {
		return new File(instance.getDataFolder(), RANKS_FILE);
	}

	public static FileConfiguration getRanksConfig() {
		return YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), RANKS_FILE));
	}
}

