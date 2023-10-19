package com.hlr.core.event;

/**
 * IThreadsPool
 * Description:
 * date: 2023/9/20 15:59
 *
 * @author hlr
 */
public interface IThreadsPool {

    // 方法开始
    void start();

    // 方法关闭
    void stop();

    // 方法立即关闭
    void stopNow(boolean flag);

}
