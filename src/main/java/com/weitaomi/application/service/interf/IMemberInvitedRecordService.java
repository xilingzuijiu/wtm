package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.MemberInvitedRecord;
import com.weitaomi.application.model.dto.InvitedParamsDto;
import com.weitaomi.application.model.dto.InvitedRecord;
import com.weitaomi.application.model.dto.TotalSharedMsg;
import com.weitaomi.systemconfig.util.Page;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public interface IMemberInvitedRecordService {
    /**
     * 获取邀请记录
     * @param memberId
     * @return
     */
    public InvitedParamsDto getInvitedParamsDto(Long memberId);

    /**
     * 获取用户邀请记录
     * @param memberId
     * @return
     */
    public Page<InvitedRecord> getInvitedRecordList(Long memberId, Integer pageIndex, Integer pageSize);

    /**
     * 获取邀请达人记录
     * @return
     */
    List<TotalSharedMsg> getTotalSharedMsg();
}
