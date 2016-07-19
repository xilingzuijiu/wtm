package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.Member;
import org.apache.ibatis.annotations.Param;

public interface MemberMapper extends IBaseMapper<Member> {
    Member getMemberByTelephone(@Param("telephone")String telephone);
    Member getMemberByMemberName(@Param("memberName")String memberName);
    public Member getByCellphoneAndPassword(@Param("cellphone")String cellphone,@Param("password")String password);
}