package com.kantboot.admin.util.old.nanshouxiangku.controller;

import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParam;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParamPageParam;
import com.kantboot.admin.util.old.nanshouxiangku.util.FindCommonUtil;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;


@RestController
@Slf4j
public abstract class OldBaseAdminController<T, ID> {

    @Resource
    EntityManagerFactory entityManagerFactory;

    @Resource
    FindCommonUtil<T,ID> findCommonUtil;

    @Resource
    IStateSuccessService stateSuccessService;

    @Resource
    ISysExceptionService exceptionService;

    /**
     * 通用查询
     *
     * @return
     */
    @PostMapping("/find_common_list")
    public RestResult findCommonByList(@RequestBody CommonParam<T> commonEntity) {
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

            return stateSuccessService.success(all,"getSuccess");
        } catch (Exception e) {
            log.error("findCommonByList", e);
            throw exceptionService.getException("getException");
        } finally {
            entityManager.close();
        }
    }

    /**
     * 通用查询
     *
     * @return
     */
    @PostMapping("/find_common_page")
    public RestResult<HashMap<String,Object>> findCommonByPage(@RequestBody CommonParamPageParam<T> pageParam) {
        CommonParam<T> commonEntity = pageParam.getData();
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
            HashMap<String, Object> result = new HashMap<>();
            result.put("totalElements", all.getTotalElements());
            result.put("totalPage", all.getTotalPages());
            result.put("content", all.getContent());
            result.put("number", all.getNumber()+1);
            result.put("size", all.getSize());

            return stateSuccessService.success(result,"getSuccess");
        } catch (Exception e) {
            log.error("findCommonByPage", e);
            throw exceptionService.getException("getException");
        } finally {
            if(entityManager!=null){
                entityManager.close();
            }
        }
    }


}
