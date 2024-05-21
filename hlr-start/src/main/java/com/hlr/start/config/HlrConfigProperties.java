package com.hlr.start.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * HlrConfigProperties
 * Description:
 * date: 2023/10/20 10:57
 *
 * @author hlr
 */
@ConfigurationProperties(prefix = "hlr")
public class HlrConfigProperties {

    private boolean springCloudAutoRegister = true;
    private boolean responseAutoBoxing = true;

    public boolean isSpringCloudAutoRegister() {
        return springCloudAutoRegister;
    }

    public void setSpringCloudAutoRegister(boolean springCloudAutoRegister) {
        this.springCloudAutoRegister = springCloudAutoRegister;
    }

    public boolean isResponseAutoBoxing() {
        return responseAutoBoxing;
    }

    public void setResponseAutoBoxing(boolean responseAutoBoxing) {
        this.responseAutoBoxing = responseAutoBoxing;
    }
}
