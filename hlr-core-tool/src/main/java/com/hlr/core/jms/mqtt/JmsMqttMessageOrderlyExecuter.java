package com.hlr.core.jms.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.hlr.core.event.AbstractLoopThread;
import com.hlr.core.jms.JmsSourceMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JmsMqttMessageOrderlyExecuter
 * Description:
 * date: 2023/11/11 17:15
 *
 * @author hlr
 */
public class JmsMqttMessageOrderlyExecuter extends AbstractLoopThread {

    private static final Logger logger = LoggerFactory.getLogger(JmsMqttMessageOrderlyExecuter.class);
    private int depth = 1000;

    private LinkedBlockingQueue<JmsMqttMessage> queue;

    private Map<String, JmsSourceMessageListener> topicListener = null;

    public JmsMqttMessageOrderlyExecuter(Map<String, JmsSourceMessageListener> topicListener) {
        super("JmsMqttMessageOrderlyExecuter");
        this.queue = new LinkedBlockingQueue<>(depth);
        this.topicListener = topicListener;

    }

    public void add(JmsMqttMessage message) {
        try {
            queue.add(message);
        } catch (Exception e) {
            logger.error("jsm mqtt message add error ...");
        }
    }

    @Override
    public void work(int var1) {

        try {
            JmsMqttMessage poll = queue.poll(1000L, TimeUnit.MILLISECONDS);
            JmsSourceMessageListener jmsSourceMessageListener = topicVerify(poll.getTopic());
            if(jmsSourceMessageListener == null){
                logger.error("topic verify error .. message：{},{}",poll.getTopic(), JSONObject.toJSONString(poll.getMqttMessage()));
                return;
            }
            jmsSourceMessageListener.handleMessage(poll.getTopic(),new String(poll.getMqttMessage().getPayload()));
            logger.debug("jms mqtt execute success :{}",JSONObject.toJSONString(poll));
        } catch (InterruptedException e) {
            logger.error("jms mqtt queue error {}",var1);
        }
    }
    
    public JmsSourceMessageListener topicVerify(String topic){
        Pattern compile = null;
        Matcher matcher = null;
        for (String s : topicListener.keySet()) {
            // 使用正则表达式匹配消息主题是否满足模式  
            compile = Pattern.compile(s);
            matcher = compile.matcher(topic);
            if(matcher.matches()){
                return topicListener.get(s);
            }
        }
        return null;
    }
    
    

    @Override
    public void destory() {
        logger.info("destory mqtt executer jms ...");
        while(queue.size() != 0){
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        logger.info("destory mqtt executer jms ok");
    }
}
