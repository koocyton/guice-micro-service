package com.doopp.gauss.server.module;

import com.doopp.gauss.server.application.ApplicationProperties;
import com.doopp.gauss.server.redis.CustomShadedJedis;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

public class RedisModule extends AbstractModule {

    private ApplicationProperties applicationProperties;

    public RedisModule(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void configure() {
    }

    @Singleton
    @Provides
    public CustomShadedJedis sessionRedis() {
        String s1 = this.applicationProperties.s("redis.session.s1");
        String s2 = this.applicationProperties.s("redis.session.s2");
        ShardedJedisPool shardedJedisPool = this.shardedJedisPool(this.jedisPoolConfig(), s1, s2);
        CustomShadedJedis customShadedJedis = new CustomShadedJedis();
        customShadedJedis.setShardedJedisPool(shardedJedisPool);
        return customShadedJedis;
    }

    private JedisPoolConfig jedisPoolConfig() {
        // Jedis池配置
        JedisPoolConfig config = new JedisPoolConfig();
        // 最大分配的对象数
        config.setMaxTotal(this.applicationProperties.i("redis.pool.maxTotal"));
        // 最大能够保持idel状态的对象数
        config.setMaxIdle(this.applicationProperties.i("redis.pool.maxIdle"));
        // 最小空闲的对象数。2.5.1以上版本有效
        config.setMinIdle(this.applicationProperties.i("redis.pool.minIdle"));
        // 当池内没有返回对象时，最大等待时间
        config.setMaxWaitMillis(this.applicationProperties.i("redis.pool.maxWaitMillis"));
        // 是否启用Lifo。如果不设置，默认为true。2.5.1以上版本有效
        config.setLifo(this.applicationProperties.b("redis.pool.lifo"));
        // 当调用borrow Object方法时，是否进行有效性检查
        config.setTestOnBorrow(this.applicationProperties.b("redis.pool.testOnBorrow"));
        // return
        return config;
    }

    private ShardedJedisPool shardedJedisPool(JedisPoolConfig jedisPoolConfig, String... hosts) {
        // map host
        List<JedisShardInfo> jedisInfoList = new ArrayList<>(hosts.length);
        // loop
        for (String host : hosts) {
            JedisShardInfo jedisShardInfo = new JedisShardInfo(host);
            jedisShardInfo.setConnectionTimeout(2000);
            jedisShardInfo.setSoTimeout(2000);
            jedisInfoList.add(jedisShardInfo);
        }
        // return ShardedJedisPool
        return new ShardedJedisPool(jedisPoolConfig, jedisInfoList);
    }
}
