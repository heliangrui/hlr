package com.hlr.core.jms.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * JmsMqttMessage
 * Description:
 * date: 2023/11/11 17:36
 *
 * @author hlr
 */
public class JmsMqttMessage {
    private String topic;
    private MqttMessage mqttMessage;

    public JmsMqttMessage(String topic, MqttMessage mqttMessage) {
        this.topic = topic;
        this.mqttMessage = mqttMessage;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public MqttMessage getMqttMessage() {
        return mqttMessage;
    }

    public void setMqttMessage(MqttMessage mqttMessage) {
        this.mqttMessage = mqttMessage;
    }
}
