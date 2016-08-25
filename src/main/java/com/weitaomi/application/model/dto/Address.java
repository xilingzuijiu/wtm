package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.City;
import com.weitaomi.application.model.bean.Province;

import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
public class Address extends Province{
    /**
     * 城市列表
     */
  private List<City> cityList;

    /**
     * 获取城市列表
     * @return cityList 城市列表
     */
    public List<City> getCityList() {
        return this.cityList;
    }

    /**
     * 设置城市列表
     * @param cityList 城市列表
     */
    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
