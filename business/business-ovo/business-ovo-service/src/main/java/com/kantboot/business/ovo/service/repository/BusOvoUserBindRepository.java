package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoUserBind;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 * Ovo用户绑定表
 * @author：方某方
 */
public interface BusOvoUserBindRepository extends JpaRepository<BusOvoUserBind, Long>,
        Repository<BusOvoUserBind, Long> {

    /**
     * 根据用户id查询
     * @param userId 用户id
     * @return BusOvoUserBind Ovo绑定的用户
     */
    BusOvoUserBind findByUserId(Long userId);

    /**
     * 根据分页查询
     * @param pageable 分页
     * @return Page<BusOvoUserBind> Ovo绑定的用户
     */
    Page<BusOvoUserBind> findAll(Pageable pageable);


}
