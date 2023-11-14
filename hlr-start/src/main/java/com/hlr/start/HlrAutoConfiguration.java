package com.hlr.start;

import com.hlr.core.cache.CacheService;
import com.hlr.core.cache.impl.RedisCacheService;
import com.hlr.core.cache.redis.RedisPool;
import com.hlr.core.config.EnumConfig;
import com.hlr.core.jms.JmsObjectMessageListener;
import com.hlr.core.jms.JmsSourceMessageListener;
import com.hlr.core.jms.annotation.JmsObjectListener;
import com.hlr.core.jms.annotation.JmsSourceListener;
import com.hlr.core.jms.kafka.JmsKafkaMQReceiver;
import com.hlr.core.jms.kafka.JmsKafkaMQSender;
import com.hlr.core.jms.mqtt.JmsMqttMqReceiver;
import com.hlr.core.jms.mqtt.JmsMqttMqSender;
import com.hlr.start.aop.MethodCacheAspect;
import com.hlr.start.config.HlrConfigProperties;
import com.hlr.start.config.KafkaConfigProperties;
import com.hlr.start.config.MqttConfigProperties;
import com.hlr.start.config.RedisPoolConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * HlrAutoConfiguration
 * Description:
 * date: 2023/10/10 19:39
 *
 * @author hlr
 */
