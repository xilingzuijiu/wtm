package com.weitaomi.systemconfig.realm;

import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.mapper.MemberMapper;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.SystemException;
import com.weitaomi.systemconfig.token.StatelessToken;
import com.weitaomi.systemconfig.util.HmacSHA256Utils;
import com.weitaomi.systemconfig.util.StringUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by supumall on 2016/8/6.
 */
public class MyImplementRealm extends AuthorizingRealm {
    private static Logger logger= LoggerFactory.getLogger(MyImplementRealm.class);
    @Autowired
    private MemberMapper memberMapper;
    @Override
    public boolean supports(AuthenticationToken token) {
        //仅支持StatelessToken类型的Token
        return token instanceof StatelessToken;
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username=String.valueOf(principalCollection.getPrimaryPrincipal());
        Member member=memberMapper.getMemberByTelephone(username,null);
        if (member!=null){
            return new SimpleAuthorizationInfo();
        } else {
            logger.error("授权失败");
            throw new IncorrectCredentialsException();
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("开始验证realm");
        StatelessToken statelessToken = (StatelessToken) authenticationToken;
        Map<String,String> params = (Map<String, String>) statelessToken.getParams();
        String username=statelessToken.getUsername();
        String password=this.getPassword(username);
        String serverDigest= HmacSHA256Utils.digest(password,params);
        return new SimpleAuthenticationInfo(username,serverDigest,super.getName());
    }
    private String getPassword(String username){
        Member member=memberMapper.getMemberByMemberName(username,null);
        if (member==null){
            throw new SystemException("shiro验证失败，该用户不存在或者无此权限");
        }else {
            String password=member.getPassword();
            if (StringUtil.isEmpty(password)){
                throw new BusinessException("获取用户密码失败");
            }
            return password;
        }
    }
}
