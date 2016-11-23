package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import com.weitaomi.application.model.bean.Article;
import com.weitaomi.application.model.bean.MemberScore;
import com.weitaomi.application.model.bean.OfficialAccount;
import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.model.dto.*;
import com.weitaomi.application.model.mapper.*;
import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.application.service.interf.IMemberTaskPoolService;
import com.weitaomi.systemconfig.constant.SystemConfig;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.*;
import org.apache.ibatis.session.RowBounds;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/24.
 */
@Service
public class MemberTaskPoolService extends BaseService implements IMemberTaskPoolService{
    private Logger logger= LoggerFactory.getLogger(MemberTaskPoolService.class);
    @Autowired
    private TaskPoolMapper taskPoolMapper;
    @Autowired
    private OfficalAccountMapper officalAccountMapper;
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private IMemberScoreService memberScoreService;
    @Autowired
    private IMemberTaskHistoryService memberTaskHistoryService;
    @Autowired
    private MemberScoreMapper memberScoreMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ICacheService cacheService;
    @Override
    @Transactional
    public String uploadAddTaskPool(TaskPool taskPool) {
        if (taskPool!=null){
            TaskPool taskPoolTemp=new TaskPool();
            taskPoolTemp.setOfficialAccountsId(taskPool.getOfficialAccountsId());
            taskPoolTemp.setTaskType(0);
            List<TaskPool> taskPools=taskPoolMapper.select(taskPoolTemp);
            if (!taskPools.isEmpty()){
                boolean temp=false;
                for (TaskPool taskPool1:taskPools){
                    if (taskPool1.getIsPublishNow()>0){
                        temp=true;
                        break;
                    }
                }
                if (temp) return "您已发布的任务没有完成，请在公众号详情中查看";
            }
            if (taskPool.getSingleScore()==null||taskPool.getSingleScore()<=0){
                throw new InfoException("单次奖励必须大于零哦~亲~");
            }
            MemberScore memberScore=memberScoreMapper.getMemberScoreByMemberId(taskPool.getMemberId());
            OfficialAccount officialAccount= officalAccountMapper.selectByPrimaryKey(taskPool.getOfficialAccountsId());
            if (memberScore==null){
                return "可用米币为零，您还不能发布任务，请充值或者去赚取米币";
            }
            if (memberScore.getMemberScore().doubleValue()-taskPool.getTotalScore()<0){
                return "可用米币为不足，请充值或者去赚取米币";
            }
            int num =taskPoolMapper.insertSelective(taskPool);
            memberScoreService.addMemberScore(taskPool.getMemberId(),4L,1,-Double.valueOf(taskPool.getTotalScore()), UUIDGenerator.generate());
            boolean flag=memberTaskHistoryService.addMemberTaskToHistory(taskPool.getMemberId(),8L,-taskPool.getTotalScore().doubleValue(),1,"发布公众号"+officialAccount.getUserName()+"求粉任务",null,null);
            if (flag){
                logger.info("处理成功");
                return "任务提交成功";
            }
        }
        return "FAIL";
    }

