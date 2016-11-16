package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.Province;
import com.weitaomi.application.model.dto.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProvinceMapper extends IBaseMapper<Province> {
    List<Address> getAllAddress();
    Map<String,String> getAddressByVague(@Param("province") String province, @Param("city") String city);
}