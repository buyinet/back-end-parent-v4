package com.kantboot.admin.system.controller;

import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParam;
import com.kantboot.base.controller.BaseAdminController;
import com.kantboot.system.module.entity.SysSettingGroup;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.service.ISysDictI18nService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 系统设置分组管理 前端控制器
 * @author 方某方
 */
@CrossOrigin
@RestController
@RequestMapping("/adminManage/system/settingGroup")
public class AdminManageSysSettingGroupController extends BaseAdminController<SysSettingGroup,Long> {

    @Resource
    ISysDictI18nService dictI18nService;

    @Override
    public RestResult getList(@RequestBody CommonParam<SysSettingGroup> param) {
        Map<String, String> groupSetting = dictI18nService.getMap("groupSetting");

        RestResult result = super.getList(param);
        List<SysSettingGroup> data = (List<SysSettingGroup>) result.getData();
        data.forEach(item->{
            String name = groupSetting.get(item.getCode());
            item.setName(name==null?item.getName():name);
        });
        return result;
    }
}
