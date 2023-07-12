package com.kantboot.admin.system.controller;

import com.alibaba.fastjson2.JSON;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParamPageParam;
import com.kantboot.base.controller.BaseAdminController;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.module.entity.SysUserInAdmin;
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
 * 系统管理-用户管理 前端控制器
 * @author 方某方
 */
@CrossOrigin
@RestController
@RequestMapping("/adminManage/system/user")
public class AdminManageSysUserController extends BaseAdminController<SysUserInAdmin,Long> {

    @Resource
    ISysRoleService roleService;


    @Override
    public RestResult<HashMap<String, Object>> getPage(@RequestBody CommonParamPageParam<SysUserInAdmin> param) {
        RestResult<HashMap<String, Object>> page = super.getPage(param);
        HashMap<String, Object> data = page.getData();
        Object content = data.get("content");
        List<SysUser> sysUser = (List<SysUser>) content;
        for (SysUser user : sysUser) {
            user.setRoles(roleService.getByRoles(user.getRoles()));
        }

        data.put("content",sysUser);
        return page;
    }
}
