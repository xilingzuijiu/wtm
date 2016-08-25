package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.Province;
import com.weitaomi.application.model.dto.Address;

import java.util.List;

public interface ProvinceMapper extends IBaseMapper<Province> {
    List<Address> getAllAddress();
}