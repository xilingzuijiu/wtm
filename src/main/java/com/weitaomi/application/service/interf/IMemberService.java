package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.ThirdLogin;
import com.weitaomi.application.model.dto.InvitedRecord;
import com.weitaomi.application.model.dto.RegisterMsg;

import java.util.List;

/**
 * Created by Administrator on 2016/6/27.
 */
public interface IMemberService {
    /**
     * 用户注册
     * @param registerMsg
     * @return
     */
   public Boolean register(RegisterMsg registerMsg);

    /**
     * 获取验证码
     * @param mobile
     * @param type
     * @return
     */
    public String sendIndentifyCode(String mobile,Integer type);

    /**
     * 绑定第三方平台
     * @param thirdLogin
     * @return
     */
    public Boolean bindThirdPlat(ThirdLogin thirdLogin);

    /**
     * 会员登陆
     * @param mobileOrName
     * @param password
     * @return
     */
    public Member login(String mobileOrName,String password);

    /**
     * 第三方登陆
     * @param openId
     * @param type
     * @return
     */
    public Member thirdPlatLogin(String openId,Integer type);

    /**
     * 获取用户信息
     * @param memberId
     * @return
     */
    public Member getMemberDetailById(Long memberId);

     /**
      * 获取邀请记录
      * @param memberId
      * @return
      */
    public List<InvitedRecord> getInvitedRecord(Long memberId);
}
