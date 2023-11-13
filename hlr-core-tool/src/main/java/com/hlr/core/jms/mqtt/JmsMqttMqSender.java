package com.hlr.core.jms.mqtt;

import com.hlr.core.event.IThreadsPool;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

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

    private ApplicationContext applicationContext;
    
    private JmsMqttMqReceiver jmsMqttMqReceiver;
    private AtomicBoolean closed = new AtomicBoolean(true);
    
    @Override
    public void start() {
        try {
            mqttClient = new MqttClient(mqttAddrs,clientId,new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            
            if(getUserName() != null){
                options.setUserName(getUserName());
            }
            if(getPassWord()!=null){
                options.setPassword(getPassWord().toCharArray());
            }
            options.setCleanSession(isCleanSession());
            options.setConnectionTimeout(getConnectionTimeout());
            options.setKeepAliveInterval(getKeepAliveInterval());
            JmsMqttMqReceiver bean = applicationContext.getBean(JmsMqttMqReceiver.class);
            mqttClient.setCallback(bean);
            mqttClient.subscribe((String[]) bean.getTopicListener().keySet().toArray());
            closed.compareAndSet(true,false);
            mqttClient.connect(options);
            
        } catch (MqttException e) {
            throw new RuntimeException("mqtt connect error ...");
        }


    }

    @Override
    public void stop() {

        if(closed.compareAndSet(false,true)){
            if (mqttClient != null){
                try {
                    mqttClient.disconnect();
                } catch (MqttException e) {
                    throw new RuntimeException("mqtt disconnect error ... ",e);
                }
            }
        }
        
    }

    @Override
    public void stopNow(boolean flag) {
        stop();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMqttAddrs() {
        return mqttAddrs;
    }

    public void setMqttAddrs(String mqttAddrs) {
        this.mqttAddrs = mqttAddrs;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getKeepAliveInterval() {
        return KeepAliveInterval;
    }

    public void setKeepAliveInterval(int keepAliveInterval) {
        KeepAliveInterval = keepAliveInterval;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getConnectionTimeout() {
        return ConnectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        ConnectionTimeout = connectionTimeout;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

    public void setMqttClient(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }
}
