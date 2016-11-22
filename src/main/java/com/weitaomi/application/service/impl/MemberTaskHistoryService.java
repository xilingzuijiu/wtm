package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.weitaomi.application.model.bean.*;
import com.weitaomi.application.model.dto.MemberTaskDto;
import com.weitaomi.application.model.dto.MemberTaskWithDetail;
import com.weitaomi.application.model.dto.RequestFrom;
import com.weitaomi.application.model.mapper.*;
import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.systemconfig.constant.SystemConfig;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.*;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/16.
 */
@Service
public class MemberTaskHistoryService  implements IMemberTaskHistoryService {
    private Logger logger= LoggerFactory.getLogger(MemberTaskHistoryService.class);
    @Autowired
    private MemberTaskHistoryMapper memberTaskHistoryMapper;
    @Autowired
    private MemberTaskMapper memberTaskMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberTaskHistoryDetailMapper memberTaskHistoryDetailMapper;
    @Autowired
    private IMemberScoreService memberScoreService;
    @Autowired
    private OfficeMemberMapper officeMemberMapper;
    @Autowired
    private WtmOfficialMemberMapper wtmOfficialMemberMapper;
    @Autowired
    private ThirdLoginMapper thirdLoginMapper;
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private ProvinceMapper provinceMapper;

    @Override
    public Page<MemberTaskWithDetail> getMemberTaskInfo(Long memberId,Integer type,Integer pageSize,Integer pageIndex) {
        List<MemberTaskWithDetail> memberTaskHistoryDtoList=memberTaskHistoryMapper.getMemberTaskHistoryList(memberId,type,new RowBounds(pageIndex,pageSize));
        PageInfo<MemberTaskWithDetail> showDtoPage=new PageInfo<MemberTaskWithDetail>(memberTaskHistoryDtoList);
        return Page.trans(showDtoPage);
    }

    @Override
    public List<MemberTaskHistoryDetail> getMemberTaskInfoDetail(Long taskHistoryId) {

        List<MemberTaskHistoryDetail> memberTaskHistoryDetails=memberTaskHistoryDetailMapper.getMemberTaskHistoryDetialList(taskHistoryId);
        if (memberTaskHistoryDetails.isEmpty()){
            return null;
        }
        return memberTaskHistoryDetails;
    }

    @Override
    public List<MemberTaskDto> getMemberDailyTask(Long memberId) {
        List<MemberTask> finishedMemberTasks=memberTaskMapper.getMemberTaskFinished(memberId,DateUtils.getTodayZeroSeconds(),DateUtils.getTodayEndSeconds());
        List<MemberTask> memberTaskList=memberTaskMapper.getAllMemberTask();
        if (memberTaskList.isEmpty()){
            return null;
        }
        List<MemberTaskDto> memberTaskDtos=new ArrayList<MemberTaskDto>();
        if (finishedMemberTasks.isEmpty()){
            for (MemberTask memberTask:memberTaskList){
                MemberTaskDto memberTaskDto=new MemberTaskDto();
                memberTaskDto.setMemberTask(memberTask);
                memberTaskDto.setIsFinisherToday(0);
                memberTaskDtos.add(memberTaskDto);
            }
            return memberTaskDtos;
        }
        for (MemberTask memberTask:memberTaskList){
            Long taskId=memberTask.getId();
            boolean flag=false;
            for (MemberTask memberTask1:finishedMemberTasks){
                if (memberTask1.getId()==taskId){
                    flag=true;
                }
            }
            MemberTaskDto memberTaskDto=new MemberTaskDto();
            memberTaskDto.setMemberTask(memberTask);
            if (flag){
                memberTaskDto.setIsFinisherToday(1);
            } else{
                memberTaskDto.setIsFinisherToday(0);
            }
            memberTaskDtos.add(memberTaskDto);
        }
        return memberTaskDtos;
    }

