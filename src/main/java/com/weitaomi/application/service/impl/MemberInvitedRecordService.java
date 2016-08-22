package com.weitaomi.application.service.impl;

import com.weitaomi.application.model.dto.InvitedParamsDto;
import com.weitaomi.application.model.dto.InvitedRecord;
import com.weitaomi.application.model.mapper.MemberInvitedRecordMapper;
import com.weitaomi.application.service.interf.IMemberInvitedRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/19.
 */
public class MemberInvitedRecordService implements IMemberInvitedRecordService {
    @Autowired
    private MemberInvitedRecordMapper memberInvitedRecordMapper;
    @Override
    public InvitedParamsDto getInvitedParamsDto(Long memberId) {
        InvitedParamsDto invitedParamsDto=memberInvitedRecordMapper.getInvitedParamsDto(memberId);
        return invitedParamsDto;
    }
}
