package com.hlr.core.cache;

/**
 * CacheSource
 * Description: 用于获取缓存 并配置缓存
 * date: 2023/10/17 10:07
 *
 * @author hlr
 */
public interface CacheSource {
    
    String getKey();
    
    Object getValue() throws Throwable;
    
}
