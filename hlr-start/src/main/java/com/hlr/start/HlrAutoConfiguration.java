package com.hlr.start;

import com.hlr.core.cache.impl.RedisCacheService;
import com.hlr.core.cache.redis.RedisPool;
import com.hlr.start.config.RedisPoolConfigProperties;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HlrAutoConfiguration
 * Description:
 * date: 2023/10/10 19:39
 *
 * @author hlr
 */
@Configuration
@EnableConfigurationProperties({RedisPoolConfigProperties.class})
public class HlrAutoConfiguration implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        HlrAutoConfiguration.applicationContext = applicationContext;
    }

    @Bean(
            initMethod = "init",
            destroyMethod = "shutDown"
    )
    @ConditionalOnProperty({"hlr.redis.server"})
    RedisPool redisPool(RedisPoolConfigProperties redisPoolConfigProperties) {
        RedisPool pool = new RedisPool();
        pool.setServer(redisPoolConfigProperties.getServer());
        pool.setConnectionTimeout(redisPoolConfigProperties.getConnectionTimeout());
        pool.setMaxIdle(redisPoolConfigProperties.getMaxIdle());
        pool.setMaxTotal(redisPoolConfigProperties.getMaxTotal());
        pool.setMaxWaitMillis(redisPoolConfigProperties.getMaxWaitMillis());
        pool.setSoTimeout(redisPoolConfigProperties.getSoTimeout());
        return pool;
    }

    @Bean
    @ConditionalOnBean({RedisPool.class})
    RedisCacheService redisCacheService(RedisPool redisPool) {
        return new RedisCacheService(redisPool.getPool());
    }

    @Bean
    HlrReadyApplicationListener hlrReadyApplicationListener() {
        return new HlrReadyApplicationListener();
    }
}
