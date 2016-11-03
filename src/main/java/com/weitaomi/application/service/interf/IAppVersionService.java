package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.WtmHistoryVersion;

/**
 * Created by Administrator on 2016/9/12.
 */
public interface IAppVersionService {
    public Object getCurrentVersion(Integer platFlag,Integer flag);
    public boolean updateAppVersion(Integer platFlag,String version,String link);
}
