package com.kantboot.admin.controller;

import com.kantboot.admin.service.service.IAdminModuleService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员菜单 前端控制器
 *
 * @author 方某方
 */
@RestController
@RequestMapping("/admin/menu")
public class AdminMenuController {

    @Resource
    private IAdminModuleService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 获取菜单
     * @return 所有菜单
     */
    @RequestMapping("/getMenus")
    public RestResult getMenus() {
        return stateSuccessService.success(service.getMenus(),"getSuccess");
    }

    /**
     * 获取不包括目录的一级菜单
     * @return 菜单
     */
    @RequestMapping("/getNoDirectoryMenus")
    public RestResult getNoDirectoryMenus() {
        return stateSuccessService.success(service.getNoDirectoryMenus(),"getSuccess");
    }

}










