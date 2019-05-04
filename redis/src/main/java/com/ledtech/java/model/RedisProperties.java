package com.ledtech.java.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName:RedisProperties
 * @Author:DK_Li
 * @Date:Created in 2019/5/4 18:09
 * @Description:Redis配置类
 * @Version:1.0
 */
@Component
@ConfigurationProperties(prefix = "spring.redis.cache")
public class RedisProperties {

    private int expireSeconds;
    private String clusterNodes;
    private Integer commandTimeout;

    public RedisProperties() {

    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public Integer getCommandTimeout() {
        return commandTimeout;
    }

    public void setCommandTimeout(Integer commandTimeout) {
        this.commandTimeout = commandTimeout;
    }
}