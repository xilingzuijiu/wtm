package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.ThirdLogin;
import org.apache.ibatis.annotations.Param;

public interface MemberMapper extends IBaseMapper<Member> {
    Member getMemberByTelephone(@Param("telephone")String telephone);
    Member getMemberByMemberName(@Param("memberName")String memberName);
    Member getMemberByInviteCode(@Param("inviteCode")String inviteCode);
    public Member getByCellphoneAndPassword(@Param("cellphone")String cellphone,@Param("password")String password);
    int upLoadMemberShowImage(@Param("memberId") Long memberId, @Param("imageUrl") String imageUrl);
}