package com.kantboot.base.service.impl;

import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParam;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParamPageParam;
import com.kantboot.admin.util.old.nanshouxiangku.service.OldBaseService;
import com.kantboot.amin.util.operate.BaseAdminOperate;
import com.kantboot.base.service.IBaseAdminService;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.util.common.exception.BaseException;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 管理员服务基类实现类
 * @param <T> 实体类
 * @param <ID> 主键类型
 * @author 方某方
 */
@Slf4j
@Service
public class BaseAdminServiceImpl<T,ID> implements IBaseAdminService<T,ID> {

    @Resource
    private EntityManagerFactory entityManagerFactory;

    @Resource
    private BaseAdminOperate<T,ID> baseAdminOperate;

    @Resource
    private ISysExceptionService exceptionService;

    @Resource
    private OldBaseService<T,ID> oldBaseService;

    @Override
    public List<T> getList(CommonParam<T> commonParam) {
        return oldBaseService.findCommonByList(commonParam);
    }


    @Override
    public HashMap<String,Object> getPage(CommonParamPageParam<T> pageParam) {
        return oldBaseService.findCommonByPage(pageParam);
    }

    @Override
    public void delete(T entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<>((Class<T>) entity.getClass(), entityManager);
            jpaRepository.deleteById(baseAdminOperate.getId(entity));
            transaction.commit();
            entityManager.close();
        } catch (Exception e) {
            transaction.rollback();
            entityManager.close();
            log.error("删除失败", e);
            // 告知前端删除失败
            throw exceptionService.getException("deleteFail");
        }
    }

    @Override
    public void save(T entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<>((Class<T>) entity.getClass(), entityManager);

            if (baseAdminOperate.getId(entity) == null) {
                // 如果id为空，那么就是新增
                jpaRepository.save(entity);
            }

            if (baseAdminOperate.getId(entity) != null) {
                // 如果id不为空，那么就是修改

                // 先根据id查询出来，然后再把entity的值赋值给byId
                T byId = jpaRepository.findById(baseAdminOperate.getId(entity))
                        .orElseThrow(() ->
                                // 如果根据id查询不到，那么就抛出异常，告知前端数据不存在
                                exceptionService.getException("dataNotExist")
                        );

                // 把entity的值赋值给byId，但是不赋值null的值
                BeanUtils.copyProperties(entity, byId, baseAdminOperate.getNullPropertyNames(entity));
                // 保存
                jpaRepository.saveAndFlush(byId);
            }
            transaction.commit();
            entityManager.close();
        }
        catch (BaseException e){
            transaction.rollback();
            entityManager.close();
            throw e;
        }
        catch (Exception e) {
            transaction.rollback();
            entityManager.close();
            log.error("保存失败", e);
            // 告知前端保存失败
            throw exceptionService.getException("saveFail");
        }
    }

    @Override
    public T getById(T entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<>((Class<T>) entity.getClass(), entityManager);
            T result = jpaRepository.findById(baseAdminOperate.getId(entity)).orElse(null);
            transaction.commit();
            entityManager.close();
            return result;
        } catch (Exception e) {
            transaction.rollback();
            entityManager.close();
            log.error("查询失败", e);
            // 告知前端查询失败
            throw exceptionService.getException("getFail");
        }
    }


}
