package com.kantboot.admin.system.controller;

import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParamPageParam;
import com.kantboot.base.controller.BaseAdminController;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.util.common.result.RestResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 系统管理-用户管理 前端控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/adminManage/system/user")
public class AdminManageSysUserController extends BaseAdminController<SysUser,Long> {

}
