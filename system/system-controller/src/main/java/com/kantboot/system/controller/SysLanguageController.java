package com.kantboot.system.controller;


import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysLanguageService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统语言控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/system/language")
public class SysLanguageController {

    @Resource
    private ISysLanguageService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 返回所有支持的语言
     * @return 结果
     */
    @RequestMapping("/getLanguages")
    public RestResult getLanguages(){
        // 返回所有支持的语言，并告知前端获取成功
        return stateSuccessService.success(service.getLanguages(), "getSuccess");
    }

}
