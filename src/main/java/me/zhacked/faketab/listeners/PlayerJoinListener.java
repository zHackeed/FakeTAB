package me.zhacked.faketab.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.inject.Inject;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;
import me.zhacked.faketab.cache.Cache;
import me.zhacked.faketab.messenger.join.JoinMessage;
import me.zhacked.faketab.player.FakePlayer;
import net.ibxnjadev.vmessenger.universal.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class PlayerJoinListener implements Listener {

    @Inject private Messenger messenger;
    @Inject private TabAPI tabAPI;
    @Inject private Cache<UUID, FakePlayer> fakePlayerCache;
    @Inject private Plugin plugin;
    @Inject private ProtocolManager protocolManager;

    private final GsonComponentSerializer gsonComponentSerializer = GsonComponentSerializer.gson();

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

        Bukkit.getScheduler().runTask(plugin, () -> fakePlayerCache.values().forEach(fakePlayer -> {
            Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(
                    fakePlayer.prefix() + fakePlayer.name() + fakePlayer.suffix()
            );

            WrappedGameProfile profile = new WrappedGameProfile(fakePlayer.uuid(), fakePlayer.name());
            PlayerInfoData playerInfoData = new PlayerInfoData(profile,
                    0,
                    EnumWrappers.NativeGameMode.SURVIVAL,
                    WrappedChatComponent.fromJson(
                            gsonComponentSerializer.serialize(component)
                    )
            );

            PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
            packet.getPlayerInfoActions().write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER));
            packet.getPlayerInfoDataLists().write(1, List.of(playerInfoData));

            protocolManager.sendServerPacket(event.getPlayer(), packet);
        }));
    }
}
