package com.weitaomi.application.model.dto;

import java.io.Serializable;

public class MobileInfo implements Serializable {

    private String appVersion;

    private String appSystemVersion;

    private String appDeviceId;

    private Integer appDeviceWidth;

    private Integer appDeviceHeight;

    private Boolean nightMode;

    public MobileInfo(String appVersion, String appSystemVersion, String appDeviceId, Integer appDeviceWidth, Integer appDeviceHeight, Boolean nightMode) {
        this.appVersion = appVersion;
        this.appSystemVersion = appSystemVersion;
        this.appDeviceId = appDeviceId;
        this.appDeviceWidth = appDeviceWidth;
        this.appDeviceHeight = appDeviceHeight;
        this.nightMode = nightMode;
    }

    public MobileInfo() {
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppSystemVersion() {
        return appSystemVersion;
    }

    public void setAppSystemVersion(String appSystemVersion) {
        this.appSystemVersion = appSystemVersion;
    }

    public String getAppDeviceId() {
        return appDeviceId;
    }

    public void setAppDeviceId(String appDeviceId) {
        this.appDeviceId = appDeviceId;
    }

    public Integer getAppDeviceWidth() {
        return appDeviceWidth;
    }

    public void setAppDeviceWidth(Integer appDeviceWidth) {
        this.appDeviceWidth = appDeviceWidth;
    }

    public Integer getAppDeviceHeight() {
        return appDeviceHeight;
    }

    public void setAppDeviceHeight(Integer appDeviceHeight) {
        this.appDeviceHeight = appDeviceHeight;
    }

    public Boolean getNightMode() {
        return nightMode;
    }

    public void setNightMode(Boolean nightMode) {
        this.nightMode = nightMode;
    }
}
