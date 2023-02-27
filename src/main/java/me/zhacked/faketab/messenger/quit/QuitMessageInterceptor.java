package me.zhacked.faketab.messenger.quit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.google.inject.Inject;
import me.zhacked.faketab.cache.Cache;
import me.zhacked.faketab.player.FakePlayer;
import net.ibxnjadev.vmessenger.universal.Interceptor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.UUID;

public class QuitMessageInterceptor implements Interceptor<QuitMessage> {

    @Inject private ProtocolManager protocolManager;
    @Inject private Plugin plugin;
    @Inject private Cache<UUID, FakePlayer> fakePlayerCache;

    @Override
    public void subscribe(QuitMessage message) {
        if (!fakePlayerCache.exists(message.playerUUID())) return;

        FakePlayer fakePlayer = fakePlayerCache.get(message.playerUUID());

        Bukkit.getScheduler().runTask(plugin, () -> {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO_REMOVE);
            packet.getUUIDLists().write(0, Collections.singletonList(fakePlayer.uuid()));

            for (Player receiver : plugin.getServer().getOnlinePlayers()) {
                protocolManager.sendServerPacket(receiver, packet);
            }

            fakePlayerCache.remove(fakePlayer.uuid());
        });
    }
}
