package com.weitaomi.application.service.impl;

import com.weitaomi.application.model.bean.WtmHistoryVersion;
import com.weitaomi.application.model.dto.RequestFrom;
import com.weitaomi.application.model.mapper.WtmHistoryVersionMapper;
import com.weitaomi.application.service.interf.IAppVersionService;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
@Service
public class AppVersionService implements IAppVersionService {
    @Autowired
    private WtmHistoryVersionMapper wtmHistoryVersionMapper;

    @Override
    public Object getCurrentVersion(Integer platFlag,Integer flag) {
        WtmHistoryVersion version=wtmHistoryVersionMapper.getCurrentVersion(RequestFrom.getById(platFlag).getName());
        if (flag==0){
            return "1.4.1";
        }
        if (flag==1){
            return version.getLink();
        } else if (flag==2) {
            return version;
        }
        return null;
    }

    @Override
    public boolean updateAppVersion(Integer platFlag, String version,String link) {
        WtmHistoryVersion wtmHistoryVersion=new WtmHistoryVersion();
        wtmHistoryVersion.setPlatform(RequestFrom.getById(platFlag).getName());
        wtmHistoryVersion.setVersion(version);
        wtmHistoryVersion.setLink(link);
        List<WtmHistoryVersion> wtmHistoryVersionList=wtmHistoryVersionMapper.select(wtmHistoryVersion);
        if (!wtmHistoryVersionList.isEmpty()){
            throw new InfoException("改版本号已经更新过");
        }
        wtmHistoryVersion.setCreateTime(DateUtils.getUnixTimestamp());
        int number =wtmHistoryVersionMapper.insertSelective(wtmHistoryVersion);
        return number>0?true:false;
    }
}
