package com.upfault.core.commands;

import com.upfault.core.CORE;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class main implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You need to be a player to use this command!");
            return true;
        }

        switch (args[0]) {
            case "help":
//                display helpful info
                break;
            case "info":
                Player player = (Player) sender;
                PluginDescriptionFile pdf = CORE.instance.getDescription();
                player.sendMessage("Plugin Information:");
                player.sendMessage("Name: " + pdf.getName());
//                add a check if there is a newer version
                player.sendMessage("Version: " + pdf.getVersion());
                player.sendMessage("Description: " + pdf.getDescription());
                player.sendMessage("Authors: " + String.join(", ", pdf.getAuthors()));
                player.sendMessage("Server Information:");
                player.sendMessage("Server IP: " + Bukkit.getIp());
                player.sendMessage("Server Version: " + Bukkit.getBukkitVersion());
                player.sendMessage("Max Players: " + Bukkit.getMaxPlayers());
                player.sendMessage("Online Players: " + Bukkit.getOnlinePlayers().size());
                break;
        }

        return false;
    }
}
