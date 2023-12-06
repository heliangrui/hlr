package com.hlr.start;

import com.hlr.core.event.ThreadsPoolFactory;
import com.hlr.db.DBConnectionPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * HlrPreStopApplicationListener
 * Description:
 * date: 2023/12/6 12:17
 *
 * @author hlr
 */
@Order(1)
public class HlrPreStopApplicationListener implements ApplicationListener<ContextClosedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(HlrPreStopApplicationListener.class);

    private static AtomicBoolean close = new AtomicBoolean(false);

    @Autowired(required = false)
    private ThreadsPoolFactory threadsPoolFactory;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        endpoint();
        DBConnectionPools.destroy();
    }

    public void endpoint() {
        if (!close.get()) {
            logger.info("preStop HlrPreStopApplicationListener .....");
            close.compareAndSet(false, true);
            if (threadsPoolFactory != null) {
                threadsPoolFactory.stop();
            }
            logger.info("preStop HlrPreStopApplicationListener end ");
        }
    }


}
