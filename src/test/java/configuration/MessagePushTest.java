package configuration;

import com.weitaomi.systemconfig.util.JpushUtils;
import com.weitaomi.systemconfig.util.SendMCUtils;
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
            JpushUtils.buildRequest("亲爱的微淘米玩家们，我们将于2016年10月24日下午12点20分升级版本，届时将有1-5分钟加载缓慢，请稍后再试~微淘小米祝小伙伴们周一愉快~",0L);
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
    @Test
    public void test2(){
        String img="欢迎注册微淘米，您本次的验证码为123456，两分钟内有效【微淘米APP】";
        SendMCUtils.sendMessage("13780642170",img);
    }
}
