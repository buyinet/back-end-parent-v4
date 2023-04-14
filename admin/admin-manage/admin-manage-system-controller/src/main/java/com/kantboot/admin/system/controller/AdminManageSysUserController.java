package com.kantboot.admin.system.controller;

import com.kantboot.base.controller.BaseAdminController;
import com.kantboot.system.module.entity.SysUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理-用户管理 前端控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/adminManage/system/user")
public class AdminManageSysUserController extends BaseAdminController<SysUser,Long> {

}
