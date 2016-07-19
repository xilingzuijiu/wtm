package com.weitaomi.application.model.bean;

import java.util.Date;
import javax.persistence.*;

@Table(name = "success_killed")
public class SuccessKilled {
    /**
     * 主键ID
     */
    @Id
    private Long success_id;

    /**
     * 秒杀产品ID
     */
    private Long seckill_id;

    /**
     * 用户手机号
     */
    private Long user_phone;

    /**
     * 秒杀状态 -1：秒杀无效，0：秒杀成功，1：已付款
     */
    private Byte state;

    /**
     * 秒杀时间
     */
    private Date create_time;

    /**
     * 获取主键ID
     *
     * @return success_id - 主键ID
     */
    public Long getSuccess_id() {
        return success_id;
    }

    /**
     * 设置主键ID
     *
     * @param success_id 主键ID
     */
    public void setSuccess_id(Long success_id) {
        this.success_id = success_id;
    }

    /**
     * 获取秒杀产品ID
     *
     * @return seckill_id - 秒杀产品ID
     */
    public Long getSeckill_id() {
        return seckill_id;
    }

    /**
     * 设置秒杀产品ID
     *
     * @param seckill_id 秒杀产品ID
     */
    public void setSeckill_id(Long seckill_id) {
        this.seckill_id = seckill_id;
    }

    /**
     * 获取用户手机号
     *
     * @return user_phone - 用户手机号
     */
    public Long getUser_phone() {
        return user_phone;
    }

    /**
     * 设置用户手机号
     *
     * @param user_phone 用户手机号
     */
    public void setUser_phone(Long user_phone) {
        this.user_phone = user_phone;
    }

    /**
     * 获取秒杀状态 -1：秒杀无效，0：秒杀成功，1：已付款
     *
     * @return state - 秒杀状态 -1：秒杀无效，0：秒杀成功，1：已付款
     */
    public Byte getState() {
        return state;
    }

    /**
     * 设置秒杀状态 -1：秒杀无效，0：秒杀成功，1：已付款
     *
     * @param state 秒杀状态 -1：秒杀无效，0：秒杀成功，1：已付款
     */
    public void setState(Byte state) {
        this.state = state;
    }

    /**
     * 获取秒杀时间
     *
     * @return create_time - 秒杀时间
     */
    public Date getCreate_time() {
        return create_time;
    }

    /**
     * 设置秒杀时间
     *
     * @param create_time 秒杀时间
     */
    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}