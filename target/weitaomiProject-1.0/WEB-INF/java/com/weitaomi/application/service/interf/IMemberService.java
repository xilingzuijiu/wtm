package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.model.bean.ThirdLogin;
import com.weitaomi.application.model.dto.*;

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
    public Boolean bindThirdPlat(Long memberId,ThirdLogin thirdLogin);

    /**
     * 会员登陆
     * @param mobileOrName
     * @param password
     * @return
     */
    public MemberInfoDto login(String mobileOrName, String password);

    /**
     * 第三方登陆
     * @param openId
     * @param type
     * @return
     */
    public MemberInfoDto thirdPlatLogin(String openId,Integer type);

    /**
     * 获取用户信息
     * @param memberId
     * @return
     */
    public Member getMemberDetailById(Long memberId);

   /**
    * 更换密码
    * @param memberId
    * @param modifyPasswordDto
    * @return
       */
     boolean modifyPassWord(Long memberId, ModifyPasswordDto modifyPasswordDto);
    /**
    * 更换密码
    * @param memberId
    * @param birth
    * @return
       */
     boolean modifyBirth(Long memberId,Long birth);

    /**
     * 更新用户地址
     * @param memberId
     * @param memberAddress
     * @return
     */
     boolean modifyMemberAddress(Long memberId, String  memberAddress);

 /**
      * 上传用户头像
      * @param memberId
      * @return
         */
    public String uploadShowImage(Long memberId,String imageFiles,String imageType);

    Boolean validateIndetifyCode(String mobile, String indentifyCode);


}
