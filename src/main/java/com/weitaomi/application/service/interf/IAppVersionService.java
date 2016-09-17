package com.weitaomi.application.service.interf;

/**
 * Created by Administrator on 2016/9/12.
 */
public interface IAppVersionService {
    public String getCurrentVersion(Integer platFlag);
    public boolean updateAppVersion(Integer platFlag,String version);
}
