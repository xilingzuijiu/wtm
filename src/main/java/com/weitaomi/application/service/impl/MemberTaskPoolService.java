package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.model.bean.MemberScore;
import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.model.dto.Address;
import com.weitaomi.application.model.dto.OfficialAccountsDto;
import com.weitaomi.application.model.dto.RequireFollowerParamsDto;
import com.weitaomi.application.model.mapper.MemberScoreMapper;
import com.weitaomi.application.model.mapper.OfficalAccountMapper;
import com.weitaomi.application.model.mapper.ProvinceMapper;
import com.weitaomi.application.model.mapper.TaskPoolMapper;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.application.service.interf.IMemberTaskPoolService;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.util.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
@Service
public class MemberTaskPoolService implements IMemberTaskPoolService{
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
    @Override
    public String uploadTaskPool(TaskPool taskPool) {
        if (taskPool!=null){
            TaskPool taskPoolTemp=new TaskPool();
            taskPoolTemp.setOfficialAccountsId(taskPool.getOfficialAccountsId());
            taskPoolTemp.setTaskType(0);
            taskPoolTemp.setIsPublishNow(1);
            List<TaskPool> taskPools=taskPoolMapper.select(taskPoolTemp);
            if (!taskPools.isEmpty()){
                return "您已发布过此任务，请在公众号详情中查看";
            }
            MemberScore memberScore=memberScoreMapper.getMemberScoreByMemberId(taskPool.getMemberId());
            if (memberScore==null){
                return "可用米币为零，您还不能发布任务，请充值或者去赚取米币";
            }
            if (memberScore.getMemberScore().doubleValue()-taskPool.getTotalScore()<0){
                return "可用米币为不足，请充值或者去赚取米币";
            }
            int num =taskPoolMapper.insertSelective(taskPool);
            memberScoreService.addMemberScore(taskPool.getMemberId(),4L,1,-Double.valueOf(taskPool.getTotalScore()), UUIDGenerator.generate());
            memberTaskHistoryService.addMemberTaskToHistory(taskPool.getMemberId(),8L,-taskPool.getTotalScore().longValue(),1,null,null);
            return "SUCCESS";
        }
        return "FAIL";
    }
    @Override
    public RequireFollowerParamsDto getRequireFollowerParamsDto(Long memberId, Long time) {
        RequireFollowerParamsDto requireFollowerParamsDto=new RequireFollowerParamsDto();
        List<OfficialAccountsDto> accountList=officalAccountMapper.getAccountsByMemberId(memberId);
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
}
