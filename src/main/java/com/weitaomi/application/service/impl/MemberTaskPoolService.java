package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.weitaomi.application.model.bean.Article;
import com.weitaomi.application.model.bean.MemberScore;
import com.weitaomi.application.model.bean.OfficialAccount;
import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.model.dto.*;
import com.weitaomi.application.model.mapper.*;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.application.service.interf.IMemberTaskPoolService;
import com.weitaomi.systemconfig.constant.SystemConfig;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.Page;
import com.weitaomi.systemconfig.util.StringUtil;
import com.weitaomi.systemconfig.util.UUIDGenerator;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
        if (StringUtil.isEmpty(publishReadRequestDto.getTitle())){
            throw new InfoException("文章标题不能为空");
        }
        Article articleTemp=articleMapper.getArticleByUrl(publishReadRequestDto.getArticleUrl());
        if (articleTemp!=null){
            throw new InfoException("该文章已经发布过，请在个人相应公众号中查看");
        }
        article.setOfficialAccountId(publishReadRequestDto.getOfficialAccountsId());
        article.setUrl(publishReadRequestDto.getArticleUrl());
        if (!StringUtil.isEmpty(publishReadRequestDto.getTitle())){
            article.setTitle(publishReadRequestDto.getTitle());
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
        taskPool.setRate(BigDecimal.valueOf(0.8));
        MemberScore memberScore=memberScoreMapper.getMemberScoreByMemberId(publishReadRequestDto.getMemberId());
        if (memberScore.getMemberScore().doubleValue()-taskPool.getNeedNumber()*taskPool.getSingleScore()<0){
            throw new InfoException("账户可用积分不足，请充值~么么哒~");
        }
        taskPool.setTotalScore(taskPool.getNeedNumber()*taskPool.getSingleScore());
        int num = taskPoolMapper.insertSelective(taskPool);
        memberScoreService.addMemberScore(publishReadRequestDto.getMemberId(),5L,1,-taskPool.getNeedNumber()*taskPool.getSingleScore().doubleValue(),UUIDGenerator.generate());
        memberTaskHistoryService.addMemberTaskToHistory(publishReadRequestDto.getMemberId(),9L,-taskPool.getNeedNumber()*taskPool.getSingleScore().doubleValue(),1,"发布文章\""+publishReadRequestDto.getTitle()+"\"求阅读任务",null,null);
        if (num>0) return "发布成功";
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
            taskPoolDtoList= taskPoolMapper.getTaskPoolAccountDto(officialAccountId,new RowBounds(0,0));
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
            taskPool.setLimitDay(0L);
            taskPool.setNeedNumber(0);
            taskPool.setSingleScore(0D);
            memberScoreService.addMemberScore(memberId, 6L,1,score.doubleValue(), UUIDGenerator.generate());
        }
        int num = taskPoolMapper.updateByPrimaryKeySelective(taskPool);
        return num>0?true:false;
    }
}