    @Override
    public Boolean isSignAccount(Long memberId){
        Integer number=cacheService.getCacheByKey("member:account:sign:"+memberId,Integer.class);
        if (number!=null&&number==1){
            return true;
        }
        List<Map<String,Long>> idMap= memberMapper.getIsFollowWtmAccount(memberId,0);
        if (!idMap.isEmpty()){
            if (idMap.get(0).get("officialMemberId")==null){
                throw new InfoException("亲，您没有关注微淘米公众号哟~，请打开微信搜索‘微淘米APP’关注并置顶。点击右下方‘签到’就可以赚钱了~");
            }
            MemberTask memberTask=memberTaskMapper.isSignAccount(memberId,DateUtils.getTodayZeroSeconds(),DateUtils.getTodayEndSeconds());
            if (memberTask!=null){
                return true;
            }else {
                return false;
            }
        }else {
            throw new InfoException("亲，您没有关注微淘米公众号哟~，请打开微信搜索‘微淘米APP’关注并置顶。点击右下方‘签到’就可以赚钱了~");
        }
    }
    @Override
    public boolean addMemberTaskToHistory(Long memberId, Long taskId, Double score, Integer flag,String detail,List<MemberTaskHistoryDetail> detailList,String taskFlag) {
        MemberTask memberTask = memberTaskMapper.selectByPrimaryKey(taskId);
        if (score==null||score==0){
            score=memberTask.getPointCount().doubleValue();
        }
        if (StringUtil.isEmpty(detail)){
            detail=memberTask.getTaskDesc();
        }
        MemberTaskWithDetail memberTaskWithDetail = new MemberTaskWithDetail();
        memberTaskWithDetail.setTaskId(taskId);
        memberTaskWithDetail.setPointCount(score);
        memberTaskWithDetail.setIsFinished(flag);
        memberTaskWithDetail.setMemberId(memberId);
        memberTaskWithDetail.setTaskName(memberTask.getTaskName());
        memberTaskWithDetail.setTaskDesc(memberTask.getTaskDesc());
        memberTaskWithDetail.setCreateTime(DateUtils.getUnixTimestamp());
        if (!StringUtil.isEmpty(taskFlag)){
            memberTaskWithDetail.setTaskFlag(taskFlag);
        }
        List<MemberTaskHistoryDetail> memberTaskHistoryDetailList = new ArrayList<MemberTaskHistoryDetail>();
        if (detailList!=null){
            //// TODO: 2016/8/25
        }else {
            MemberTaskHistoryDetail memberTaskHistoryDetail = new MemberTaskHistoryDetail();
            memberTaskHistoryDetail.setTaskName(memberTask.getTaskName());
            memberTaskHistoryDetail.setTaskDesc(detail);
            memberTaskHistoryDetail.setPointCount(score);
            memberTaskHistoryDetail.setIsFinished(1);
            memberTaskHistoryDetail.setCreateTime(DateUtils.getUnixTimestamp());
            memberTaskHistoryDetailList.add(memberTaskHistoryDetail);
        }
        memberTaskWithDetail.setMemberTaskHistoryDetailList(memberTaskHistoryDetailList);
        memberTaskHistoryMapper.insertSelective((MemberTaskHistory) memberTaskWithDetail);
        if (!memberTaskWithDetail.getMemberTaskHistoryDetailList().isEmpty()){
            memberTaskHistoryDetailMapper.insertIntoDetail(memberTaskWithDetail.getMemberTaskHistoryDetailList(),memberTaskWithDetail.getId(),DateUtils.getUnixTimestamp());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateMemberTaskToHistory(Long memberTaskId) {
        return false;
    }

    @Override
    @Transactional
    public MemberScore addDailyTask(Long memberId, Long typeId) {
        List<MemberTaskHistory> memberTaskHistoryList=memberTaskMapper.getIsMemberTaskFinished(memberId,typeId,DateUtils.getTodayZeroSeconds(),DateUtils.getTodayEndSeconds());
        if (!memberTaskHistoryList.isEmpty()){
            throw new InfoException("该任务今天已完成");
        }
        MemberTask memberTask=memberTaskMapper.selectByPrimaryKey(typeId);
        this.addMemberTaskToHistory(memberId,typeId,null,1,null,null,null);
        Long taskId=typeId+10;
        MemberScore memberScore=memberScoreService.addMemberScore(memberId,taskId,1,memberTask.getPointCount().doubleValue(), UUIDGenerator.generate());
        if (memberScore!=null){
            return memberScore;
        }
        return null;
    }

    @Override
    public String signAccounts(Map map){
        logger.info("用户签到信息为:{}", JSON.toJSONString(map));
        String openId = (String) map.get("openid");
        String nickName= Base64.decodeToString((String) map.get("nickname"));
        Long time=DateUtils.getTodayEndSeconds()-DateUtils.getUnixTimestamp();
        Integer number=cacheService.getCacheByKey(openId+":"+nickName,Integer.class);
        if (number!=null&&number==1){
            throw new InfoException("该任务今天已完成");
        }
        String sexTemp=map.get("sex").toString();
        Integer sex=-1;
        if (!StringUtil.isEmpty(sexTemp)) {
            sex= Integer.valueOf(sexTemp);
        }
        Long start=System.currentTimeMillis();
        if (StringUtil.isEmpty(openId)){
            throw new BusinessException("获取用户信息失败");
        }
        logger.info("公众号签到，用户openId为:"+openId);
        List<Long> memberId=wtmOfficialMemberMapper.getMemberIdByOpenId(openId);
        if (memberId.isEmpty()||memberId.get(0)==null){
            return "没有微淘米账号,请下载微淘米APP注册";
        }
        MemberScore memberScore = this.addDailyTask(memberId.get(0),10L);
        logger.info("请求时间为:"+(System.currentTimeMillis()-start));
        if (memberScore!=null){
            List<ThirdLogin> thirdLoginList = thirdLoginMapper.getThirdLoginByMemberId(memberId.get(0));
            Member member=memberMapper.selectByPrimaryKey(memberId.get(0));
            if (member==null){
                throw new InfoException("用户不存在");
            }else if (member.getSex()==3){
                throw new InfoException("用户已经被禁用，请联系客服人员");
            }
            for (ThirdLogin thirdLogin:thirdLoginList){
                if (thirdLogin.getNickname()!=nickName){
                    thirdLogin.setNickname(nickName);
                    thirdLoginMapper.updateByPrimaryKeySelective(thirdLogin);
                }
                if (sex!=-1&&member.getSex()!=sex){
                    member.setSex(sex);
                    memberMapper.updateByPrimaryKeySelective(member);
                }
            }
            logger.info("签到成功，用户ID为{}",memberId.get(0));
            cacheService.setCacheByKey(openId + ":" + nickName, 1, time.intValue());
            cacheService.setCacheByKey("member:account:sign:"+memberId.get(0), 1, time.intValue());
            return "签到成功，现在您可以返回APP领取任务";
        }
        logger.info("签到失败，用户ID为{}",memberId.get(0));
        return "签到失败，请稍后再试...";
    }
    @Override
    public void recordMemberAddressAndEtc(Long memberId, String phoneType, String ip){
        memberMapper.updateMemberPhoneType(memberId,phoneType);
        Map<String,String> address= IpUtils.getAddressByIp(ip);
        logger.info("address:{},Ip:{}", JSON.toJSONString(address),ip);
        Map<String,String> addr=null;
        if (address!=null){
            if (!StringUtil.isEmpty(address.get("province"))&&!StringUtil.isEmpty(address.get("city"))) {
                addr = provinceMapper.getAddressByVague(address.get("province"), address.get("city"));
            }
        }
        if (addr!=null) {
            Member member = memberMapper.selectByPrimaryKey(memberId);
            if (StringUtil.isEmpty(member.getProvince()) || StringUtil.isEmpty(member.getProvince())) {
                member.setProvince(addr.get("province"));
                member.setCity(addr.get("city"));
                memberMapper.updateByPrimaryKeySelective(member);
            }
        }
    }
}
