package configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.PaymentApprove;
import com.weitaomi.application.model.dto.MemberInfoDto;
import com.weitaomi.application.model.dto.MemberTaskDto;
import com.weitaomi.application.model.mapper.ArticleMapper;
import com.weitaomi.application.model.mapper.MemberMapper;
import com.weitaomi.application.model.mapper.MemberTaskHistoryMapper;
import com.weitaomi.application.model.mapper.ThirdLoginMapper;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.application.service.interf.IPaymentService;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.HttpRequestUtils;
import com.weitaomi.systemconfig.util.StringUtil;
import com.weitaomi.systemconfig.util.UUIDGenerator;
import com.weitaomi.systemconfig.wechat.WechatConfig;
import com.weitaomi.systemconfig.wechat.WechatPayParams;
import common.BaseContextCase;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private IPaymentService paymentService;

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
        String msg = "{\"unionId\":\"oaPViwd0R6XQW4MrQ2LaQTXuOTvc\",\"url\":\"http://192.168.0.77:8001/weitaomi/frontPage/index.html\",\"flag\":\"1\"}";
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
    @Test
    public void testJson(){
        List<Long> longs=new ArrayList<>();
        longs.add(1L);
        longs.add(2L);
        longs.add(3L);
        longs.add(4L);
        longs.add(5L);
        System.out.println(JSON.toJSONString(longs));

    }
    @Test
    public void testApprove(){

        PaymentApprove approve=new PaymentApprove();
        approve.setAccountNumber("oKfbJvqbJ4PlaFj-ajkrVoMaYllU");
        approve.setAccountName("宁凡荣");
        approve.setAmount(BigDecimal.valueOf(0.1));
        approve.setCreateTime(DateUtils.getUnixTimestamp());
        approve.setIsPaid(1);
        List<PaymentApprove> approves=new ArrayList<>();
        approves.add(approve);



        paymentService.patchWechatCustomers(approves,"192.168.0.77");


    }

    @Test
    public void  test2(){
        Map<String,String> params=new HashMap<>();
        params.put("appid", WechatConfig.APP_ID);
        params.put("mch_id",WechatConfig.MCH_APPID);
        params.put("nonce_str", UUIDGenerator.generate());
        params.put("out_trade_no", UUIDGenerator.generate());
        params.put("body","微淘米会员充值");
        params.put("notify_url","123456");
        params.put("trade_type","APP");
        params.put("total_fee","10");
        params.put("spbill_create_ip","10");
//        params=this.paraFilter(params);
        String pre_sign= StringUtil.formatParaMap(params);
        pre_sign=pre_sign+"key="+WechatConfig.API_KEY;
        String sign= DigestUtils.md5Hex(pre_sign).toUpperCase();
        params.put("sign",sign);
        String value=JSON.toJSONString(params);
        System.out.println(value);
        WechatPayParams wechatPayParams=JSONObject.parseObject(value,WechatPayParams.class);
        XStream xStream=new XStream();
        xStream.autodetectAnnotations(true);
        String requestParams=xStream.toXML(wechatPayParams);

        System.out.println(requestParams);
    }

    private XStream getXStream(){
        XStream xStream = new XStream();
//        xStream.autodetectAnnotations(true);
        //设置xml节点属性信息
        xStream.useAttributeFor(WechatPayParams.class,"appid");
        xStream.useAttributeFor(WechatPayParams.class,"mch_id");
        xStream.useAttributeFor(WechatPayParams.class,"nonce_str");
        xStream.useAttributeFor(WechatPayParams.class,"body");
        xStream.useAttributeFor(WechatPayParams.class,"notify_url");
        xStream.useAttributeFor(WechatPayParams.class,"trade_type");
        xStream.useAttributeFor(WechatPayParams.class,"total_fee");
        xStream.useAttributeFor(WechatPayParams.class,"spbill_create_ip");
        xStream.useAttributeFor(WechatPayParams.class,"sign");
        return xStream;
    }
}

