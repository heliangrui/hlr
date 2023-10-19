package com.hlr.core.event.async;

/**
 * ITaskExecuter
 * Description:
 * date: 2023/10/19 11:46
 *
 * @author hlr
 */
public interface ITaskExecuter {

    void setThreadPoolSize(int size);

    void start();

    void stop();

    void add(IAsyncExecer var1, Object... var2);

    void add(IAsyncExecerUnWaitOnFull var1, Object... var2);

    int getTaskSize();

    int getCounter();

    void setMaxRetryTime(int var1);

}
