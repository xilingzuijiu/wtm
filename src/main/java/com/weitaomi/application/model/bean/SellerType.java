package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_seller_type")
public class SellerType extends BaseModel {

    /**
     * 商家类型
     */
    @Column(name = "typeCode")
    private Integer typeCode;

    /**
     * 商家类型名称
     */
    @Column(name = "userTypeName")
    private String userTypeName;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
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
     * 获取商家类型
     *
     * @return typeCode - 商家类型
     */
    public Integer getTypeCode() {
        return typeCode;
    }

    /**
     * 设置商家类型
     *
     * @param typeCode 商家类型
     */
    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * 获取商家类型名称
     *
     * @return userTypeName - 商家类型名称
     */
    public String getUserTypeName() {
        return userTypeName;
    }

    /**
     * 设置商家类型名称
     *
     * @param userTypeName 商家类型名称
     */
    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
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