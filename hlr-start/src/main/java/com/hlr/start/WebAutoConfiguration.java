package com.hlr.start;

import com.hlr.start.config.HlrConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * WebAutoConfiguration
 * Description:
 * date: 2023/10/20 12:13
 *
 * @author hlr
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ResponseBodyAdvice.class})
@EnableConfigurationProperties({HlrConfigProperties.class})
public class WebAutoConfiguration {

    @Bean
    GlobalWebExceptionHandler defaultGlobalWebExceptionHandler(HlrConfigProperties hlrConfigProperties) {
        return new GlobalWebExceptionHandler(hlrConfigProperties);
    }

}
