package me.zhacked.faketab.listeners;

import com.google.inject.Inject;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;
import me.zhacked.faketab.messenger.join.JoinMessage;
import net.ibxnjadev.vmessenger.universal.Messenger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    @Inject private Messenger messenger;
    @Inject private TabAPI tabAPI;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        tabAPI.getEventBus().register(PlayerLoadEvent.class, loadEvent -> {
            TabPlayer tabPlayer = loadEvent.getPlayer();
            UUID uuid = tabPlayer.getUniqueId();
            String tabName = TabAPI.getInstance().getTablistFormatManager().getOriginalName(tabPlayer);
            String tabPrefix = TabAPI.getInstance().getTablistFormatManager().getOriginalPrefix(tabPlayer);
            String tabSuffix = TabAPI.getInstance().getTablistFormatManager().getOriginalSuffix(tabPlayer);

            messenger.sendMessage(new JoinMessage(
                    uuid, tabPrefix, tabSuffix, tabName
            ));
        });
    }
}
