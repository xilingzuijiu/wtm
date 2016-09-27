package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.ThirdLogin;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ThirdLoginDto extends ThirdLogin {
    private Integer sex;

    public Integer getSex() {
        return this.sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
