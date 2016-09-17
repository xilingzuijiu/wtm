package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.WtmHistoryVersion;

public interface WtmHistoryVersionMapper extends IBaseMapper<WtmHistoryVersion> {
    String getCurrentVersion(String platFlag);
}