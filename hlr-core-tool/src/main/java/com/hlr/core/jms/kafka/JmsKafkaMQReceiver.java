package com.hlr.core.jms.kafka;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.hlr.core.event.IThreadsPool;
import com.hlr.core.jms.IJmsReceiver;
import com.hlr.core.jms.JmsMessageListener;
import com.hlr.core.jms.JmsObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
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

    private KafkaConsumer consumer = null;
    private String kafkaAddrs;
    private String clientId = null;
    private String consumerGroup = null;
    private int cousumeThreadMin = 16;
    private Map<String, JmsMessageListener> listeners;
    private Map<String, Map<String, JmsMessageListener>> topicListeners;
    private Map<String, Map<String, Class<JmsObject>>> topicTypes;
    private int fetchMinBytes = 500;
    private int maxPollRecords = 500;
    private int pollTimeout = 1000;
    private AtomicBoolean closed = new AtomicBoolean(false);
    private ExecutorService pollThread;
    private JmsKafkaMessageOrderlyExecuter executer;

    public JmsKafkaMQReceiver() {
    }

    public void start() {
        if (this.listeners != null && this.listeners.size() != 0) {
            if (this.clientId == null) {
                throw new RuntimeException("must need clientId");
            } else {
                if (this.consumerGroup == null) {
                    this.consumerGroup = this.clientId;
                }

                Properties props = new Properties();
                props.put("bootstrap.servers", this.kafkaAddrs);
                props.put("key.deserializer", StringDeserializer.class.getName());
                props.put("value.deserializer", StringDeserializer.class.getName());
                props.put("group.id", this.consumerGroup);
                props.put("enable.auto.commit", "false");
                props.put("client.id", this.clientId);
                props.put("fetch.min.bytes", this.fetchMinBytes);
                props.put("max.poll.records", this.maxPollRecords);
                props.put("auto.offset.reset", "latest");
                this.pollThread = Executors.newFixedThreadPool(1);
                this.consumer = new KafkaConsumer(props);
                logger.info("rocketmq start clientId:{} consumerGroup:{} kafkaAddrs:{} fetchMinBytes:{}", new Object[]{this.clientId, this.consumerGroup, this.kafkaAddrs, this.fetchMinBytes});

                try {
                    this.topicListeners = new HashMap();
                    this.topicTypes = new HashMap();
                    Iterator var2 = this.listeners.keySet().iterator();

                    while(var2.hasNext()) {
                        String topic = (String)var2.next();
                        String[] topicExps = topic.split("@");
                        Map<String, JmsMessageListener> tagsListeners = (Map)this.topicListeners.get(topicExps[0]);
                        Map<String, Class<JmsObject>> tagsTypes = (Map)this.topicTypes.get(topicExps[0]);
                        if (tagsListeners == null) {
                            tagsListeners = new HashMap();
                            this.topicListeners.put(topicExps[0], tagsListeners);
                        }

                        if (tagsTypes == null) {
                            tagsTypes = new HashMap();
                            this.topicTypes.put(topicExps[0], tagsTypes);
                        }

                        String[] tags = topicExps[1].split("\\|\\|");
                        String[] var8 = tags;
                        int var9 = tags.length;

                        for(int var10 = 0; var10 < var9; ++var10) {
                            String tag = var8[var10];
                            ((Map)tagsListeners).put(tag, this.listeners.get(topic));
                            ((Map)tagsTypes).put(tag, Class.forName(topicExps[2]));
                        }
                    }

                    this.consumer.subscribe(this.topicListeners.keySet());
                    logger.info("kafakamq subscribe clientId:{} consumerGroup:{} topics:{}", new Object[]{this.clientId, this.consumerGroup, this.topicListeners.keySet()});
                    this.executer = new JmsKafkaMessageOrderlyExecuter(this.topicListeners, this.topicTypes);
                    this.executer.setThreadPoolSize(this.cousumeThreadMin);
                    this.pollThread.submit(() -> {
                        while(!this.closed.get()) {
                            ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofMillis((long)this.pollTimeout));
                            if (records.count() > 0) {
                                this.process(records);

                                try {
                                    this.consumer.commitAsync();
                                } catch (Exception var3) {
                                    logger.error("commit failed", var3);
                                }
                            }
                        }

                    });
                    this.executer.start();
                    logger.info("kafkamq consumer clientId:{} consumerGroup:{} subscription:{} started", this.clientId, this.consumerGroup);
                } catch (Exception var12) {
                    var12.printStackTrace();
                }

            }
        } else {
            throw new RuntimeException("must need topics");
        }
    }

    private void process(ConsumerRecords<String, String> records) {
        Iterator var2 = records.iterator();

        while(var2.hasNext()) {
            ConsumerRecord<String, String> msg = (ConsumerRecord)var2.next();
            this.executer.add(msg.partition(), msg);
        }

    }

    public void shutdown() {
        if (this.closed.compareAndSet(false, true)) {
            logger.info("kafkamq consumer shutdown");
            this.pollThread.shutdownNow();
            this.executer.stop();

            try {
                this.consumer.commitSync();
            } finally {
                this.consumer.close();
            }

            logger.info("kafkamq consumer shutdown success");
        }
    }

    public void stop() {
        this.shutdown();
    }

    @Override
    public void stopNow(boolean flag) {
        this.shutdown();
    }
    public String getKafkaAddrs() {
        return this.kafkaAddrs;
    }

    public void setKafkaAddrs(String kafkaAddrs) {
        this.kafkaAddrs = kafkaAddrs;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getConsumerGroup() {
        return this.consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public int getFetchMinBytes() {
        return this.fetchMinBytes;
    }

    public void setFetchMinBytes(int fetchMinBytes) {
        this.fetchMinBytes = fetchMinBytes;
    }

    public int getPollTimeout() {
        return this.pollTimeout;
    }

    public void setPollTimeout(int pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public void setListeners(Map<String, JmsMessageListener> listeners) {
        this.listeners = listeners;
    }

    public int getMaxPollRecords() {
        return this.maxPollRecords;
    }

    public void setMaxPollRecords(int maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }
}