    @Override
    @Transactional
    public String uploadReadTaskPool(PublishReadRequestDto publishReadRequestDto) {
        Article article=new Article();
        if (publishReadRequestDto==null){
            throw new InfoException("发布任务失败，任务信息为空");
        }
        if (publishReadRequestDto.getMemberId()==null){
            throw new InfoException("用户信息为空，请重试或者联系客服人员");
        }
        if (publishReadRequestDto.getOfficialAccountsId()==null){
            throw new InfoException("文章所属公众号为空");
        }
        if (StringUtil.isEmpty(publishReadRequestDto.getArticleUrl())){
            throw new InfoException("文章地址不能为空");
        }
        Article articleTemp=articleMapper.getArticleByUrl(publishReadRequestDto.getArticleUrl());
        if (articleTemp!=null){
            throw new InfoException("该文章已经发布过，请在个人相应公众号中查看");
        }
        Document doc = null;
        try {
            doc = Jsoup.connect(publishReadRequestDto.getArticleUrl()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String title = doc.title();
        if (!publishReadRequestDto.getArticleUrl().endsWith("wechat_redirect")) {
            String suffix = "&scene=0#wechat_redirect";
            String urlParent = doc.getElementsByTag("script").get(10).toString();
            String urlParams = urlParent.substring(urlParent.indexOf("msg_link") + 12, urlParent.indexOf("#rd")).replace("\\x26amp;", "@");
            String[] arr = urlParams.split("@");
            String articleUrl = arr[0] + "&" + arr[1] + "&" + arr[2] + "&" + arr[3] + suffix;
            if (arr[1].length()!=14||arr[2].length()!=5||arr[3].length()!=35){
                throw new InfoException("文章链接格式不正确");
            }
            article.setOfficialAccountId(publishReadRequestDto.getOfficialAccountsId());
            article.setUrl(articleUrl);
        }else {
            article.setOfficialAccountId(publishReadRequestDto.getOfficialAccountsId());
            article.setUrl(publishReadRequestDto.getArticleUrl());
        }
        if (article.getUrl().endsWith("wechat_redirect")){
            article.setArticleType(0);
        }else if (article.getUrl().endsWith("#rd")){
            article.setArticleType(1);
        }else {
            article.setArticleType(0);
        }
        if (!StringUtil.isEmpty(title)){
            article.setTitle(title);
        }
        if (!StringUtil.isEmpty(publishReadRequestDto.getArticleAbstract())){
            article.setArticleAbstract(publishReadRequestDto.getArticleAbstract());
        }
        if (!StringUtil.isEmpty(publishReadRequestDto.getImageFile())){
            String image =publishReadRequestDto.getImageFile();
            String imageUrl = "/article/showImage/" + DateUtils.getUnixTimestamp() + "."+image.substring(image.indexOf("image/")+6,image.indexOf("base64")-1);
            this.uploadImage(imageUrl,image.substring(image.indexOf("base64")+7));
            if (!StringUtil.isEmpty(imageUrl)){
                article.setImageUrl(SystemConfig.UPYUN_PREFIX+imageUrl);
            }
        }else {
            Elements elements = doc.body().getElementsByTag("img");
            String certainUrl="";
            List<String> imgList=new ArrayList<>();
            if (!elements.isEmpty()) {
                for (Element element : elements) {
                    String url = element.attr("data-src");
                    String rateTemp = element.attr("data-ratio");
                    Double rate = 0.00;
                    if (!StringUtil.isEmpty(rateTemp)) {
                        rate=Double.valueOf(rateTemp);
                    }

                    if (!StringUtil.isEmpty(url)) {
                        if (rate >= 0.6 && rate <= 1.5) {
                            imgList.add(url);
                        }
                    }else {
                        url= element.attr("src");
                        if (!StringUtil.isEmpty(url)) {
                            if (rate >= 0.6 && rate <= 1.5) {
                                imgList.add(url);
                            }
                        }
                    }
//                    if (rate == 1.0) {
//                        certainUrl = url;
//                        break;
//                    }
                }
            }
            if (StringUtil.isEmpty(certainUrl)){
                if (imgList.isEmpty()){
                    certainUrl="http://weitaomi.b0.upaiyun.com/article/showImage/common.png";
                    article.setImageUrl(certainUrl);
                } else {
                    certainUrl=imgList.get(0);
                    byte[] bytes = StreamUtils.getImageFromNetByUrl(certainUrl);
                    String imageUrl = "/article/showImage/" + System.currentTimeMillis() + ".jpg";
                    if (this.uploadImage(bytes,imageUrl)){
                        article.setImageUrl(SystemConfig.UPYUN_PREFIX+imageUrl);
                    }
                }
            }

        }
        article.setCreateTime(DateUtils.getUnixTimestamp());
        article.setIsTop(0);
        articleMapper.insertSelective(article);
        TaskPool taskPool=new TaskPool();
        taskPool.setArticleId(article.getId());
        taskPool.setTaskType(1);
        List<TaskPool> taskPoolList=taskPoolMapper.select(taskPool);
        if (taskPoolList.size()>0){
            throw new InfoException("您已发布过该文章的阅读任务");
        }
        if (publishReadRequestDto.getIsPublishNow()!=null){
            taskPool.setIsPublishNow(publishReadRequestDto.getIsPublishNow());
        }
        if (publishReadRequestDto.getLimitDay()!=null){
            taskPool.setLimitDay(publishReadRequestDto.getLimitDay().longValue());
        }
        if (publishReadRequestDto.getSingleScore()==null||publishReadRequestDto.getSingleScore()<=0){
            throw new InfoException("单次奖励必须大于零哦~亲~");
        }
        if (publishReadRequestDto.getNeedNumber()==null||publishReadRequestDto.getNeedNumber()==0){
            throw new InfoException("单次阅读需求数必须大于零哦~亲~");
        }
        taskPool.setSingleScore(publishReadRequestDto.getSingleScore());
        taskPool.setNeedNumber(publishReadRequestDto.getNeedNumber());
        taskPool.setCreateTime(DateUtils.getUnixTimestamp());
        String rateTemp= cacheService.getCacheByKey("task:rate:percent",String.class);
        Double rate=0.5;
        if (!StringUtil.isEmpty(rateTemp)){
            rate = Double.valueOf(rateTemp);
        }else {
            cacheService.setCacheByKey("task:rate:percent",0.5,null);
        }
        taskPool.setRate(BigDecimal.valueOf(rate));
        MemberScore memberScore=memberScoreMapper.getMemberScoreByMemberId(publishReadRequestDto.getMemberId());
        if (memberScore.getMemberScore().doubleValue()-taskPool.getNeedNumber()*taskPool.getSingleScore()<0){
            throw new InfoException("账户可用积分不足，请充值~么么哒~");
        }
        taskPool.setTotalScore(taskPool.getNeedNumber()*taskPool.getSingleScore());
        int num = taskPoolMapper.insertSelective(taskPool);
        memberScoreService.addMemberScore(publishReadRequestDto.getMemberId(),5L,1,-taskPool.getNeedNumber()*taskPool.getSingleScore().doubleValue(),UUIDGenerator.generate());
        memberTaskHistoryService.addMemberTaskToHistory(publishReadRequestDto.getMemberId(),9L,-taskPool.getNeedNumber()*taskPool.getSingleScore().doubleValue(),1,"发布文章\""+title+"\"求阅读任务",null,null);
        if (num>0) return "提交审核成功";
        return "发布失败";
    }

    @Override
    public RequireFollowerParamsDto getRequireFollowerParamsDto(Long memberId) {
        RequireFollowerParamsDto requireFollowerParamsDto=new RequireFollowerParamsDto();
        List<OfficialAccountsDto> accountList=officalAccountMapper.getAccountsByMemberId(memberId,0L);
        if (accountList.isEmpty()){
            throw new BusinessException("公众号列表为空");
        }
        requireFollowerParamsDto.setOfficialAccountList(accountList);
        List<Address> addressList=provinceMapper.getAllAddress();
        if (addressList.isEmpty()){
            throw new BusinessException("获取地区列表失败，请稍后再试");
        }
        requireFollowerParamsDto.setAddressList(addressList);
//        logger.info(JSON.toJSONString(requireFollowerParamsDto));
        return requireFollowerParamsDto;
    }

    @Override
    public RequireFollowerParamsDto getMemberArticlePublishMsg(Long memberId) {
        RequireFollowerParamsDto requireFollowerParamsDto=new RequireFollowerParamsDto();
        List<OfficialAccountsDto> accountList=officalAccountMapper.getAccountsByMemberId(memberId,1L);
        if (accountList.isEmpty()){
            throw new BusinessException("公众号列表为空");
        }
        requireFollowerParamsDto.setOfficialAccountList(accountList);
//        logger.info(JSON.toJSONString(requireFollowerParamsDto));
        return requireFollowerParamsDto;
    }

    @Override
    public Page<TaskPoolDto> getTaskPoolDto(Long officialAccountId, Integer type, int pageSize, int pageIndex) {
        List<TaskPoolDto> taskPoolDtoList=null;
        if (type==1) {
            taskPoolDtoList= taskPoolMapper.getTaskPoolArticleDto(officialAccountId,new RowBounds(pageIndex,pageSize));
        }
        if (type==0) {
            taskPoolDtoList= taskPoolMapper.getTaskPoolAccountDto(officialAccountId,new RowBounds(pageIndex,pageSize));
        }
        PageInfo<TaskPoolDto> taskPoolDtoPageInfo=new PageInfo<TaskPoolDto>(taskPoolDtoList);
        return Page.trans(taskPoolDtoPageInfo);
    }

    @Override
    @Transactional
    public Boolean updateTaskPoolDto(Long memberId,Long taskPoolId, Integer isPublishNow,Integer needNumber,Double singScore,Integer limitDay) {
        TaskPool taskPool=taskPoolMapper.selectByPrimaryKey(taskPoolId);
        taskPool.setIsPublishNow(isPublishNow);
        taskPool.setCreateTime(DateUtils.getUnixTimestamp());
        if (taskPool==null){
            throw new InfoException("修改失败,没有此任务");
        }
        TaskPool taskPoolTemp=new TaskPool();
        taskPoolTemp.setIsPublishNow(1);
        long scoreId=0;
        long taskId=0;
        if (taskPool.getTaskType()==0){
            scoreId=4;
            taskId=8;
            taskPoolTemp.setOfficialAccountsId(taskPool.getOfficialAccountsId());
        }
        if (taskPool.getTaskType()==1){
            scoreId=5;
            taskId=9;
            taskPoolTemp.setArticleId(taskPool.getArticleId());
        }
        List<TaskPool> taskPoolList=taskPoolMapper.select(taskPoolTemp);
        if (isPublishNow==1){
            if (taskPoolList.size()>=1){
                throw new InfoException("您还有一个相同的任务正在进行中，请将该任务下架后再上架新任务，或者等任务结束");
            }
            if (singScore==null||singScore==0){
                throw new InfoException("单次任务米币必须为大于零的整数");
            }
            if (needNumber==null||needNumber==0){
                throw new InfoException("任务需求量必须为大于零的整数");
            }
            Double totalScore=singScore*needNumber-taskPool.getTotalScore();
            MemberScore  memberScore=memberScoreMapper.getMemberScoreByMemberId(memberId);
            OfficialAccount officialAccount= officalAccountMapper.selectByPrimaryKey(taskPool.getOfficialAccountsId());
            if (totalScore>memberScore.getMemberScore().doubleValue()){
                throw new InfoException("账户剩余米币大于发布任务消费米币");
            }
            taskPool.setTotalScore(singScore*needNumber);
            taskPool.setLimitDay(limitDay.longValue());
            taskPool.setNeedNumber(needNumber);
            taskPool.setSingleScore(singScore);
            memberScoreService.addMemberScore(taskPool.getMemberId(),scoreId,1,-Double.valueOf(totalScore), UUIDGenerator.generate());
            memberTaskHistoryService.addMemberTaskToHistory(taskPool.getMemberId(),taskId,-totalScore.doubleValue(),1,"重新发布公众号"+officialAccount.getUserName()+"求粉任务",null,null);
        } else if (isPublishNow==0){
            Double score=taskPool.getTotalScore();
            taskPool.setTotalScore(0D);
            memberScoreService.addMemberScore(memberId, 6L,1,score.doubleValue(), UUIDGenerator.generate());
        }
        int num = taskPoolMapper.updateByPrimaryKeySelective(taskPool);
        return num>0?true:false;
    }
    @Override
    @Transactional
    public Boolean accountUnderCarrige(Map<String,String> params) {
        String appId="";
        if (params!=null){
            appId=params.get("appid");
            logger.info("公众号取消授权，appId:{}",appId);

        }
        OfficialAccount officialAccount=officalAccountMapper.getOfficalAccountByAppId(appId);
        if (officialAccount!=null) {
            TaskPool taskPool=taskPoolMapper.getTaskPoolByOfficialId(officialAccount.getId(),1);
            if (taskPool!=null) {
                Double score = taskPool.getTotalScore();
                taskPool.setTotalScore(0D);
                taskPool.setIsPublishNow(0);
                taskPool.setLimitDay(0L);
                memberScoreService.addMemberScore(officialAccount.getMemberId(), 6L, 1, score.doubleValue(), UUIDGenerator.generate());
                memberTaskHistoryService.addMemberTaskToHistory(officialAccount.getMemberId(), 14L,score.doubleValue(), 1,"公众号"+officialAccount.getUserName()+"取消授权,关注任务结束，米币返还，米币数为"+score.doubleValue(),null,null);
                taskPoolMapper.updateByPrimaryKeySelective(taskPool);
            }
            officialAccount.setLevel2(-1);
        int num=officalAccountMapper.updateByPrimaryKeySelective(officialAccount);
            logger.info("公众号取消授权结束");
            return num>0;
        }
        return false;
    }
}
