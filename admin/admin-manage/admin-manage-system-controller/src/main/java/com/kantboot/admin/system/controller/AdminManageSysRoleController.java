package com.kantboot.admin.system.controller;

import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParam;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParamPageParam;
import com.kantboot.base.controller.BaseAdminController;
import com.kantboot.system.module.entity.SysRole;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.service.ISysRoleService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 系统管理-角色管理 前端控制器
 * @author 方某方
 */
@CrossOrigin
@RestController
@RequestMapping("/adminManage/system/role")
public class AdminManageSysRoleController extends BaseAdminController<SysRole,Long> {

    @Resource
    ISysRoleService roleService;


    @Override
    public RestResult getList(CommonParam<SysRole> param) {
        RestResult result = super.getList(param);
        result.setData(roleService.getByRoles((List<SysRole>) result.getData()));
        return result;
    }
}
