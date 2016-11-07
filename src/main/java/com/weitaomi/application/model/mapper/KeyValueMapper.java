package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.KeyValue;
import org.apache.ibatis.annotations.Param;

public interface KeyValueMapper extends IBaseMapper<KeyValue> {
    String getValueBykey(@Param("key") String key);
}