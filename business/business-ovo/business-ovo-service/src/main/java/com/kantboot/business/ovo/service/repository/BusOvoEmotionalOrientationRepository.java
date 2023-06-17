package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoEmotionalOrientation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * 情感取向
 * @author 方某方
 */
public interface BusOvoEmotionalOrientationRepository
        extends JpaRepository<BusOvoEmotionalOrientation, Long> {


    /**
     * 根据性别限制和性取向限制获取情感取向
     * @param genderLimit 性别限制
     * @param sexualOrientationLimit 性取向限制
     * @return 情感取向列表
     */
    List<BusOvoEmotionalOrientation> findByGenderLimitAndSexualOrientationLimit(Integer genderLimit, Boolean sexualOrientationLimit);

    /**
     * 根据情感取向编码获取情感取向
     * @param code 情感取向编码
     * @return 情感取向
     */
    BusOvoEmotionalOrientation findByCode(String code);



}
