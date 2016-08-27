package configuration;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.dto.MemberInfoDto;
import com.weitaomi.application.model.dto.MemberTaskDto;
import com.weitaomi.application.model.mapper.ArticleMapper;
import com.weitaomi.application.model.mapper.MemberMapper;
import com.weitaomi.application.model.mapper.MemberTaskHistoryMapper;
import com.weitaomi.application.model.mapper.ThirdLoginMapper;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.systemconfig.util.HttpRequestUtils;
import com.weitaomi.systemconfig.util.UUIDGenerator;
import common.BaseContextCase;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * Created by supumall on 2016/7/6.
 */
public class MyBatisTest extends BaseContextCase {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    ThirdLoginMapper thirdLoginMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    MemberTaskHistoryMapper memberTaskHistoryMapper;

    //    @Autowired
//    IMemberTaskHistoryService memberTaskHistoryService;
    @Test
    public void testAddMember() {
        Member member = new Member();
        member.setBirth(100000000L);
        member.setCreateTime(100000000L);
        member.setEmail("weitaomi@test.com");
        member.setImageUrl("/admin/image/10039689.jpg");
        member.setInvitedCode("100086");
        member.setMemberName("guest");
        member.setNickName("游客凭证");
        member.setPassword(new Sha256Hash("guest_123456", "qwerty").toString());
        member.setSalt("qwerty");
        member.setSex(0);
        member.setTelephone("13131313131");
        System.out.println(memberMapper.insertSelective(member));
        System.out.println(member.getId());
    }

    @Test
    public void testSelectMember() {
        Member member = memberMapper.selectByPrimaryKey(1L);
        System.out.println(JSON.toJSONString(member));
    }

    @Test
    public void testSelectSelf() {
        Member member = memberMapper.getMemberByTelephone("13105187050");
        System.out.println(JSON.toJSONString(member));
    }

    @Test
    public void testThridLoginMapper() {
        System.out.println("========================================>");
        System.out.println(UUIDGenerator.generate());
        System.out.println(JSON.toJSONString(thirdLoginMapper.getThirdLoginInfo("123456")));
        System.out.println("========================================>");
//        DigestUtils
    }

    @Test
    public void testPushAddAccounts() {
        String url = "http://www.yuyinggzs.com/index.php/home/js/index";
        String msg = "{\"unionId\":\"oaPViwbX7W-ddS5BKHk7PtSZxHDM\",\"url\":\"http://192.168.0.77:8001/weitaomi/frontPage/index.html\",\"flag\":\"1\"}";
        try {
            HttpRequestUtils.postStringEntity(url, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGetMemberInfo(){
        System.out.println(new Sha256Hash("123456", "HIrUyH").toString());
    }
}

