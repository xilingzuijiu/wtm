package com.weitaomi.application.model.dto;

/**
 * Created by Administrator on 2016/8/22.
 */
public class MemberScoreFlowDto {
    /**
     * 类型id
     */
    private Long typeId;
    /**
     * 名字
     */
    private String typeName;
    /**
     * 描述
     */
    private String typeDesc;
    /**
     * 流动几分
     */
    private Long flowCount;
    /**
     * 时间
     */
    private Long flowTime;

    /**
     * 是否完成
     */
    private Integer isFinished;

    /**
     * 获取流动几分
     * @return flowCount 流动几分
     */
    public Long getFlowCount() {
        return this.flowCount;
    }

    /**
     * 设置流动几分
     * @param flowCount 流动几分
     */
    public void setFlowCount(Long flowCount) {
        this.flowCount = flowCount;
    }

    /**
     * 获取时间
     * @return flowTime 时间
     */
    public Long getFlowTime() {
        return this.flowTime;
    }

    /**
     * 设置时间
     * @param flowTime 时间
     */
    public void setFlowTime(Long flowTime) {
        this.flowTime = flowTime;
    }

    /**
     * 获取名字
     * @return typeName 名字
     */
    public String getTypeName() {
        return this.typeName;
    }

    /**
     * 设置名字
     * @param typeName 名字
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 获取描述
     * @return typeDesc 描述
     */
    public String getTypeDesc() {
        return this.typeDesc;
    }

    /**
     * 设置描述
     * @param typeDesc 描述
     */
    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    /**
     * 获取是否完成
     * @return isFinished 是否完成
     */
    public Integer getIsFinished() {
        return this.isFinished;
    }

    /**
     * 设置是否完成
     * @param isFinished 是否完成
     */
    public void setIsFinished(Integer isFinished) {
        this.isFinished = isFinished;
    }

    /**
     * 获取类型id
     * @return typeId 类型id
     */
    public Long getTypeId() {
        return this.typeId;
    }

    /**
     * 设置类型id
     * @param typeId 类型id
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}
