package com.hlr.core.jms.annotation;

import com.hlr.core.config.EnumConfig;
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
public @interface JmsObjectListener {
    
    String topic() default "";
    Class<?> jmsObject();

    EnumConfig.JmsObjectType jmsType() default EnumConfig.JmsObjectType.JMS_KAFKA;
    
}
