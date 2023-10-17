package com.hlr.core.cache.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * DelCache
 * Description:
 * date: 2023/10/17 17:30
 *
 * @author hlr
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DelCache {
    String[] names();
    
    int keyNum() default 1;
    
}
