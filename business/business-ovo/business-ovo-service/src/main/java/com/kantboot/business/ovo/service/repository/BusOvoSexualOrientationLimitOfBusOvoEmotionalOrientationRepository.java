package com.kantboot.business.ovo.service.repository;


import com.kantboot.business.ovo.module.entity.BusOvoSexualOrientationLimitOfBusOvoEmotionalOrientation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * 情感取向的性取向限制的仓库接口
 * 用于查询情感取向的性取向限制
 *
 * @author 方某方
 */
public interface BusOvoSexualOrientationLimitOfBusOvoEmotionalOrientationRepository
        extends JpaRepository<BusOvoSexualOrientationLimitOfBusOvoEmotionalOrientation, Long>,
        Repository<BusOvoSexualOrientationLimitOfBusOvoEmotionalOrientation, Long> {

    /**
     * 根据性取向Code获取情感取向
     * @param sexualOrientationCode 性取向Code
     * @return 列表
     */
    List<BusOvoSexualOrientationLimitOfBusOvoEmotionalOrientation> findBySexualOrientationCode(String sexualOrientationCode);

    /**
     * 根据情感取向code获取
     * @param emotionalOrientationCode 情感取向code
     * @return 列表
     */
    List<BusOvoSexualOrientationLimitOfBusOvoEmotionalOrientation> findByEmotionalOrientationCode(String emotionalOrientationCode);
}
