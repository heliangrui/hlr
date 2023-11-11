package com.hlr.core.jms.mqtt;

import com.hlr.core.event.IThreadsPool;
import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * JmsMqttMqSender
 * Description:
 * date: 2023/11/11 16:52
 *
 * @author hlr
 */
public class JmsMqttMqSender implements IThreadsPool {

    private String clientId;
    // 客户端地址
    private String mqttAddrs;
    private String userName;
    private String passWord;
    // 保持活动间隔时间
    private int KeepAliveInterval = 20;
    // 设置清除会话标志
    private boolean cleanSession = false;
    //设置MQTT协议版本
    private int version = 0;
    // 设置连接超时时间
    private int timeout = 20;
    // 设置连接超时时间常数
    private int ConnectionTimeout = 2000;

    private int qos = 0;
    
    private MqttClient mqttClient;
    
    private 
    
    @Override
    public void start() {
        
    }

    @Override
    public void stop() {

    }

    @Override
    public void stopNow(boolean flag) {

    }
}
