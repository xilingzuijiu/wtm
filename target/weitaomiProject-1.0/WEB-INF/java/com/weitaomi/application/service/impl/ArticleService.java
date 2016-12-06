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
import org.apache.shiro.crypto.hash.Sha256Hash;
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
public class ArticleService extends BaseService implements IArticleService {
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
        Long timestart=System.currentTimeMillis();
        if (articleSearch.getSearchWay()==0){
            Integer number = 20;
            if (cacheService.getCacheByKey("read:article:limit:number",Integer.class)!=null){
                number=cacheService.getCacheByKey("read:article:limit:number",Integer.class);
            }
            List<ArticleShowDto> articleShowDtoList=articleMapper.getAtricleList(memberId,articleSearch,new RowBounds(1,number),sourceType);
            PageInfo<ArticleShowDto> showDtoPage=new PageInfo<ArticleShowDto>(articleShowDtoList);
            Page page = Page.trans(showDtoPage);
            page.setTotal(Long.valueOf(articleShowDtoList.size()));
            logger.info("获取文章请求时间："+(System.currentTimeMillis()-timestart));
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
        Integer numberLimit = 20;
        Integer timeLimitSeconds=60*60;
        if (cacheService.getCacheByKey("read:article:limit:time",Integer.class)!=null){
            timeLimitSeconds=cacheService.getCacheByKey("read:article:limit:time",Integer.class);
        }
        if (cacheService.getCacheByKey("read:article:limit:number",Integer.class)!=null){
            numberLimit=cacheService.getCacheByKey("read:article:limit:number",Integer.class);
        }
        if (!keyValueService.keyIsAvaliableByCondition(tableName,numberLimit.longValue(),numberLimit.longValue()+5,timeLimitSeconds,Integer.valueOf(articleId.size()).longValue(),1,true)){
            throw new InfoException("抱歉哦~亲~,有关规定一段时间内阅读文章太多属于‘频繁操作’，为了您更好的阅读，请稍后领取阅读任务或者减少领取的阅读列表数量~");
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
            String uuid=UUIDGenerator.generate();
            Map<String,Object> param=new HashMap<>();
            Map<String,Object> paramTemp=cacheService.getCacheByKey("member:obtain:artile:readList:"+uuid,Map.class);
            if (paramTemp==null){
                Member member=memberMapper.selectByPrimaryKey(memberId);
                String onlyId=new Sha256Hash(uuid, member.getSalt()).toString();
                param.put("onlyId",onlyId);
                param.put("memberId",memberId);
                param.put("createTime",time);
                cacheService.setCacheByKey("member:obtain:artile:readList:"+uuid,param,10*60);
            }
            map.put("url",messageUrl + "?uuid=" + uuid);
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
    public List<ArticleReadRecordDto> getArticleReadRecordDto(String uuid) {
        Map<String,Object> params=cacheService.getCacheByKey("member:obtain:artile:readList:"+uuid,Map.class);
        if (params!=null){
            Long memberId=Long.parseLong(params.get("memberId").toString());
            Long createTime=Long.parseLong(params.get("createTime").toString());
            String onlyId=(String)params.get("onlyId");
            if (memberId!=null&&createTime!=null){
                Member member=memberMapper.selectByPrimaryKey(memberId);
                if (member!=null){
                    String onlyIdTemp=new Sha256Hash(uuid, member.getSalt()).toString();
                    if (onlyIdTemp.equals(onlyId)){
                        return articleReadRecordMapper.getArticleReadRecordDto(memberId, createTime);
                    }
                }
            }else {
                return null;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public  Boolean readArticleRequest(Long memberId, Long time, Long articleId) {
        Long time1=System.currentTimeMillis();
        this.isArticleAccessToRead(memberId);
        logger.info("文章可读判断时间："+(System.currentTimeMillis()-time1));
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
        Double scoreAmount=taskPool.getTotalScore();
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
        memberTaskHistoryService.addMemberTaskToHistory(memberId,6L,taskPool.getFinishScore(),1,"阅读文章-"+article.getTitle(),null,null);
        memberScoreService.addMemberScore(memberId,13L,1,taskPool.getFinishScore(),UUIDGenerator.generate());
        Integer number=cacheService.getCacheByKey("article:number:"+articleId,Integer.class);
        if (number!=null&&number>0) {
            if (number > taskPool.getNeedNumber()&&scoreAmount<=0) {
                cacheService.delKeyFromRedis("article:number:"+articleId);
                cacheService.setCacheByKey("article:number:"+articleId,number,60);
                throw new InfoException("任务池中该文章已完成阅读");
            } else {
                cacheService.increCacheBykey("article:number:"+articleId,1L);
            }
        }else {
            cacheService.setCacheByKey("article:number:"+articleId,1,null);
        }
        logger.info("文章处理时间："+(System.currentTimeMillis()-time1));
        return true;
    }

    @Override
    public synchronized Boolean pcreadArticleRequest(Long memberId, Long articleId) {
//        this.isArticleAccessToRead(memberId);
//        int num=articleReadRecordMapper.getArticleReadRecord(memberId, articleId);
//        if (num>0){
//            throw new InfoException("您已经在其他平台领取过该任务，请完成阅读~");
//        }
//        ArticleReadRecord articleReadRecord=new ArticleReadRecord();
//        articleReadRecord.setArticleId(articleId);
//        articleReadRecord.setMemberId(memberId);
//        articleReadRecord.setCreateTime(DateUtils.getUnixTimestamp());
//        articleReadRecord.setType(1);
//        TaskPool taskPool=taskPoolMapper.getTaskPoolByArticleId(articleId,1);
//        if (taskPool==null){
//            throw new InfoException("任务池中没有该文章");
//        }
//        Double scoreAmount=taskPool.getTotalScore();
//        Double score = taskPool.getTotalScore()-taskPool.getSingleScore();
//        if (score<0){
//            throw new InfoException("任务池中该文章的剩余米币不足以支付阅读任务");
//        }
//        articleReadRecordMapper.insertSelective(articleReadRecord);
//        taskPool.setTotalScore(score);
//        Article article=articleMapper.selectByPrimaryKey(articleId);
//        OfficialAccount account=officalAccountMapper.selectByPrimaryKey(article.getOfficialAccountId());
//        if (score<taskPool.getSingleScore()){
//            taskPool.setTotalScore(0D);
//            taskPool.setLimitDay(0L);
//            taskPool.setIsPublishNow(0);
//            memberScoreService.addMemberScore(account.getMemberId(), 6L,1,score.doubleValue(), UUIDGenerator.generate());
//            JpushUtils.buildRequest("您发布的文章"+article.getTitle()+"阅读任务已经完成，任务终止",account.getMemberId());
//        }
//        taskPoolMapper.updateByPrimaryKeySelective(taskPool);
//        memberTaskHistoryService.addMemberTaskToHistory(memberId,6L, BigDecimal.valueOf(taskPool.getSingleScore()).multiply(taskPool.getRate()).doubleValue(),1,"阅读文章-"+article.getTitle(),null,null);
//        memberScoreService.addMemberScore(memberId,13L,1,BigDecimal.valueOf(taskPool.getSingleScore()).multiply(taskPool.getRate()).doubleValue(),UUIDGenerator.generate());
//        Integer number=cacheService.getCacheByKey("article:number:"+articleId,Integer.class);
//        if (number!=null&&number>0) {
//            if (number > taskPool.getNeedNumber()&&scoreAmount<=0) {
//                cacheService.delKeyFromRedis("article:number:"+articleId);
//                cacheService.setCacheByKey("article:number:"+articleId,number,60);
//                throw new InfoException("任务池中该文章已完成阅读");
//            } else {
//                cacheService.increCacheBykey("article:number:"+articleId,1L);
//            }
//        }else {
//            cacheService.setCacheByKey("article:number:"+articleId,1,null);
//        }
//        return true;
        return false;
    }

    private void isArticleAccessToRead(Long memberId){
        String tableName="wap:artile:read:limit:"+memberId;
        Integer number = 20;
        Integer timeLimitSeconds=60*60;
        if (cacheService.getCacheByKey("read:article:limit:time",Integer.class)!=null){
            timeLimitSeconds=cacheService.getCacheByKey("read:article:limit:time",Integer.class);
        }
        if (cacheService.getCacheByKey("read:article:limit:number",Integer.class)!=null){
            number=cacheService.getCacheByKey("read:article:limit:number",Integer.class);
        }
        if (!keyValueService.keyIsAvaliableByCondition(tableName,number.longValue(),number.longValue()+5,timeLimitSeconds,1L,1,true)){
            Long timeLimit=cacheService.getCacheByKey(tableName,Integer.class)+timeLimitSeconds-DateUtils.getUnixTimestamp();
            Long seconds=timeLimit%60;
            Double minutes = Math.floor(timeLimit/60);
            throw new InfoException("抱歉哦~亲~，有关规定一段时间内阅读文章太多属于‘频繁操作’，为了您更好的阅读，请"+minutes.intValue()+"分钟"+seconds+"秒后再来完成阅读任务~");
        }
        String invitorRewardCheckKey=memberId+":readNumberRecordForInvitedReward:"+13;
        boolean flag=false;
        MemberInvitedRecord memberInvitedRecord=memberInvitedRecordMapper.getMemberInvitedRecordByMemberId(memberId);
        if (memberInvitedRecord!=null&&memberInvitedRecord.getIsAccessForInvitor()==0) {
            flag=true;
        }
        if (!keyValueService.keyIsAvaliableByCondition(invitorRewardCheckKey,number.longValue()+5, number.longValue()+10,null,1L,1,flag)){
            if (memberInvitedRecord!=null&&memberInvitedRecord.getIsAccessForInvitor()==0){
                MemberTask memberTask = memberTaskMapper.selectByPrimaryKey(3L);
                memberScoreService.addMemberScore(memberInvitedRecord.getParentId(),16L,1,memberTask.getPointCount().doubleValue(),UUIDGenerator.generate());
                memberInvitedRecord.setIsAccessForInvitor(1);
                memberInvitedRecordMapper.updateByPrimaryKeySelective(memberInvitedRecord);
                cacheService.delKeyFromRedis(invitorRewardCheckKey);
            }
        }
    }
}
