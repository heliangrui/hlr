package com.hlr.start.config;

import com.hlr.core.jms.mqtt.JmsMqttMqCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MqttConfigProperties
 * Description:
 * date: 2023/11/11 15:27
 *
 * @author hlr
 */
@ConfigurationProperties(prefix = "hlr.mqtt")
public class MqttConfigProperties {

    // 客户端标识
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

    public static void main(String[] args) throws MqttException {

        MqttClient mqttClient = null;
        try {
            mqttClient = new MqttClient("","");
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        mqttClient.setCallback(new JmsMqttMqCallback());
        MqttConnectOptions options = new MqttConnectOptions();
        
        String topic = "3331111/1231232879/event";
        String modelTopic = "333+/event";

        modelTopic = modelTopic.replace("+", ".*").replace("#", ".*");

        // 使用正则表达式匹配消息主题是否满足模式  
        Pattern compile = Pattern.compile(modelTopic);
        Matcher matcher = compile.matcher(topic);
        System.out.println(matcher.matches());

    }
    
}
