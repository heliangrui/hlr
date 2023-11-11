package com.hlr.core.jms.mqtt;

import com.hlr.core.event.IThreadsPool;
import com.hlr.core.jms.IJmsReceiver;
import com.hlr.core.jms.JmsSourceMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * JmsMqttMqReceiver
 * Description:
 * date: 2023/11/11 16:54
 *
 * @author hlr
 */
public class JmsMqttMqReceiver  implements IThreadsPool, IJmsReceiver , MqttCallback {
    
    private Map<String,JmsSourceMessageListener> topicListener = null;
    
    private JmsMqttMessageOrderlyExecuter executer;
    
    @Override
    public void start() {
        
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void stopNow(boolean flag) {

    }

    @Override
    public void connectionLost(Throwable throwable) {
        
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
