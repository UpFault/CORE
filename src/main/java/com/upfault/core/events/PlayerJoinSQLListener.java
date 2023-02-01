package com.upfault.core.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerJoinSQLListener implements Listener {

	private Connection connection;
	private final String url;
	private final String username;
	private final String password;

	public PlayerJoinSQLListener(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		try {
			connection = DriverManager.getConnection(url, username, password);
			String sql = "INSERT INTO players (username) VALUES (?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, event.getPlayer().getName());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

