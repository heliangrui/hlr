package com.hlr.start.endpoint;

import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.hlr.start.HlrPreStopApplicationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.cloud.client.serviceregistry.Registration;

/**
 * SpringCloudGraceShutdownEndpoint
 * Description: springcloud 服务
 * date: 2023/12/6 14:02
 *
 * @author hlr
 */
@Endpoint(id = "graceshutdown")
public class SpringCloudGraceShutdownEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(GraceShutdownEndpoint.class);
    NacosServiceRegistry registry;
    Registration registration;
    HlrPreStopApplicationListener hlrPreStopApplicationListener;

    public SpringCloudGraceShutdownEndpoint(NacosServiceRegistry registry, Registration registration, HlrPreStopApplicationListener hlrPreStopApplicationListener) {
        this.registry = registry;
        this.registration = registration;
        this.hlrPreStopApplicationListener = hlrPreStopApplicationListener;
    }

    @ReadOperation
    public String endpoint() {
        registry.deregister(registration);
        hlrPreStopApplicationListener.endpoint();
        return "success";
    }
}
