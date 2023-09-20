package com.hlr.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ThreadsPoolFactory
 * Description: 线程启动工厂
 * date: 2023/9/20 16:44
 *
 * @author hlr
 */
public class ThreadsPoolFactory implements IThreadsPool{
    private static final Logger logger = LoggerFactory.getLogger(ThreadsPoolFactory.class);
    // 线程池
    private List<IThreadsPool> pools = new ArrayList();
    private AtomicBoolean processed = new AtomicBoolean(false);

    public ThreadsPoolFactory() {
    }

    public void start() {
        if (!this.processed.get()) {
            logger.debug("start threadspools {}", this.pools.size());
            Iterator var1 = this.pools.iterator();

            while(var1.hasNext()) {
                IThreadsPool pool = (IThreadsPool)var1.next();
                logger.debug("threadspool {} starting...", pool);
                pool.start();
                logger.debug("threadspool {} started.", pool);
            }

            logger.debug("start threadspools ok");
            this.processed.compareAndSet(false, true);
        }

    }

    public void stop() {
        if (this.processed.get()) {
            logger.debug("stop threadspools {}", this.pools.size());

            for(int i = this.pools.size() - 1; i >= 0; --i) {
                IThreadsPool pool = (IThreadsPool)this.pools.get(i);
                logger.debug("threadspool {} stoping...", pool);
                pool.stop();
                logger.debug("threadspool {} stoped.", pool);
            }

            logger.debug("stop threadspools over");
            this.processed.compareAndSet(true, false);
        }

    }

    public void stopNow(boolean discart) {
        if (this.processed.get()) {
            logger.debug("stop threadspools {}", this.pools.size());

            for(int i = this.pools.size() - 1; i >= 0; --i) {
                IThreadsPool pool = (IThreadsPool)this.pools.get(i);
                logger.debug("threadspool {} stoping...", pool);
                pool.stopNow(discart);
                logger.debug("threadspool {} stoped.", pool);
            }

            logger.debug("stop threadspools over");
            this.processed.compareAndSet(true, false);
        }

    }

    public void addPool(IThreadsPool threadsPool) {
        this.pools.add(threadsPool);
    }

    public List<IThreadsPool> getPools() {
        return this.pools;
    }

    public void setPools(List<IThreadsPool> pools) {
        this.pools = pools;
    }
}
