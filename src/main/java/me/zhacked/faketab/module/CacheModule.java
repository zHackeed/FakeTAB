package me.zhacked.faketab.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import me.zhacked.faketab.cache.Cache;
import me.zhacked.faketab.cache.DefaultCache;
import me.zhacked.faketab.helper.TypeLiteralHelper;
import me.zhacked.faketab.player.FakePlayer;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class CacheModule extends AbstractModule {

    @Override
    public void configure() {
        bindCacheN(UUID.class, FakePlayer.class);
    }

    private <K, V> void bindCacheN(Class<K> clazz, Class<V> tClass) {
        bind(TypeLiteralHelper.getParameterizedTypeLiteral(Cache.class, clazz, tClass))
                .to(TypeLiteralHelper.getParameterizedTypeLiteral(DefaultCache.class, clazz, tClass))
                .in(Scopes.SINGLETON);
    }
}

