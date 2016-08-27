package com.weitaomi.application.service.impl;

import com.weitaomi.application.model.dto.InvitedParamsDto;
import com.weitaomi.application.model.dto.InvitedRecord;
import com.weitaomi.application.model.dto.TotalSharedMsg;
import com.weitaomi.application.model.mapper.MemberInvitedRecordMapper;
import com.weitaomi.application.service.interf.IMemberInvitedRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/19.
 */
@Service
public class MemberInvitedRecordService implements IMemberInvitedRecordService {
    @Autowired
    private MemberInvitedRecordMapper memberInvitedRecordMapper;
    @Override
    public InvitedParamsDto getInvitedParamsDto(Long memberId) {
        InvitedParamsDto invitedParamsDto=memberInvitedRecordMapper.getInvitedParamsDto(memberId);
        return invitedParamsDto;
    }

    @Override
    public List<InvitedRecord> getInvitedRecordList(Long memberId) {
        List<InvitedRecord> invitedRecordList=memberInvitedRecordMapper.getInvitedRecord(memberId);
        if (invitedRecordList.isEmpty()){
            return null;
        }
        return invitedRecordList;
    }
    @Override
    public List<TotalSharedMsg> getTotalSharedMsg(){
        List<TotalSharedMsg> totalSharedMsgList=memberInvitedRecordMapper.getTotalSharedMsg();
        if (totalSharedMsgList.isEmpty()){
            return null;
        }
        return totalSharedMsgList;
    }
}
