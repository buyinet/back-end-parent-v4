package com.kantboot.admin.system.controller;

import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParam;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParamPageParam;
import com.kantboot.admin.util.old.nanshouxiangku.entity.OperatorEntity;
import com.kantboot.base.controller.BaseAdminController;
import com.kantboot.system.module.entity.SysDictI18n;
import com.kantboot.system.module.entity.SysRole;
import com.kantboot.system.service.ISysRoleService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @Resource
    AdminManageSysDictI18nController adminManageSysDictI18nController;


    @Override
    public RestResult getList(@RequestBody CommonParam<SysRole> param) {
        RestResult result = super.getList(param);
        result.setData(roleService.getByRoles((List<SysRole>) result.getData()));
        return result;
    }

    /**
     * 获取角色列表
     * @param param 参数
     * @return 角色列表
     */
    @RequestMapping("/getI18nPage")
    public RestResult getI18nPage(@RequestBody CommonParamPageParam<SysDictI18n> param) {
        if (null == param.getData()) {
            param.setData(new CommonParam<>());
        }
        SysDictI18n entity = param.getData().getEntity();
        if (null == entity) {
            entity = new SysDictI18n();
            param.getData().setEntity(entity);
        }
        if (null == param.getData().getAnd()){
            param.getData().setAnd(new OperatorEntity<>());
        }
        if (null == param.getData().getAnd().getEq()){
            param.getData().getAnd().setEq(new ArrayList<>());
        }
        param.getData().getAnd().getEq().add(new SysDictI18n().setDictGroupCode("role"));

        return adminManageSysDictI18nController.getPage(param);
    }


}
