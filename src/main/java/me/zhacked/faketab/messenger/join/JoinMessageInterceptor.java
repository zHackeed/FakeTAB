package me.zhacked.faketab.messenger.join;

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

public class JoinMessageInterceptor implements Interceptor<JoinMessage> {

    @Inject private ProtocolManager protocolManager;
    @Inject private Plugin plugin;
    @Inject private Cache<UUID, FakePlayer> fakePlayerCache;

    private final GsonComponentSerializer gsonComponentSerializer = GsonComponentSerializer.gson();

    @Override
    public void subscribe(JoinMessage message) {
        Player player = Bukkit.getPlayer(message.uuid());
        if (player != null) return;

        fakePlayerCache.add(message.uuid(),
                new FakePlayer(message.uuid(), message.prefix(), message.suffix(), message.name()));

        Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(
                message.prefix() + message.name() + message.suffix()
        );


        Bukkit.getScheduler().runTask(plugin, () -> {
            WrappedGameProfile profile = new WrappedGameProfile(message.uuid(), message.name());
            PlayerInfoData playerInfoData = new PlayerInfoData(profile,
                    0,
                    EnumWrappers.NativeGameMode.SURVIVAL,
                    WrappedChatComponent.fromJson(
                        gsonComponentSerializer.serialize(component)
                    )
            );

            PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
            packet.getPlayerInfoDataLists().write(0, List.of(playerInfoData));

            for (Player receiver : plugin.getServer().getOnlinePlayers()) {
                protocolManager.sendServerPacket(receiver, packet);
            }
        });
    }
}
