package com.hlr.start;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.hlr.start.config.HlrConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

import java.util.Objects;

/**
 * HlrAutoRegisterApplicationLister
 * Description:
 * date: 2024/5/20 19:06
 *
 * @author hlr
 */
@Order(2)
public class HlrAutoRegisterApplicationLister implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(HlrAutoRegisterApplicationLister.class);

    @Autowired
    private NacosRegistration nacosRegistration;
    @Autowired
    private NacosAutoServiceRegistration nacosAutoServiceRegistration;

    private HlrConfigProperties hlrConfigProperties;

    public HlrAutoRegisterApplicationLister(HlrConfigProperties configProperties) {
        this.hlrConfigProperties = configProperties;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (hlrConfigProperties.isSpringCloudAutoRegister()) {
            String port = event.getApplicationContext().getEnvironment().getProperty("server.port");
            logger.debug("[ON ApplicationReadyEvent] nacos register on port:{} autoRegister:{}", port, this.nacosRegistration.getNacosDiscoveryProperties().isRegisterEnabled());
            this.nacosRegistration.getNacosDiscoveryProperties().setRegisterEnabled(true);
            this.nacosRegistration.setPort(Integer.parseInt(port));
            this.nacosRegistration.getMetadata().put("hlr.msa.timestamp", System.currentTimeMillis() + "");
            this.nacosAutoServiceRegistration.start();
        }
    }
}
