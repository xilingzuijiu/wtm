package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.MemberScoreFlow;

import java.util.List;

/**
 * Created by Administrator on 2016/8/22.
 */
public class MyWalletDto {
    /**
     * 可用米币
     */
    private Long avaliableBalance;
    /**
     * 米币流动记录
     */
    private List<MemberScoreFlowDto> memberScoreFlowDtoList;


    /**
     * 获取可用米币
     * @return avaliableBalance 可用米币
     */
    public Long getAvaliableBalance() {
        return this.avaliableBalance;
    }

    /**
     * 设置可用米币
     * @param avaliableBalance 可用米币
     */
    public void setAvaliableBalance(Long avaliableBalance) {
        this.avaliableBalance = avaliableBalance;
    }

    /**
     * 获取米币流动记录
     * @return memberScoreFlowDtoList 米币流动记录
     */
    public List<MemberScoreFlowDto> getMemberScoreFlowDtoList() {
        return this.memberScoreFlowDtoList;
    }

    /**
     * 设置米币流动记录
     * @param memberScoreFlowDtoList 米币流动记录
     */
    public void setMemberScoreFlowDtoList(List<MemberScoreFlowDto> memberScoreFlowDtoList) {
        this.memberScoreFlowDtoList = memberScoreFlowDtoList;
    }
}
