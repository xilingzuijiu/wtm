package com.weitaomi.application.model.dto;

/**
 * Created by Administrator on 2016/8/19.
 */
public class ModifyPasswordDto {
    /**
     * 0:忘记密码   1：修改密码
     */
    private Integer flag;
    /**
     * 原密码：登录进去之后需要此参数
     */
    private String password;
    /**
     * 新密码
     */
    private String newPassword;
    /**
     * 手机号（忘记密码时需要）
     */
    private String mobile;


    /**
     * 获取0:忘记密码   1：修改密码
     * @return flag 0:忘记密码   1：修改密码
     */
    public Integer getFlag() {
        return this.flag;
    }

    /**
     * 设置0:忘记密码   1：修改密码
     * @param flag 0:忘记密码   1：修改密码
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /**
     * 获取原密码：登录进去之后需要此参数
     * @return password 原密码：登录进去之后需要此参数
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * 设置原密码：登录进去之后需要此参数
     * @param password 原密码：登录进去之后需要此参数
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取新密码
     * @return newPassword 新密码
     */
    public String getNewPassword() {
        return this.newPassword;
    }

    /**
     * 设置新密码
     * @param newPassword 新密码
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * 获取手机号（忘记密码时需要）
     * @return mobile 手机号（忘记密码时需要）
     */
    public String getMobile() {
        return this.mobile;
    }

    /**
     * 设置手机号（忘记密码时需要）
     * @param mobile 手机号（忘记密码时需要）
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
