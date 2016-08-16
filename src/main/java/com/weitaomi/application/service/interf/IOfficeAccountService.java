package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.dto.AddOfficalAccountDto;
import com.weitaomi.application.model.dto.AddResponseTaskDto;
import com.weitaomi.application.model.dto.OfficialAccountMsg;
import com.weitaomi.application.model.dto.UnFollowOfficicalAccountDto;

import java.util.List;

/**
 * Created by supumall on 2016/8/9.
 */
public interface IOfficeAccountService {
    /**
     * 获取待关注公众号列表
     * @param memberId
     * @return
     */
    public List<UnFollowOfficicalAccountDto> getAccountsByMemberId(Long memberId);

    /**
     * 推送关注任务
     * @param addOfficalAccountDto
     */
    public void pushAddRequest(AddOfficalAccountDto addOfficalAccountDto);

    /**
     * {"originId":""," nickname ":"昵称，如果unionId一致则换成unionId","time":"关注时间"}
     */
    public void receiveAddOffical(AddResponseTaskDto addResponseTaskDto);

    /**
     * 获取公众号关注列表
     * @param memberId
     * @return
     */
    public List<OfficialAccountMsg> getOfficialAccountMsg(Long memberId);

}
