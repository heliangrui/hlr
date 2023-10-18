package com.hlr.start.aop;

import com.hlr.core.cache.CacheService;
import com.hlr.core.cache.CacheSource;
import com.hlr.core.cache.annotation.DelCache;
import com.hlr.core.cache.annotation.GetCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * MethodCacheAspect
 * Description:
 * date: 2023/10/17 17:32
 *
 * @author hlr
 */
@Order(2)
@Aspect
public class MethodCacheAspect {

    private static final Logger logger = LoggerFactory.getLogger(MethodCacheAspect.class);

    private CacheService cacheService;
    public MethodCacheAspect(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Around("@annotation(delCache)")
    public Object delCache(ProceedingJoinPoint point, DelCache delCache) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = point.getSignature().getDeclaringType().getName() + "." + point.getSignature().getName();
        String[] names = delCache.names();
        StringBuilder cacheString = new StringBuilder();
        boolean flag = false;
        Object proceed = null;
        try {
            proceed = point.proceed();
            flag = true;
            for (String name : names) {
                String cacheKey = getCacheKey(name, point.getArgs(), delCache.keyNum());
                cacheString.append(cacheKey).append(",");
                cacheService.deleteCache(cacheKey);
            }

        } finally {
            long endTime = System.currentTimeMillis();
            if (!flag) {
                logger.debug("delCache method:{}, error :{} , time:{}", methodName, cacheString.toString(), endTime - startTime);
            }
        }
        long endTime = System.currentTimeMillis();
        logger.debug("delCache success method:{}. :{} , time:{}", methodName, cacheString.toString(), endTime - startTime);

        return proceed;
    }


    @Around("@annotation(getCache)")
    public Object getCache(ProceedingJoinPoint point, GetCache getCache) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = point.getSignature().getDeclaringType().getName() + "." + point.getSignature().getName();
        String cacheKey = null;
        boolean flag = false;
        Object proceed = null;
        try {
            flag = true;
            String finalCacheKey = getCacheKey(getCache.name(), point.getArgs(), getCache.keyNum());
            cacheKey = finalCacheKey;
            proceed = cacheService.getCacheable(new CacheSource() {
                @Override
                public String getKey() {
                    return finalCacheKey;
                }

                @Override
                public Object getValue() throws Throwable {
                    return point.proceed();
                }
            }, getCache.expiry());
        } finally {
            long endTime = System.currentTimeMillis();
            if (!flag) {
                logger.debug("getCache method:{}, error :{} , time:{}", methodName, cacheKey, endTime - startTime);
            }
        }
        long endTime = System.currentTimeMillis();
        logger.debug("getCache success method:{}. :{} , time:{}", methodName, cacheKey, endTime - startTime);

        return proceed;
    }

    private String getCacheKey(String prefix, Object[] keys, int keyNum) {
        StringBuffer sb = new StringBuffer();
        sb.append(prefix).append(".");
        if (keyNum <= keys.length) {
            for (int i = 0; i < keyNum; ++i) {
                sb.append(keys[i]).append(".");
            }
        }

        return sb.toString();
    }

}
