package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_province")
public class Province extends BaseModel {

    private String provinceID;

    private String province;

    /**
     * @return provinceID
     */
    public String getProvinceID() {
        return provinceID;
    }

    /**
     * @param provinceID
     */
    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    /**
     * @return province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province
     */
    public void setProvince(String province) {
        this.province = province;
    }
}