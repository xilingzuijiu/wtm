package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weitaomi.application.model.bean.KeyValue;
import com.weitaomi.application.model.dto.KeyValueDto;
import com.weitaomi.application.model.mapper.KeyValueMapper;
import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.application.service.interf.IKeyValueService;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/5.
 */
@Service
public class KeyValueService implements IKeyValueService{
    @Autowired
    private KeyValueMapper keyValueMapper;
    @Autowired
    private ICacheService cacheService;
    @Override
    public List<KeyValueDto> getKeyValueDtoList(String mapKey,String id) {
        String mapValue=keyValueMapper.getValueBykey(mapKey);
        List<KeyValueDto> keyValueDtoList=JSONObject.parseArray(mapValue,KeyValueDto.class);
        if (!keyValueDtoList.isEmpty()){
            if (StringUtil.isEmpty(id)){
                return keyValueDtoList;
            }else {
                List<KeyValueDto> keyValueDtos=new ArrayList<>();
                for (KeyValueDto keyValueDto:keyValueDtoList){
                    if (keyValueDto.getId().equals(id)){
                        keyValueDtos.add(keyValueDto);
                    }
                }
                return keyValueDtos;
            }
        }
        return null;
    }
    @Override
    public boolean keyIsExist(String tableName, String idKey){
        String mapValue=keyValueMapper.getValueBykey(tableName);
        List<KeyValueDto> keyValueDtoList=JSONObject.parseArray(mapValue,KeyValueDto.class);
        if (!keyValueDtoList.isEmpty()){
                for (KeyValueDto keyValueDto:keyValueDtoList){
                    if (keyValueDto.getId().equals(idKey)){
                        return true;
                    }
                }

        }
        return false;
    }

    @Override
    public boolean keyIsAvaliableByCondition(String tableName, Long redisNumberLimitMin,Long redisNumberLimitMax, Integer timeLimitSeconds,Long numberIncrease ,Integer numberStart,boolean isStart) {
        Long flagTemp = cacheService.getCacheByKey(tableName,Long.class);
        boolean flag=true;
        if (flagTemp!=null) {
            if (flagTemp.intValue()>=redisNumberLimitMin&&flagTemp.intValue()<=redisNumberLimitMax){
                cacheService.delKeyFromRedis(tableName);
                cacheService.setCacheByKey(tableName, DateUtils.getUnixTimestamp(),timeLimitSeconds);
            } else if (flagTemp >redisNumberLimitMax) {
                flag = false;
            } else if (flagTemp > 0&&flagTemp.intValue()<redisNumberLimitMin) {
                if ((flagTemp+numberIncrease)>=redisNumberLimitMax){
                    flag=false;
                }else {
                    cacheService.increCacheBykey(tableName, numberIncrease);
                }
            }
        }else {
            if (isStart) {
                cacheService.setCacheByKey(tableName, numberStart, timeLimitSeconds);
            }
        }
        return flag;
    }
}
