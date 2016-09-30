package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.OfficialAccount;
import com.weitaomi.application.model.dto.AddOfficalAccountDto;
import com.weitaomi.application.model.dto.MemberAccountLabel;
import com.weitaomi.application.model.dto.OfficialAccountMsg;
import com.weitaomi.application.model.dto.OfficialAccountsDto;

import java.util.List;
import java.util.Map;

/**
 * Created by supumall on 2016/8/9.
 */
public interface IOfficeAccountService {
    /**
     * 获取待关注公众号列表
     * @param memberId
     * @return
     */
    public List<OfficialAccountsDto> getAccountsByMemberId(Long memberId);
    /**
     * 查看已关注公众号
     */
    List<MemberAccountLabel> getOfficialAccountMsgList(Long memberId);

    /**
     * 更新已关注公众号
     * @param memberId
     * @return
     */
    Integer signOfficialAccountMsgList(Long memberId);

    /**
     * 推送关注任务
     * @param addOfficalAccountDto
     */
    public boolean pushAddRequest(Long memberId,AddOfficalAccountDto addOfficalAccountDto);

    /**
     * {"originId":""," nickname ":"昵称，如果unionId一致则换成unionId","time":"关注时间"}
     */
    public Boolean pushAddFinished(Map<String,String> params);

    /**
     * 获取用户待关注公众号列表
     * @param memberId
     * @return
     */
    public List<OfficialAccountMsg> getOfficialAccountMsg(Long memberId,String unionId);

    /**
     * 获取商户公众号列表
     * @param memberId
     * @return
     */
    public List<OfficialAccount> getOfficialAccountList(Long memberId);

    boolean updateOfficialAccountList(Long accountId,Integer isOpen);
}
