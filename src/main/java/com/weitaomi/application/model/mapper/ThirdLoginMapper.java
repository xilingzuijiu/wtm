package com.weitaomi.application.model.mapper;
import com.weitaomi.application.model.bean.ThirdLogin;
import org.apache.ibatis.annotations.Param;

public interface ThirdLoginMapper extends IBaseMapper<ThirdLogin> {
    public ThirdLogin getThirdLoginInfo(@Param("openId") String openId);
    Long getMemberIdByUnionId(@Param("unionId") String unionId);
}