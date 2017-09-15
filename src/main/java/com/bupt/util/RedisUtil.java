package com.bupt.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
@Component
public class RedisUtil {
    private Logger logger = Logger.getLogger(RedisUtil.class);

    private RedisTemplate<String, String> redisTemplate;


    /**
     * 批量删除对应的key
     * @param key
     */
    public void remove(String key,String... hashKeys) {
        redisTemplate.opsForHash().delete(key,hashKeys);
    }



    public boolean exists(String key,String hashKey) {
        return redisTemplate.opsForHash().hasKey(key,hashKey);
    }

    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(String key,String hashKey) {
        Object result = null;
        result = redisTemplate.opsForHash().get(key,hashKey);
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, String hashKey,String value) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取所有的key的hashkey
     * @param key
     * @return
     */
    public Map<Object,Object> getAllKeyValue(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取hash中key数量
     * @param key
     * @return
     */
    public long getKeySize(String key){
       // return redisTemplate.opsForHash().keys(key).size();
        return redisTemplate.opsForHash().size(key);
    }



    /**
     * 获取hash中所有的剑值
     * @param key
     * @return
     */
    public Map<Object,Object>getKeyValue(String key){
        return redisTemplate.opsForHash().entries(key);
    }
    @Autowired
    public void setRedisTemplate(
            RedisTemplate<String, String> redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
        this.redisTemplate = redisTemplate;

    }

}
