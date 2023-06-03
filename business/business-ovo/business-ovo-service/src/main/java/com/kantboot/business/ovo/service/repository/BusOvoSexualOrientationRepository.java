package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoSexualOrientation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * 性取向表
 * @author 方某方
 */
public interface BusOvoSexualOrientationRepository extends JpaRepository<BusOvoSexualOrientation, Long>,
        Repository<BusOvoSexualOrientation, Long> {

}
