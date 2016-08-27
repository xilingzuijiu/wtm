package com.weitaomi.application.model.dto;

import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 */
public class RequireFollowerParamsDto {
    /**
     * 用户公众号列表
     */
    private List<OfficialAccountsDto> officialAccountList;
    /**
     * 地址列表
     */
    private List<Address> addressList;

    /**
     * 获取用户公众号列表
     * @return officialAccountList 用户公众号列表
     */
    public List<OfficialAccountsDto> getOfficialAccountList() {
        return this.officialAccountList;
    }

    /**
     * 设置用户公众号列表
     * @param officialAccountList 用户公众号列表
     */
    public void setOfficialAccountList(List<OfficialAccountsDto> officialAccountList) {
        this.officialAccountList = officialAccountList;
    }

    /**
     * 获取地址列表
     * @return addressList 地址列表
     */
    public List<Address> getAddressList() {
        return this.addressList;
    }

    /**
     * 设置地址列表
     * @param addressList 地址列表
     */
    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}


