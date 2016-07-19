package configuration;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.mapper.MemberMapper;
import common.BaseContextCase;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by supumall on 2016/7/6.
 */
public class MyBatisTest extends BaseContextCase{
    @Autowired
    private MemberMapper memberMapper;
    @Test
    public void testAddMember(){
        Member member=new Member();
        member.setBirth(100000000L);
        member.setCreateTime(100000000L);
        member.setEmail("1098795459@qq.com");
        member.setImageUrl("/admin/image/10039689.jpg");
        member.setInvitedCode("100086");
        member.setMemberName("xilingzuijiu");
        member.setNickName("猪八戒");
        member.setPassword(new Sha256Hash("123456789","asdfghj").toString());
        member.setSalt("asdfghj");
        member.setSex(0);
        member.setTelephone("13105187050");
        System.out.println(memberMapper.insertSelective(member));
        System.out.println(member.getId());
    }
    @Test
    public void testSelectMember(){
        Member member= memberMapper.selectByPrimaryKey(1L);
        System.out.println(JSON.toJSONString(member));
    }
    @Test
    public void testSelectSelf(){
        Member member= memberMapper.getMemberByTelephone("13105187050");
        System.out.println(JSON.toJSONString(member));
    }
}
