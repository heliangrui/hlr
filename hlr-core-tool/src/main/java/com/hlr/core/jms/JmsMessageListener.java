package com.hlr.core.jms;

/**
 * JmsMessageListener
 * Description:
 * date: 2023/11/6 16:38
 *
 * @author hlr
 */
public interface JmsMessageListener {
    void handleMessage(JmsObject var1);
}
