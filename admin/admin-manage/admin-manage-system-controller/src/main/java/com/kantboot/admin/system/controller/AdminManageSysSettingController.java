package com.kantboot.admin.system.controller;

import com.kantboot.base.controller.BaseAdminController;
import com.kantboot.system.module.entity.SysSetting;
import com.kantboot.system.module.entity.SysSettingGroup;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统设置分组管理 前端控制器
 * @author 方某方
 */
@CrossOrigin
@RestController
@RequestMapping("/adminManage/system/setting")
public class AdminManageSysSettingController extends BaseAdminController<SysSetting,Long> {
    
}
