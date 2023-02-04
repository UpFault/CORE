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

	public static String getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		Date date = new Date();
		return formatter.format(date);
	}
	public static void setWorldProperties(World world) {
		world.setPVP(false);
		world.setDifficulty(Difficulty.PEACEFUL);
	}

	public static File getPlayerFile(UUID uuid) {
		return new File(CORE.getInstance().getDataFolder() + "/playerdata", uuid + ".yml");
	}

	public static  FileConfiguration getPlayerConfig(UUID uuid) {
		return YamlConfiguration.loadConfiguration(getPlayerFile(uuid));
	}

}
