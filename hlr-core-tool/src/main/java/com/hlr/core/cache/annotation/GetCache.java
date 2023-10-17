package com.hlr.core.cache.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * GetCache
 * Description:
 * date: 2023/10/17 17:27
 *
 * @author hlr
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GetCache {
    String name();
    
    int keyNum() default 1;
    
    int expiry() default 2678400;
}
