package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_area")
public class Area extends BaseModel {

    private String areaID;

    private String area;

    private String father;

    /**
     * @return areaID
     */
    public String getAreaID() {
        return areaID;
    }

    /**
     * @param areaID
     */
    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }

    /**
     * @return area
     */
    public String getArea() {
        return area;
    }

    /**
     * @param area
     */
    public void setArea(String area) {
        this.area = area;
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