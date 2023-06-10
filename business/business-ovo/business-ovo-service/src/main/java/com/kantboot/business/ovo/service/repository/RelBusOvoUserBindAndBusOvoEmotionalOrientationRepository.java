package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.RelBusOvoUserBindAndBusOvoEmotionalOrientation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * Ovo用户绑定表和Ovo情感属性表关联表仓库接口
 * 由于是多对多关系，所以不需要实现类
 * @author 方某方
 */
public interface RelBusOvoUserBindAndBusOvoEmotionalOrientationRepository extends JpaRepository<RelBusOvoUserBindAndBusOvoEmotionalOrientation, Long>, Repository<RelBusOvoUserBindAndBusOvoEmotionalOrientation, Long> {

    /**
     * 根据用户id删除
     * @param userId 用户id
     * @return 删除的数量
     */
    Integer deleteByUserId(Long userId);
}
