package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.WTMAccountMessage;
import org.apache.ibatis.annotations.Param;

public interface WTMAccountMessageMapper extends IBaseMapper<WTMAccountMessage> {
    int updateToken(@Param("time") String time, @Param("token") String token);
}