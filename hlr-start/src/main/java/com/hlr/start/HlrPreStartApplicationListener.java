package com.hlr.start;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * HlrPreStartApplicationListener
 * Description:
 * date: 2023/12/7 17:22
 *
 * @author hlr
 */
@Order(-1)
public class HlrPreStartApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private AtomicBoolean processed = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        if (!processed.get()) {
            initMsaTag();
            initNacos();
            processed.compareAndSet(false, true);
        }
    }

    /**
     * 对dubbo 与服务注册进行标记
     */
    void initMsaTag() {
        if (System.getProperty("hlr.msa.tag") != null) {
            System.setProperty("dubbo.provider.tag", System.getProperty("hlr.msa.tag"));
            System.setProperty("spring.cloud.nacos.discovery.metadata.tag", System.getProperty("hlr.msa.tag"));
        }

    }

    public boolean isReady() {
        return processed.get();
    }

    /**
     * 禁止自动注册导致手动更新失效问题
     */
    void initNacos() {
        System.setProperty("spring.cloud.nacos.discovery.register-enabled", "false");
    }
}
