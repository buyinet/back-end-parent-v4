package com.kantboot.system.controller;

import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工具控制器
 * 用于提供一些工具接口
 * @author 方某方
 */
@RestController
@RequestMapping("/system/util")
public class UtilController {

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 获取当前的GMT时间
     * @return 当前的GMT时间
     */
    @RequestMapping("/getGMTTime")
    public RestResult getGMTTime() {
        return stateSuccessService.success(System.currentTimeMillis(), "getSuccess");
    }


}
