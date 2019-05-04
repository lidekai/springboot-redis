package com.ledtech.java.util;

import com.ledtech.java.handler.RedisMQTask;
import com.ledtech.java.model.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

import java.util.List;

/**
 * @ClassName:SyniRedisTemplate
 * @Author:DK_Li
 * @Date:Created in 2019/5/4 18:12
 * @Description:Redis操作模板
 * @Version:1.0
 */
@Component
public class SyniRedisTemplate {

    public static final String CHANNEL_FORWARD_PREFIX = "syni:channel";
    public static final String USER_FORWARD_CACHE_PREFIX = "syni:user";//缓存前缀
    public static final String MESSAGE_QUEUE_KEY = "syni:message:queue";

    private static final Logger log = LoggerFactory.getLogger(SyniRedisTemplate.class);

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private RedisProperties redisProperties;

    public static final String KEY_SPLIT = ":"; //用于隔开缓存前缀与缓存键值

    /**
     * 设置缓存
     *
     * @param prefix 缓存前缀（用于区分缓存，防止缓存键值重复）
     * @param key    缓存key
     * @param value  缓存value
     */
    public void set(String prefix, String key, String value) {
        jedisCluster.set(prefix + KEY_SPLIT + key, value);
        log.debug("RedisUtil:set cache key={},value={}", prefix + KEY_SPLIT + key, value);
    }

    /**
     * 设置缓存，并且自己指定过期时间
     *
     * @param prefix
     * @param key
     * @param value
     * @param expireTime 过期时间
     */
    public void setWithExpireTime(String prefix, String key, String value, int expireTime) {
        jedisCluster.setex(prefix + KEY_SPLIT + key, expireTime, value);
        log.debug("RedisUtil:setWithExpireTime cache key={},value={},expireTime={}", prefix + KEY_SPLIT + key, value,
                expireTime);
    }

    /**
     * 设置缓存，并且由配置文件指定过期时间
     *
     * @param prefix
     * @param key
     * @param value
     */
    public void setWithExpireTime(String prefix, String key, String value) {
        int EXPIRE_SECONDS = redisProperties.getExpireSeconds();
        jedisCluster.setex(prefix + KEY_SPLIT + key, EXPIRE_SECONDS, value);
        log.debug("RedisUtil:setWithExpireTime cache key={},value={},expireTime={}", prefix + KEY_SPLIT + key, value,
                EXPIRE_SECONDS);
    }

    /**
     * 获取指定key的缓存
     *
     * @param prefix
     * @param key
     */
    public String get(String prefix, String key) {
        String value = jedisCluster.get(prefix + KEY_SPLIT + key);
        log.debug("RedisUtil:get cache key={},value={}", prefix + KEY_SPLIT + key, value);
        return value;
    }

    /**
     * 删除指定key的缓存
     *
     * @param prefix
     * @param key
     */
    public void deleteWithPrefix(String prefix, String key) {
        jedisCluster.del(prefix + KEY_SPLIT + key);
        log.debug("RedisUtil:delete cache key={}", prefix + KEY_SPLIT + key);
    }

    public void delete(String key) {
        jedisCluster.del(key);
        log.debug("RedisUtil:delete cache key={}", key);
    }

    /**
     * 发布消息到redis通道
     *
     * @param channel
     * @param message
     */
    public boolean publish(String channel, String message) {
        boolean flag = false;
        try {
            jedisCluster.publish(channel, message);
            flag = true;
        } catch (Exception e) {
            log.error("RedisUtil:publish message error={}", e.getMessage());
        } finally {

        }
        return flag;
    }

    /**
     * 订阅redis通道消息
     *
     * @param jedisPubSub 监听处理类
     * @param channels    监听的消息通道
     */
    public boolean subscribe(JedisPubSub jedisPubSub, String channels) {
        boolean flag = false;
        try {
            new Thread(new RedisMQTask(jedisCluster, jedisPubSub, channels)).start();
//            jedisCluster.subscribe(jedisPubSub, channels);
            flag = true;
        } catch (Exception e) {
            log.error("RedisUtil:subscribe message error={}", e.getMessage());
        } finally {

        }
        return flag;
    }

    public long putMessage(String message) {
        return jedisCluster.lpush(MESSAGE_QUEUE_KEY, message);
    }

    public List<String> brpopMessage() {
        /**
         * brpop支持多个列表(队列)
         * brpop指令是支持队列优先级的，比如这个例子中MESSAGE_QUEUE_KEY的优先级大于testKey（顺序决定）。
         * 如果两个列表中都有元素，会优先返回优先级高的列表中的元素，所以这儿优先返回MESSAGE_QUEUE_KEY
         * 0表示不限制等待，会一直阻塞在这儿
         */
//        return jedisCluster.brpop(0, MESSAGE_QUEUE_KEY, "syni:message:tqueue");
        return jedisCluster.brpop(0, MESSAGE_QUEUE_KEY);

    }

    public String popMessage() {
        return jedisCluster.rpop(MESSAGE_QUEUE_KEY);
    }
}
