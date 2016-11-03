package com.weitaomi.application.service.impl;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.weitaomi.application.model.bean.*;
import com.weitaomi.application.model.dto.*;
import com.weitaomi.application.model.mapper.*;
import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.application.service.interf.IMemberService;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.systemconfig.constant.SystemConfig;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.*;
import org.apache.ibatis.cache.CacheException;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/6/27.
 */
@Service
public class MemberService extends BaseService implements IMemberService {
    private Logger logger = LoggerFactory.getLogger(MemberService.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberTaskMapper memberTaskMapper;
    @Autowired
    private ThirdLoginMapper thirdLoginMapper;
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private MemberInvitedRecordMapper memberInvitedRecordMapper;
    @Autowired
    private OfficalAccountMapper officalAccountMapper;
    @Autowired
    private IMemberScoreService memberScoreService;
    @Autowired
    private IMemberTaskHistoryService memberTaskHistoryService;

    @Override
    @Transactional
    public MemberInfoDto register(RegisterMsg registerMsg,Integer sourceType) {
        if (registerMsg == null || registerMsg.getMember() == null) {
            throw new BusinessException("注册参数不能为空");
        }
        Member member = registerMsg.getMember();
        if (member.getTelephone() == null || member.getTelephone().isEmpty()) {
            throw new InfoException("手机号不能为空");
        }
        if (!member.getTelephone().matches("^[1][34578]\\d{9}$")) {
            throw new BusinessException("手机格式不正确");
        }
        String registerKey=member.getTelephone()+":register";
        Integer flag= cacheService.getCacheByKey(registerKey,Integer.class);
        if (flag!=null&&flag==1){
            throw new InfoException("手机号已经被注册，请勿重复注册");
        }
        Member memberExist = memberMapper.getMemberByTelephone(member.getTelephone(),null);
        if (memberExist != null) {
            throw new InfoException("手机号已经被注册");
        }
        String salt = StringUtil.random(6);
        String password = member.getPassword();
        Member memberTemp = new Member();
        memberTemp.setTelephone(member.getTelephone());
        if (password.length() < 6) {
            throw new InfoException("密码长度不能小于六位");
        }
        if (salt != null && !salt.isEmpty()) {
            memberTemp.setSalt(salt);
            String newSHAPassword = new Sha256Hash(password, salt).toString();
            memberTemp.setPassword(newSHAPassword);
        }
        String memberName = member.getMemberName();
        if (memberName == null || memberName.isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        Member memberFlag = memberMapper.getMemberByMemberName(memberName,null);
        if (memberFlag != null) {
            throw new InfoException("用户名已经注册");
        }
        memberTemp.setMemberName(memberName);
        if (member.getBirth() != null) {
            memberTemp.setBirth(member.getBirth());
        }
        if (member.getEmail() != null && member.getEmail().matches("[_a-z\\d\\-\\./]+@[_a-z\\d\\-]+(\\.[_a-z\\d\\-]+)*(\\.(info|biz|com|edu|gov|net|am|bz|cn|cx|hk|jp|tw|vc|vn))$")) {
            memberTemp.setEmail(member.getEmail());
        }
       if (!this.validateIndetifyCode(memberTemp.getTelephone(),registerMsg.getIdentifyCode())){
//        if (false) {
            throw new InfoException("验证码错误，请重试");
        }
        memberTemp.setSource(member.getSource());
        memberTemp.setCreateTime(DateUtils.getUnixTimestamp());
        Long newMemberId = null;
        if (registerMsg.getFlag() != null && registerMsg.getFlag() == 0) {
            memberMapper.insertSelective(memberTemp);
            newMemberId = memberTemp.getId();
            memberTemp.setInvitedCode(StringUtil.toSerialNumber(newMemberId));
            memberMapper.updateByPrimaryKeySelective(memberTemp);
        } else if (registerMsg.getFlag() != null && registerMsg.getFlag() == 1) {
            ThirdLogin thirdLogin = registerMsg.getThirdLogin();
            if (thirdLogin == null) {
                throw new BusinessException("第三方注册信息为空");
            }
            if (thirdLogin.getOpenId() == null || thirdLogin.getOpenId().isEmpty()) {
                throw new BusinessException("第三方OpenId为空");
            }
            List<Long> memberId=thirdLoginMapper.getMemberIdByUnionId(thirdLogin.getUnionId(),0);
            if (!memberId.isEmpty()){
                throw new InfoException("微信号已经绑定站内账号");
            }
            memberMapper.insertSelective(memberTemp);
            newMemberId = memberTemp.getId();
            String code = StringUtil.toSerialNumber(newMemberId);
            if (!StringUtil.isEmpty(thirdLogin.getImageFiles())) {
                String imageUrl="";
                if (memberTemp.getSource().equals("WAP")){
                    imageUrl=thirdLogin.getImageFiles();
                }else {
                    imageUrl = this.uploadShowImage(newMemberId, thirdLogin.getImageFiles(), "jpg");
                }
                memberTemp.setImageUrl(imageUrl);
            }
            memberTemp.setInvitedCode(code);
            memberTemp.setSex(thirdLogin.getSex());
            memberMapper.updateByPrimaryKeySelective(memberTemp);
            thirdLogin.setMemberId(newMemberId);
            thirdLogin.setCreateTime(DateUtils.getUnixTimestamp());
            thirdLogin.setSourceType(sourceType);
            thirdLoginMapper.insertSelective(thirdLogin);
        }
        if (registerMsg.getInvitedCode() != null && !registerMsg.getInvitedCode().isEmpty()) {
            Member memberInvited = memberMapper.getMemberByInviteCode(registerMsg.getInvitedCode());
            if (memberInvited==null){
                throw new InfoException("邀请码不正确");
            }
            if (memberInvited.getId()==newMemberId){
                throw new InfoException("亲，你不能邀请自己，真的~");
            }
            if (memberInvited != null) {
                MemberInvitedRecord memberInvitedRecordTemp = memberInvitedRecordMapper.getMemberInvitedRecordByMemberId(newMemberId);
                if (memberInvitedRecordTemp == null) {
                    MemberInvitedRecord memberInvitedRecord = new MemberInvitedRecord();
                    memberInvitedRecord.setMemberId(newMemberId);
                    memberInvitedRecord.setParentId(memberInvited.getId());
                    memberInvitedRecord.setIsAccessible(1);
                    memberInvitedRecord.setCreateTime(DateUtils.getUnixTimestamp());
                    int num = memberInvitedRecordMapper.insertSelective(memberInvitedRecord);
                    if (num > 0) {
                        MemberTask memberTask = memberTaskMapper.selectByPrimaryKey(3L);
                        memberTaskHistoryService.addMemberTaskToHistory(memberInvitedRecord.getParentId(), 3L, null, 1, null, null,null);
                        memberScoreService.addMemberScore(memberInvited.getId(), 3L,1, memberTask.getPointCount().doubleValue(), UUIDGenerator.generate());
                    }
                }
            }
        }
        cacheService.setCacheByKey(registerKey,1,null);
        if (registerMsg.getFlag() == 1) {
            return memberMapper.getMemberByTelephone(registerMsg.getMember().getTelephone(), sourceType);
        }else {
            return memberMapper.getMemberByTelephone(registerMsg.getMember().getTelephone(), null);
        }
    }

    @Override
    @Transactional
    public Boolean bindThirdPlat(Long memberId, ThirdLogin thirdLogin,Integer sourceType) {
        if (thirdLogin == null) {
            throw new BusinessException("第三方注册信息为空");
        }
        thirdLogin.setMemberId(memberId);
        thirdLogin.setSourceType(sourceType);
        if (thirdLogin.getOpenId() == null || thirdLogin.getOpenId().isEmpty()) {
            throw new BusinessException("第三方OpenId为空");
        }
        if (thirdLogin.getMemberId() == null) {
            throw new BusinessException("绑定的用户Id为空");
        }
        if (thirdLogin.getType() == null) {
            throw new BusinessException("登陆平台类型为空");
        }
        List<ThirdLogin> thirdLoginTemp = thirdLoginMapper.getThirdLoginInfo(thirdLogin.getUnionId(),null);
        if(!thirdLoginTemp.isEmpty() && memberId.longValue() != ((ThirdLogin)thirdLoginTemp.get(0)).getMemberId().longValue()) {
            throw new InfoException("微淘米账号和和要绑定的微信不匹配，您已在其他终端绑定过一个账号，请绑定该账号");
        }
        List<ThirdLogin> thirdLoginFlag = thirdLoginMapper.getThirdLoginInfo(thirdLogin.getUnionId(),sourceType);
        if (!thirdLoginFlag.isEmpty()) {
            throw new InfoException("您已经绑定此账号");
        }
        ThirdLogin thirdLogins = thirdLoginMapper.getUnionIdByMemberId(memberId,sourceType);
        if (thirdLogins!=null){
            throw new InfoException("该账号已经绑定一个微信号，微信昵称为："+thirdLogins.getNickname());
        }
        thirdLogin.setCreateTime(DateUtils.getUnixTimestamp());
        Member member = memberMapper.selectByPrimaryKey(memberId);
        if (member==null){
            throw new InfoException("用户信息为空");
        }
        member.setSex(thirdLogin.getSex());
        if (!StringUtil.isEmpty(thirdLogin.getImageFiles())&&thirdLogin.getImageFiles().contains("http")){
            if (member.getImageUrl()=="http://weitaomi.b0.upaiyun.com/member/showMessage/000000.png") {
                member.setImageUrl(thirdLogin.getImageFiles());
            }
        }
        memberMapper.updateByPrimaryKeySelective(member);
        Integer num = thirdLoginMapper.insertSelective(thirdLogin);
        return num > 0 ? true : false;
    }

    @Override
    public MemberInfoDto login(String mobileOrName, String password,Integer sourceType) {
        if (mobileOrName != null) {
            MemberInfoDto member = null;
            if (mobileOrName.matches("^[1][34578]\\d{9}$")) {
                logger.info("手机登陆用户");
                int num=memberMapper.getIsBindWxAccountsByMobile(mobileOrName,sourceType);
                if (num<=0){
                    sourceType=null;
                }
                member = memberMapper.getMemberByTelephone(mobileOrName,sourceType);
                if (member == null) {
                    throw new InfoException("手机号未注册，请注册");
                }
                if(member.getSex()==3){
                    throw new InfoException("用户已经被禁用，请联系客服人员");
                }
            } else {
                int num=memberMapper.getIsBindWxAccountsByMemberName(mobileOrName,sourceType);
                if (num<=0){
                    sourceType=null;
                }
                member = memberMapper.getMemberByMemberName(mobileOrName,sourceType);
                if (member == null) {
                    throw new InfoException("用户不存在，请注册");
                }
                logger.info("用户名登陆用户");
            }
            if (!member.getPassword().equals(new Sha256Hash(password, member.getSalt()).toString())) {
                throw new InfoException("用户名或密码错误");
            }

            String key = "member:login:" + member.getId();
            Integer times = 60 * 60 * 24 * 30;
            cacheService.setCacheByKey(key, member, times);
            if (sourceType==null){
                member.setThirdLogin(null);
            }
            return member;
        } else {
            throw new InfoException("用户名/密码为空");
        }
    }

    @Override
    public MemberInfoDto thirdPlatLogin(String unionId,String openid, Integer type,Integer sourceType) {
        if (unionId == null || unionId.isEmpty()) {
            throw new BusinessException("第三方OpenId为空");
        }
        List<ThirdLogin> thirdLogin = thirdLoginMapper.getThirdLoginInfo(unionId,null);
        if (thirdLogin.isEmpty()) {
            throw new BusinessException("该微信账号未绑定App账号，若有App账号，请登录后绑定微信，或者注册App账户并自动绑定微信");
        }else {
            List<ThirdLogin> thirdLoginMsg = thirdLoginMapper.getThirdLoginInfo(unionId,sourceType);
            if (!thirdLoginMsg.isEmpty()){
                if (!thirdLogin.get(0).getType().equals(type)) {
                    throw new InfoException("登录平台与OpenID不匹配");
                }
                if (thirdLoginMsg.get(0).getMemberId() == null) {
                    throw new InfoException("该平台账号未绑定，请重新绑定");
                }
                MemberInfoDto member = memberMapper.getMemberInfoById(thirdLoginMsg.get(0).getMemberId(),sourceType);
                if (member == null) {
                    throw new InfoException("微信号未绑定，请重新注册");
                }
                if(member.getSex()==3){
                    throw new InfoException("用户已经被禁用，请联系客服人员");
                }
                String key = "member:login:" + member.getId();
                Integer times = 60 * 60 * 24 * 30;
                cacheService.setCacheByKey(key, member, times);
                return member;
            }
            else {
                int typeValue=0;
                if (sourceType==0){
                    typeValue=1;
                }else if (sourceType==1){
                    typeValue=0;
                }
                List<ThirdLogin> thirdLoginMsList = thirdLoginMapper.getThirdLoginInfo(unionId,typeValue);
                if (thirdLoginMsList.isEmpty()){
                    throw new InfoException("该微信账号未绑定App账号，若有App账号，请登录后绑定微信，或者注册App账户并自动绑定微信");
                }
                ThirdLogin thirdLoginMs=thirdLoginMsList.get(0);
                thirdLoginMs.setOpenId(openid);
                thirdLoginMs.setSourceType(sourceType);
                thirdLoginMs.setId(null);
                thirdLoginMapper.insertSelective(thirdLoginMs);
                MemberInfoDto member = memberMapper.getMemberInfoById(thirdLoginMs.getMemberId(),sourceType);
                if (member == null) {
                    throw new InfoException("微信号未绑定，请重新注册");
                }
                if(member.getSex()==3){
                    throw new InfoException("用户已经被禁用，请联系客服人员");
                }
                String key = "member:login:" + member.getId();
                Integer times = 60 * 60 * 24 * 30;
                cacheService.setCacheByKey(key, member, times);
                return member;
            }
        }

    }

    /**
     * 设置缓存
     *
     * @param key
     * @param IdentifyCode
     * @param timeout
     * @throws CacheException
     */
    private void setIndentifyCodeToCache(String key, String IdentifyCode, Long timeout) {
        Long timeoutTemp = timeout;
        if (timeout == null) {
            timeoutTemp = 120L;
        }
        ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
        valueOper.set(key, IdentifyCode, timeoutTemp, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    private String getIndentifyCodeFromCache(String key) {
        ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
        StringBuilder sb = new StringBuilder();
        String type = null;
        String value = valueOper.get(key);
        if (value != null) {
            return value;
        }
        return "00000000";
    }

    @Override
    public Member getMemberDetailById(Long memberId) {
        String key = "member:login:" + memberId;
        Member member = cacheService.getCacheByKey(key, Member.class);
        if (member == null) {
            throw new InfoException("请先完成登陆");
        }
        return memberMapper.selectByPrimaryKey(memberId);
    }

    @Override
    public String modifyPassWord(Long memberId, ModifyPasswordDto modifyPasswordDto) {
        boolean flag = false;
        if (modifyPasswordDto == null) {
            throw new InfoException("密码信息为空，请重新修改或者登录");
        }
        if (modifyPasswordDto.getFlag() == 0) {
            Member member = memberMapper.getMemberByTelephone(modifyPasswordDto.getMobile(),null);
            if (member == null) {
                throw new InfoException("用户不存在");
            }
            String salt = "";
            if (!StringUtil.isEmpty(member.getSalt())) {
                salt = member.getSalt();
            } else {
                salt = StringUtil.random(6);
            }
            member.setPassword(new Sha256Hash(modifyPasswordDto.getNewPassword(), salt).toString());
            int number = memberMapper.updateByPrimaryKeySelective(member);
            flag = number > 0 ? true : false;
            if (flag){
                return member.getPassword();
            }
        } else if (modifyPasswordDto.getFlag() == 1) {
            Member member = memberMapper.selectByPrimaryKey(memberId);
            if (member == null) {
                throw new InfoException("用户不存在");
            }
            if (!new Sha256Hash(modifyPasswordDto.getPassword(), member.getSalt()).toString().equals(member.getPassword())) {
                throw new InfoException("原密码错误");
            }
            String salt = "";
            if (!StringUtil.isEmpty(member.getSalt())) {
                salt = member.getSalt();
            } else {
                salt = StringUtil.random(6);
            }
            member.setPassword(new Sha256Hash(modifyPasswordDto.getNewPassword(), salt).toString());
            int number = memberMapper.updateByPrimaryKeySelective(member);
            flag = number > 0 ? true : false;
            if (flag){
                return member.getPassword();
            }
        }

        return "修改失败";
    }

    @Override
    public boolean modifyBirth(Long memberId, Long birth) {
        Member member = memberMapper.selectByPrimaryKey(memberId);
        member.setBirth(birth);
        int num = memberMapper.updateByPrimaryKeySelective(member);
        return num > 0;
    }

    @Override
    public boolean modifyMemberAddress(Long memberId, String memberAddress) {
        Member member = memberMapper.selectByPrimaryKey(memberId);
        String[] address = memberAddress.split(",");
        if (address.length == 4) {
            member.setProvince(address[0]);
            member.setCity(address[1]);
            member.setArea(address[2]);
            member.setMemberAddress(address[3]);
        }
        int number = memberMapper.updateByPrimaryKeySelective(member);
        return number > 0 ? true : false;
    }
    @Override
    public boolean modifyMemberName(Long memberId, String memberName) {
        Member member = memberMapper.selectByPrimaryKey(memberId);
        if (member==null){
            throw new InfoException("账户不存在");
        }
        if (memberName.equals(member.getMemberName())){
            throw new InfoException("用户名不能相同");
        }
        Member member1=memberMapper.getMemberByMemberName(memberName,null);
        if (member1!=null){
            throw new InfoException("用户名已经存在");
        }
        member.setMemberName(memberName);
        int number = memberMapper.updateByPrimaryKeySelective(member);
        return number > 0 ? true : false;
    }

    @Override
    @Transactional
    public String uploadShowImage(Long memberId, String imageFiles, String imageType) {
        String imageUrl = "/member/showImage/" + memberId + DateUtils.getUnixTimestamp() + "." + imageType;
        if (super.uploadImage(imageUrl, imageFiles)) {
            memberMapper.upLoadMemberShowImage(memberId, SystemConfig.UPYUN_PREFIX+imageUrl);
        }
        return SystemConfig.UPYUN_PREFIX+imageUrl;
    }

    @Override
    public String sendIndentifyCode(String mobile, Integer type) {
        if (!mobile.matches("^[1][34578]\\d{9}$")) {
            throw new BusinessException("手机号码格式不正确");
        }
        String key = "member:indentifyCode:" + mobile;
        String value = this.getIndentifyCodeFromCache(key);
        if (value != null && !value.equals("00000000")) {
            throw new InfoException("验证码已发送，请120s后重试");
        }
        String identifyCode = StringUtil.numRandom(6);
        String content = null;
        content = MessageFormat.format(new String(PropertiesUtil.getValue("verifycode.msg")), identifyCode);
        SendMCUtils.sendMessage(mobile, content);
        this.setIndentifyCodeToCache(key, identifyCode, 120L);
        return identifyCode;
    }

    @Override
    public Boolean validateIndetifyCode(String mobile, String indentifyCode) {
        String key = "member:indentifyCode:" + mobile;
        String value = this.getIndentifyCodeFromCache(key);
        if (value == null) {
            throw new InfoException("验证码未发送");
        }
        if (indentifyCode.equals(value)) {
            return true;
        }
        return false;
    }
}
