package com.kantboot.base.controller;

import com.alibaba.fastjson2.JSON;
import com.kantboot.admin.util.old.nanshouxiangku.controller.OldBaseAdminController;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParam;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParamPageParam;
import com.kantboot.base.service.IBaseAdminService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 管理员基础控制器
 * 用于继承
 * @param <T> 实体类
 * @param <ID> 主键的类型
 * @author 方某方
 */
@RestController
public class BaseAdminController<T,ID> extends OldBaseAdminController<T,ID> {

    @Resource
    private IBaseAdminService<T,ID> service;

    @Resource
    private IStateSuccessService stateSuccessService;



    /**
     * 通用删除
     * @param entity 实体类
     * @return 结果
     */
    @RequestMapping("/delete")
    public RestResult delete(@RequestBody T entity) {
        service.delete(entity);
        return stateSuccessService.success(null, "deleteSuccess");
    }

    /**
     * 通用保存
     * @param entity 实体类
     * @return 结果
     */
    @RequestMapping("/save")
    public RestResult save(@RequestBody T entity) {
        service.save(entity);
        return stateSuccessService.success(null, "saveSuccess");
    }

    /**
     * 通用根据id查询
     * @param entity 实体类
     * @return 结果
     */
    @RequestMapping("/getById")
    public RestResult getById(@RequestBody T entity) {
        return stateSuccessService.success(service.getById(entity), "getSuccess");
    }

    @RequestMapping("/getList")
    public RestResult getList(@RequestBody CommonParam<T> param) {
        return stateSuccessService.success(findCommonByList(param), "getSuccess");
    }

    @RequestMapping("/getPage")
    public RestResult<HashMap<String,Object>> getPage(@RequestBody CommonParamPageParam<T> param) {
        return stateSuccessService.success(findCommonByPage(param), "getSuccess");
    }

}
