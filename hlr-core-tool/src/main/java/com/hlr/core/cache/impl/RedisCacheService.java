package com.hlr.core.cache.impl;

import com.hlr.common.file.SerializeUtils;
import com.hlr.core.cache.CacheService;
import com.hlr.core.cache.CacheSource;
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
    public void setString(String key, String value, int expiry) {
        ShardedJedis resource = this.pool.getResource();
        try {
            if (key != null) {
                if (expiry == 0) {
                    resource.set(key.getBytes(), value.getBytes(StandardCharsets.UTF_8));
                } else {
                    resource.setex(key.getBytes(), expiry, value.getBytes(StandardCharsets.UTF_8));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(key, value);
        } finally {
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
                if (bytes != null) {
                    result = new String(bytes, StandardCharsets.UTF_8);
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(key);
        } finally {
            resource.close();
        }
        return null;
    }

    @Override
    public void setCacheable(CacheSource cacheSource) {
        setCacheable(cacheSource, 0);
    }

    @Override
    public void deleteCache(String key) {
        ShardedJedis resource = this.pool.getResource();
        try {
            if (key != null) {
                resource.del(key.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(key);
        } finally {
            resource.close();
        }

    }

    @Override
    public void setCacheable(CacheSource cacheSource, int expiry) {
        ShardedJedis resource = this.pool.getResource();
        String key = null;
        try {
            key = cacheSource.getKey();
            if (key != null) {
                if (expiry == 0) {
                    resource.set(key.getBytes(StandardCharsets.UTF_8), SerializeUtils.serialize(cacheSource.getValue()));
                } else {
                    resource.setex(key.getBytes(StandardCharsets.UTF_8), expiry, SerializeUtils.serialize(cacheSource.getValue()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(key);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            resource.close();
        }
    }

    @Override
    public void deleteCache(CacheSource cacheSource) {
        if (cacheSource != null) {
            deleteCache(cacheSource.getKey());
        }
    }

    @Override
    public Object getCacheable(CacheSource cacheSource) {
        return getCacheable(cacheSource, 0);
    }

    @Override
    public Object getCacheable(CacheSource cacheSource, int expiry) {
        ShardedJedis resource = this.pool.getResource();
        String key = null;
        Object object = null;
        try {
            key = cacheSource.getKey();
            if (key != null) {
                byte[] keybytes = key.getBytes(StandardCharsets.UTF_8);
                byte[] bytes = resource.get(keybytes);

                if (bytes == null) {
                    object = cacheSource.getValue();
                    if (expiry == 0) {
                        resource.set(keybytes, SerializeUtils.serialize(object));
                    } else {
                        resource.setex(keybytes, expiry, SerializeUtils.serialize(object));
                    }
                } else {
                    object = SerializeUtils.unSerialize(bytes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(key);
        } catch (Throwable e) {
            logger.error(key);
        } finally {
            resource.close();
        }
        return object;
    }

    @Override
    public Object getAndSet(CacheSource cacheSource) {
        return getAndSet(cacheSource, 0);
    }

    @Override
    public Object getAndSet(CacheSource cacheSource, int expiry) {
        ShardedJedis resource = this.pool.getResource();
        String key = null;
        Object object = null;
        try {
            key = cacheSource.getKey();
            if (key != null) {
                byte[] keybytes = key.getBytes(StandardCharsets.UTF_8);
                byte[] bytes = resource.get(keybytes);

                if (bytes == null) {
                    object = cacheSource.getValue();
                    if (expiry == 0) {
                        resource.set(keybytes, SerializeUtils.serialize(object));
                    } else {
                        resource.setex(keybytes, expiry, SerializeUtils.serialize(object));
                    }
                } else {
                    object = SerializeUtils.unSerialize(bytes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(key);
        } catch (Throwable e) {
            logger.error(key);
        } finally {
            resource.close();
        }
        return object;
    }
}
