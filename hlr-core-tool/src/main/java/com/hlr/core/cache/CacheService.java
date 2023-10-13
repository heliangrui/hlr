package com.hlr.core.cache;

/**
 * CacheService
 * Description:
 * date: 2023/10/13 15:33
 *
 * @author hlr
 */
public interface CacheService {

    void setString(String paramString1, String paramString2);

    void setString(String paramString1, String paramString2, long paramLong);

    String getString(String paramString);

}
