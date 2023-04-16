package com.kantboot.system.controller;

import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysDictI18nService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典国际化控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/system/dictI18n")
public class SysDictI18nController {

    @Resource
    private ISysDictI18nService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 根据字典分组编码获取字典
     *
     * @param dictGroupCode 字典分组编码
     * @return 字典
     */
    @RequestMapping("/getMap")
    public RestResult getMap(@RequestParam("dictGroupCode") String dictGroupCode){
        return stateSuccessService.success(service.getMap(dictGroupCode), "getSuccess");
    }

    /**
     * 根据字典分组编码和字典编码获取字典
     * @param dictGroupCode 字典分组编码
     * @param dictCode 字典编码
     * @return 字典
     */
    @RequestMapping("/getValue")
    public RestResult<String> getValue(@RequestParam("dictGroupCode") String dictGroupCode,
                               @RequestParam("dictCode") String dictCode){
        return stateSuccessService.success(service.getValue(dictGroupCode, dictCode), "getSuccess");
    }

    /**
     * 获取前端的字典表
     * 获取分组编码是appFront的字典表
     * @return 结果
     */
    @RequestMapping("/getAppFront")
    public RestResult getAppFront(){
        return stateSuccessService.success(service.getAppFront(), "getSuccess");
    }

}
