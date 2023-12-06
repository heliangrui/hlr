package com.hlr.start;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.hlr.start.endpoint.DubboGraceShutdownEndpoint;
import com.hlr.start.endpoint.GraceShutdownEndpoint;
import com.hlr.start.endpoint.SpringCloudGraceShutdownEndpoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Bean;

/**
 * HlrEndpointAutoConfiguration
 * Description:
 * date: 2023/12/6 13:46
 *
 * @author hlr
 */
@ConditionalOnClass({Endpoint.class, NacosServiceRegistry.class})
public class HlrEndpointAutoConfiguration {

    @Bean
    @ConditionalOnNacosDiscoveryEnabled
    @ConditionalOnMissingBean
    @ConditionalOnAvailableEndpoint
    @ConditionalOnProperty(value = {"spring.cloud.service-registry.auto-registration.enabled", "fhd.spring-cloud-auto-register"}, havingValue = "true", matchIfMissing = true)
    public SpringCloudGraceShutdownEndpoint springCloudGraceShutdownPoint(NacosServiceRegistry registry, Registration registration, HlrPreStopApplicationListener hlrPreStopApplicationListener) {
        return new SpringCloudGraceShutdownEndpoint(registry, registration, hlrPreStopApplicationListener);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnAvailableEndpoint
    @ConditionalOnProperty(value = {"dubbo.scan.base-packages"}, matchIfMissing = false)
    public DubboGraceShutdownEndpoint dubboGraceShutdownPoint(HlrPreStopApplicationListener hlrPreStopApplicationListener) {
        return new DubboGraceShutdownEndpoint(hlrPreStopApplicationListener);
    }

    @Bean
    @ConditionalOnAvailableEndpoint
    @ConditionalOnMissingBean({DubboGraceShutdownEndpoint.class, SpringCloudGraceShutdownEndpoint.class})
    public GraceShutdownEndpoint normalGraceShutdownPoint(HlrPreStopApplicationListener hlrPreStopApplicationListener) {
        return new GraceShutdownEndpoint(hlrPreStopApplicationListener);
    }

}
