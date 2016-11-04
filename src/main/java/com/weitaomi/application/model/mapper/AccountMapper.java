package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.Account;
import org.apache.ibatis.annotations.Param;

public interface AccountMapper extends IBaseMapper<Account> {
    Account getAccount(@Param("realName") String realName);
}