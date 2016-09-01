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
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(20000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JpushUtils.buildRequest("这是一条通知，通知给MemberID=11的用户",11L);
        }
    }
}
