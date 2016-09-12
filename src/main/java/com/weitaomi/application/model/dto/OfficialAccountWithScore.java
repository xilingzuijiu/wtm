package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.OfficialAccount;

/**
 * Created by Administrator on 2016/8/19.
 */
public class OfficialAccountWithScore extends OfficialAccount{
    /**
     * 积分
     */
    private Double score;

    /**
     * 获取积分
     * @return score 积分
     */
    public Double getScore() {
        return this.score;
    }

    /**
     * 设置积分
     * @param score 积分
     */
    public void setScore(Double score) {
        this.score = score;
    }
}
