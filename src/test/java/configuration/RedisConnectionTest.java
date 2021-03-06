package configuration;

import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.application.service.interf.IKeyValueService;
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
    @Autowired
    private IKeyValueService keyValueService;
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
        String avaliableScoreKey="task:publish:pay";
        String table="member:score:type:isAvaliableToSuper";
        String avaliableScore="member:score:type:isAvaliableScore";
        String memberScoreKey="member:score:type:isMemberScore";
        int[] value1={1,2,4,5,6,8,9,10,12,13,14,15,16,17,19,20};
        int[] value2={12,13,14,15,17,18,20};
        int[] value3={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,18,19,20};
        for (int i :value1){
//            cacheService.setToHashTable(avaliableScoreKey,"follow",30.0,null);
//            cacheService.setToHashTable(avaliableScoreKey,"read",1.5,null);
            cacheService.setToHashTable(avaliableScore,""+i,1,null);
        }
//        for (int i :value2){
//            cacheService.setToHashTable(table,""+i,1,null);
//        }
//        for (int i :value3){
//            cacheService.setToHashTable(memberScoreKey,""+i,1,null);
//        }
    }
    @Test
    public void testKeyValueIsExist(){
        String table="member:score:type:isAvaliableToSuper";
        boolean flag = keyValueService.keyIsExist(table,"16");
        System.out.println(flag);
    }
}
