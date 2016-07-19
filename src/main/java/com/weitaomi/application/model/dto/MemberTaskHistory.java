package com.weitaomi.application.model.dto;

/**
 * Created by supumall on 2016/7/16.
 */
public class MemberTaskHistory {
    /**
     * 类型
     */
    private String typeName;
    /**
     * 描述
     */
    private String typeDesc;
    /**
     * 时间
     */
    private String createTime;

    /**
     * 获取类型
     * @return typeName 类型
     */
    public String getTypeName() {
        return this.typeName;
    }

    /**
     * 设置类型
     * @param typeName 类型
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
     * 获取时间
     * @return createTime 时间
     */
    public String getCreateTime() {
        return this.createTime;
    }

    /**
     * 设置时间
     * @param createTime 时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
