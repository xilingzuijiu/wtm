package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_city")
public class City extends BaseModel {

    private String cityID;

    private String city;

    private String father;

    /**
     * @return cityID
     */
    public String getCityID() {
        return cityID;
    }

    /**
     * @param cityID
     */
    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    /**
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return father
     */
    public String getFather() {
        return father;
    }

    /**
     * @param father
     */
    public void setFather(String father) {
        this.father = father;
    }
}