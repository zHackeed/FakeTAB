package me.zhacked.faketab.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import me.zhacked.faketab.connection.RedisConnection;
import me.zhacked.faketab.file.YAMLFile;
import me.zhacked.faketab.redis.Redis;
import net.ibxnjadev.vmessenger.redis.RedisMessenger;
import net.ibxnjadev.vmessenger.universal.Messenger;
import net.ibxnjadev.vmessenger.universal.serialize.ObjectJacksonAdapter;

public class RedisModule extends AbstractModule {

    private final YAMLFile config;

    public RedisModule(YAMLFile config) {
        this.config = config;
    }

    @Override
    public void configure() {
        RedisConnection redisConnection = new RedisConnection(config);
        redisConnection.connect();

        bind(RedisConnection.class).toInstance(redisConnection);

        Redis redis = new Redis(redisConnection.getJedis());
        bind(Redis.class).toProvider(() -> redis).in(Scopes.SINGLETON);

        ObjectJacksonAdapter objectJacksonAdapter = new ObjectJacksonAdapter();

        Messenger messenger = new RedisMessenger("FakeTAB",
                redisConnection.getJedis(),
                redisConnection.getJedis().getResource(),
                objectJacksonAdapter,
                new ObjectMapper());

        bind(Messenger.class).toProvider(() -> messenger).in(Scopes.SINGLETON);
    }
}
