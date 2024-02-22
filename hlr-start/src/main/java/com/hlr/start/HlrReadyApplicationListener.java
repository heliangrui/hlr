package com.hlr.start;

import com.hlr.core.event.ThreadsPoolFactory;
import com.hlr.db.DBConnectionPools;
import com.hlr.start.feign.FeignWarmup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * HlrReadyApplicationListener
 * Description:
 * date: 2023/10/16 17:23
 *
 * @author hlr
 */
@Order(1)
public class HlrReadyApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(HlrReadyApplicationListener.class);

    private static AtomicBoolean ready = new AtomicBoolean(false);

    @Autowired(required = false)
    private ThreadsPoolFactory threadsPoolFactory;
    
    @Autowired(required = false)
    private FeignWarmup feignWarmup;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!ready.get()) {
            DBConnectionPools.getInstance();
            logger.info("[msa]-threadsPool start factory...");
            if (threadsPoolFactory != null) {
                threadsPoolFactory.start();
            } else {
                logger.info("[msa]-there is no threadsPool factory be configured");
            }
            ready.compareAndSet(false, true);
            logger.info("[msa]-threadsPool start success");
            
            if(feignWarmup !=null){
                feignWarmup.init();
            }
            
        }

    }
}
