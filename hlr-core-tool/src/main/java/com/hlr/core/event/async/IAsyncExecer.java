package com.hlr.core.event.async;

/**
 * IAsyncExecer
 * Description:actuator
 * date: 2023/10/19 11:42
 *
 * @author hlr
 */
public interface IAsyncExecer {

    void asyncExec(Object[] params) throws Exception;

    String getExecName();
}
