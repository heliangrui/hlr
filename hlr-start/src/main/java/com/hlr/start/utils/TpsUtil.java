package com.hlr.start.utils;

import com.hlr.common.SpeedCounterUtil;

import java.util.concurrent.atomic.AtomicLong;

/**
 * TpsUtil
 * Description:
 * date: 2023/10/20 10:08
 *
 * @author hlr
 */
public class TpsUtil {

    private static final SpeedCounterUtil speed = new SpeedCounterUtil(1000, "tpsCounter", 0);
    private static AtomicLong requestCounter = new AtomicLong();

    public static int getTps() {
        return (int) speed.tps();
    }

    public static long getRequest() {
        return requestCounter.get();
    }

    public static void inc() {
        speed.inc();
        requestCounter.incrementAndGet();
    }

}
