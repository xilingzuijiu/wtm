package com.weitaomi.application.model.dto;

/**
 * Created by Administrator on 2016/8/26.
 */
public class TotalSharedMsg {
    private String imageUrl;
    private Double shareCounts;
    private String memberName;

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getShareCounts() {
        return this.shareCounts;
    }

    public void setShareCounts(Double shareCounts) {
        this.shareCounts = shareCounts;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
