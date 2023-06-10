package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoUserBindLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * Ovo用户绑定位置表
 * @author 方某方
 */
public interface BusOvoUserBindLocationRepository
extends JpaRepository<BusOvoUserBindLocation, Long> {

    /**
     * 根据用户id查询
     * @param pageable 分页
     * @param latitude 纬度
     * @param longitude 经度
     * @param range 范围,单位米
     * @return Page<BusOvoUserBindLocation> Ovo绑定的用户
     */
    @Query(value = "SELECT *," +
            " ROUND(6378.138 * 2 * ASIN(SQRT(POW(SIN((:latitude * PI() / 180 - e.latitude * PI() / 180) / 2), 2) " +
            "+ COS(:latitude * PI() / 180) * COS(e.latitude * PI() / 180) *" +
            " POW(SIN((:longitude * PI() / 180 - e.longitude * PI() / 180) / 2), 2)))) " +
            "* 1000 AS distance " +
            "  FROM bus_ovo_user_bind_location e" +
            "  HAVING" +
            "  distance <= :range" +
            " ORDER BY distance ASC",
            countQuery = "SELECT COUNT(*) FROM bus_ovo_user_bind_location",
            nativeQuery = true)
    Page<BusOvoUserBindLocation> findAllWithDistance(Pageable pageable,Double latitude,Double longitude,Double range);

    /**
     * 根据用户id查询
     * @param userId 用户id
     * @return BusOvoUserBindLocation Ovo绑定的用户
     */
    BusOvoUserBindLocation findByUserId(Long userId);

}
