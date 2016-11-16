package configuration;

import com.weitaomi.application.service.interf.ICacheService;
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
    @Autowired
    private ICacheService cacheService;
    @Test
    public void testKeyValue(){
        String key="test.key.value";
        String value="这是一个神奇的世界";
        ValueOperations valueOperations= redisTemplate.opsForValue();
        valueOperations.set(key,value);
        String values= (String)valueOperations.get("test.key.value");
        System.out.println(values);
    }
    @Test
    public void testCacheData(){
        String avaliableScoreKey="member:score:type:isAvaliableScore";
        String table="member:score:type:isAvaliableToSuper";
        int[] value1={1,2,4,5,6,8,9,10,12,13,14,15,17,20};
        int[] value2={11,12,13,14,15,16,20};
        for (int i :value1){
            cacheService.setToHashTable(avaliableScoreKey,""+i,1,null);
        }
        for (int i :value2){
            cacheService.setToHashTable(table,""+i,1,null);
        }
    }
}
