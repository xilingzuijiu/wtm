package configuration;

import com.weitaomi.application.service.interf.ICacheService;
import common.BaseContextCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/11/5.
 */
public class CacheTest extends BaseContextCase{
    @Autowired
    private ICacheService cacheService;
    @Test
    public void testIncreCacheTest(){
        cacheService.setCacheByKey("111111",123,10);
        Integer number=cacheService.getCacheByKey("111111",Integer.class);
        System.out.println(number);
        Integer number2=cacheService.increCacheBykey("111111",73L);
        System.out.println(number2);
        cacheService.delKeyFromRedis("111111");
    }
}
