package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2016/9/19.
 */
@Controller
@RequestMapping("/")
public class PageController extends BaseController{
    @RequestMapping("")
    public String indexPage(){
        return "index";
    }
}
