package com.kantboot.amin.util.operate;

import jakarta.persistence.Id;
import lombok.SneakyThrows;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 管理员基础工具类
 * 用于继承
 *
 * @param <T>  实体类
 * @param <ID> 主键的类型
 * @author 方某方
 */
@Component
public class BaseAdminOperate<T, ID> {

    /**
     * 根据实体类获取id
     *
     * @param entity 实体类
     * @return id 主键
     */
    @SneakyThrows
    public ID getId(T entity) {
        // 查询又@Id注解的字段
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Id.class)) {
                declaredField.setAccessible(true);
                return (ID) declaredField.get(entity);
            }
        }
        return null;
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


}
