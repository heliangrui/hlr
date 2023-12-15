package com.hlr.start;

import com.hlr.core.event.ThreadsPoolFactory;
import com.hlr.db.DBConnectionPools;
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

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!ready.get()) {
            DBConnectionPools.getInstance();
            logger.info("start threadspool factory...");
            if (threadsPoolFactory != null) {
                threadsPoolFactory.start();
            } else {
                logger.info("threr is no threadspool factory be configured");
            }
            ready.compareAndSet(false, true);
            logger.info("start threadspool success");
        }

    }
}
