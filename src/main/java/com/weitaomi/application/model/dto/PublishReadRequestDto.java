package com.weitaomi.application.model.dto;

/**
 * Created by Administrator on 2016/9/7.
 */
public class PublishReadRequestDto {
    /**
     * 公众号ID
     */
    private Long officialAccountsId;
    /**
     * 用户ID
     */
    private Long memberId;
    /**
     * 文章地址
     */
    private String articleUrl;
    /**
     * 文章图片地址
     */
    private String imageFile;
    /**
     * 文章标题
     */
    private String  title;
    /**
     * 文章摘要
     */
    private String  articleAbstract;
    /**
     * 单次奖励
     */
    private Integer singleScore;
    /**
     * 需求数量
     */
    private Integer needNumber;
    /**
     * 时间限
     */
    private Integer limitDay;
    /**
     * 是否立即发布
     */
    private Integer isPublishNow;


    /**
     * 获取公众号ID
     * @return officialAccountsId 公众号ID
     */
    public Long getOfficialAccountsId() {
        return this.officialAccountsId;
    }

    /**
     * 设置公众号ID
     * @param officialAccountsId 公众号ID
     */
    public void setOfficialAccountsId(Long officialAccountsId) {
        this.officialAccountsId = officialAccountsId;
    }

    /**
     * 获取文章地址
     * @return articleUrl 文章地址
     */
    public String getArticleUrl() {
        return this.articleUrl;
    }

    /**
     * 设置文章地址
     * @param articleUrl 文章地址
     */
    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    /**
     * 获取文章标题
     * @return title 文章标题
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * 设置文章标题
     * @param title 文章标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取文章摘要
     * @return articleAbstract 文章摘要
     */
    public String getArticleAbstract() {
        return this.articleAbstract;
    }

    /**
     * 设置文章摘要
     * @param articleAbstract 文章摘要
     */
    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    /**
     * 获取单次奖励
     * @return singleScore 单次奖励
     */
    public Integer getSingleScore() {
        return this.singleScore;
    }

    /**
     * 设置单次奖励
     * @param singleScore 单次奖励
     */
    public void setSingleScore(Integer singleScore) {
        this.singleScore = singleScore;
    }

    /**
     * 获取需求数量
     * @return needNumber 需求数量
     */
    public Integer getNeedNumber() {
        return this.needNumber;
    }

    /**
     * 设置需求数量
     * @param needNumber 需求数量
     */
    public void setNeedNumber(Integer needNumber) {
        this.needNumber = needNumber;
    }

    /**
     * 获取时间限
     * @return limitDay 时间限
     */
    public Integer getLimitDay() {
        return this.limitDay;
    }

    /**
     * 设置时间限
     * @param limitDay 时间限
     */
    public void setLimitDay(Integer limitDay) {
        this.limitDay = limitDay;
    }

    /**
     * 获取是否立即发布
     * @return isPublishNow 是否立即发布
     */
    public Integer getIsPublishNow() {
        return this.isPublishNow;
    }

    /**
     * 设置是否立即发布
     * @param isPublishNow 是否立即发布
     */
    public void setIsPublishNow(Integer isPublishNow) {
        this.isPublishNow = isPublishNow;
    }

    /**
     * 获取文章图片地址
     * @return imageFile 文章图片地址
     */
    public String getImageFile() {
        return this.imageFile;
    }

    /**
     * 设置文章图片地址
     * @param imageFile 文章图片地址
     */
    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * 获取用户ID
     * @return memberId 用户ID
     */
    public Long getMemberId() {
        return this.memberId;
    }

    /**
     * 设置用户ID
     * @param memberId 用户ID
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
