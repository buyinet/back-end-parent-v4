package com.kantboot.system.controller;

import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysRoleService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统角色控制器
 * 用于前端获取角色信息
 * @author 方某方
 */
@CrossOrigin
@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    @Resource
    private ISysRoleService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    @RequestMapping("/getI18nMap")
    public RestResult getI18nMap(){
        return stateSuccessService.success(service.getI18nMap(), "getSuccess");
    }

}
