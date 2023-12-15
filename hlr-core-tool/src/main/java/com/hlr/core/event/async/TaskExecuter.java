package com.hlr.core.event.async;

import com.hlr.common.SpeedCounterUtil;
import com.hlr.core.event.AbstractLoopThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TaskExecuter
 * Description: 异步执行器实现类
 * date: 2023/10/19 11:49
 *
 * @author hlr
 */
public class TaskExecuter extends AbstractLoopThread implements ITaskExecuter {
    private Logger logger = LoggerFactory.getLogger(TaskExecuter.class);
    // 失败重试次数
    private int maxRetryTime = 3;
    // 任务队列
    private LinkedBlockingQueue<TaskItem> queue;
    // 队列大小
    private int maxSize = 0;
    // 队列满时是否溢出
    private boolean discardOnFull = false;
    // 队列已满未处理个数
    private AtomicInteger discardCounter = new AtomicInteger();
    private AtomicBoolean close = new AtomicBoolean(true);
    // 计数器
    private AtomicInteger counter = new AtomicInteger();

    // 记录级别  >0 loglevel*100个打印下日志
    private int logLevel = 0;

    // 记录执行速率
    private SpeedCounterUtil speedCounterUtil = new SpeedCounterUtil(1000);


    public TaskExecuter() {
        super("TaskExecuter");
    }

    public TaskExecuter(boolean needlog) {
        super("TaskExecuter", needlog);
    }

    public TaskExecuter(String name, boolean needlog) {
        super(name, needlog);
    }

    @Override
    public void start() {
        if (close.compareAndSet(true, false)) {
            if (this.maxSize == 0) {
                this.maxSize = Integer.MAX_VALUE;
            }
            this.queue = new LinkedBlockingQueue<>(this.maxSize);
            this.logger.debug("start task executer capacity:{}", this.maxSize);
            super.start();
        }

    }

    @Override
    public void work(int var1) {
        try {

            TaskItem item = queue.poll(1000L, TimeUnit.MILLISECONDS);
            if (item == null) {
                return;
            }
            speedCounterUtil.inc();
            long startTime = System.currentTimeMillis();
            try {
                // 异步方法执行
                item.getExecer().asyncExec((Object[]) item.getObj());
            } catch (Exception e) {
                if (item.getExecCount() < maxRetryTime) {
                    item.setExecCount(item.getExecCount() + 1);
                    queue.put(item);
                } else {
                    logger.error("{} {}", item.getExecer().getExecName(), item.getObj());
                }
            }
            counter.incrementAndGet();
            long endTime = System.currentTimeMillis();
            if (logLevel == 0 || (logLevel > 0 && counter.get() % (logLevel * 100) == 0)) {
                logger.debug("task executer {} totalSucc:{} discard:{} succ:{} tps:{} queue:{} {}ms {}ms", item.getExecer().getExecName(), this.counter.get(), this.discardCounter.get(), this.getBundle(), this.speedCounterUtil.tps(), this.queue.size(), endTime - startTime, endTime - item.getCreateTime());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void destory() {
        if (close.compareAndSet(false, true)) {
            this.logger.info("destory task executer...");

            while (this.queue.size() > 0) {
                this.logger.info("have {} task left, just wait for it to complete", this.queue.size());

                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var2) {
                }
            }

            this.logger.info("destory task executer ok");
        }
    }

    @Override
    public void add(IAsyncExecer exec, Object... obj) {
        if (!close.get()) {
            try {
                if (this.queue.size() >= this.maxSize && this.discardOnFull) {
                    this.logger.warn("task executer {} discarding on capacity:{}", exec.getExecName(), this.maxSize);
                    this.discardCounter.incrementAndGet();
                } else {
                    this.queue.put(new TaskItem(exec, obj));
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        } else {
            logger.info("taskExecuter add error,because already closed!");
        }
    }

    @Override
    public void add(IAsyncExecerUnWaitOnFull exec, Object... obj) {
        if (!close.get()) {
            try {
                if (!this.queue.offer(new TaskItem(exec, obj))) {
                    this.logger.warn("task executer {} full on capacity:{}", exec.getExecName(), this.maxSize);
                    this.discardCounter.incrementAndGet();
                    exec.onFull(obj);
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        } else {
            logger.info("taskExecuter add error,because already closed!");
        }

    }

    @Override
    public int getTaskSize() {
        return queue.size();
    }

    @Override
    public int getCounter() {
        return counter.get();
    }

    @Override
    public void setMaxRetryTime(int maxRetryTime) {
        this.maxRetryTime = maxRetryTime;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public boolean isDiscardOnFull() {
        return discardOnFull;
    }

    public void setDiscardOnFull(boolean discardOnFull) {
        this.discardOnFull = discardOnFull;
    }
}
