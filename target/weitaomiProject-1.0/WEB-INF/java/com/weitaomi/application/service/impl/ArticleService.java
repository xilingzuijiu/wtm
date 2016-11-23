package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.weitaomi.application.model.bean.*;
import com.weitaomi.application.model.dto.ArticleDto;
import com.weitaomi.application.model.dto.ArticleReadRecordDto;
import com.weitaomi.application.model.dto.ArticleSearch;
import com.weitaomi.application.model.dto.ArticleShowDto;
import com.weitaomi.application.model.mapper.*;
import com.weitaomi.application.service.interf.IArticleService;
import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.*;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by supumall on 2016/7/7.
 */
@Service
public class ArticleService implements IArticleService {
    private Logger logger= LoggerFactory.getLogger(ArticleService.class);
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleReadRecordMapper articleReadRecordMapper;
    @Autowired
    private IMemberScoreService memberScoreService;
    @Autowired
    private IMemberTaskHistoryService memberTaskHistoryService;
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private TaskPoolMapper taskPoolMapper;
    @Autowired
    private OfficalAccountMapper officalAccountMapper;
    @Autowired
    private AccountAdsMapper accountAdsMapper;
    @Autowired
    private MemberInvitedRecordMapper memberInvitedRecordMapper;
    @Autowired
    private MemberTaskMapper memberTaskMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private TaskFailPushToWechatMapper taskFailPushToWechatMapper;
    @Override
    public Page<ArticleShowDto> getAllArticle(Long memberId,ArticleSearch articleSearch,Integer sourceType) {
        if (articleSearch.getSearchWay()==0){
            Integer number = 20;
            if (cacheService.getCacheByKey("read:article:limit:number",Integer.class)!=null){
                number=cacheService.getCacheByKey("read:article:limit:number",Integer.class);
            }
            List<ArticleShowDto> articleShowDtoList=articleMapper.getAtricleList(memberId,articleSearch,new RowBounds(1,number),sourceType);
            PageInfo<ArticleShowDto> showDtoPage=new PageInfo<ArticleShowDto>(articleShowDtoList);
            Page page = Page.trans(showDtoPage);
            page.setTotal(Long.valueOf(articleShowDtoList.size()));
            return page;
        }else{
            //TODO
        }
        return null;
    }

    @Override
    @Transactional
    public Boolean addArticle(ArticleDto article) {
        if (article==null){
            throw new BusinessException("文章信息为空");
        }
        if (article.getTitle()==null||!article.getTitle().isEmpty()){
            throw new BusinessException("文章标题为空");
        }
        if (article.getArticleAbstract()==null||article.getArticleAbstract().isEmpty()){
            throw new BusinessException("文章摘要为空");
        }
        if (article.getUrl()==null||!article.getUrl().isEmpty()){
            throw new BusinessException("文章地址为空");
        }
        if (article.getOfficialAccountId()==null){
            throw new BusinessException("文章所属机构为空");
        }
        article.setCreateTime(DateUtils.getUnixTimestamp());
        return articleMapper.insertSelective(article)>=0?true:false;
    }

    @Override
    @Transactional
    public Boolean modifyAticle(Article article) {
        if (article==null){
            throw new BusinessException("文章信息为空");
        }
        if (article.getTitle()==null||!article.getTitle().isEmpty()){
            throw new BusinessException("文章标题为空");
        }
        if (article.getArticleAbstract()==null||article.getArticleAbstract().isEmpty()){
            throw new BusinessException("文章摘要为空");
        }
        if (article.getUrl()==null||!article.getUrl().isEmpty()){
            throw new BusinessException("文章地址为空");
        }
        if (article.getOfficialAccountId()==null){
            throw new BusinessException("文章所属机构为空");
        }
        return articleMapper.updateByPrimaryKeySelective(article)>=0?true:false;
    }

    @Override
    public Boolean putArticleToTop(Long articleId, Integer isTop) {
        if (articleId==null){
            throw new BusinessException("所选文章ID为空");
        }
        return articleMapper.putArticleToTop(articleId, isTop)>=0?true:false;
    }

