package com.kantboot.admin.system.controller;

import com.kantboot.base.controller.BaseAdminController;
import com.kantboot.system.module.entity.SysRole;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理-角色管理 前端控制器
 * @author 方某方
 */
@CrossOrigin
@RestController
@RequestMapping("/adminManage/system/role")
public class AdminManageSysRoleController extends BaseAdminController<SysRole,Long> {
}
