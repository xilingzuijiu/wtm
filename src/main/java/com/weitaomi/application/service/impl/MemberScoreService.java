package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.model.bean.*;
import com.weitaomi.application.model.dto.MemberTaskWithDetail;
import com.weitaomi.application.model.dto.RewardCountDto;
import com.weitaomi.application.model.mapper.*;
import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.application.service.interf.IKeyValueService;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.systemconfig.constant.SystemConfig;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.IpUtils;
import com.weitaomi.systemconfig.util.PropertiesUtil;
import com.weitaomi.systemconfig.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by supumall on 2016/7/8.
 */
@Service
public class MemberScoreService implements IMemberScoreService {
    private final Logger logger = LoggerFactory.getLogger(MemberScoreService.class);
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private MemberScoreMapper memberScoreMapper;
    @Autowired
    private MemberScoreFlowMapper memberScoreFlowMapper;
    @Autowired
    private MemberScoreFlowTypeMapper memberScoreFlowTypeMapper;
    @Autowired
    private MemberInvitedRecordMapper memberInvitedRecordMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private IKeyValueService keyValueService;
    private Lock lock=new ReentrantLock();
    @Override
    @Transactional
    public MemberScore addMemberScore(Long memberId, Long typeId,Integer isFinished, Double score, String sessionId){
        try{
        if (sessionId == null) {
            throw new BusinessException("幂等性操作，请生成随机数");
        }
        String key = "member:score:" + sessionId;
        String avaliableScoreKey="member:score:type:isAvaliableScore";
        MemberScore memberScoreCache = cacheService.getCacheByKey(key, MemberScore.class);
        if (memberScoreCache != null) {
            throw new BusinessException("重复操作");
        } else {
            if (typeId == null) {
                throw new BusinessException("积分类型ID为空");
            } else {
                MemberScoreFlowType memberScoreFlowType = memberScoreFlowTypeMapper.selectByPrimaryKey(typeId);
                if (memberScoreFlowType == null) {
                    throw new InfoException("查无此积分流动类型");
                }
                BigDecimal increaseScore = null;
                BigDecimal scoreBefore = BigDecimal.ZERO;
                MemberScore memberScore = memberScoreMapper.getMemberScoreByMemberId(memberId);
                if (score != null) {
                    BigDecimal rate = BigDecimal.ONE;
                    increaseScore = BigDecimal.valueOf(score).multiply(rate);
                }

                if (memberScore == null) {
                    if (increaseScore.doubleValue() < 0) {
                        throw new BusinessException("可用积分不足");
                    }
                    memberScore = new MemberScore();
                    memberScore.setMemberId(memberId);
                    memberScore.setMemberScore(increaseScore);
                    //todo
                    boolean flag= cacheService.keyExistInHashTable(avaliableScoreKey,typeId.toString());
                    if (!flag){
                        flag=keyValueService.keyIsExist(avaliableScoreKey,typeId.toString());
                    }
                    if (flag) {
                        memberScore.setAvaliableScore(increaseScore);
                        if (typeId==1){
                            BigDecimal charge=increaseScore;
                            if (charge.doubleValue()>=0){
                                charge=BigDecimal.ZERO;
                            }
                            memberScore.setInValidScore(charge);
                            memberScore.setRechargeCurrentScore(increaseScore);
                            memberScore.setRechargeTotalScore(increaseScore);
                        }
                    }
                    memberScore.setCreateTime(DateUtils.getUnixTimestamp());
                    memberScore.setUpdateTime(DateUtils.getUnixTimestamp());
                    memberScoreMapper.insertSelective(memberScore);
                } else {
                    scoreBefore=memberScore.getMemberScore();
                    //总米币
                    BigDecimal afterScore = scoreBefore.add(increaseScore);
                    //可用米币
                    BigDecimal avaliableScore=memberScore.getAvaliableScore();
                    //充值米币（current）
                    BigDecimal rechargeCurrentScore=memberScore.getRechargeCurrentScore();
                    //充值米币（total）
                    BigDecimal rechargeTotalScore=memberScore.getRechargeTotalScore();
                    boolean flag= cacheService.keyExistInHashTable(avaliableScoreKey,typeId.toString());
                    if (!flag){
                        flag=keyValueService.keyIsExist(avaliableScoreKey,typeId.toString());
                    }
                    if (flag) {
                        avaliableScore = avaliableScore.add(increaseScore);
                        if (typeId==1) {
                            rechargeTotalScore = rechargeTotalScore.add(increaseScore);
                            rechargeCurrentScore = rechargeCurrentScore.add(increaseScore);
                        }else if (typeId==4||typeId==5){
                            if (increaseScore.doubleValue()<0&&increaseScore.abs().doubleValue()<=rechargeCurrentScore.doubleValue()){
                                rechargeCurrentScore=rechargeCurrentScore.add(increaseScore);
                            }else if (increaseScore.doubleValue()<0&&increaseScore.abs().doubleValue()>rechargeCurrentScore.doubleValue()){
                                rechargeCurrentScore=BigDecimal.ZERO;
                            }
                        }
                    }
                    if (afterScore.doubleValue() < 0) {
                        throw new BusinessException("可用积分不足");
                    }
                    if (typeId!=17&&typeId!=16) {
                        memberScore.setMemberScore(afterScore);
                    }
                    if (typeId!=8) {
                        memberScore.setAvaliableScore(avaliableScore);
                    }
                    memberScore.setRechargeCurrentScore(rechargeCurrentScore);
                    memberScore.setRechargeTotalScore(rechargeTotalScore);
                    memberScore.setRate(BigDecimal.ONE);
                    memberScore.setUpdateTime(DateUtils.getUnixTimestamp());
                    memberScoreMapper.updateByPrimaryKeySelective(memberScore);
                }
                MemberScoreFlow memberScoreFlow = new MemberScoreFlow();
                memberScoreFlow.setMemberId(memberId);
                memberScoreFlow.setTypeId(typeId);
                memberScoreFlow.setIsFinished(isFinished);
                memberScoreFlow.setFlowScore(increaseScore);
                memberScoreFlow.setMemberScoreAfter(memberScore.getMemberScore());
                memberScoreFlow.setMemberScoreBefore(scoreBefore);
                memberScoreFlow.setMemberScoreId(memberScore.getId());
                memberScoreFlow.setCreateTime(DateUtils.getUnixTimestamp());
                Boolean flag = memberScoreFlowMapper.insertSelective(memberScoreFlow) > 0 ? true : false;
                if (!flag) {
                    throw new BusinessException("积分记录失败");
                }
                cacheService.setCacheByKey(key, memberScore, 60);
                String table="member:score:type:isAvaliableToSuper";
                boolean flagTemp= cacheService.keyExistInHashTable(table,typeId.toString());
                if (!flagTemp){
                    flagTemp=keyValueService.keyIsExist(table,typeId.toString());
                }
                if (flagTemp&&increaseScore.doubleValue()>0) {
                    //处理上级奖励问题
                    MemberInvitedRecord memberInvitedRecord = memberInvitedRecordMapper.getMemberInvitedRecordByMemberId(memberId);
                    if (memberInvitedRecord != null) {
                        Double rewardScore=increaseScore.multiply(BigDecimal.valueOf(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
//                        Long expiresTime=DateUtils.getTodayEndSeconds()-DateUtils.getUnixTimestamp()+3*60*60;
                        String tableName="member:extra:reward";
                        String idKey = memberInvitedRecord.getMemberId()+":"+memberInvitedRecord.getParentId();
                        Member member = memberMapper.selectByPrimaryKey(memberInvitedRecord.getMemberId());
                        if (cacheService.keyExistInHashTable(tableName,idKey)){
                            RewardCountDto rewardCountDtoTemp=cacheService.getFromHashTable(tableName,idKey);
                            if (rewardCountDtoTemp!=null){
                                Double totalFlowScore=rewardCountDtoTemp.getTotalFlowScore()+rewardScore;
                                rewardCountDtoTemp.setTotalFlowScore(totalFlowScore);
                                cacheService.reSetToHashTable(tableName,idKey,rewardCountDtoTemp);
                            }else {
                                RewardCountDto rewardCountDto = new RewardCountDto();
                                rewardCountDto.setMemberId(memberInvitedRecord.getMemberId());
                                rewardCountDto.setParentId(memberInvitedRecord.getParentId());
                                rewardCountDto.setMemberName(member.getMemberName());
                                rewardCountDto.setTotalFlowScore(rewardScore);
                                cacheService.setToHashTable(tableName,idKey,rewardCountDto,null);
                            }
                        }else {
                            RewardCountDto rewardCountDto = new RewardCountDto();
                            rewardCountDto.setMemberId(memberInvitedRecord.getMemberId());
                            rewardCountDto.setParentId(memberInvitedRecord.getParentId());
                            rewardCountDto.setMemberName(member.getMemberName());
                            rewardCountDto.setTotalFlowScore(rewardScore);
                            cacheService.setToHashTable(tableName,idKey,rewardCountDto,0L);
                        }
                    }
                }
                return memberScore;
            }
        }
        }catch (Exception e){
            logger.info("米币流动时出现异常");
        }
        return null;
    }
    @Override
    public List<MemberScoreFlow> getMemberScoreFlowList(Long memberId) {
        return memberScoreFlowMapper.getMemberScoreFlowListByMemberId(memberId);
    }
    @Override
    @Transactional
    public MemberScore getMemberScoreById(Long memberId,String phoneType,String ip) {
        try{
        MemberScore memberScore = memberScoreMapper.getMemberScoreByMemberId(memberId);
        if (memberScore!=null) {
            return memberScore;
        }
        }catch (Exception e){
            logger.info("刷新钱包时出现异常");
        }
        return null;
    }
    @Override
    public Map getAvaliableScoreAndWxInfo(long memberId) {
        Map map=memberScoreMapper.getAvaliableScoreAndWxInfo(memberId);
        if (StringUtil.isEmpty((String)map.get("nickname"))){
            throw new InfoException("未获取到微信信息，请绑定微信或者联系客服人员");
        }
        return map;
    }
}
