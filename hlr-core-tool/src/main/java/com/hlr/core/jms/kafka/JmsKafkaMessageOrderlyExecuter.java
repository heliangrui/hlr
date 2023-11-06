package com.hlr.core.jms.kafka;

import com.hlr.common.ConvertUtil;
import com.hlr.core.event.AbstractLoopThread;
import com.hlr.core.jms.JmsMessageListener;
import com.hlr.core.jms.JmsObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private Map<String, Map<String, JmsMessageListener>> topicListeners;
    private Map<String, Map<String, Class<JmsObject>>> topicTypes;
    private int capacity = 1000;

    public JmsKafkaMessageOrderlyExecuter(Map<String, Map<String, JmsMessageListener>> topicListeners, Map<String, Map<String, Class<JmsObject>>> topicTypes) {
        super("JmsKafkaMessageOrderlyExecuter");
        this.topicListeners = topicListeners;
        this.topicTypes = topicTypes;
    }

    public void start() {
        this.queues = new ArrayList<>();

        for(int i = 0; i < this.getThreadPoolSize(); ++i) {
            this.queues.add(new LinkedBlockingQueue<>(this.capacity));
        }

        super.start();
    }

    public void add(int order, ConsumerRecord<String, String> msg) {
        try {
            ((LinkedBlockingQueue)this.queues.get(order)).put(msg);
        } catch (InterruptedException var4) {
            this.logger.error("", var4);
        }

    }

    public void work(int order) {
        try {
            ConsumerRecord<String, String> msg = (ConsumerRecord)((LinkedBlockingQueue)this.queues.get(order)).poll(1000L, TimeUnit.MILLISECONDS);
            if (null != msg) {
                JmsMessageListener tagListener = (JmsMessageListener)((Map)this.topicListeners.get(msg.topic())).get("*");
                Class<JmsObject> tagType = (Class)((Map)this.topicTypes.get(msg.topic())).get("*");

                try {
                    if (tagListener != null && tagType != null) {
                        JmsObject m = (JmsObject) ConvertUtil.json2object((String)msg.value(), tagType);
                        long t1 = System.currentTimeMillis();
                        tagListener.handleMessage(m);
                        long t2 = System.currentTimeMillis();
                        this.logger.debug("partition:{} offset:{} topic:{} orderKey:{}  msgLen:{} btime:{} time:{}", new Object[]{msg.partition(), msg.offset(), msg.topic(), msg.key(), ((String)msg.value()).length(), DTUtil.fmtLongTime((long)m.getBtime()), t2 - t1});
//                        MetricsHelper.recordSummary("msa_jms_seconds", ArithUtil.div((double)(t2 - t1), 1000.0), new String[]{"topic", msg.topic(), "side", "receiver"});
                    } else {
                        this.logger.error("partition:{} offset:{} topic:{} no tagListener", new Object[]{msg.partition(), msg.offset(), msg.topic()});
                    }
                } catch (Throwable var10) {
                    this.logger.error("", var10);
                }
            }
        } catch (InterruptedException var11) {
        } catch (Exception var12) {
            this.logger.error("", var12);
        }

    }

    public void destory() {
        this.logger.info("destory jms executer...");
        int total = 0;
        
        while((total = this.sumQueuesLeft()) > 0) {
            this.logger.info("have {} jms task left, just wait for it to complete", total);

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException var3) {
            }
        }

        this.logger.info("destory jms executer ok");
    }

    private int sumQueuesLeft() {
        int total = 0;

        LinkedBlockingQueue list;
        for(Iterator var2 = this.queues.iterator(); var2.hasNext(); total += list.size()) {
            list = (LinkedBlockingQueue)var2.next();
        }

        return total;
    }

    public int getRemainCapacity(int order) {
        return ((LinkedBlockingQueue)this.queues.get(order)).remainingCapacity();
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }