package com.weitaomi.systemconfig.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 获取以kryo序列化的redisTemplate实例
 * Created by Martin on 2015/5/19.
 */
@Component
public class RedisTemplateKryo {

    private RedisTemplate<String, Object> instance;

    @Autowired
    private RedisConnectionFactory jedisConnectionFactory;

    public RedisTemplate<String, Object> getInstance() {
        if (instance == null) {
            instance = new RedisTemplate<String, Object>();
            instance.setConnectionFactory(jedisConnectionFactory);
            instance.setKeySerializer(new StringRedisSerializer());
            instance.setValueSerializer(new JdkSerializationRedisSerializer());
            instance.setHashKeySerializer(new StringRedisSerializer());
            instance.setHashValueSerializer(new JdkSerializationRedisSerializer());
            instance.afterPropertiesSet();
        }
        return instance;
    }

}
