package com.upfault.core.events;

import com.upfault.core.CORE;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class PlayerDataEvent implements Listener {

	private Connection connection;

	public PlayerDataEvent() {
		FileConfiguration config = CORE.instance.getConfig();
		String host = config.getString("database.host");
		int port = config.getInt("database.port");
		String databaseName = config.getString("database.database");
		String username = config.getString("database.user");
		String password = config.getString("database.password");
		File localDbFile = new File(CORE.instance.getDataFolder(), "database/playerData.db");

		if (localDbFile.exists()) {
			useLocalDb(localDbFile);
		} else {
			useHostedDb(host, port, databaseName, username, password);
		}

		try {
			PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS playerData (uuid TEXT PRIMARY KEY, name TEXT, rank TEXT, friends TEXT)");
			statement.executeUpdate();
		} catch (SQLException e) {
			Bukkit.getLogger().severe(e.getMessage());
		}
	}

	private void useLocalDb(File localDbFile) {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + localDbFile.getAbsolutePath());
			Bukkit.getLogger().info("[PlayerDataEvent] Connected to local database: " + localDbFile.getAbsolutePath());
		} catch (Exception e) {
			Bukkit.getLogger().severe("[PlayerDataEvent] Failed to connect to local database: " + e.getMessage());
		}
	}

	private void useHostedDb(String host, int port, String databaseName, String username, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			if (host == null || databaseName == null || username == null || password == null) {
				Bukkit.getLogger().severe("[PlayerDataEvent] Error: Missing required values in config.yml");
				return;
			}

			if (host.equals("localhost") && port == 3306 && databaseName.equals("db") && username.equals("username") && password.equals("password")) {
				Bukkit.getLogger().warning("[PlayerDataEvent] The database credentials are still set to the default values!");
				return;
			}
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName, username, password);
			Bukkit.getLogger().info("[PlayerDataEvent] Connected to hosted database: " + databaseName);
		} catch (Exception e) {
			Bukkit.getLogger().severe("[PlayerDataEvent] Failed to connect to hosted database: " + e.getMessage());
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();

		// Check if the player data is already in the table
		String checkQuery = "SELECT * FROM playerData WHERE uuid = ?";
		try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
			checkStmt.setString(1, playerUUID.toString());
			ResultSet result = checkStmt.executeQuery();
			if (!result.next()) {
				// Player data is not in the table, insert it
				String insertQuery = "INSERT INTO playerData (uuid, name, rank, friends) VALUES (?, ?, ?, ?)";
				try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
					insertStmt.setString(1, playerUUID.toString());
					insertStmt.setString(2, player.getName());
					insertStmt.setString(3, "default");
					insertStmt.setString(4, "[]");
					insertStmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
