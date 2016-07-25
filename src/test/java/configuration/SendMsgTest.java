package configuration;

import com.weitaomi.application.service.interf.IMemberService;
import com.weitaomi.systemconfig.util.SendMCUtils;
import common.BaseContextCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by supumall on 2016/7/14.
 */
public class SendMsgTest extends BaseContextCase {
    @Autowired
    private IMemberService memberService;
    @Test
    public void testGetBalance(){
        System.out.println(SendMCUtils.getMessageNumber());
    }
    @Test
    public void testSendMsg(){
        String phone="13105187050";
        memberService.sendIndentifyCode(phone,0);
    }
    @Test
    public void testSendMessage() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.post("/app/admin/member/getIdentifyCode")
                .param("mobile","13105187050").param("type","0")
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}
