package com.hlr.core.event.async;

/**
 * IAsyncExecerUnWaitOnFull
 * Description:
 * date: 2023/10/19 11:48
 *
 * @author hlr
 */
public interface IAsyncExecerUnWaitOnFull extends IAsyncExecer {

    // 未执行 才执行的方法
    void onFull(Object[] params) throws Exception;

}
