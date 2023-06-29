package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Ovo用户绑定表
 * @author：方某方
 */
public interface BusOvoUserRepository extends JpaRepository<BusOvoUser, Long> {

    /**
     * 根据用户id查询
     * @param userId 用户id
     * @return BusOvoUserBind Ovo绑定的用户
     */
    BusOvoUser findByUserId(Long userId);

    /**
     * 根据分页查询
     * @param pageable 分页
     * @return Page<BusOvoUserBind> Ovo绑定的用户
     */
    Page<BusOvoUser> findAll(Pageable pageable);

    /**
     * 根据分页查询，且排除某个用户
     * @param pageable 分页
     * @param userId 用户id
     * @return Page<BusOvoUserBind> Ovo绑定的用户
     */
    @Query(value = "SELECT b FROM BusOvoUser b WHERE b.userId <> :userId")
    Page<BusOvoUser> findAllExcludeUserId(Pageable pageable, Long userId);


}
