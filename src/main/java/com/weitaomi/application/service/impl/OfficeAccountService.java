package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.model.bean.*;
import com.weitaomi.application.model.dto.*;
import com.weitaomi.application.model.mapper.*;
import com.weitaomi.application.service.interf.*;
import com.weitaomi.systemconfig.constant.SystemConfig;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by supumall on 2016/8/9.
 */
@Service
public class OfficeAccountService implements IOfficeAccountService {
    private static Logger logger= LoggerFactory.getLogger(OfficeAccountService.class);
    @Autowired
    private OfficalAccountMapper officalAccountMapper;
    @Autowired
    private MemberTaskHistoryMapper memberTaskHistoryMapper;
    @Autowired
    private WtmOfficialMemberMapper wtmOfficialMemberMapper;
    @Autowired
    private ThirdLoginMapper thirdLoginMapper;
    @Autowired
    private MemberTaskMapper memberTaskMapper;
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private TaskPoolMapper taskPoolMapper;
    @Autowired
    private OfficeMemberMapper officeMemberMapper;
    @Autowired
    private IMemberScoreService memberScoreService;
    @Autowired
    private IMemberTaskHistoryService memberTaskHistoryService;
    @Autowired
    private AccountAdsMapper accountAdsMapper;
    @Override
    public List<OfficialAccountsDto> getAccountsByMemberId(Long memberId){
        return officalAccountMapper.getAccountsByMemberId(memberId,0L);
    }
    /**
     * 查看已关注公众号
     */
    @Override
    public List<MemberAccountLabel> getOfficialAccountMsgList(Long memberId,Integer sourceType){
        List<MemberAccountLabel> officialAccountNameList=officeMemberMapper.getOfficialAccountNameList(memberId,sourceType);
        return officialAccountNameList;
    }
    /**
     *更新已关注公众号
     */
    @Override
    public Integer signOfficialAccountMsgList(Long memberId,Integer sourceType){
        List<MemberAccountLabel> officialAccountNameList=officeMemberMapper.getOfficialAccountNameList(memberId,sourceType);
        Integer num=officeMemberMapper.updateOfficialMemberList(memberId);
        for (MemberAccountLabel memberAccountLabel: officialAccountNameList) {
            String key = memberAccountLabel.getNickName() + ":" + memberAccountLabel.getSex() + ":" + memberAccountLabel.getOriginId();
            logger.info("用户执行标注公众号操作，key：{}",key);
            int number = memberTaskHistoryMapper.deleteMemberTaskUnfinished(memberId, 0, memberAccountLabel.getOriginId());
            cacheService.delKeyFromRedis(key);
        }
        return num;
    }
    @Override
    @Transactional
    public boolean pushAddRequest(Long memberId,AddOfficalAccountDto addOfficalAccountDto) {
        logger.info("app用户ID：{}开始拉取公众号列表，参数为：{}",memberId,JSON.toJSONString(addOfficalAccountDto));
        List<OfficeMember> officeMembers=officeMemberMapper.getOfficeMemberList(memberId);
        Long timeStart=System.currentTimeMillis();
        if (!officeMembers.isEmpty()){
            String info="您还有未关注公众号，请到公众号内完成\n未完成任务将会在1小时后删除\n若领任务之前已关注，请标注这些公众号";
            throw new InfoException(info);
        }
        if (addOfficalAccountDto==null){
            throw new BusinessException("任务列表为空");
        }
        String unionId=addOfficalAccountDto.getUnionId();
        if (StringUtil.isEmpty(unionId)){
            throw new BusinessException("用户唯一识别码为空");
        }
        List<OfficialAccountMsg> list=addOfficalAccountDto.getLinkList();
        if (list.isEmpty()){
            throw new BusinessException("要关注的公号列表为空");
        }
        List<OfficeMember> officeMemberList=new ArrayList<>();
        List<Long> idList=new ArrayList<>();
        for (OfficialAccountMsg officialAccountMsg:list){
            ThirdLoginDto thirdLoginDto=thirdLoginMapper.getThirdlogInDtoMemberId(memberId,0);
            String key= Base64Utils.encodeToString(thirdLoginDto.getNickname().getBytes())+":"+thirdLoginDto.getSex()+":"+officialAccountMsg.getOriginId();
            logger.info("key is {}",key);
            OfficialAccountWithScore officialAccountWithScore=officalAccountMapper.getOfficialAccountWithScoreById(officialAccountMsg.getOriginId());
            MemberCheck memberCheck=new MemberCheck(memberId,officialAccountWithScore.getId());
            MemberCheck valueTemp = cacheService.getCacheByKey(key,MemberCheck.class);
            if (valueTemp!=null){
                throw new InfoException("公众号"+officialAccountWithScore.getUserName()+"的关注未完成，请先完成");
            }
            cacheService.setCacheByKey(key,memberCheck, SystemConfig.TASK_CACHE_TIME);
            OfficeMember officeMember=new OfficeMember();
            officeMember.setMemberId(memberId);
            officeMember.setOfficeAccountId(officialAccountWithScore.getId());
            officeMember.setIsAccessNow(0);
            officeMemberList.add(officeMember);
            TaskPool taskPool = taskPoolMapper.getTaskPoolByOfficialId(officialAccountWithScore.getId(), 1);
            //添加到待完成任务记录中
            String detail ="关注公众号"+officialAccountWithScore.getUserName()+"，领取"+taskPool.getRate().multiply(BigDecimal.valueOf(officialAccountWithScore.getScore())).doubleValue()+"米币";
            memberTaskHistoryService.addMemberTaskToHistory(memberId,1L,taskPool.getRate().multiply(BigDecimal.valueOf(officialAccountWithScore.getScore())).doubleValue(),0,detail,null,officialAccountMsg.getOriginId());
            idList.add(officialAccountWithScore.getId());
        }
        Long time =DateUtils.getUnixTimestamp();
        int number=officeMemberMapper.batchAddOfficeMember(officeMemberList,time);
        if (number>0) {
            String url = PropertiesUtil.getValue("server.officialAccount.receiveAddRequest.url");
            Map<String,Object>  map=new HashMap<>();
            map.put("unionId",unionId);
            map.put("officialAccountIdList",idList);
            map.put("flag","1");
            Integer accountAdsId=accountAdsMapper.getLatestAccountAdsId();
            if (accountAdsId!=null){
                map.put("accountAdsId",accountAdsId);
            }
            try {
                HttpRequestUtils.postStringEntity(url, JSON.toJSONString(map));
                logger.info("领取关注任务列表时间："+(System.currentTimeMillis()-timeStart));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    @Transactional
    public boolean markAddRecord(Long memberId, OfficialAccountMsg officialAccountMsg) {
        logger.info("wap用户ID：{}开始拉取公众号列表，参数为：{}",memberId,JSON.toJSONString(officialAccountMsg));
        List<OfficeMember> officeMembers=officeMemberMapper.getOfficeMemberList(memberId);
        if (!officeMembers.isEmpty()){
            String info="您还有未关注公众号，请到公众号内完成\n未完成任务将会在1小时后删除\n若领任务之前已关注，请标注这些公众号";
            throw new InfoException(info);
        }
        ThirdLogin thirdLogin=thirdLoginMapper.getUnionIdByMemberId(memberId,1);
        if (thirdLogin==null||StringUtil.isEmpty(thirdLogin.getUnionId())){
            throw new InfoException("用户唯一识别码为空");
        }
        if (officialAccountMsg==null){
            throw new InfoException("要关注的公号列表为空");
        }
        List<OfficeMember> officeMemberList=new ArrayList<>();
        List<Long> idList=new ArrayList<>();
        ThirdLoginDto thirdLoginDto=thirdLoginMapper.getThirdlogInDtoMemberId(memberId,1);
        String key=Base64Utils.encodeToString(thirdLoginDto.getNickname().getBytes())+":"+thirdLoginDto.getSex()+":"+officialAccountMsg.getOriginId();
        logger.info("key is {}",key);
        OfficialAccountWithScore officialAccountWithScore=officalAccountMapper.getOfficialAccountWithScoreById(officialAccountMsg.getOriginId());
        MemberCheck memberCheck=new MemberCheck(memberId,officialAccountWithScore.getId());
        MemberCheck valueTemp = cacheService.getCacheByKey(key,MemberCheck.class);
        if (valueTemp!=null){
            throw new InfoException("公众号"+officialAccountWithScore.getUserName()+"的关注未完成，请先完成");
        }
        cacheService.setCacheByKey(key,memberCheck, SystemConfig.TASK_CACHE_TIME);
        OfficeMember officeMember=new OfficeMember();
        officeMember.setMemberId(memberId);
        officeMember.setOfficeAccountId(officialAccountWithScore.getId());
        officeMember.setIsAccessNow(0);
        officeMemberList.add(officeMember);
        TaskPool taskPool = taskPoolMapper.getTaskPoolByOfficialId(officialAccountWithScore.getId(), 1);
        //添加到待完成任务记录中
        String detail ="关注公众号"+officialAccountWithScore.getUserName()+"，领取"+officialAccountWithScore.getScore()+"米币";
        memberTaskHistoryService.addMemberTaskToHistory(memberId,1L,taskPool.getRate().multiply(BigDecimal.valueOf(officialAccountWithScore.getScore())).doubleValue(),0,detail,null,officialAccountMsg.getOriginId());
        idList.add(officialAccountWithScore.getId());
        Long time =DateUtils.getUnixTimestamp();
        int number=officeMemberMapper.batchAddOfficeMember(officeMemberList,time);
        return number>0?true:false;
    }
    @Override
    public List<OfficialAccountMsg> getOfficialAccountMsg(Long memberId,String unionId,Integer sourceType) {
        Member member=memberMapper.selectByPrimaryKey(memberId);
        if (member==null){
            throw new InfoException("用户信息为空");
        }
        List<OfficialAccountMsg> officialAccountMsgs=officalAccountMapper.getOfficialAccountMsg(memberId,unionId,member.getSex(),member.getProvince(),member.getCity());
        if (officialAccountMsgs.isEmpty()){
            return null;
        }
        return officialAccountMsgs;
    }

    @Override
    public List<OfficialAccount> getOfficialAccountList(Long memberId) {
        return officalAccountMapper.getOfficialAccountList(memberId);
    }
    @Override
    public boolean updateOfficialAccountList(Long accountId, Integer isOpen) {
        OfficialAccount officialAccount=officalAccountMapper.selectByPrimaryKey(accountId);
        officialAccount.setIsOpen(isOpen);
        return officalAccountMapper.updateByPrimaryKeySelective(officialAccount)==1?true:false;
    }

    @Override
    public String addOfficialAccount(Long memberId, String addUrl, String remark) {
        Map<String,String> map=new HashMap<>();
        map.put("addUrl",addUrl);
        map.put("memberId",memberId.toString());
        map.put("remark",remark);
        try {
           String url= HttpRequestUtils.postString("http://www.weitaomi.com.cn/index.php/kfz/login/index",JSON.toJSONString(map));
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
