package com.weitaomi.application.model.bean;

import javax.persistence.*;

@Table(name = "wtm_member_score_flow_type")
public class MemberScoreFlowType {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 积分流动类型
     */
    private String typeName;

    /**
     * 积分流动类型描述
     */
    private String typeDesc;

    /**
     * 创建日期
     */
    private Long createTime;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

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
}