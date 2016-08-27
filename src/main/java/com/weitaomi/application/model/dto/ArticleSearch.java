package com.weitaomi.application.model.dto;

/**
 * Created by supumall on 2016/7/7.
 */
public class ArticleSearch {
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 查询开始时间
     */
    private Long startTime;
    /**
     * 查询结束时间
     */
    private Long endTime;
    /**
     * 商家ID
     */
    private Long officialAccountId;
    /**
     * 分页默认第一页
     */
    private Integer pageIndex=0;
    /**
     * 分页默认50
     */
        private Integer pageSize=50;
    /**
     * 查询方式（后续扩展需要）
      */
    private Integer searchWay=0;
    /**
     * 文章标题关键字
     */
    private String titleKeyWord;

    /**
     * 获取文章ID
     * @return articleId 文章ID
     */
    public Long getArticleId() {
        return this.articleId;
    }

    /**
     * 设置文章ID
     * @param articleId 文章ID
     */
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    /**
     * 获取查询开始时间
     * @return startTime 查询开始时间
     */
    public Long getStartTime() {
        return this.startTime;
    }

    /**
     * 设置查询开始时间
     * @param startTime 查询开始时间
     */
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取查询结束时间
     * @return endTime 查询结束时间
     */
    public Long getEndTime() {
        return this.endTime;
    }

    /**
     * 设置查询结束时间
     * @param endTime 查询结束时间
     */
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取分页默认第一页
     * @return pageIndex 分页默认第一页
     */
    public Integer getPageIndex() {
        return this.pageIndex;
    }

    /**
     * 设置分页默认第一页
     * @param pageIndex 分页默认第一页
     */
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 获取分页默认50
     * @return pageSize 分页默认50
     */
    public Integer getPageSize() {
        return this.pageSize;
    }

    /**
     * 设置分页默认50
     * @param pageSize 分页默认50
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取查询方式
     * @return searchWay 查询方式
     */
    public Integer getSearchWay() {
        return this.searchWay;
    }

    /**
     * 设置查询方式
     * @param searchWay 查询方式
     */
    public void setSearchWay(Integer searchWay) {
        this.searchWay = searchWay;
    }

    /**
     * 获取文章标题关键字
     * @return titleKeyWord 文章标题关键字
     */
    public String getTitleKeyWord() {
        return this.titleKeyWord;
    }

    /**
     * 设置文章标题关键字
     * @param titleKeyWord 文章标题关键字
     */
    public void setTitleKeyWord(String titleKeyWord) {
        this.titleKeyWord = titleKeyWord;
    }

    /**
     * 获取商家ID
     * @return officialAccountId 商家ID
     */
    public Long getOfficialAccountId() {
        return this.officialAccountId;
    }

    /**
     * 设置商家ID
     * @param officialAccountId 商家ID
     */
    public void setOfficialAccountId(Long officialAccountId) {
        this.officialAccountId = officialAccountId;
    }
}
