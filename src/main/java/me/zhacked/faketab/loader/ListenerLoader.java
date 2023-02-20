package me.zhacked.faketab.loader;

import com.google.inject.Inject;
import me.zhacked.faketab.listeners.PlayerJoinListener;
import me.zhacked.faketab.listeners.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ListenerLoader implements Loader {

    @Inject private Plugin plugin;
    @Inject private PlayerJoinListener playerJoinListener;
    @Inject private PlayerQuitListener playerQuitListener;

    @Override
    public void load() {
        Bukkit.getPluginManager().registerEvents(playerJoinListener, plugin);
        Bukkit.getPluginManager().registerEvents(playerQuitListener, plugin);
    }
}
