package com.hlr.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * AbstractLoopThread
 * Description:
 * date: 2023/9/20 16:01
 *
 * @author hlr
 */
public abstract class AbstractLoopThread implements IThreadsPool{
    
    private static final Logger logger = LoggerFactory.getLogger(AbstractLoopThread.class);
    // 固定线程池大小
    private int threadpoolsize = 1;
    // 固定线程池名称
    private String name;
    // 固定线程池
    private ExecutorService[] threadPools;
    // 是否关闭
    protected boolean closing = false;
    // 是否日志
    private boolean needlog = true;
    private ThreadLocal<Object> bundles = new ThreadLocal();

    public AbstractLoopThread(String name) {
        this.name = name;
    }

    public AbstractLoopThread(String name, boolean needlog) {
        this.name = name;
        this.needlog = needlog;
    }

    // 线程创建与运行
    public void start() {
        this.threadPools = new ExecutorService[this.threadpoolsize];

        for(int i = 0; i < this.threadpoolsize; ++i) {
            int finalI = i;
            this.threadPools[i] = Executors.newFixedThreadPool(1, new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    return new Thread(r, AbstractLoopThread.this.name + "-" + finalI);
                }
            });
            this.doWork(this.threadPools[i], i);
            logger.debug("started {} >>>>>", this.name + "-" + i);
        }

    }

    public void stop() {
        this.stopnow(false);
    }

    public void stopnow(boolean discart) {
        this.closing = true;
        if (!discart) {
            this.destory();
        }


        for(int i = 0; i < this.threadpoolsize; ++i) {
            this.threadPools[i].shutdown();
            logger.debug("stoping. {} <<<<<", this.name + "-" + i);
        }

        try {
            for(int i = 0; i < this.threadpoolsize; ++i) {
                if (this.threadPools[i].awaitTermination(3L, TimeUnit.SECONDS)) {
                    logger.debug("grase stoped. {} <<<<<", this.name + "-" + i);
                } else {
                    this.threadPools[i].shutdownNow();
                    if (this.threadPools[i].awaitTermination(3L, TimeUnit.SECONDS)) {
                        logger.debug("force stoped. {} <<<<<", this.name + "-" + i);
                    } else {
                        logger.error("error can't stop {} <<<<<", this.name + "-" + i);
                    }
                }
            }
        } catch (InterruptedException var4) {
            logger.error("", var4);

            for(int i = 0; i < this.threadpoolsize; ++i) {
                this.threadPools[i].shutdownNow();
            }

            Thread.currentThread().interrupt();
        }

    }

    public void doWork(final ExecutorService exec, final int order) {
        exec.submit(new Runnable() {
            public void run() {
                AbstractLoopThread.this.work(exec, order);
            }
        });
    }

    public void work(ExecutorService exec, int order) {
        try {
            long t1 = System.currentTimeMillis();
            this.work(order);
            long t2 = System.currentTimeMillis();
            if (this.needlog) {
                logger.debug("{} loop work time:{}", this.getName(), t2 - t1);
            }
        } catch (Exception var15) {
            logger.error("", var15);
        } finally {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException var14) {
                logger.error("", var14);
                Thread.currentThread().interrupt();
            }

            this.doWork(exec, order);
        }

    }

    public abstract void work(int var1);

    public abstract void destory();

    public Object getBundle() {
        return this.bundles.get();
    }

    public void setBundle(Object value) {
        this.bundles.set(value);
    }

    public int getThreadpoolsize() {
        return this.threadpoolsize;
    }

    public void setThreadpoolsize(int threadpoolsize) {
        this.threadpoolsize = threadpoolsize;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
