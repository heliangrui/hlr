package com.hlr.start;

import com.hlr.start.web.WebTraceInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfiguration
 * Description:
 * date: 2023/10/20 10:35
 *
 * @author hlr
 */

@Configuration
@ConditionalOnWebApplication
public class WebMvcConfiguration implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebTraceInterceptor()).addPathPatterns("/**");
    }
}
