package com.weitaomi.systemconfig.filter;

import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.systemconfig.constant.ActionConstants;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.token.StatelessToken;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.StringUtil;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by supumall on 2016/8/6.
 */
public class StatelessAuthenticFilter extends AccessControlFilter {
    private static Logger logger= LoggerFactory.getLogger(StatelessAuthenticFilter.class);
    @Autowired
    private ICacheService cacheService;
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest httpServletRequest=(HttpServletRequest) servletRequest;
        //authentication = username:randomkey:digest      digest=time+username+randomkey+url;
        String authentication=httpServletRequest.getHeader(ActionConstants.PARAM_AUTHENTICATION);
        String uri=httpServletRequest.getRequestURI();
        logger.info("请求路径为 :"+uri);
        Long time=Long.valueOf(httpServletRequest.getHeader("time"));
        if (StringUtil.isEmpty(authentication)||StringUtil.isEmpty(uri)||time==null){
            throw new BusinessException("非法请求参数");
        }
        if (!this.isAccessTime(time)){
            logger.warn("time:{}",time);
            throw new BusinessException("非法的请求时间");
        }
        Map<String,String> map=this.formLinkedMap(authentication,uri,time);
        String username=map.get("username");
        String randomkey=map.get("randomkey");
        String digest=map.get("digest");
        map.remove("digest");
        if (!this.isAccessRandomkey(randomkey)){
            throw new BusinessException("该缓存随机数不可用");
        }
        //生成无状态的token
        StatelessToken statelessToken=new StatelessToken(username,map,digest);
        //交给设置的realm进行验证处理
        try {
            logger.info("客户端信息：randomKey：{}，time:{},url:{},username:{},digest:{}",randomkey,time,uri,username,digest);
            super.getSubject(servletRequest,servletResponse).login(statelessToken);
        }catch (Exception e){
            logger.info("login failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private boolean isAccessTime(Long time){
        Long now= DateUtils.getUnixTimestamp();
        if (Math.abs(now-time)>600){
            return false;
        }
        return true;
    }
    private Map<String,String> formLinkedMap(String authentication,String url,Long time){
        Map<String,String> map=new LinkedHashMap<String, String>();
        String[] params=authentication.split(":");
        map.put("time",time.toString());
        map.put("username",params[0]);
        map.put("randomkey",params[1]);
        map.put("url",url);
        map.put("digest",params[2]);
        return map;
    }
    private boolean isAccessRandomkey(String randomkey){
        String key="shiro:randomkey:"+randomkey;
        String value=cacheService.getCacheByKey(key,String.class);
        if (value!=null){
            return false;
        } else {
            cacheService.setCacheByKey(key,"1",3600);
            return true;
        }
    }
}
