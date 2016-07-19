package configuration;

import com.weitaomi.systemconfig.util.SendMCUtils;
import common.BaseContextCase;
import org.junit.Test;

/**
 * Created by supumall on 2016/7/14.
 */
public class SendMsgTest extends BaseContextCase {
    @Test
    public void testGetBalance(){
        System.out.println(SendMCUtils.getMessageNumber());
    }
    @Test
    public void testSendMsg(){
        String phone="13105187050";
        String msg="1314";
        SendMCUtils.sendMessage(phone,msg);
    }
}
