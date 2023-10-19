package com.hlr.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * SpeedCounter
 * Description: 用户计算速率
 * date: 2023/10/19 10:57
 *
 * @author hlr
 */
public class SpeedCounterUtil {

    private static final Logger logger = LoggerFactory.getLogger(SpeedCounterUtil.class);
    private static NumberFormat nf = NumberFormat.getInstance();
    // 队列 记录时间后追加
    private ArrayBlockingQueue<Long> queue;
    // 当前 处理时间
    private long footer;
    // 队列大小
    private int point = 0;
    // 线程，是否开启 用于打印speed tps信息
    private Thread thread;

    /**
     * 初始化
     *
     * @param point
     */
    public SpeedCounterUtil(int point) {
        this.point = point;
        this.queue = new ArrayBlockingQueue<>(point);
    }

    /**
     * 初始化 并开启线程记录
     *
     * @param point       队列大小
     * @param counterName 线程名称
     * @param monitorSec  秒 多长时间纪律一次
     */
    public SpeedCounterUtil(int point, String counterName, final int monitorSec) {
        this.queue = new ArrayBlockingQueue(point);
        this.point = point;
        if (monitorSec != 0) {
            this.thread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep((long) (monitorSec * 1000));
                        } catch (InterruptedException var2) {
                            return;
                        }

                        logger.debug("speedCounter speed:{} tps:{}", speed(), tpsStr());
                    }
                }
            });
            if (counterName == null) {
                counterName = "SpeedCounter";
            }

            this.thread.setName(counterName);
            this.thread.setDaemon(true);
            this.thread.start();
        }
    }

    public static void main(String[] args) {

        SpeedCounterUtil c = new SpeedCounterUtil(1000, "aa", 1);

        while (true) {
            c.inc();

            try {
                Thread.sleep(300L);
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }
        }
    }

    public void inc() {
        this.footer = System.currentTimeMillis();
        if (this.queue.size() >= this.point) {
            this.queue.poll();
        }

        this.queue.offer(this.footer);
    }

    public String speed() {
        double time = this.check();
        return time < 1.0 ? time * 1000.0 + "ns" : time + "ms";
    }

    /**
     * 根据队列 当前时间- 头部开始时间 / 队列长度
     * 每 毫秒 队列增长个数
     *
     * @return
     */
    public double check() {
        Long header = (Long) this.queue.peek();
        if (header == null) {
            header = (long) DateUtil.getNowIntTime();
        }

        return DoubleUtil.div((double) (this.footer - header), (double) this.queue.size(), 4);
    }

    /**
     * 每秒处理大小
     *
     * @return
     */
    public double tps() {
        return DoubleUtil.div(1000.0, this.check(), 1);
    }

    public String tpsStr() {
        return nf != null ? nf.format(this.tps()) : "";
    }

}
