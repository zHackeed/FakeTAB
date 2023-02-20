package me.zhacked.faketab.connection;

import me.zhacked.faketab.file.YAMLFile;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisConnection implements Connection {

    private JedisPool jedisPool;
    private final YAMLFile config;

    public RedisConnection(YAMLFile yamlFile) {
        this.config = yamlFile;
    }

    @Override
    public void connect() {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(8);

            if (config.get("redis", "password").getString() != null && !config.get("redis", "password").getString("El path es nulo, comprueba si es existente.").trim().isEmpty()) {
                jedisPool = new JedisPool(jedisPoolConfig, config.get("redis", "host").getString("El path es nulo"),
                        config.get("redis", "port").getInt(0),
                        2000,
                        config.get("redis", "password").getString("El path es nulo"));
            } else {
                jedisPool = new JedisPool(jedisPoolConfig, config.get("redis", "host").getString("El path es nulo"),
                        config.get("redis", "port").getInt(0),
                        2000);
            }

        } catch (JedisConnectionException e) {
            System.out.println("Error connecting");
        }
    }

    @Override
    public void disconnect() {
        jedisPool.close();
    }

    public JedisPool getJedis() {
        return jedisPool;
    }

}
