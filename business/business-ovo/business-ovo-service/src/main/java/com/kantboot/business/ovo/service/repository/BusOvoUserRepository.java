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
     * 查询userId大于某个userId的用户，根据创建时间排序
     * @param pageable 分页
     * @param id 用户id
     * @return Page<BusOvoUserBind> Ovo绑定的用户
     */
    Page<BusOvoUser> findAllByUserIdGreaterThanOrderByGmtCreate(Pageable pageable, Long id);

    /**
     * 查询userId小于某个userId的用户，根据创建时间排序
     * @param pageable 分页
     * @param id 用户id
     * @return Page<BusOvoUserBind> Ovo绑定的用户
     */
    Page<BusOvoUser> findAllByUserIdLessThanOrderByGmtCreate(Pageable pageable, Long id);

    /**
     * 根据分页查询，且排除某个用户
     * @param pageable 分页
     * @param userId 用户id
     * @return Page<BusOvoUserBind> Ovo绑定的用户
     */
    @Query(value = "SELECT b FROM BusOvoUser b WHERE b.userId <> :userId")
    Page<BusOvoUser> findAllExcludeUserId(Pageable pageable, Long userId);

    @Query(value = "SELECT a.*, ROUND(6378.138 * 2 * ASIN(SQRT(POW(SIN((:latitude * PI() / 180 - e.latitude * PI() / 180) / 2), 2) + COS(:latitude * PI() / 180) * COS(e.latitude * PI() / 180) * POW(SIN((:longitude * PI() / 180 - e.longitude * PI() / 180) / 2), 2)))) * 1000 AS distance FROM bus_ovo_user a LEFT JOIN bus_ovo_user_bind_location e ON a.user_id = e.user_id HAVING distance <= :range ORDER BY distance ASC",
            countQuery = "SELECT COUNT(*) FROM bus_ovo_user",
            nativeQuery = true)
    Page<BusOvoUser> findAllWithDistance(Pageable pageable, Double latitude, Double longitude, Double range);



}
