package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_key_value")
public class KeyValue extends BaseModel {
    /**
     * key-value键
     */
    @Column(name = "mapKey")
    private String mapKey;

    /**
     * key-value值ַ
     */
    @Column(name = "mapValue")
    private String mapValue;

    /**
     * 创建时间
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
     * 获取key-value键
     *
     * @return mapKey - key-value键
     */
    public String getMapKey() {
        return mapKey;
    }

    /**
     * 设置key-value键
     *
     * @param mapKey key-value键
     */
    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }

    /**
     * 获取key-value值ַ
     *
     * @return mapValue - key-value值ַ
     */
    public String getMapValue() {
        return mapValue;
    }

    /**
     * 设置key-value值ַ
     *
     * @param mapValue key-value值ַ
     */
    public void setMapValue(String mapValue) {
        this.mapValue = mapValue;
    }

    /**
     * 获取创建时间
     *
     * @return createTime - 创建时间
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}