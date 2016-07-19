package configuration;

import common.BaseContextCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * Created by supumall on 2016/7/4.
 */
public class RedisConnectionTest extends BaseContextCase {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void testKeyValue(){
        String key="test.key.value";
        String value="这是一个神奇的世界";
        ValueOperations valueOperations= redisTemplate.opsForValue();
        valueOperations.set(key,value);
        String values= (String)valueOperations.get("test.key.value");
        System.out.println(values);
    }
}
