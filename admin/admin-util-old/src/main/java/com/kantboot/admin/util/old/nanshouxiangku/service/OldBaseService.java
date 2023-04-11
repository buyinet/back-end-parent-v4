package com.kantboot.admin.util.old.nanshouxiangku.service;

import com.kantboot.system.service.ISysExceptionService;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParam;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParamPageParam;
import com.kantboot.admin.util.old.nanshouxiangku.util.FindCommonUtil;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;


/**
 * 通用查询
 * 对很早之前的代码稍微改了一下下
 * 很早之前的代码，代码太乱了，
 * 不适合维护和优化，但是需要使用，之后要重构
 * @param <T>
 * @param <ID>
 * @author 方某方
 */
@Service
@Slf4j
public abstract class OldBaseService<T, ID> {

    @Resource
    private EntityManagerFactory entityManagerFactory;

    @Resource
    private FindCommonUtil<T,ID> findCommonUtil;

    @Resource
    private ISysExceptionService exceptionService;

    /**
     * 通用查询
     *
     * @return
     */
    public List<T> findCommonByList(CommonParam<T> commonParam) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();
            //返回表示此类型实际类型参数的 Type 对象的数组(),赋值给this.classt
            Class entityClass = (Class)type.getActualTypeArguments()[0];
            Class<T> aClass = entityClass;

            SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>(aClass, entityManager);

            Specification<T> specification = findCommonUtil.findCommon(commonParam, entityManager, transaction);
            List<T> all = jpaRepository.findAll(specification);
            transaction.commit();
            entityManager.close();
            return all;
        } catch (Exception e) {
            e.printStackTrace();
            if(entityManager!=null){
                entityManager.close();
            }
            throw exceptionService.getException("queryError");
        } finally {
            entityManager.close();
        }
    }

    /**
     * 通用查询
     */
    public HashMap<String, Object> findCommonByPage(CommonParamPageParam<T> pageParam) {
        CommonParam<T> commonParam = pageParam.getData();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();
            // 返回表示此类型实际类型参数的 Type 对象的数组(),赋值给this.classt
            Class entityClass = (Class)type.getActualTypeArguments()[0];
            Class<T> aClass = entityClass;

            commonParam.setEntity((T) entityClass.newInstance());

            SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>(aClass, entityManager);
            Specification<T> specification = findCommonUtil.findCommon(commonParam, entityManager, transaction);
            Page<T> all = jpaRepository.findAll(specification, pageParam.getPageable());
            transaction.commit();
            entityManager.close();
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
            throw exceptionService.getException("queryError");
        } finally {
            if(entityManager!=null){
                entityManager.close();
            }
        }
    }


}
