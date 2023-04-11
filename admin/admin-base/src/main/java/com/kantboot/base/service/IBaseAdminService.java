package com.kantboot.base.service;

import org.springframework.web.bind.annotation.RequestBody;

/**
 * 管理员服务基类
 * @author 方某方
 */
public interface IBaseAdminService<T,ID> {

    /**
     * 删除
     * @param entity 实体类
     */
    void delete(T entity);

    /**
     * 保存
     * 有id则更新，无id则新增
     * @param entity 实体类
     */
    void save(T entity);

    /**
     * 根据id查询
     * @param entity 实体类
     *               获取其中的id
     * @return 实体类
     */
    T getById(T entity);

}
