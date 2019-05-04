package com.ledtech.java.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

/**
 * @ClassName:RedisMQTask
 * @Author:DK_Li
 * @Date:Created in 2019/5/4 18:13
 * @Description:处理监听到的消息任务
 * @Version:1.0
 */
public class RedisMQTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RedisMQTask.class);

    private JedisCluster jedisCluster;
    private JedisPubSub jedisPubSub;
    private String channels;

    public RedisMQTask(JedisCluster jedisCluster, JedisPubSub jedisPubSub, String channels) {
        this.jedisCluster = jedisCluster;
        this.jedisPubSub = jedisPubSub;
        this.channels = channels;
    }

    @Override
    public void run() {
        // 监听channel通道的消息
        if(jedisCluster != null) {
            jedisCluster.subscribe(jedisPubSub, channels);
        }
    }
}
