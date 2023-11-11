package com.hlr.core.jms.kafka;

import com.hlr.common.DateUtil;
import com.hlr.common.RandomUtil;
import com.hlr.common.SerializationUtil;
import com.hlr.core.event.IThreadsPool;
import com.hlr.core.jms.JmsObject;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JmsKafkaMQSender
 * Description:
 * date: 2023/11/11 10:25
 *
 * @author hlr
 */
public class JmsKafkaMQSender implements IThreadsPool {
    private static final Logger logger = LoggerFactory.getLogger(JmsKafkaMQSender.class);
    private String kafkaAddrs;
    private int retries;
    private int lingerMs;
    private int batchSize;
    private int acks = 1;
    private String clientId = null;
    private KafkaProducer<Long, String> producer;
    private AtomicBoolean closed = new AtomicBoolean(false);

    public void start() {
        try {
            Properties props = new Properties();
            props.put("bootstrap.servers", this.kafkaAddrs);
            props.put("key.serializer", StringSerializer.class.getName());
            props.put("value.serializer", StringSerializer.class.getName());
            props.put("retries", this.retries);
            props.put("linger.ms", this.lingerMs);
            props.put("batch.size", this.batchSize);
            props.put("acks", this.acks + "");
            props.put("client.id", this.clientId);
            this.producer = new KafkaProducer<>(props);
            logger.debug("kafkamq producer started");
        } catch (Exception var2) {
            var2.printStackTrace();
            logger.error("", var2);
        }

    }

    public boolean send(final String topic, final JmsObject object) {
        if (!closed.get() && this.producer != null) {
            if (topic != null && object != null) {
                try {
                    long key = object.getOrderKey() == 0L ? (long) RandomUtil.getRandomRange(20) : object.getOrderKey();
                    ProducerRecord<Long, String> producerRecord = new ProducerRecord(topic, key + "", new String(SerializationUtil.serialize(object)));
                    long t1 = System.currentTimeMillis();
                    Future<RecordMetadata> result = this.producer.send(producerRecord, new Callback() {
                        public void onCompletion(RecordMetadata metadata, Exception exception) {
                            if (exception != null) {
                                JmsKafkaMQSender.logger.error(" send fail. topic:{} orderKey:{} btime:{}", topic, object.getOrderKey(), DateUtil.fmtLongTime((long) object.getBtime()), exception);
                                throw new RuntimeException("kafka生产数据异常...");
                            }
                        }
                    });
                    if (result == null) {
                        logger.error(" send fail. topic:{} orderKey:{} btime:{}", topic, object.getOrderKey(), DateUtil.fmtLongTime((long) object.getBtime()));
                        return false;
                    } else {
                        long t2 = System.currentTimeMillis();
                        logger.debug("topic:{} orderKey:{} msgLen:{} btime:{} mqbtime:{} time:{}", producerRecord.topic(), producerRecord.key(), ((String) producerRecord.value()).length(), DateUtil.fmtLongTime((long) object.getBtime()), DateUtil.fmtLongTime((long) DateUtil.getNowIntTime()), t2 - t1);
                        return true;
                    }
                } catch (Exception var11) {
                    logger.error("sender", var11);
                    return false;
                }
            } else {
                logger.error("params error");
                return false;
            }
        } else {
            logger.error("sender stoped.");
            return false;
        }
    }

    public void shutdown() {
        if (this.closed.compareAndSet(false, true)) {
            logger.info("kafkamq producer shutdown");
            this.producer.close();
        }
    }

    public void stop() {
        this.shutdown();
    }

    public void stopNow(boolean discart) {
        this.shutdown();
    }

    public String getKafkaAddrs() {
        return this.kafkaAddrs;
    }

    public void setKafkaAddrs(String kafkaAddrs) {
        this.kafkaAddrs = kafkaAddrs;
    }

    public int getRetries() {
        return this.retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getLingerMs() {
        return this.lingerMs;
    }

    public void setLingerMs(int lingerMs) {
        this.lingerMs = lingerMs;
    }

    public int getBatchSize() {
        return this.batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getAcks() {
        return this.acks;
    }

    public void setAcks(int acks) {
        this.acks = acks;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
