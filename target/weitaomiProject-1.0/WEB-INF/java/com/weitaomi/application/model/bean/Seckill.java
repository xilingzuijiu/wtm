package com.weitaomi.application.model.bean;

import javax.persistence.Id;
import java.util.Date;

public class Seckill {
    /**
     * id
     */
    @Id
    private Long seckill_id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 秒杀开始时间
     */
    private Date start_time;

    /**
     * 秒杀结束时间
     */
    private Date end_time;

    /**
     * 时间
     */
    private Date create_time;

    /**
     * 获取id
     *
     * @return seckill_id - id
     */
    public Long getSeckill_id() {
        return seckill_id;
    }

    /**
     * 设置id
     *
     * @param seckill_id id
     */
    public void setSeckill_id(Long seckill_id) {
        this.seckill_id = seckill_id;
    }

    /**
     * 获取商品名称
     *
     * @return goodsName - 商品名称
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * 设置商品名称
     *
     * @param goodsName 商品名称
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /**
     * 获取商品数量
     *
     * @return goodsNum - 商品数量
     */
    public Integer getGoodsNum() {
        return goodsNum;
    }

    /**
     * 设置商品数量
     *
     * @param goodsNum 商品数量
     */
    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    /**
     * 获取秒杀开始时间
     *
     * @return start_time - 秒杀开始时间
     */
    public Date getStart_time() {
        return start_time;
    }

    /**
     * 设置秒杀开始时间
     *
     * @param start_time 秒杀开始时间
     */
    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    /**
     * 获取秒杀结束时间
     *
     * @return end_time - 秒杀结束时间
     */
    public Date getEnd_time() {
        return end_time;
    }

    /**
     * 设置秒杀结束时间
     *
     * @param end_time 秒杀结束时间
     */
    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    /**
     * 获取时间
     *
     * @return create_time - 时间
     */
    public Date getCreate_time() {
        return create_time;
    }

    /**
     * 设置时间
     *
     * @param create_time 时间
     */
    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}