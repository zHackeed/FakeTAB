package me.zhacked.faketab.service;

import com.google.inject.Inject;
import me.zhacked.faketab.connection.RedisConnection;
import me.zhacked.faketab.loader.MainLoader;
import org.bukkit.plugin.Plugin;

public class MainService implements Service {

    @Inject
    private RedisConnection redisConnection;
    @Inject private Plugin plugin;
    @Inject private MainLoader loader;

    @Override
    public void start() {
        loader.load();
    }

    @Override
    public void stop() {
        redisConnection.disconnect();
        plugin.getServer().getScheduler().cancelTasks(plugin);
    }
}
