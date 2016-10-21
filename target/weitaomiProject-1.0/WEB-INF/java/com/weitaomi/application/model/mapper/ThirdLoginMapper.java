package com.weitaomi.application.model.mapper;
import com.weitaomi.application.model.bean.ThirdLogin;
import com.weitaomi.application.model.dto.ThirdLoginDto;
import org.apache.ibatis.annotations.Param;

public interface ThirdLoginMapper extends IBaseMapper<ThirdLogin> {
    public ThirdLogin getThirdLoginInfo(@Param("unionId") String unionId, @Param("sourceType") Integer sourceType);
    public ThirdLogin getThirdLoginByOpenId(@Param("openId") String openId);
    public ThirdLoginDto getThirdlogInDtoMemberId(@Param("memberId") Long memberId, @Param("sourceType") Integer sourceType);
    Long getMemberIdByUnionId(@Param("unionId") String unionId, @Param("sourceType") Integer sourceType);
    ThirdLogin getUnionIdByMemberId(@Param("memberId") Long memberId, @Param("sourceType") Integer sourceType);
}