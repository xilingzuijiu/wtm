package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.AccountAds;

public interface AccountAdsMapper extends IBaseMapper<AccountAds> {
    Integer getLatestAccountAdsId();
}