    @Override
    @Transactional
    public Boolean readArticle(Long memberId,String unionId,List<Long> articleId) {
        String tableName="app:artile:read:limit:"+memberId.toString();
        Integer flagTemp = cacheService.getCacheByKey(tableName,Integer.class);
        if (flagTemp!=null&&flagTemp>0){
            throw new InfoException("抱歉哦~亲~，微信规定一段时间内阅读文章太多属于‘频繁操作’，为了您更好的阅读，请每小时领取一次阅读任务~");
        } else {
            cacheService.setCacheByKey(tableName,1,62*60);
        }
        long ts=System.currentTimeMillis();
        if(memberId==null){
            throw new BusinessException("用户ID为空");
        }
        if(articleId.isEmpty()){
            throw new BusinessException("文章ID为空");
        }
        Long time=DateUtils.getUnixTimestamp();
        int number = articleReadRecordMapper.insertReadArticleRecord(memberId,articleId,0,time);
        boolean flag= number>0?true:false;
        if (flag){
            String url = PropertiesUtil.getValue("server.officialAccount.receiveAddRequest.url");
            String messageUrl = PropertiesUtil.getValue("server.officialAccount.message.url");
            Map<String,String>  map=new HashMap<>();
            map.put("unionId",unionId);
            map.put("url",messageUrl + "?memberId=" + memberId + "&requestTime=" +time);
            map.put("flag","0");
            Integer accountAdsId=accountAdsMapper.getLatestAccountAdsId();
            if (accountAdsId!=null){
                map.put("accountAdsId",accountAdsId.toString());
            }
            try {
                logger.info("时间为：{}",System.currentTimeMillis()-ts);
                String result = HttpRequestUtils.postStringEntity(url,JSON.toJSONString(map));
                if (!StringUtil.isEmpty(result)) {
                    Map<String,String> params= (Map<String, String>) JSONObject.parse(result);
                    boolean flagTem = Boolean.valueOf(params.get("temp"));
                    if (!flagTem){
                        TaskFailPushToWechat taskFailPushToWechat=new TaskFailPushToWechat();
                        taskFailPushToWechat.setParams(JSON.toJSONString(map));
                        taskFailPushToWechat.setPostUrl(url);
                        taskFailPushToWechat.setType(1);
                        taskFailPushToWechat.setIsPushToWechat(0);
                        taskFailPushToWechat.setCreateTime(DateUtils.getUnixTimestamp());
                        taskFailPushToWechatMapper.insertSelective(taskFailPushToWechat);
                    }
                }
                logger.info("交互时间为：{}",System.currentTimeMillis()-ts);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<ArticleReadRecordDto> getArticleReadRecordDto(Long memberId, Long createTime) {
        return articleReadRecordMapper.getArticleReadRecordDto(memberId, createTime);
    }

    @Override
    @Transactional
    public synchronized Boolean readArticleRequest(Long memberId, Long time, Long articleId) {
        this.isArticleAccessToRead(memberId);
        ArticleReadRecord readRecord=new ArticleReadRecord();
        readRecord.setArticleId(articleId);
        readRecord.setMemberId(memberId);
        readRecord.setType(1);
        List<ArticleReadRecord> articleReadRecords=articleReadRecordMapper.select(readRecord);
        if (!articleReadRecords.isEmpty()){
            throw new InfoException("该文章您已阅读");
        }
        readRecord.setArticleId(articleId);
        readRecord.setMemberId(memberId);
        readRecord.setType(0);
        if (time!=null&&time>0) {
            readRecord.setCreateTime(time);
        }
        List<ArticleReadRecord> articleReadRecordList=articleReadRecordMapper.select(readRecord);
        if (articleReadRecordList.isEmpty()||articleReadRecordList.size()>1){
            throw new InfoException("文章不存在或者已阅读");
        }
        readRecord=articleReadRecordList.get(0);
        readRecord.setType(1);
        articleReadRecordMapper.updateByPrimaryKeySelective(readRecord);
        TaskPool taskPool=taskPoolMapper.getTaskPoolByArticleId(articleId,1);
        if (taskPool==null){
            throw new InfoException("任务池中没有该文章");
        }
        Integer number=cacheService.getCacheByKey("article:number:"+articleId,Integer.class);
        if (number!=null&&number>0) {
            if (number > taskPool.getNeedNumber()) {
                cacheService.delKeyFromRedis("article:number:"+articleId);
                cacheService.setCacheByKey("article:number:"+articleId,number,60);
                throw new InfoException("任务池中该文章已完成阅读");
            } else {
                cacheService.increCacheBykey("article:number:"+articleId,1L);
            }
        }else {
            cacheService.setCacheByKey("article:number:"+articleId,1,null);
        }
        Double score = taskPool.getTotalScore()-taskPool.getSingleScore();
        if (score<0){
            throw new InfoException("任务池中该文章的剩余米币不足以支付阅读任务");
        }
        taskPool.setTotalScore(score);
        Article article=articleMapper.selectByPrimaryKey(articleId);
        OfficialAccount account=officalAccountMapper.selectByPrimaryKey(article.getOfficialAccountId());
        Double singleScore=taskPool.getSingleScore();
        if (score<taskPool.getSingleScore()){
            taskPool.setTotalScore(0D);
            taskPool.setLimitDay(0L);
            taskPool.setIsPublishNow(0);
            memberScoreService.addMemberScore(account.getMemberId(), 6L,1,score.doubleValue(), UUIDGenerator.generate());
            JpushUtils.buildRequest("您发布的文章"+article.getTitle()+"阅读任务已完成",account.getMemberId());
        }
        taskPoolMapper.updateByPrimaryKeySelective(taskPool);
        memberTaskHistoryService.addMemberTaskToHistory(memberId,6L, BigDecimal.valueOf(taskPool.getSingleScore()).multiply(taskPool.getRate()).doubleValue(),1,"阅读文章-"+article.getTitle(),null,null);
        memberScoreService.addMemberScore(memberId,13L,1,BigDecimal.valueOf(singleScore).multiply(taskPool.getRate()).doubleValue(),UUIDGenerator.generate());
        return true;
    }

    @Override
    public synchronized Boolean pcreadArticleRequest(Long memberId, Long articleId) {
        this.isArticleAccessToRead(memberId);
        int num=articleReadRecordMapper.getArticleReadRecord(memberId, articleId);
        if (num>0){
            throw new InfoException("您已经在其他平台领取过该任务，请完成阅读~");
        }
        ArticleReadRecord articleReadRecord=new ArticleReadRecord();
        articleReadRecord.setArticleId(articleId);
        articleReadRecord.setMemberId(memberId);
        articleReadRecord.setCreateTime(DateUtils.getUnixTimestamp());
        articleReadRecord.setType(1);
        TaskPool taskPool=taskPoolMapper.getTaskPoolByArticleId(articleId,1);
        if (taskPool==null){
            throw new InfoException("任务池中没有该文章");
        }
        Integer number=cacheService.getCacheByKey("article:number:"+articleId,Integer.class);
        if (number!=null&&number>0) {
            if (number > taskPool.getNeedNumber()) {
                cacheService.delKeyFromRedis("article:number:"+articleId);
                cacheService.setCacheByKey("article:number:"+articleId,number,60);
                throw new InfoException("任务池中该文章已完成阅读");
            } else {
                cacheService.increCacheBykey("article:number:"+articleId,1L);
            }
        }else {
            cacheService.setCacheByKey("article:number:"+articleId,1,null);
        }
        Double score = taskPool.getTotalScore()-taskPool.getSingleScore();
        if (score<0){
            throw new InfoException("任务池中该文章的剩余米币不足以支付阅读任务");
        }
        articleReadRecordMapper.insertSelective(articleReadRecord);
        taskPool.setTotalScore(score);
        Article article=articleMapper.selectByPrimaryKey(articleId);
        OfficialAccount account=officalAccountMapper.selectByPrimaryKey(article.getOfficialAccountId());
        if (score<taskPool.getSingleScore()){
            taskPool.setTotalScore(0D);
            taskPool.setLimitDay(0L);
            taskPool.setIsPublishNow(0);
            memberScoreService.addMemberScore(account.getMemberId(), 6L,1,score.doubleValue(), UUIDGenerator.generate());
            JpushUtils.buildRequest("您发布的文章"+article.getTitle()+"阅读任务已经完成，任务终止",account.getMemberId());
        }
        taskPoolMapper.updateByPrimaryKeySelective(taskPool);
        memberTaskHistoryService.addMemberTaskToHistory(memberId,6L, BigDecimal.valueOf(taskPool.getSingleScore()).multiply(taskPool.getRate()).doubleValue(),1,"阅读文章-"+article.getTitle(),null,null);
        memberScoreService.addMemberScore(memberId,13L,1,BigDecimal.valueOf(taskPool.getSingleScore()).multiply(taskPool.getRate()).doubleValue(),UUIDGenerator.generate());
        return true;
    }

    private void isArticleAccessToRead(Long memberId){
        Long flagTemp = cacheService.getCacheByKey("wap:artile:read:limit:"+memberId,Long.class);
        Integer number = 20;
        Integer timeLimitSeconds=60*60;
        if (cacheService.getCacheByKey("read:article:limit:time",Integer.class)!=null){
            timeLimitSeconds=cacheService.getCacheByKey("read:article:limit:time",Integer.class);
        }else {
            cacheService.setCacheByKey("read:article:limit:time",timeLimitSeconds,null);
        }
        if (cacheService.getCacheByKey("read:article:limit:number",Integer.class)!=null){
            number=cacheService.getCacheByKey("read:article:limit:number",Integer.class);
        }
        if (flagTemp!=null) {
            if (flagTemp.intValue()==number){
                cacheService.delKeyFromRedis("wap:artile:read:limit:"+memberId);
                cacheService.setCacheByKey("wap:artile:read:limit:"+memberId,DateUtils.getUnixTimestamp(),timeLimitSeconds);
            } else if (flagTemp > number) {
                Long timeLimit=flagTemp+timeLimitSeconds-DateUtils.getUnixTimestamp();
                Long seconds=timeLimit%60;
                Double minutes = Math.floor(timeLimit/60);
                throw new InfoException("抱歉哦~亲~，微信规定一段时间内阅读文章太多属于‘频繁操作’，为了您更好的阅读，请"+minutes.intValue()+"分钟"+seconds+"秒后再来完成阅读任务~");
            } else if (flagTemp > 0) {
                cacheService.increCacheBykey("wap:artile:read:limit:" + memberId, 1L);
            }
        }else {
            cacheService.setCacheByKey("wap:artile:read:limit:"+memberId,1,timeLimitSeconds);
        }
        Integer value=cacheService.getCacheByKey(memberId+":readNumberRecordForInvitedReward:"+13,Integer.class);
        if (value!=null&&value>0){
            if (value>=25){
                MemberInvitedRecord memberInvitedRecord=memberInvitedRecordMapper.getMemberInvitedRecordByMemberId(memberId);
                if (memberInvitedRecord!=null&&memberInvitedRecord.getIsAccessForInvitor()==0){
                    MemberTask memberTask = memberTaskMapper.selectByPrimaryKey(3L);
                    memberScoreService.addMemberScore(memberInvitedRecord.getParentId(),16L,1,memberTask.getPointCount().doubleValue(),UUIDGenerator.generate());
                    memberInvitedRecord.setIsAccessForInvitor(1);
                    memberInvitedRecordMapper.updateByPrimaryKeySelective(memberInvitedRecord);
                    cacheService.delKeyFromRedis(memberId+":readNumberRecordForInvitedReward:"+13);
                }
            }else {
                cacheService.increCacheBykey(memberId+":readNumberRecordForInvitedReward:"+13,1L);
            }
        }else {
            MemberInvitedRecord memberInvitedRecord=memberInvitedRecordMapper.getMemberInvitedRecordByMemberId(memberId);
            if (memberInvitedRecord!=null&&memberInvitedRecord.getIsAccessForInvitor()==0) {
                cacheService.setCacheByKey(memberId + ":readNumberRecordForInvitedReward:" + 13, 1, null);
            }
        }
    }
}
