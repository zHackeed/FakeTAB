package me.zhacked.faketab.messenger.quit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.inject.Inject;
import me.zhacked.faketab.cache.Cache;
import me.zhacked.faketab.player.FakePlayer;
import net.ibxnjadev.vmessenger.universal.Interceptor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class QuitMessageInterceptor implements Interceptor<QuitMessage> {

    @Inject private ProtocolManager protocolManager;
    @Inject private Plugin plugin;
    @Inject private Cache<UUID, FakePlayer> fakePlayerCache;

    private final GsonComponentSerializer gsonComponentSerializer = GsonComponentSerializer.gson();

    @Override
    public void subscribe(QuitMessage message) {
        if (!fakePlayerCache.exists(message.playerUUID())) return;

        FakePlayer fakePlayer = fakePlayerCache.get(message.playerUUID());
        Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(
                fakePlayer.prefix() + fakePlayer.name() + fakePlayer.suffix()
        );

        Bukkit.getScheduler().runTask(plugin, () -> {
            WrappedGameProfile profile = new WrappedGameProfile(fakePlayer.uuid(), fakePlayer.name());
            PlayerInfoData playerInfoData = new PlayerInfoData(profile,
                    0,
                    EnumWrappers.NativeGameMode.SURVIVAL,
                    WrappedChatComponent.fromJson(
                            gsonComponentSerializer.serialize(component)
                    )
            );

            PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO_REMOVE);
            packet.getPlayerInfoDataLists().write(0, List.of(playerInfoData));

            for (Player receiver : plugin.getServer().getOnlinePlayers()) {
                protocolManager.sendServerPacket(receiver, packet);
            }
        });
    }
}
