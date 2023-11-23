package com.hlr.start;

import com.hlr.start.feign.FeignErrorDecoder;
import com.hlr.start.feign.FeignInterceptor;
import com.hlr.start.feign.FeignResponseAspect;
import com.hlr.start.feign.FeignWarmup;
import feign.Feign;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FeignAutoConfiguration
 * Description:
 * date: 2023/11/23 16:17
 *
 * @author hlr
 */
@Configuration
@ConditionalOnClass({Feign.class})
public class FeignAutoConfiguration {
    
    @Bean
    @ConditionalOnClass({RequestInterceptor.class})
    public FeignInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }

    @Bean
    @ConditionalOnClass({ErrorDecoder.class})
    public FeignErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    @ConditionalOnClass({RequestInterceptor.class})
    public FeignResponseAspect feignResponseAspect() {
        return new FeignResponseAspect();
    }

    @Bean
    public FeignWarmup feignWarmUper() {
        return new FeignWarmup();
    }


}
