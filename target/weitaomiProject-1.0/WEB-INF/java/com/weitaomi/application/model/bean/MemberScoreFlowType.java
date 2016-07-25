package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "wtm_member_score_flow_type")
public class MemberScoreFlowType  extends BaseModel{
    /**
     * 积分数量
     */
    @Column(name = "flowCount")
    private BigDecimal flowCount;
    /**
     * 积分流动类型
     */
    @Column(name = "typeName")
    private String typeName;

    /**
     * 积分流动类型描述
     */
    @Column(name = "typeDesc")
    private String typeDesc;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;

    /**
     * 获取积分流动类型
     *
     * @return typeName - 积分流动类型
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * 设置积分流动类型
     *
     * @param typeName 积分流动类型
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 获取积分流动类型描述
     *
     * @return typeDesc - 积分流动类型描述
     */
    public String getTypeDesc() {
        return typeDesc;
    }

    /**
     * 设置积分流动类型描述
     *
     * @param typeDesc 积分流动类型描述
     */
    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    /**
     * 获取创建日期
     *
     * @return createTime - 创建日期
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取积分数量
     * @return flowCount 积分数量
     */
    public BigDecimal getFlowCount() {
        return this.flowCount;
    }

    /**
     * 设置积分数量
     * @param flowCount 积分数量
     */
    public void setFlowCount(BigDecimal flowCount) {
        this.flowCount = flowCount;
    }
}