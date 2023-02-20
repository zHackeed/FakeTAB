package me.zhacked.faketab.module;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.inject.AbstractModule;
import me.neznamy.tab.api.TabAPI;
import me.zhacked.faketab.FakeTABPlugin;
import me.zhacked.faketab.file.FileMatcher;
import me.zhacked.faketab.file.YAMLFile;
import me.zhacked.faketab.service.MainService;
import me.zhacked.faketab.service.Service;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.Plugin;

public class MainModule extends AbstractModule {

    private final FakeTABPlugin plugin;

    public MainModule(FakeTABPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void configure() {
        bind(FakeTABPlugin.class).toInstance(plugin);
        bind(Plugin.class).to(FakeTABPlugin.class);

        FileMatcher matcher = new FileMatcher()
                .bind("config", new YAMLFile(plugin, "config"));

        install(matcher.build());
        bind(Service.class).to(MainService.class);

        bind(TabAPI.class).toInstance(TabAPI.getInstance());
        bind(ProtocolManager.class).toProvider(ProtocolLibrary::getProtocolManager);

        bind(MiniMessage.class).toProvider(MiniMessage::miniMessage);
        install(new CacheModule());
        install(new RedisModule(matcher.get("config").get()));
    }
}
