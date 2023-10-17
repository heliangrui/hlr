package com.hlr.core.cache;

/**
 * CacheService
 * Description:
 * date: 2023/10/13 15:33
 *
 * @author hlr
 */
public interface CacheService {

    void setString(String key, String value);

    /**
     * 
     * @param key
     * @param value
     * @param expiry 时间 秒
     */
    void setString(String key, String value, int expiry);

    String getString(String paramString);
    
    void setCacheable(CacheSource cacheSource);
    
    void deleteCache(String key);

    /**
     * 
     * @param cacheSource
     * @param expiry 秒
     */
    void setCacheable(CacheSource cacheSource, int expiry);
    
    void deleteCache(CacheSource cacheSource);

    /**
     * 获取缓存-为空设置缓存
     * @param cacheSource
     * @return
     */
    Object getCacheable(CacheSource cacheSource);

    /**
     * 获取缓存-为空设置缓存
     * @param cacheSource
     * @param expiry 秒
     * @return
     */
    Object getCacheable(CacheSource cacheSource, int expiry);

    /**
     * 获取并设置缓存
     * @param cacheSource
     * @return
     */
    Object getAndSet(CacheSource cacheSource);
    
    /**
     * 获取并设置缓存
     * @param cacheSource
     * @param expiry 秒值
     * @return
     */
    Object getAndSet(CacheSource cacheSource, int expiry);

}
