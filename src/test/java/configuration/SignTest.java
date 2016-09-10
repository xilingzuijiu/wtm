package configuration;

import com.alibaba.fastjson.JSON;
import com.weitaomi.systemconfig.alipay.AlipayConfig;
import com.weitaomi.systemconfig.alipay.sign.RSA;
import common.BaseContextCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/10.
 */
public class SignTest extends BaseContextCase {
    @Test
    public void signTest(){
        Map map=new HashMap();
        map.put("a","123");

        System.out.println(RSA.sign(JSON.toJSONString(map), AlipayConfig.private_key, AlipayConfig.input_charset));
    }
}
