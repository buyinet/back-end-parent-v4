package com.kantboot.system.controller;


import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysFileGroupService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件组服务前端控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/system/fileGroup")
public class SysFileGroupController {

    @Resource
    private ISysFileGroupService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 创建文件组
     * @param code 文件组编码
     * @param name 文件组名称
     * @return 文件组
     */
    @RequestMapping("/create")
    public RestResult create(String code, String name){
        // 创建成功
        return stateSuccessService.success(service.create(code, name), "createSuccess");
    }

}