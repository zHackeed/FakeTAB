package me.zhacked.faketab.listeners;

import com.google.inject.Inject;
import me.zhacked.faketab.messenger.quit.QuitMessage;
import net.ibxnjadev.vmessenger.universal.Messenger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @Inject private Messenger messenger;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        messenger.sendMessage(new QuitMessage(event.getPlayer().getUniqueId()));
    }
}
