package com.weitaomi.application.model.dto;

/**
 * Created by supumall on 2016/8/9.
 */
public class OfficialAccountsDto {
    /**
     * 公号原始ID
     */
    private Long id;
    /**
     * 公号名字
     */
    private String ofiicialAccountName;


    /**
     * 获取公号原始ID
     * @return id 公号原始ID
     */
    public Long getId() {
        return this.id;
    }

    /**
     * 设置公号原始ID
     * @param id 公号原始ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取公号名字
     * @return ofiicialAccountName 公号名字
     */
    public String getOfiicialAccountName() {
        return this.ofiicialAccountName;
    }

    /**
     * 设置公号名字
     * @param ofiicialAccountName 公号名字
     */
    public void setOfiicialAccountName(String ofiicialAccountName) {
        this.ofiicialAccountName = ofiicialAccountName;
    }
}
