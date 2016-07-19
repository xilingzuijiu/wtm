package com.weitaomi.application.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by supumall on 2016/7/4.
 */
public class BaseModel{
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * 获取主键
     * @return id 主键
     */
    public Long getId() {
        return this.id;
    }

    /**
     * 设置主键
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }
}
