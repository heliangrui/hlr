package com.hlr.core.jms.mqtt;

import com.hlr.core.event.IThreadsPool;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JmsMqttMqSender
 * Description:
 * date: 2023/11/11 16:52
 *
 * @author hlr
 */
public class JmsMqttMqSender implements IThreadsPool {
    private static final Logger logger = LoggerFactory.getLogger(JmsMqttMqSender.class);

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
    private AtomicBoolean closed = new AtomicBoolean(true);
    MqttMessage mqttMessage = null;
    private JmsMqttMqReceiver jmsMqttMqReceiver = null;
    private MqttConnectOptions options = null;

    @Override
    public void start() {
        try {
            logger.info("JmsMqttMqSender connect start ...");
            mqttClient = new MqttClient(mqttAddrs, clientId, new MemoryPersistence());
            options = new MqttConnectOptions();

            if (getUserName() != null) {
                options.setUserName(getUserName());
            }
            if (getPassWord() != null) {
                options.setPassword(getPassWord().toCharArray());
            }
            // 设置自动重连  
            options.setAutomaticReconnect(true);
            options.setCleanSession(isCleanSession());
            options.setConnectionTimeout(getConnectionTimeout());
            options.setKeepAliveInterval(getKeepAliveInterval());
            // 回调类获取
            jmsMqttMqReceiver = applicationContext.getBean(JmsMqttMqReceiver.class);
            // 连接
            reConnection();
        } catch (MqttException e) {
            throw new RuntimeException("JmsMqttMqSender connect error ...");
        }
    }

    /**
     *
     */
    protected void reConnection() {
        closed.set(true);
        while (!connection()) {
            logger.info("JmsMqttMqSender connect wait ...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean connection() {
        try {
            mqttClient.setCallback(jmsMqttMqReceiver);
            mqttClient.connect(options);
            mqttClient.subscribe(jmsMqttMqReceiver.getTopicListener().keySet().toArray(new String[0]));
            logger.info("JmsMqttMqSender connect ok");
            closed.compareAndSet(true, false);
            return true;
        } catch (Exception e) {
            boolean connected = mqttClient.isConnected();
            if (connected) {
                closed.compareAndSet(true, false);
            } else {
                closed.set(true);
            }
            return connected;
        }
    }

    public boolean send(String topic, String message) {
        if (!closed.get()) {
            mqttMessage = new MqttMessage();
            mqttMessage.setQos(qos);
            mqttMessage.setPayload(message.getBytes());
            mqttMessage.setRetained(false);
            try {
                mqttClient.publish(topic, mqttMessage);
                return true;
            } catch (MqttException e) {
                logger.error("JmsMqttMqSender send error:{},{}", topic, message, e);
            }
        }
        return false;
    }

    @Override
    public void stop() {
        logger.info("JmsMqttMqSender stop ...");
        if (closed.compareAndSet(false, true)) {
            if (mqttClient != null) {
                try {
                    mqttClient.disconnect();
                    logger.info("JmsMqttMqSender stop ok.");
                } catch (MqttException e) {
                    logger.error("JmsMqttMqSender disconnect error ... ", e);
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
