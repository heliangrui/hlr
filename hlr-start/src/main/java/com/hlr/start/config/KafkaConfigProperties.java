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
    private int retries = 0;
    private int lingerMs = 0;
    private int batchSize = 0;
    private int acks = 1;
    @Deprecated
    private int threadpoolsize = 1;
    @Deprecated
    private int maxSize = 100;
    private String consumerGroup = null;
    private int fetchMinBytes = 500;
    private int pollTimeout = 1000;
    private int maxPollRecords = 500;
    private Map<String, String> topics;

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

    public int getThreadpoolsize() {
        return threadpoolsize;
    }

    public void setThreadpoolsize(int threadpoolsize) {
        this.threadpoolsize = threadpoolsize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
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

    public Map<String, String> getTopics() {
        return topics;
    }

    public void setTopics(Map<String, String> topics) {
        this.topics = topics;
    }
}
