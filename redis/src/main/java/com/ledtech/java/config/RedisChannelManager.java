package com.ledtech.java.config;

import com.ledtech.java.util.SyniRedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName:RedisChannelManager
 * @Author:DK_Li
 * @Date:Created in 2019/5/4 18:11
 * @Description:redis消息通道管理器
 * @Version:1.0
 */
@Component
public class RedisChannelManager {
    private Logger log = LoggerFactory.getLogger(RedisChannelManager.class);

    @Autowired
    private SyniRedisTemplate redisTemplate;

    /**
     * key：userId
     * value：channel
     */
    private Map<String, String> channelMap = null;

    public RedisChannelManager() {
        channelMap = new ConcurrentHashMap<String, String>();
    }

    public synchronized void putChannel(String userId, String channel) {
        if(channelMap != null) {
            channelMap.put(userId, channel);
        }
    }

    public synchronized void removeChannel(String userId) {
        if(channelMap != null) {
            channelMap.remove(userId);
        }
    }

    public synchronized void destroy() {
        log.info("RedisChannelManager destroy.");
        if(channelMap != null) {
            List<String> channels = new ArrayList<String>(channelMap.values());
            //取消所有订阅的消息通道
            if(channels != null && channels.size() > 0) {
                for (String channel : channels) {
                    log.info("RedisChannelManager: close channel={}", channel);
                    redisTemplate.publish(channel, "syni_cmd_close");
                }
                channels.clear();
                channels = null;
            }
            channelMap.clear();
            channelMap = null;
        }
    }
}
