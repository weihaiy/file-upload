package com.wei.util.fileupload.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author weihaiyang
 * @ClassDescription 类的描述
 * @since 2024/8/19 15:18 星期一
 */
@Component
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 从 Redis 中获取指定键的值
     * @param key
     * @return
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 向 Redis 中存储一个键值对，并设置其过期时间
     * timeout 指定时间量，timeUnit 指定时间单位
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     */
    public void setValueWithExpiry(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 从 Redis 中删除指定键及其对应的值
     * @param key
     */
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    public boolean setObject(final String key, Object value, Integer expireTime) {
        try {

            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
