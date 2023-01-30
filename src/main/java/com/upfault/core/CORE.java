package com.upfault.core;

import com.upfault.core.commands.main;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CORE extends JavaPlugin {

    public static CORE instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        registerCommand();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }

    private void registerCommand() {
        Objects.requireNonNull(getCommand("core")).setExecutor(new main());
    }
}
