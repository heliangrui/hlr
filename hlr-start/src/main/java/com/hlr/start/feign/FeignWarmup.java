package com.hlr.start.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * FeignWarmup
 * Description: feign 接口检测
 * date: 2023/11/23 15:45
 *
 * @author hlr
 */
public class FeignWarmup {
    private static final Logger logger = LoggerFactory.getLogger(FeignWarmup.class);
    @Autowired
    private ApplicationContext applicationContext;

    public FeignWarmup() {
    }

    public void init() {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(FeignClient.class);
        if (beans.size() != 0) {
            Iterator var2 = beans.entrySet().iterator();

            while(var2.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)var2.next();
                Class<?> clazz = entry.getValue().getClass();
                try {
                    Method method = clazz.getMethod("health");
                    logger.info("init feign: {} return: {}", entry.getKey(), method.invoke(entry.getValue()));
                } catch (NoSuchMethodException var6) {
                    logger.warn("init feign client fail: no method of health in " + (String)entry.getKey());
                } catch (Exception var7) {
                    logger.warn("init feign: {} return exception", entry.getKey());
                }
            }
            logger.info("init feign client done!");
        }
    }
    
}
