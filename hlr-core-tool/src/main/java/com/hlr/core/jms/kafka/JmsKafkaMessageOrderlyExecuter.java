package com.hlr.core.jms.kafka;

import com.hlr.common.DateUtil;
import com.hlr.common.SerializationUtil;
import com.hlr.core.event.AbstractLoopThread;
import com.hlr.core.jms.JmsObjectMessageListener;
import com.hlr.core.jms.JmsObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * JmsKafkaMessageOrderlyExecuter
 * Description:
 * date: 2023/11/6 16:39
 *
 * @author hlr
 */
public class JmsKafkaMessageOrderlyExecuter extends AbstractLoopThread {
    private Logger logger = LoggerFactory.getLogger(JmsKafkaMessageOrderlyExecuter.class);
    private List<LinkedBlockingQueue<ConsumerRecord<String, String>>> queues;
    private Map<String, JmsObjectMessageListener> topicListeners;
    private Map<String, Class<JmsObject>> topicTypes;
    private int capacity = 1000;

    public JmsKafkaMessageOrderlyExecuter(Map<String, JmsObjectMessageListener> topicListeners, Map<String, Class<JmsObject>> topicTypes) {
        super("JmsKafkaMessageOrderlyExecuter");
        this.topicListeners = topicListeners;
        this.topicTypes = topicTypes;
    }

    @Override
    public void start() {
        queues = new ArrayList<>(getThreadPoolSize());
        for (int i = 0; i < getThreadPoolSize(); i++) {
            queues.add(new LinkedBlockingQueue<>(capacity));
        }

        super.start();
    }

    @Override
    public void work(int index) {
        try {
            ConsumerRecord<String, String> msg = queues.get(index).poll(1000L, TimeUnit.MILLISECONDS);
            if (msg != null) {
                Class<JmsObject> jmsObjectClass = topicTypes.get(msg.topic());
                JmsObjectMessageListener jmsMessageListener = topicListeners.get(msg.topic());
                if (jmsMessageListener != null && jmsObjectClass != null) {
                    JmsObject deserialize = SerializationUtil.deserialize(msg.value().getBytes(), jmsObjectClass);
                    long t1 = System.currentTimeMillis();
                    jmsMessageListener.handleMessage(deserialize);
                    long t2 = System.currentTimeMillis();
                    this.logger.debug("partition:{} offset:{} topic:{} orderKey:{}  msgLen:{} btime:{} time:{}", msg.partition(), msg.offset(), msg.topic(), msg.key(), ((String) msg.value()).length(), DateUtil.fmtLongTime((long) deserialize.getBtime()), t2 - t1);
                } else {
                    this.logger.error("partition:{} offset:{} topic:{} no tagListener", msg.partition(), msg.offset(), msg.topic());
                }
            }
        } catch (Exception e) {
            logger.error("index:{}", index, e);
        }

    }

    @Override
    public void destory() {
        logger.info("destory kafka executer jms ...");
        // 开始清空队列
        while (sumQueuesSize() > 0) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
            }
        }
        logger.info("destory kafka executer jms ok");
    }

    public int sumQueuesSize() {
        int total = 0;
        for (LinkedBlockingQueue<ConsumerRecord<String, String>> queue : queues) {
            total += queue.size();
        }
        return total;
    }

    public void add(int partition, ConsumerRecord<String, String> record) {
        //优先根据分区取模，如果当前分区队列满时，如果有空闲队列，优先使用空闲队列
        int i = partition % getThreadPoolSize();
        try {
            if(queues.get(i).remainingCapacity() > 0){
                queues.get(i).put(record);
            }else{
                Optional<LinkedBlockingQueue<ConsumerRecord<String, String>>> first = 
                        queues.stream().filter(o -> o.remainingCapacity() > 0).findFirst();
                if(first.isPresent()){
                    first.get().put(record);
                }else{
                    queues.get(i).put(record);
                }
            }
        } catch (InterruptedException e) {
            logger.error("kafka msg add error", e);
        }
    }
    
    
    
    
    
}