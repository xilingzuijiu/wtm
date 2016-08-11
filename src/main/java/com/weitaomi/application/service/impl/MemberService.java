package com.weitaomi.application.service.impl;

import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.MemberInvitedRecord;
import com.weitaomi.application.model.bean.ThirdLogin;
import com.weitaomi.application.model.dto.InvitedRecord;
import com.weitaomi.application.model.dto.RegisterMsg;
import com.weitaomi.application.model.mapper.MemberInvitedRecordMapper;
import com.weitaomi.application.model.mapper.MemberMapper;
import com.weitaomi.application.model.mapper.ThirdLoginMapper;
import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.application.service.interf.IMemberService;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.PropertiesUtil;
import com.weitaomi.systemconfig.util.SendMCUtils;
import com.weitaomi.systemconfig.util.StringUtil;
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
public class MemberService implements IMemberService {
    private Logger logger= LoggerFactory.getLogger(MemberService.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private ThirdLoginMapper thirdLoginMapper;
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private MemberInvitedRecordMapper memberInvitedRecordMapper;
    @Override
    @Transactional
    public Boolean register(RegisterMsg registerMsg) {
        if (registerMsg==null||registerMsg.getMember()==null){
            throw new BusinessException("注册参数不能为空");
        }
        Member member=registerMsg.getMember();
        if (member.getTelephone()==null||member.getTelephone().isEmpty()){
            throw new BusinessException("手机号不能为空");
        }
        if (!member.getTelephone().matches("^[1][34578]\\d{9}$")){
            throw new BusinessException("手机格式不正确");
        }
        Member memberExist=memberMapper.getMemberByTelephone(member.getTelephone());
        if (memberExist!=null){
            throw new BusinessException("手机号已经被注册");
        }
        String salt= StringUtil.random(6);
        String password=member.getPassword();
        Member memberTemp=new Member();
        memberTemp.setTelephone(member.getTelephone());
        if (password.length()<6){
            throw new BusinessException("密码长度不能小于六位");
        }
        if (salt!=null&&!salt.isEmpty()) {
            memberTemp.setSalt(salt);
            String newSHAPassword = new Sha256Hash(password, salt).toString();
            memberTemp.setPassword(newSHAPassword);
        }
        String memberName=member.getMemberName();
        if (memberName==null||memberName.isEmpty()){
            throw new BusinessException("用户名不能为空");
        }
        Member memberFlag=memberMapper.getMemberByTelephone(memberName);
        if (memberFlag!=null){
            throw new BusinessException("用户名已经注册");
        }
        memberTemp.setMemberName(memberName);
        if (member.getBirth()!=null){
            memberTemp.setBirth(member.getBirth());
        }
        if (member.getEmail()!=null&&member.getEmail().matches("[_a-z\\d\\-\\./]+@[_a-z\\d\\-]+(\\.[_a-z\\d\\-]+)*(\\.(info|biz|com|edu|gov|net|am|bz|cn|cx|hk|jp|tw|vc|vn))$")){
            memberTemp.setEmail(member.getEmail());
        }
        if (!this.validateIndetifyCode(memberTemp.getTelephone(),registerMsg.getIdentifyCode())){
            throw new BusinessException("验证码错误，请重试");
        }
        memberTemp.setSource(member.getSource());
        memberTemp.setCreateTime(DateUtils.getUnixTimestamp());
        Long newMemberId=null;
        if (registerMsg.getFlag()!=null&&registerMsg.getFlag()==0){
            memberMapper.insertSelective(memberTemp);
            newMemberId=memberTemp.getId();
        }else if (registerMsg.getFlag()!=null&&registerMsg.getFlag()==1){
            ThirdLogin thirdLogin=registerMsg.getThirdLogin();
            if (thirdLogin==null){
                throw new BusinessException("第三方注册信息为空");
            }
            if (thirdLogin.getOpenId()==null||thirdLogin.getOpenId().isEmpty()){
                throw new BusinessException("第三方OpenId为空");
            }
            memberMapper.insertSelective(memberTemp);
            newMemberId =memberTemp.getId();
            String code=StringUtil.toSerialNumber(newMemberId);
            memberTemp.setInvitedCode(code);
            memberMapper.updateByPrimaryKeySelective(memberTemp);
            thirdLogin.setMemberId(newMemberId);
            thirdLogin.setCreateTime(DateUtils.getUnixTimestamp());
            thirdLoginMapper.insertSelective(thirdLogin);
        }
        if (registerMsg.getInvitedCode()!=null&&!registerMsg.getInvitedCode().isEmpty()){
            Member memberInvited=memberMapper.getMemberByInviteCode(registerMsg.getInvitedCode());
            if (memberInvited!=null) {
                MemberInvitedRecord memberInvitedRecordTemp=memberInvitedRecordMapper.getMemberInvitedRecordByMemberId(newMemberId);
                if (memberInvitedRecordTemp==null) {
                    MemberInvitedRecord memberInvitedRecord = new MemberInvitedRecord();
                    memberInvitedRecord.setMemberId(newMemberId);
                    memberInvitedRecord.setParentId(memberInvited.getId());
                    memberInvitedRecord.setIsAccessible(1);
                    memberInvitedRecord.setCreateTime(DateUtils.getUnixTimestamp());
                    memberInvitedRecordMapper.insertSelective(memberInvitedRecord);
                }
            }
        }
        return true;
    }
    @Override
    @Transactional
    public Boolean bindThirdPlat(ThirdLogin thirdLogin) {
        if (thirdLogin==null){
            throw new BusinessException("第三方注册信息为空");
        }
        if (thirdLogin.getOpenId()==null||thirdLogin.getOpenId().isEmpty()){
            throw new BusinessException("第三方OpenId为空");
        }
        if (thirdLogin.getUnionId()==null||thirdLogin.getOpenId().isEmpty()){
            throw new BusinessException("第三方OpenId为空");
        }
        if (thirdLogin.getMemberId()==null){
            throw new BusinessException("绑定的用户Id为空");
        }
        if (thirdLogin.getType()==null){
            throw new BusinessException("登陆平台类型为空");
        }
        ThirdLogin thirdLoginFlag=thirdLoginMapper.getThirdLoginInfo(thirdLogin.getOpenId());
        if (thirdLoginFlag!=null){
            throw new BusinessException("该用户已经绑定此账号");
        }
        thirdLogin.setCreateTime(DateUtils.getUnixTimestamp());
        Integer num=thirdLoginMapper.insertSelective(thirdLogin);
        return num>0?true:false;
    }

    @Override
    public Member login(String mobileOrName, String password) {
        if (mobileOrName!=null) {
            Member member=null;
            if (mobileOrName.matches("^[1][34578]\\d{9}$")) {
                logger.info("手机登陆用户");
                member=memberMapper.getMemberByTelephone(mobileOrName);
                if (member==null){
                    throw new BusinessException("手机号未注册，请注册");
                }
            }else {
                member=memberMapper.getMemberByMemberName(mobileOrName);
                if (member==null){
                    throw new BusinessException("用户不存在，请注册");
                }
                logger.info("用户名登陆用户");
            }
            if (!member.getPassword().equals(new Sha256Hash(password,member.getSalt()).toString())){
                throw new BusinessException("用户名或密码错误");
            }
            String key="member:login:"+member.getId();
            Integer times=60*60*24*30;
            cacheService.setCacheByKey(key,member,times);
            return member;
        }else{
            throw new BusinessException("用户名/密码为空");
        }
    }

    @Override
    public Member thirdPlatLogin(String openId, Integer type) {
        if (openId==null||openId.isEmpty()){
            throw new BusinessException("第三方OpenId为空");
        }
        ThirdLogin thirdLogin=thirdLoginMapper.getThirdLoginInfo(openId);
        if (!thirdLogin.getType().equals(type)){
            throw new BusinessException("登录平台与OpenID不匹配");
        }
        if (thirdLogin.getMemberId()==null){
            throw new BusinessException("该平台账号未绑定，请重新绑定");
        }
        Member member=memberMapper.selectByPrimaryKey(thirdLogin.getMemberId());
        if (member==null){
            throw new BusinessException("获取账号失败，请重试");
        }
        String key="member:login:"+member.getId();
        Integer times=60*60*24*30;
        cacheService.setCacheByKey(key,member,times);
        return member;
    }
    /**
     * 设置缓存
     * @param key
     * @param IdentifyCode
     * @param timeout
     * @throws CacheException
     */
    private void setIndentifyCodeToCache(String key, String IdentifyCode, Long timeout)  {
        Long timeoutTemp = timeout;
        if (timeout == null) {
            timeoutTemp = 120L;
        }
        ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
        valueOper.set(key, IdentifyCode, timeoutTemp, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    private String getIndentifyCodeFromCache(String key) {
        ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
        StringBuilder sb = new StringBuilder();
        String type = null;
        String value = valueOper.get(key);
        if (value == null) {
            throw new BusinessException("验证码错误");
        } else {
            return value;
        }
    }
    @Override
    public Member getMemberDetailById(Long memberId) {
        String key="member:login:"+memberId;
        Member member= cacheService.getCacheByKey(key,Member.class);
        if(member==null){
            throw new BusinessException("请先完成登陆");
        }
        return memberMapper.selectByPrimaryKey(memberId);
    }

    @Override
    public List<InvitedRecord> getInvitedRecord(Long memberId) {
        List<InvitedRecord> invitedRecordList=memberInvitedRecordMapper.getInvitedRecord(memberId);
        if (invitedRecordList!=null&&!invitedRecordList.isEmpty()){
            return invitedRecordList;
        }
        return null;
    }

    @Override
    public String sendIndentifyCode(String mobile, Integer type) {
        if (!mobile.matches("^[1][34578]\\d{9}$")) {
            throw new BusinessException("手机号码格式不正确");
        }
        String identifyCode=StringUtil.numRandom(6);
        String content= null;
        content = MessageFormat.format(new String(PropertiesUtil.getValue("verifycode.msg")),identifyCode);
        SendMCUtils.sendMessage(mobile,content);
        String key="member:indentifyCode:"+mobile;
        this.setIndentifyCodeToCache(key,identifyCode,120L);
        return identifyCode;
    }
    private Boolean validateIndetifyCode(String mobile,String indentifyCode) {
        String key="member:indentifyCode:"+mobile;
        String value =this.getIndentifyCodeFromCache(key);
        if(indentifyCode.equals(value)){
            return true;
        }
        return false;
    }

}
