package com.hlr.core.cache.impl;

import com.hlr.core.cache.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.nio.charset.StandardCharsets;

/**
 * RedisCacheService
 * Description:
 * date: 2023/10/13 15:48
 *
 * @author hlr
 */
public class RedisCacheService implements CacheService {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

    private ShardedJedisPool pool;

    public RedisCacheService(ShardedJedisPool pool) {
        this.pool = pool;
    }

    @Override
    public void setString(String key, String value) {
        this.setString(key, value, 0);
    }

    @Override
    public void setString(String key, String value, long expiry) {
        ShardedJedis resource = this.pool.getResource();
        try {
            if (key != null) {
                if (expiry == 0) {
                    resource.set(key.getBytes(), value.getBytes(StandardCharsets.UTF_8));
                } else {
                    resource.setex(key.getBytes(), (int) (expiry / 1000), value.getBytes(StandardCharsets.UTF_8));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(key, value);
            resource.close();
        }
    }
    @Override
    public String getString(String key) {
        ShardedJedis resource = this.pool.getResource();
        String result;
        try {
            if (key != null) {
                byte[] bytes = resource.get(key.getBytes());
                if(bytes != null){
                    result = new String(bytes, StandardCharsets.UTF_8);
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(key);
            resource.close();
        }
        return null;
    }
}
