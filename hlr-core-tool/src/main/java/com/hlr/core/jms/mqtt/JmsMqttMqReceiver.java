package com.hlr.core.jms.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.hlr.core.event.IThreadsPool;
import com.hlr.core.jms.IJmsReceiver;
import com.hlr.core.jms.JmsSourceMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JmsMqttMqReceiver
 * Description:
 * date: 2023/11/11 16:54
 *
 * @author hlr
 */
public class JmsMqttMqReceiver implements IThreadsPool, IJmsReceiver, MqttCallback {
    private static final Logger logger = LoggerFactory.getLogger(JmsMqttMqReceiver.class);

    private Map<String, JmsSourceMessageListener> topicListener = null;

    private JmsMqttMessageOrderlyExecuter executer;

    private AtomicBoolean closed = new AtomicBoolean(true);
    private ApplicationContext applicationContext;
    private int threadSize;


    @Override
    public void start() {
        logger.info("JmsMqttMqReceiver start ...");
        // 解析topic  * #
        Map<String, JmsSourceMessageListener> listener = new HashMap<>();
        for (String topic : topicListener.keySet()) {
            JmsSourceMessageListener jmsSourceMessageListener = topicListener.get(topic);
            topic = topic.replace("+", ".*").replace("#", ".*");
            listener.put(topic, jmsSourceMessageListener);
        }
        // 初始化 消息消费实例
        executer = new JmsMqttMessageOrderlyExecuter(listener);
        executer.setThreadPoolSize(threadSize);
        // 默认是 true 关闭状态 开启 false
        closed.compareAndSet(true, false);
        // 消费实例启动
        executer.start();
        logger.info("JmsMqttMqReceiver start ok.");

    }

    @Override
    public void shutdown() {
        logger.info("JmsMqttMqReceiver stop ...");
        // close  设为 true 关闭状态
        if (closed.compareAndSet(false, true)) {
            if (executer != null) {
                // 关闭线程
                executer.stop();
                logger.info("JmsMqttMqReceiver stop ok.");
            }
        }

    }

    @Override
    public void stop() {
        shutdown();
    }

    @Override
    public void stopNow(boolean flag) {
        shutdown();
    }

    @Override
    public void connectionLost(Throwable throwable) {
        // 失败重连
        logger.info("mqtt connection error restart...");
        JmsMqttMqSender bean = applicationContext.getBean(JmsMqttMqSender.class);
        bean.reConnection();

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        // 消息接受
        if (!closed.get()) {
            executer.add(new JmsMqttMessage(s, mqttMessage));
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // 消息发送成功
        logger.debug("Jms mqtt send message success:{}", JSONObject.toJSONString(iMqttDeliveryToken));
    }

    public int getThreadSize() {
        return threadSize;
    }

    public void setThreadSize(int threadSize) {
        this.threadSize = threadSize;
    }

    public Map<String, JmsSourceMessageListener> getTopicListener() {
        return topicListener;
    }

    public void setTopicListener(Map<String, JmsSourceMessageListener> topicListener) {
        this.topicListener = topicListener;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
