package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.OfficialAccount;
import com.weitaomi.application.model.dto.AddOfficalAccountDto;
import com.weitaomi.application.model.dto.MemberAccountLabel;
import com.weitaomi.application.model.dto.OfficialAccountMsg;
import com.weitaomi.application.model.dto.OfficialAccountsDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by supumall on 2016/8/9.
 */
public interface IOfficeAccountService {
    /**
     * 查看已关注公众号
     */
    List<MemberAccountLabel> getOfficialAccountMsgList(Long memberId,Integer sourceType);

    /**
     * 更新已关注公众号
     * @param memberId
     * @return
     */
    Integer signOfficialAccountMsgList(Long memberId,Integer sourceType);

    /**
     * 推送关注任务
     * @param addOfficalAccountDto
     */
    @Transactional
    public boolean pushAddRequest(Long memberId,AddOfficalAccountDto addOfficalAccountDto);

    /**
     * {"originId":""," nickname ":"昵称，如果unionId一致则换成unionId","time":"关注时间"}
     */
    public Boolean pushAddFinished(Map<String,Object> params);
    /**
     * 标记要关注该公众号
     * @param memberId
     * @param officialAccountMsg
     * @return
     */
    boolean markAddRecord(Long memberId, OfficialAccountMsg officialAccountMsg);

    /**
     * 获取用户待关注公众号列表
     * @param memberId
     * @return
     */
    public List<OfficialAccountMsg> getOfficialAccountMsg(Long memberId,String unionId,Integer sourceType);

    /**
     * 获取商户公众号列表
     * @param memberId
     * @return
     */
    public List<OfficialAccount> getOfficialAccountList(Long memberId);

    boolean updateOfficialAccountList(Long accountId,Integer isOpen);

    String addOfficialAccount(Long memberId,String addUrl,String remark);
}
