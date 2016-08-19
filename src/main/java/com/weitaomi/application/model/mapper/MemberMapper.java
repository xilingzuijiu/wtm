package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.ThirdLogin;
import com.weitaomi.application.model.dto.MemberInfoDto;
import org.apache.ibatis.annotations.Param;

public interface MemberMapper extends IBaseMapper<Member> {
    MemberInfoDto getMemberByTelephone(@Param("telephone")String telephone);
    MemberInfoDto getMemberInfoById(@Param("memberId")Long memberId);
    MemberInfoDto getMemberByMemberName(@Param("memberName")String memberName);
    Member getMemberByInviteCode(@Param("inviteCode")String inviteCode);
    public Member getByCellphoneAndPassword(@Param("cellphone")String cellphone,@Param("password")String password);
    int upLoadMemberShowImage(@Param("memberId") Long memberId, @Param("imageUrl") String imageUrl);
}