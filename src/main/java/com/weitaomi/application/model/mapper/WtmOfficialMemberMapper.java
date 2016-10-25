package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.WtmOfficialMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WtmOfficialMemberMapper extends IBaseMapper<WtmOfficialMember> {
    WtmOfficialMember getWtmOfficialMemberByUnionId(@Param("unionId") String unionId);
    List<Long> getMemberIdByOpenId(@Param("openId") String openId);
}