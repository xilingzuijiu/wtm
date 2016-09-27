package configuration;

import com.weitaomi.systemconfig.util.JpushUtils;
import org.junit.Test;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Administrator on 2016/8/12.
 */
public class MessagePushTest {
    @Test
    public void testPushMessage(){
        String value="{\"id\":7,\"message\":\"hello World\"}";
//        for (int i = 0; i < 5; i++) {
            JpushUtils.buildRequest("这是一条通知，通知给memberId={}的用户",3L);
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//        }
    }

    @Test
    public void test1(){
       String  headimgurl = "http://wx.qlogo.cn/mmopen/nEGn2EWllIyMNTpHrbiaWVLQnDTpe8WMFiccOI4dWibzTPcOLKo9vNTIaCJJ0QpgwiaqrRQiboXMC30JbDucTulHgiaMaQB9T6yXeu/0";
        String headimg="http://wx.qlogo.cn/mmopen/kk8ZYnic6GnNPTFvH79gt4IodfRUWYmXA78VZBWc3YdWfkz5iccvWyEvgUJ4H7BgWvY8YamHCUtEpxB8lraL2ibKYvgVvyicBfwic/0";
    }
}
