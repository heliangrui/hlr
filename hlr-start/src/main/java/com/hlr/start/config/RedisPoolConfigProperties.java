package com.hlr.start.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RedisPollConfigProperties
 * Description:
 * date: 2023/10/10 18:46
 *
 * @author hlr
 */
@ConfigurationProperties( prefix = "hlr.redis")
public class RedisPoolConfigProperties {
    private int maxTotal = 10;
    private int maxIdle = 1;
    private int maxWaitMillis = 5000;
    private int soTimeout = 3000;
    private int connectionTimeout = 3000;
    private String server;

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
