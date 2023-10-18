package com.hlr.core.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DelCache
 * Description:
 * date: 2023/10/17 17:30
 *
 * @author hlr
 */
@Inherited()
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelCache {
    String[] names();
    
    int keyNum() default 1;
    
}
