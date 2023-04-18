package com.kantboot.system.controller;

import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysPageService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 页面控制器
 * 用于前端获取页面信息
 * @author 方某方
 */
@CrossOrigin
@RestController
@RequestMapping("/system/page")
public class SysPageController {

    @Resource
    private ISysPageService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    @RequestMapping("/get")
    public RestResult get(@RequestParam("code") String code){
        return stateSuccessService.success(service.get(code), "getSuccess");
    }

    @RequestMapping("/getDictList")
    public RestResult getDictList(@RequestParam("code") String code){
        return stateSuccessService.success(service.getDictList(code), "getSuccess");
    }

    @RequestMapping("/getDictMap")
    public RestResult getDictMap(@RequestParam("code") String code){
        return stateSuccessService.success(service.getDictMap(code), "getSuccess");
    }

}
