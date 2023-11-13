package com.hlr.core.jms.kafka;

import com.hlr.core.event.IThreadsPool;
import com.hlr.core.jms.IJmsReceiver;
import com.hlr.core.jms.JmsObjectMessageListener;
import com.hlr.core.jms.JmsObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JmsKafkaMQReceiver
 * Description:
 * date: 2023/11/6 16:15
 *
 * @author hlr
 */
public class JmsKafkaMQReceiver implements IThreadsPool, IJmsReceiver {

    private static final Logger logger = LoggerFactory.getLogger(JmsKafkaMQReceiver.class);
    // 存放注解配置 和 监听消息对象
    Map<String, JmsObjectMessageListener> listeners = new HashMap();
    Map<String, Class<JmsObject>> topicTypes;
    Map<String, JmsObjectMessageListener> topicListener;
    // kafka 客户端
    private KafkaConsumer consumer = null;
    // 地址
    private String kafkaAddrs;
    // 客户端标识
    private String clientId = null;
    private String consumerGroup = null;
    private int cousumeThreadMin = 16;
    // 消费者参数 批量拉取字节数
    private int fetchMinBytes = 500;
    //一次最多拉取记录数
    private int maxPollRecords = 500;
    // 批量拉取等待时间
    private int pollTimeout = 1000;
    private AtomicBoolean closed = new AtomicBoolean(false);
    // 接受消息线程
    private ExecutorService pollThread;
    // 线程执行方法
    private JmsKafkaMessageOrderlyExecuter executer;

    @Override
    public void start() {
        if (listeners == null || listeners.size() == 0) {
            throw new RuntimeException("must need kafka topics");
        }
        if (clientId == null) {
            throw new RuntimeException("must need kafka clientId");
        }
        if (consumerGroup == null) {
            consumerGroup = clientId;
        }
        // kafka 初始化参数
        Properties props = new Properties();
        props.put("bootstrap.servers", this.kafkaAddrs);
        props.put("key.deserializer", org.apache.kafka.common.serialization.StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("group.id", this.consumerGroup);
        props.put("enable.auto.commit", "false");
        props.put("client.id", this.clientId);
        props.put("fetch.min.bytes", this.fetchMinBytes);
        props.put("max.poll.records", this.maxPollRecords);
        props.put("auto.offset.reset", "latest");
        // kafka 客户端
        this.consumer = new KafkaConsumer(props);
        // 初始化接受消息线程池
        pollThread = Executors.newFixedThreadPool(1);

        try {
            topicTypes = new HashMap<>();
            topicListener = new HashMap<>();
            for (String s : listeners.keySet()) {
                String[] split = s.split("@");
                topicListener.put(split[0], listeners.get(s));
                topicTypes.put(split[0], (Class<JmsObject>) Class.forName(split[1]));
            }
            // 设置监听消息
            consumer.subscribe(topicListener.keySet());
            executer = new JmsKafkaMessageOrderlyExecuter(topicListener, topicTypes);
            // 设置线程池大小
            executer.setThreadPoolSize(cousumeThreadMin);
            // 初始化
            executer.start();
            pollThread.submit(() -> {
                while (!closed.get()) {
                    ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofMillis((long) this.pollTimeout));
                    if (records.count() > 0) {
                        process(records);

                        try {
                            this.consumer.commitAsync();
                        } catch (Exception var3) {
                            logger.error("commit failed", var3);
                        }
                    }

                }

            });
            logger.info("kafkamq consumer clientId:{} consumerGroup:{} subscription:{} started", this.clientId, this.consumerGroup, topicListener.keySet());
        } catch (Exception e) {

        }
    }

    private void process(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            executer.add(record.partition(), record);
        }

    }

    @Override
    public void shutdown() {
        if (closed.compareAndSet(false, true)) {
            logger.info("kafkamq consumer shutdown");
            // 关闭接受消息线程
            pollThread.shutdownNow();
            // 关闭执行消息线程
            executer.stop();
            try {
                consumer.commitAsync();
            } finally {
                consumer.close();
            }
            logger.info("kafkamq consumer shutdown success");
        }

    }

    @Override
    public void stop() {
        this.shutdown();
    }

    @Override
    public void stopNow(boolean flag) {
        this.shutdown();
    }

    public KafkaConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(KafkaConsumer consumer) {
        this.consumer = consumer;
    }

    public String getKafkaAddrs() {
        return kafkaAddrs;
    }

    public void setKafkaAddrs(String kafkaAddrs) {
        this.kafkaAddrs = kafkaAddrs;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public int getCousumeThreadMin() {
        return cousumeThreadMin;
    }

    public void setCousumeThreadMin(int cousumeThreadMin) {
        this.cousumeThreadMin = cousumeThreadMin;
    }

    public int getFetchMinBytes() {
        return fetchMinBytes;
    }

    public void setFetchMinBytes(int fetchMinBytes) {
        this.fetchMinBytes = fetchMinBytes;
    }

    public int getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(int maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public int getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(int pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public Map<String, JmsObjectMessageListener> getListeners() {
        return listeners;
    }

    public void setListeners(Map<String, JmsObjectMessageListener> listeners) {
        this.listeners = listeners;
    }
}
