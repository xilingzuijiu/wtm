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
            JpushUtils.buildRequest("各位微淘米小主们，11点15分系统升级开始了，请收好爪机，明日再来~~",0L);
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
