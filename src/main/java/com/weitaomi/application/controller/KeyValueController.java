package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.dto.KeyValueDto;
import com.weitaomi.application.service.interf.IKeyValueService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2016/11/5.
 */
@Controller
@RequestMapping("/common/admin/keyValue")
public class KeyValueController extends BaseController {
    @Autowired
    private IKeyValueService keyValueService;
    @ResponseBody
    @RequestMapping(value = "/getKeyValueDtoList",method = RequestMethod.GET)
    public AjaxResult getKeyValueDtoList(String mapKey,String id){
        return  AjaxResult.getOK(keyValueService.getKeyValueDtoList(mapKey,id));
    }
}
