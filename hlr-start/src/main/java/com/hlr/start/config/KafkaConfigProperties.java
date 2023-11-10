package com.hlr.start.config;

import java.util.Map;

/**
 * KafkaConfigProperties
 * Description:
 * date: 2023/11/6 15:56
 *
 * @author hlr
 */
public class KafkaConfigProperties {

    private String clientId = null;
    private String kafkaAddrs;
    // 发送失败，重试次数
    private int retries = 0;
    //最小批量发送等待毫秒，默认为0ms
    private int lingerMs = 0;
    // 积攒多少字节后批量发送
    private int batchSize = 0;
    // 同步策略
    private int acks = 1;
    private String consumerGroup = null;
    // 消费者参数 批量拉取字节数
    private int fetchMinBytes = 500;
    // 批量拉取等待时间
    private int pollTimeout = 1000;
    //一次最多拉取记录数
    private int maxPollRecords = 500;
    // 可根据最大分区进行调整，消费消息线程数
    private int threadSize = 16;

    public int getThreadSize() {
        return threadSize;
    }

    public void setThreadSize(int threadSize) {
        this.threadSize = threadSize;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getKafkaAddrs() {
        return kafkaAddrs;
    }

    public void setKafkaAddrs(String kafkaAddrs) {
        this.kafkaAddrs = kafkaAddrs;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(int lingerMs) {
        this.lingerMs = lingerMs;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getAcks() {
        return acks;
    }

    public void setAcks(int acks) {
        this.acks = acks;
    }
    
    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public int getFetchMinBytes() {
        return fetchMinBytes;
    }

    public void setFetchMinBytes(int fetchMinBytes) {
        this.fetchMinBytes = fetchMinBytes;
    }

    public int getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(int pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public int getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(int maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }
}
