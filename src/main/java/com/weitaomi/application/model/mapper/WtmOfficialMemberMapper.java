package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.WtmOfficialMember;
import org.apache.ibatis.annotations.Param;

public interface WtmOfficialMemberMapper extends IBaseMapper<WtmOfficialMember> {
    WtmOfficialMember getWtmOfficialMemberByUnionId(@Param("unionId") String unionId);
    Long getMemberIdByOpenId(@Param("openId") String openId);
}