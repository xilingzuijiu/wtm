package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.Account;
import com.weitaomi.application.model.mapper.AccountMapper;
import com.weitaomi.application.service.interf.IAppVersionService;
import com.weitaomi.application.service.interf.IBackPageService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.util.GetMacAddress;
import com.weitaomi.systemconfig.util.IpUtils;
import com.weitaomi.systemconfig.util.PropertiesUtil;
import com.weitaomi.systemconfig.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Decoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/3.
 */
@Controller
@RequestMapping("")
public class BackPageController extends BaseController {
    @Autowired
    private IAppVersionService appVersionService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private IBackPageService backPageService;
    @RequestMapping("/backDeal")
    public ModelAndView redirectToBackDeal(HttpServletRequest request, HttpServletResponse response) {
        String ip = IpUtils.getIpAddr(request);
        String ipWhite = PropertiesUtil.getValue("backDeal.ipWhites");
        boolean flag = false;
        if (ip.matches(ipWhite)) {
            flag = true;
        }
        if (!flag) {
            String mac = GetMacAddress.getMacAddress(ip);
            String[] macs = PropertiesUtil.getValue("backDeal.macWhites").split(";");
            for (String temp : macs) {
                if (mac == temp) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            ModelAndView modelAndView = new ModelAndView();
            String sId = request.getHeader("accountId");
            Long accountId = 0L;
            if (!StringUtils.isEmpty(sId)) {
                accountId = Long.parseLong(sId);
                HttpSession session = request.getSession();
                if (session != null) {
                    String realName = (String) session.getAttribute("realName");
                    if (!StringUtil.isEmpty(realName)) {
                        Account account = accountMapper.getAccount(realName);
                        if (account != null) {
                            //成功进入
                            modelAndView.setViewName("/frontPage/backward/withdraws.html");
                        } else {
                            //失败进入
                            modelAndView.setViewName("/backDeal/login");
                            return modelAndView;
                        }
                    }
                } else {
                    //失败进入
                    modelAndView.setViewName("/backDeal/login");
                    return modelAndView;
                }
            } else {
                //失败进入
                modelAndView.setViewName("/backDeal/login");
                return modelAndView;
            }
        }
        return null;
    }

    @RequestMapping("/backDeal/login")
    public ModelAndView login(HttpServletRequest request,String realName,String password,HttpServletResponse response) {
        String ip = IpUtils.getIpAddr(request);
        String ipWhite = PropertiesUtil.getValue("backDeal.ipWhites");
        boolean flag = false;
        if (ip.matches(ipWhite)) {
            flag = true;
        }
        if (!flag) {
            String mac = GetMacAddress.getMacAddress(ip);
            String[] macs = PropertiesUtil.getValue("backDeal.macWhites").split(";");
            for (String temp : macs) {
                if (mac == temp) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            Account account=new Account();
            account.setRealName(realName);
            account.setPassword(password);
            Account account1=accountMapper.selectOne(account);
            if (account1!=null){
                HttpSession session=request.getSession();
                Cookie idCookie=new Cookie("accountId",account.getId().toString());
                idCookie.setMaxAge(24*60*60);
                idCookie.setPath("/frontPage/backward");
                response.addCookie(idCookie);
                session.setAttribute("realName",realName);
                ModelAndView modelAndView=new ModelAndView("/backDeal");
                return modelAndView;
            }
        }
        return  null;
    }
    @ResponseBody
    @RequestMapping(value = "/pc/backDeal/dealWithArticle",method = RequestMethod.POST)
    public AjaxResult getAllWaitApprovalArticleList(@RequestParam(name = "pageIndex",defaultValue = "0",required = false) Integer pageIndex,
                                                    @RequestParam(name = "pageSize",defaultValue = "10",required = false) Integer pageSize){
        if (pageIndex==null){
            pageIndex=0;
        }
        if (pageSize==null){
            pageSize=10;
        }
        return AjaxResult.getOK(backPageService.getAllArticle(pageIndex, pageSize));
    }
    @ResponseBody
    @RequestMapping("/pc/backDeal/patchCheckArticle")
    public AjaxResult patchCheckArticle(List<Long> poolIdList){
        return AjaxResult.getOK(backPageService.patchCheckArticle(poolIdList));
    }

    /**
     * 上传文件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/backward/uploadUnyunFiles", method = RequestMethod.POST)
    public AjaxResult uploadShowImage(HttpServletRequest request,@RequestBody(required = true) Map<String,String> params){
        Long memberId=this.getUserId(request);
        String files=params.get("files");
        String path=params.get("path");
        String suffix=params.get("suffix");
        String yunPath="";
        if (!StringUtil.isEmpty(files)&&!StringUtil.isEmpty(path)&&!StringUtil.isEmpty(suffix)){
            yunPath=backPageService.uploadUnyunFiles(path,files,suffix);
        }
        return AjaxResult.getOK(yunPath);
    }

}
