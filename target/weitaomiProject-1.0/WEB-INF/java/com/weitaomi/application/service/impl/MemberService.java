package com.weitaomi.application.service.impl;

import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.ThirdLogin;
import com.weitaomi.application.model.dto.RegisterMsg;
import com.weitaomi.application.model.mapper.MemberMapper;
import com.weitaomi.application.model.mapper.ThirdLoginMapper;
import com.weitaomi.application.service.interf.IMemberService;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.util.DateUtils;
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
        String salt= StringUtil.numRandom(6);
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
        if (!this.validateIndetifyCode(memberTemp.getTelephone())){
            throw new BusinessException("验证码错误，请重试");
        }
            memberTemp.setCreateTime(DateUtils.getUnixTimestamp());
        if (registerMsg.getFlag()!=null&&registerMsg.getFlag()==0){
            memberMapper.insertSelective(memberTemp);
            return true;
        }else if (registerMsg.getFlag()!=null&&registerMsg.getFlag()==1){
            ThirdLogin thirdLogin=registerMsg.getThirdLogin();
            if (thirdLogin==null){
                throw new BusinessException("第三方注册信息为空");
            }
            if (thirdLogin.getOpenId()==null||thirdLogin.getOpenId().isEmpty()){
                throw new BusinessException("第三方OpenId为空");
            }
            memberMapper.insertSelective(memberTemp);
            Long newMemberId = memberMapper.getByCellphoneAndPassword(memberTemp.getTelephone(), memberTemp.getPassword()).getId();
            thirdLogin.setMemberId(newMemberId);
            thirdLogin.setCreateTime(DateUtils.getUnixTimestamp());
            thirdLoginMapper.insertSelective(thirdLogin);
            return true;
        }
        return false;
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
        return member;
    }

    @Override
    public String sendIndetifyCode(String mobile, Integer type) {

        return null;
    }
    private Boolean validateIndetifyCode(String mobile) {

        return null;
    }
    /**
     * 设置缓存
     * @param key
     * @param IdentifyCode
     * @param timeout
     * @throws CacheException
     */
    private void setIndetifyCodeToCache(String key, String IdentifyCode, Long timeout)  {
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
    private String getIndetifyCodeFromCache(String key) {
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
}