@Configuration
@EnableConfigurationProperties({RedisPoolConfigProperties.class, HlrConfigProperties.class, KafkaConfigProperties.class, MqttConfigProperties.class})
public class HlrAutoConfiguration implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(HlrAutoConfiguration.class);
    private static ApplicationContext applicationContext;
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        HlrAutoConfiguration.applicationContext = applicationContext;
    }

    @Bean(initMethod = "init", destroyMethod = "shutDown")
    @ConditionalOnProperty({"hlr.redis.server"})
    RedisPool redisPool(RedisPoolConfigProperties redisPoolConfigProperties) {
        RedisPool pool = new RedisPool();
        pool.setServer(redisPoolConfigProperties.getServer());
        pool.setConnectionTimeout(redisPoolConfigProperties.getConnectionTimeout());
        pool.setMaxIdle(redisPoolConfigProperties.getMaxIdle());
        pool.setMaxTotal(redisPoolConfigProperties.getMaxTotal());
        pool.setMaxWaitMillis(redisPoolConfigProperties.getMaxWaitMillis());
        pool.setSoTimeout(redisPoolConfigProperties.getSoTimeout());
        return pool;
    }

    @Bean
    @ConditionalOnBean({RedisPool.class})
    RedisCacheService redisCacheService(RedisPool redisPool) {
        return new RedisCacheService(redisPool.getPool());
    }

    @Bean
    HlrReadyApplicationListener hlrReadyApplicationListener() {
        return new HlrReadyApplicationListener();
    }

    @Bean
    @ConditionalOnBean({CacheService.class})
    @ConditionalOnProperty(value = {"hlr.method.cache.enabled"}, havingValue = "false", matchIfMissing = true)
    MethodCacheAspect methodCacheAspect(CacheService cacheService) {
        return new MethodCacheAspect(cacheService);
    }

    @Bean
    @ConditionalOnProperty({"hlr.kafka.kafkaAddrs"})
    @ConditionalOnBean({JmsObjectMessageListener.class})
    JmsKafkaMQReceiver jmsKafkaMQReceiver(KafkaConfigProperties kafkaConfigProperties) {
        JmsKafkaMQReceiver jmsKafkaMQReceiver = new JmsKafkaMQReceiver();
        // 基础信心配置
        jmsKafkaMQReceiver.setClientId(kafkaConfigProperties.getClientId() == null ? this.appName : kafkaConfigProperties.getClientId());
        jmsKafkaMQReceiver.setKafkaAddrs(kafkaConfigProperties.getKafkaAddrs());
        jmsKafkaMQReceiver.setFetchMinBytes(kafkaConfigProperties.getFetchMinBytes());
        jmsKafkaMQReceiver.setPollTimeout(kafkaConfigProperties.getPollTimeout());
        jmsKafkaMQReceiver.setMaxPollRecords(kafkaConfigProperties.getMaxPollRecords());
        jmsKafkaMQReceiver.setConsumerGroup(kafkaConfigProperties.getConsumerGroup());
        jmsKafkaMQReceiver.setCousumeThreadMin(kafkaConfigProperties.getThreadSize());
        // 获取监听消息bean对象
        Map<String, Object> jmsListeners = applicationContext.getBeansWithAnnotation(JmsObjectListener.class);
        // 存放注解配置 和 监听消息对象
        Map<String, JmsObjectMessageListener> listeners = new HashMap();
        jmsListeners.forEach((a, b) -> {

            if (b instanceof JmsObjectMessageListener) {
                //获取监听消息对象
                JmsObjectMessageListener jmsMessageListener = (JmsObjectMessageListener) b;
                //获取监听消息注解配置
                JmsObjectListener annotation = jmsMessageListener.getClass().getAnnotation(JmsObjectListener.class);
                if (annotation != null && annotation.jmsType() == EnumConfig.JmsObjectType.JMS_KAFKA) {
                    // 组装注解配置信息
                    StringBuilder sb = new StringBuilder(annotation.topic());
                    sb.append("@").append(annotation.jmsObject().getName());
                    listeners.put(sb.toString(), jmsMessageListener);
                }

            }

        });
        jmsKafkaMQReceiver.setListeners(listeners);
        if (listeners.isEmpty()) {
            return null;
        }

        return jmsKafkaMQReceiver;
    }

    @Bean
    @ConditionalOnProperty({"hlr.kafka.kafkaAddrs"})
    JmsKafkaMQSender jmsKafkaMQSender(KafkaConfigProperties kafkaConfigProperties) {
        JmsKafkaMQSender sender = new JmsKafkaMQSender();
        sender.setClientId(kafkaConfigProperties.getClientId() == null ? this.appName : kafkaConfigProperties.getClientId());
        sender.setKafkaAddrs(kafkaConfigProperties.getKafkaAddrs());
        sender.setAcks(kafkaConfigProperties.getAcks());
        sender.setBatchSize(kafkaConfigProperties.getBatchSize());
        sender.setLingerMs(kafkaConfigProperties.getLingerMs());
        sender.setRetries(kafkaConfigProperties.getRetries());
        return sender;
    }

    @Bean
    @ConditionalOnProperty({"hlr.mqtt.mqttAddrs"})
    JmsMqttMqReceiver jmsKafkaMQReceiver(MqttConfigProperties mqttConfigProperties) {
        JmsMqttMqReceiver jmsMqttMqReceiver = new JmsMqttMqReceiver();
        // 获取监听消息bean对象
        Map<String, Object> jmsListeners = applicationContext.getBeansWithAnnotation(JmsSourceListener.class);
        // 存放注解配置 和 监听消息对象
        Map<String, JmsSourceMessageListener> listeners = new HashMap();
        jmsListeners.forEach((a, b) -> {

            if (b instanceof JmsSourceMessageListener) {
                //获取监听消息对象
                JmsSourceMessageListener jmsMessageListener = (JmsSourceMessageListener) b;
                //获取监听消息注解配置
                JmsSourceListener annotation = jmsMessageListener.getClass().getAnnotation(JmsSourceListener.class);
                if (annotation != null && annotation.jmsType() == EnumConfig.JmsSourceType.JMS_MQTT) {
                    listeners.put(annotation.topic(), jmsMessageListener);
                }

            }
        });
        jmsMqttMqReceiver.setApplicationContext(applicationContext);
        jmsMqttMqReceiver.setThreadSize(mqttConfigProperties.getThreadSize());
        jmsMqttMqReceiver.setTopicListener(listeners);
        return jmsMqttMqReceiver;
    }

    @Bean
    @ConditionalOnProperty({"hlr.mqtt.mqttAddrs"})
    JmsMqttMqSender jmsMqttMqSender(MqttConfigProperties mqttConfigProperties) {
        JmsMqttMqSender jmsMqttMqSender = new JmsMqttMqSender();
        jmsMqttMqSender.setClientId(mqttConfigProperties.getClientId() == null ? this.appName : mqttConfigProperties.getClientId());
        jmsMqttMqSender.setMqttAddrs(mqttConfigProperties.getMqttAddrs());
        jmsMqttMqSender.setQos(mqttConfigProperties.getQos());
        jmsMqttMqSender.setPassWord(mqttConfigProperties.getPassWord());
        jmsMqttMqSender.setCleanSession(mqttConfigProperties.isCleanSession());
        jmsMqttMqSender.setTimeout(mqttConfigProperties.getTimeout());
        jmsMqttMqSender.setVersion(mqttConfigProperties.getVersion());
        jmsMqttMqSender.setConnectionTimeout(mqttConfigProperties.getConnectionTimeout());
        jmsMqttMqSender.setKeepAliveInterval(mqttConfigProperties.getKeepAliveInterval());
        jmsMqttMqSender.setUserName(mqttConfigProperties.getUserName());
        jmsMqttMqSender.setApplicationContext(applicationContext);
        return jmsMqttMqSender;
    }


}
