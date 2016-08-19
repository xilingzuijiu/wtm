package com.weitaomi.application.service.impl;

import com.weitaomi.application.model.bean.MemberInvitedRecord;
import com.weitaomi.application.model.bean.MemberScore;
import com.weitaomi.application.model.bean.MemberScoreFlow;
import com.weitaomi.application.model.bean.MemberScoreFlowType;
import com.weitaomi.application.model.mapper.MemberInvitedRecordMapper;
import com.weitaomi.application.model.mapper.MemberScoreFlowMapper;
import com.weitaomi.application.model.mapper.MemberScoreFlowTypeMapper;
import com.weitaomi.application.model.mapper.MemberScoreMapper;
import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by supumall on 2016/7/8.
 */
@Service
public class MemberScoreService implements IMemberScoreService {
    private final Logger logger = LoggerFactory.getLogger(MemberScoreService.class);
//    private RuntimeSchema<Member> schema=RuntimeSchema.createFrom(Member.class);
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
    @Override
    @Transactional
    public MemberScore addMemberScore(Long memberId, Long typeId, Long score, String sessionId) {
        if (sessionId==null){
            throw new BusinessException("幂等性操作，请生成随机数");
        }
        String key="member:score:"+sessionId;
        MemberScore memberScoreCache=cacheService.getCacheByKey(key,MemberScore.class);
        if (memberScoreCache!=null){
            throw new BusinessException("重复操作");
        }else {
            if (typeId == null) {
                throw new BusinessException("积分类型ID为空");
            } else {
                MemberScoreFlowType memberScoreFlowType=memberScoreFlowTypeMapper.selectByPrimaryKey(typeId);
                if (memberScoreFlowType==null){
                    throw new BusinessException("查无此积分流动类型");
                }
                BigDecimal increaseScore=null;
                BigDecimal scoreBefore=BigDecimal.ZERO;
                MemberScore memberScore = memberScoreMapper.getMemberScoreByMemberId(memberId);
                if (memberScoreFlowType.getFlowCount()!=null&&memberScoreFlowType.getFlowCount().equals(BigDecimal.ZERO)){
                    increaseScore=BigDecimal.valueOf(score);
                }else if (memberScoreFlowType.getFlowCount()!=null&&!memberScoreFlowType.getFlowCount().equals(BigDecimal.ZERO)){
                    BigDecimal rate=BigDecimal.ONE;
                    if (memberScore != null) {
                        scoreBefore=memberScore.getMemberScore();
                        rate=memberScore.getRate();
                    }
                    increaseScore=memberScoreFlowType.getFlowCount().multiply(rate);
                }
                MemberScoreFlow memberScoreFlow = new MemberScoreFlow();
                memberScoreFlow.setMemberId(memberId);
                memberScoreFlow.setTypeId(typeId);
                memberScoreFlow.setDetail(memberScoreFlowType.getTypeDesc());
                memberScoreFlow.setFlowScore(increaseScore);
                memberScoreFlow.setMemberScoreAfter(memberScore.getMemberScore());
                memberScoreFlow.setMemberScoreBefore(scoreBefore);
                memberScoreFlow.setMemberScoreId(memberScore.getId());
                memberScoreFlow.setCreateTime(DateUtils.getUnixTimestamp());
                Boolean flag= memberScoreFlowMapper.insertSelective(memberScoreFlow)==1?true:false;
                if (!flag){
                    throw new BusinessException("积分记录失败");
                }
                if (memberScore == null) {
                    if (increaseScore.doubleValue()<0){
                        throw new BusinessException("可用积分不足");
                    }
                    memberScore = new MemberScore();
                    memberScore.setMemberId(memberId);
                    memberScore.setMemberScore(increaseScore);
                    memberScore.setValidScore(increaseScore);
                    memberScore.setCreateTime(DateUtils.getUnixTimestamp());
                    memberScoreMapper.insertSelective(memberScore);
                } else {
                    BigDecimal afterScore=memberScore.getMemberScore().add(increaseScore);
                    if (afterScore.doubleValue()<0){
                        throw new BusinessException("可用积分不足");
                    }
                    memberScore.setMemberScore(afterScore);
                    if (afterScore.doubleValue()>= Double.valueOf(PropertiesUtil.getValue("score.level.one"))){
                        memberScore.setRate(BigDecimal.valueOf(2.00));
                    } else if (afterScore.doubleValue()<= Double.valueOf(PropertiesUtil.getValue("score.level.one.one"))){
                        memberScore.setRate(BigDecimal.valueOf(1.00));
                    } else {
                        Double levelOne=Double.valueOf(PropertiesUtil.getValue("score.level.one"));
                        Double rateBase=Double.valueOf(PropertiesUtil.getValue("score.level.one.base"));
                        BigDecimal rateIncrease = afterScore.subtract(BigDecimal.valueOf(rateBase)).divide(BigDecimal.valueOf(levelOne),1,BigDecimal.ROUND_FLOOR);
                        memberScore.setRate(rateIncrease.add(BigDecimal.ONE));
                    }
                    memberScore.setValidScore(memberScore.getValidScore().add(increaseScore));
                    memberScore.setUpdateTime(DateUtils.getUnixTimestamp());
                    memberScoreMapper.updateByPrimaryKeySelective(memberScore);
                }
                cacheService.setCacheByKey(key,memberScore,60*60);
                if (typeId!=1&&typeId!=2) {
                    //处理上级奖励问题
                    MemberInvitedRecord memberInvitedRecord = memberInvitedRecordMapper.getMemberInvitedRecordByMemberId(memberId);
                    if (memberInvitedRecord != null) {
                        MemberScore memberScore1=memberScoreMapper.getMemberScoreByMemberId(memberInvitedRecord.getParentId());
                        BigDecimal beforeAmount=memberScore1.getMemberScore();
                        BigDecimal afterScore=memberScore1.getMemberScore().add(increaseScore.multiply(BigDecimal.valueOf(0.1)));
                        if (afterScore.doubleValue()<0){
                            throw new BusinessException("可用积分不足");
                        }
                        memberScore.setMemberScore(afterScore);
                        if (afterScore.doubleValue()>= Double.valueOf(PropertiesUtil.getValue("score.level.one"))){
                            memberScore1.setRate(BigDecimal.valueOf(2.00));
                        } else if (afterScore.doubleValue()<= Double.valueOf(PropertiesUtil.getValue("score.level.one.one"))){
                            memberScore1.setRate(BigDecimal.valueOf(1.00));
                        } else {
                            Double levelOne=Double.valueOf(PropertiesUtil.getValue("score.level.one"));
                            Double rateBase=Double.valueOf(PropertiesUtil.getValue("score.level.one.base"));
                            BigDecimal rateIncrease = afterScore.subtract(BigDecimal.valueOf(rateBase)).divide(BigDecimal.valueOf(levelOne),1,BigDecimal.ROUND_FLOOR);
                            memberScore1.setRate(rateIncrease.add(BigDecimal.ONE));
                        }
                        memberScore1.setValidScore(memberScore1.getValidScore().add(increaseScore.multiply(BigDecimal.valueOf(0.1))));
                        memberScore1.setUpdateTime(DateUtils.getUnixTimestamp());
                        memberScoreMapper.updateByPrimaryKeySelective(memberScore1);
                        MemberScoreFlowType memberScoreFlowType1=memberScoreFlowTypeMapper.selectByPrimaryKey(3L);
                        MemberScoreFlow memberScoreFlow1 = new MemberScoreFlow();
                        memberScoreFlow1.setMemberId(memberInvitedRecord.getParentId());
                        memberScoreFlow1.setTypeId(3L);
                        memberScoreFlow1.setDetail(memberScoreFlowType1.getTypeDesc());
                        memberScoreFlow1.setFlowScore(increaseScore.multiply(BigDecimal.valueOf(0.1)));
                        memberScoreFlow1.setMemberScoreAfter(memberScore1.getMemberScore());
                        memberScoreFlow1.setMemberScoreBefore(beforeAmount);
                        memberScoreFlow1.setMemberScoreId(memberScore1.getId());
                        memberScoreFlow1.setCreateTime(DateUtils.getUnixTimestamp());
                        memberScoreFlowMapper.insertSelective(memberScoreFlow1);
                    }
                }
                return memberScore;
            }
        }
    }

    @Override
    public List<MemberScoreFlow> getMemberScoreFlowList(Long memberId) {
        return memberScoreFlowMapper.getMemberScoreFlowListByMemberId(memberId);
    }

}
