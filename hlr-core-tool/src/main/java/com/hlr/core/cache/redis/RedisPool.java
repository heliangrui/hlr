package com.hlr.core.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * RedisPool
 * Description:
 * date: 2023/10/10 19:24
 *
 * @author hlr
 */
public class RedisPool {
    private static final Logger log = LoggerFactory.getLogger(RedisPool.class);
    private int maxTotal = 10;
    private int maxIdle = 1;
    private int maxWaitMillis = 5000;
    private int soTimeout = 3000;
    private int connectionTimeout = 3000;
    private String server;
    private ShardedJedisPool pool;
    
    public void init(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        List<JedisShardInfo> shards = new ArrayList<>();
        String[] servers = server.split(",");

        for (String ipPort : servers) {
            String[] params = ipPort.split(":");
            JedisShardInfo info = new JedisShardInfo(params[0], Integer.valueOf(params[1]), params[2]);
            if (params.length == 4) {
                info.setPassword(params[3]);
            }
            info.setConnectionTimeout(this.connectionTimeout);
            info.setSoTimeout(this.soTimeout);
            shards.add(info);
        }
        this.pool = new ShardedJedisPool(config, shards);
        log.debug("init redis pool {}", this.server);
    }
    
    public void shutDown(){
        log.debug("shutDown redis pool");
        if (this.pool != null) {
            this.pool.close();
        }
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
