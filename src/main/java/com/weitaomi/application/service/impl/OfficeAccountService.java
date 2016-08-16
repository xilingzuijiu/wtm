package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.model.bean.OfficalAccount;
import com.weitaomi.application.model.bean.WtmOfficialMember;
import com.weitaomi.application.model.dto.AddOfficalAccountDto;
import com.weitaomi.application.model.dto.AddResponseTaskDto;
import com.weitaomi.application.model.dto.OfficialAccountMsg;
import com.weitaomi.application.model.dto.UnFollowOfficicalAccountDto;
import com.weitaomi.application.model.mapper.OfficalAccountMapper;
import com.weitaomi.application.model.mapper.WtmOfficialMemberMapper;
import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.application.service.interf.IOfficeAccountService;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.util.HttpRequestUtils;
import com.weitaomi.systemconfig.util.PropertiesUtil;
import com.weitaomi.systemconfig.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by supumall on 2016/8/9.
 */
@Service
public class OfficeAccountService implements IOfficeAccountService {
    private static Logger logger= LoggerFactory.getLogger(OfficeAccountService.class);
    @Autowired
    private OfficalAccountMapper officalAccountMapper;
    @Autowired
    private WtmOfficialMemberMapper wtmOfficialMemberMapper;
    @Autowired
    private ICacheService cacheService;
    @Override
    public List<UnFollowOfficicalAccountDto> getAccountsByMemberId(Long memberId){
        return officalAccountMapper.getAccountsByMemberId(memberId);
    }
    @Override
    @Transactional
    public void pushAddRequest(AddOfficalAccountDto addOfficalAccountDto) {
        if (addOfficalAccountDto==null){
            throw new BusinessException("任务列表为空");
        }
        String unionId=addOfficalAccountDto.getUnionId();
        if (StringUtil.isEmpty(unionId)){
            throw new BusinessException("用户唯一识别码为空");
        }
        WtmOfficialMember wtmOfficialMember=wtmOfficialMemberMapper.getWtmOfficialMemberByUnionId(unionId);
        String nickName="";
        if (wtmOfficialMember!=null){
            nickName=wtmOfficialMember.getNickname();
        }
        List<OfficialAccountMsg> list=addOfficalAccountDto.getLinkList();
        if (list.isEmpty()){
            throw new BusinessException("要关注的公号列表为空");
        }
        List<Map<String,String>> mapList=new ArrayList<Map<String, String>>();
        Map<String,Object> params=new ManagedMap<String, Object>();
        params.put("unionId",addOfficalAccountDto.getUnionId());
        for (OfficialAccountMsg officialAccountMsg:list){
            String key=nickName+":"+officialAccountMsg.getOriginId();
            OfficalAccount officalAccount=officalAccountMapper.getOfficalAccountByoriginId(officialAccountMsg.getOriginId());
            String value=officalAccount.getId().toString();
            cacheService.setCacheByKey(key,value,60*30);
            Map<String,String> map=new HashMap<String, String>();
            map.put("name",officialAccountMsg.getUsername());
            map.put("addUrl",officialAccountMsg.getAddUrl());
            mapList.add(map);
        }
        params.put("linkList",mapList);
        String param= JSON.toJSONString(params);
        String url= PropertiesUtil.getValue("server.officialAccount.receiveAddRequest.url");
        try {
            HttpRequestUtils.postStringEntity(url,param);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveAddOffical(AddResponseTaskDto addResponseTaskDto) {

    }

    @Override
    public List<OfficialAccountMsg> getOfficialAccountMsg(Long memberId) {
        List<OfficialAccountMsg> officialAccountMsgs=officalAccountMapper.getOfficialAccountMsg(memberId);
        if (officialAccountMsgs.isEmpty()){
            return null;
        }
        return officialAccountMsgs;
    }

}
