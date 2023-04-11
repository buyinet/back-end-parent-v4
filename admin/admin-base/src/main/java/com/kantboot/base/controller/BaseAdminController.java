package com.kantboot.base.controller;

import com.kantboot.amin.util.operate.BaseAdminOperate;
import com.kantboot.base.service.IBaseAdminService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员基础控制器
 * 用于继承
 * @param <T> 实体类
 * @param <ID> 主键的类型
 * @author 方某方
 */
@RestController
public class BaseAdminController<T,ID> {

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

}
