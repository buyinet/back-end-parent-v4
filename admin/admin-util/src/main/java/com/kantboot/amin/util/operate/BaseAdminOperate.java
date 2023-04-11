package com.kantboot.amin.util.operate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.amin.util.param.IndirectSelectParam;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.SneakyThrows;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 管理员基础工具类
 * @param <T>  实体类
 * @param <ID> 主键的类型
 * @author 方某方
 */
@Component
public class BaseAdminOperate<T, ID> {

    @Resource
    private EntityManagerFactory entityManagerFactory;


    /**
     * 根据实体类获取id
     *
     * @param entity 实体类
     * @return id 主键
     */
    @SneakyThrows
    public ID getId(T entity) {
       return (ID) entityManagerFactory.getPersistenceUnitUtil().getIdentifier(entity);
    }

    /**
     * 获取值为 null 的属性名称
     * @param source 实体类
     * @return 属性名称数组
     */
    public String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        // 获取值为 null 的属性名称
        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null){
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 根据条件查询获取list
     * @param param 参数
     * @return 查询条件
     */
    public List<T> getList(IndirectSelectParam<T> param) {
        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                // 设置and 下 = 的查询条件
                Predicate andEqPredicate = criteriaBuilder.conjunction();
                // 设置查询条件
                if (null!=param.getAnd()||null!=param.getAnd().getEq()) {
                    JSONObject.parseObject(JSON.toJSONString(param.getAnd().getEq())).forEach((k, v) -> {
                        andEqPredicate.getExpressions().add(criteriaBuilder.equal(root.get(k), v));
                    });
                }

                Predicate and = criteriaBuilder.and(andEqPredicate);
                return null;
            }
        };
        return null;
    }


}
