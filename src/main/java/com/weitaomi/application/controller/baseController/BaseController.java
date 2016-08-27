package com.weitaomi.application.controller.baseController;

import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.dto.MobileInfo;
import com.weitaomi.application.model.dto.RequestFrom;
import com.weitaomi.application.model.mapper.MemberMapper;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.InfoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Jemry.Liu on 2015/5/9.
 */
public abstract class BaseController {

    private Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Autowired
    protected RedisTemplate redisTemplate;
    @Autowired
    protected MemberMapper memberMapper;
    /**
     * 获取用户ID，用户ID可能为NULL,需自行判断
     *
     * @param request
     * @return
     */
    protected Long getUserId(HttpServletRequest request) {
        String sId = request.getHeader("memberId");

        if (!StringUtils.isEmpty(sId)) {
            try {
                Long userId = Long.parseLong(sId);
                Member member=memberMapper.selectByPrimaryKey(userId);
                if (member==null){
                    throw new InfoException("用户不存在");
                }else if (member.getSex()==3){
                    throw new InfoException("用户已经被禁用，请联系客服人员");
                }
                return userId;
            } catch (NumberFormatException e) {
                logger.warn("请求头memberId参数格式错误:{}", sId);
            }

        }

        return null;
    }

    /**
     * 获取用户ID,当userId为空的时候抛出异常
     *
     * @param request
     * @return
     * @throws BusinessException 用户ID不能为空
     */
    protected Long getNotNullUserId(HttpServletRequest request) throws BusinessException {
        Long userId = getUserId(request);
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        return userId;
    }

    /**
     * 获取请求来源类型
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    protected RequestFrom getRequestFrom(HttpServletRequest request) throws BusinessException {

        String from = request.getHeader("from");

        if (StringUtils.isEmpty(from)) {
            throw new BusinessException("请求头错误未包含来源字段");
        }

        try {
            int iFom = Integer.parseInt(from);

            return RequestFrom.getById(iFom);
        } catch (NumberFormatException e) {
            throw new BusinessException("请求头来源字段类型错误");
        }

    }


    /**
     * 获取移动端请求头信息
     *
     * @param request
     * @return MobileInfo
     * @throws BusinessException
     */
    protected MobileInfo getMobileInfo(HttpServletRequest request) throws BusinessException {
        String appVersion = request.getHeader("appVersion");
        String systemVersion = request.getHeader("appSystemVersion");
        String deviceId = request.getHeader("appDeviceId");
        Integer width = null;
        Integer height = null;
        int night = 0;
        try {
            width = Integer.parseInt(request.getHeader("appDeviceWidth"));
            height = Integer.parseInt(request.getHeader("appDeviceHeight"));
            if (request.getHeader("nightMode") != null) {
                night = Integer.parseInt(request.getHeader("nightMode"));
            }
        } catch (NumberFormatException e) {
            throw new BusinessException("移动端请求头不符合约定");
        }
        if (StringUtils.isEmpty(appVersion) || width == null || height == null) {
            throw new BusinessException("移动端请求头不符合约定");
        }

        return new MobileInfo(appVersion, systemVersion, deviceId, width, height, night != 0);
    }

    /**
     * 获取菜单ID
     *
     * @param request the request
     * @return the menu id
     */
    protected Long getMenuId(HttpServletRequest request) {

        String sId = request.getHeader("menuId");

        if (!StringUtils.isEmpty(sId)) {
            try {
                Long menuId = Long.parseLong(sId);
                return menuId;
            } catch (NumberFormatException e) {
                logger.warn("请求头menuId参数格式错误:{}", sId);
            }

        }

        return null;
    }

//    /**
//     * pc端从token中获取userId
//     * @param token
//     * @return
//     */
//    protected  Long getPcNotNullUserId(String token) throws Exception {
//        ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
//        StringBuilder sb = new StringBuilder();
//        String userId = DESUtil.decode(token).split(",")[0];
//        String key = sb.append("member:").append(":token:").append(userId).toString();
//        if (valueOper.get(key) != null && token.equals(valueOper.get(key))) {
//            return StringUtil.toLong(userId);
//        } else {
//            return null;
//        }
//    }
}
