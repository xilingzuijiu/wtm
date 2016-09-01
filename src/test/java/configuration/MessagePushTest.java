package configuration;

import com.weitaomi.systemconfig.util.JpushUtils;
import org.junit.Test;

/**
 * Created by Administrator on 2016/8/12.
 */
public class MessagePushTest {
    @Test
    public void testPushMessage(){
        String value="{\"id\":7,\"message\":\"hello World\"}";
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JpushUtils.buildRequest(value);
        }
    }
}
