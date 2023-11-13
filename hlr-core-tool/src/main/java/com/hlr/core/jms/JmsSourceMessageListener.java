package com.hlr.core.jms;

/**
 * JmsStringMessageListener
 * Description: mqtt 使用
 * date: 2023/11/11 16:58
 *
 * @author hlr
 */
public interface JmsSourceMessageListener {
    void handleMessage(String topic, String jmsString);
}
