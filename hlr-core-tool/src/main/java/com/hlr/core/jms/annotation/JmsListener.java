package com.hlr.core.jms.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JmsListener
 * Description: 消息注解
 * date: 2023/11/6 15:54
 *
 * @author hlr
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface JmsListener {
    
    String topic() default "";
    Class<?> jmsObject();
    
}
