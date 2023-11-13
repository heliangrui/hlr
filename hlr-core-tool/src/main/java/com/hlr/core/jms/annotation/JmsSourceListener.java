package com.hlr.core.jms.annotation;

import com.hlr.core.config.EnumConfig;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JmsSourceListener
 * Description:
 * date: 2023/11/13 13:47
 *
 * @author hlr
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface JmsSourceListener {

    String topic() default "";
    EnumConfig.JmsSourceType jmsType() default EnumConfig.JmsSourceType.JMS_MQTT;
}
