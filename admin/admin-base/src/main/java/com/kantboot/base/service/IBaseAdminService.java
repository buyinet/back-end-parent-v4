package com.kantboot.base.service;

import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParamPageParam;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParam;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;

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

    /**
     * 查询所有，不分页
     * @param commonParam 实体类
     * @return 实体类
     */
    List<T> getList(CommonParam<T> commonParam);

    /**
     * 分页查询
     * @param pageParam 实体类
     * @return 实体类
     */
    HashMap<String,Object> getPage(CommonParamPageParam<T> pageParam);

}
