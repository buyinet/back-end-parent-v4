package com.kantboot.admin.util.old.nanshouxiangku.controller;

import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.bind.annotation.RestController;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonEntity;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonEntityPageParam;
import com.kantboot.admin.util.old.nanshouxiangku.util.FindCommonUtil;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;


/**
 * 通用查询
 * 很早之前的代码，代码太乱了，不适合维护和优化，但是需要使用
 * @param <T>
 * @param <ID>
 * @author 方某方
 */
@RestController
@Slf4j
public abstract class OldBaseService<T, ID> {

    @Resource
    EntityManagerFactory entityManagerFactory;

    @Resource
    FindCommonUtil<T> findCommonUtil;

    /**
     * 通用查询
     *
     * @return
     */
    public List<T> findCommonByList(CommonEntity<T> commonEntity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();
            //返回表示此类型实际类型参数的 Type 对象的数组(),赋值给this.classt
            Class entityClass = (Class)type.getActualTypeArguments()[0];
            Class<T> aClass = entityClass;

            SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>(aClass, entityManager);

            Specification<T> specification = findCommonUtil.findCommon(commonEntity, entityManager, transaction);
            List<T> all = jpaRepository.findAll(specification);
            transaction.commit();
            entityManager.close();
            return all;
        } catch (Exception e) {
            e.printStackTrace();
            if(entityManager!=null){
                entityManager.close();
            }
        } finally {
            entityManager.close();
        }
        return null;
    }

    /**
     * 通用查询
     *
     * @return
     */
    public HashMap<String, Object> findCommonByPage(CommonEntityPageParam<T> pageParam) {
        CommonEntity<T> commonEntity = pageParam.getData();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();
            // 返回表示此类型实际类型参数的 Type 对象的数组(),赋值给this.classt
            Class entityClass = (Class)type.getActualTypeArguments()[0];
            Class<T> aClass = entityClass;

            commonEntity.setEntity((T) entityClass.newInstance());

            SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>(aClass, entityManager);
            Specification<T> specification = findCommonUtil.findCommon(commonEntity, entityManager, transaction);
            Page<T> all = jpaRepository.findAll(specification, pageParam.getPageable());
            transaction.commit();
            entityManager.close();
            long endDate = System.currentTimeMillis();
            HashMap<String, Object> result = new HashMap<>();
            result.put("totalElements", all.getTotalElements());
            result.put("totalPage", all.getTotalPages());
            result.put("content", all.getContent());
            result.put("number", all.getNumber()+1);
            result.put("size", all.getSize());

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            if(entityManager!=null){
                entityManager.close();
            }
        } finally {
            if(entityManager!=null){
                entityManager.close();
            }
        }
        return null;
    }


}
