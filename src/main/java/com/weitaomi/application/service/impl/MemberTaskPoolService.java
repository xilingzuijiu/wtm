package com.weitaomi.application.service.impl;

import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.model.dto.Address;
import com.weitaomi.application.model.dto.OfficicalAccountsDto;
import com.weitaomi.application.model.dto.RequireFollowerParamsDto;
import com.weitaomi.application.model.mapper.OfficalAccountMapper;
import com.weitaomi.application.model.mapper.ProvinceMapper;
import com.weitaomi.application.model.mapper.TaskPoolMapper;
import com.weitaomi.application.service.interf.IMemberTaskPoolService;
import com.weitaomi.systemconfig.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
@Service
public class MemberTaskPoolService implements IMemberTaskPoolService{
    @Autowired
    private TaskPoolMapper taskPoolMapper;
    @Autowired
    private OfficalAccountMapper officalAccountMapper;
    @Autowired
    private ProvinceMapper provinceMapper;
    @Override
    public boolean uploadTaskPool(TaskPool taskPool) {
        if (taskPool!=null){
            int num =taskPoolMapper.insertSelective(taskPool);
            return num>0?true:false;
        }
        return false;
    }
    @Override
    public RequireFollowerParamsDto getRequireFollowerParamsDto(Long memberId, Long time) {
        RequireFollowerParamsDto requireFollowerParamsDto=new RequireFollowerParamsDto();
        List<OfficicalAccountsDto> accountList=officalAccountMapper.getAccountsByMemberId(memberId);
        if (accountList.isEmpty()){
            throw new BusinessException("公众号列表为空");
        }
        requireFollowerParamsDto.setOfficialAccountList(accountList);
        List<Address> addressList=provinceMapper.getAllAddress();
        if (addressList.isEmpty()){
            throw new BusinessException("获取地区列表失败，请稍后再试");
        }
        requireFollowerParamsDto.setAddressList(addressList);
        return requireFollowerParamsDto;
    }
}
