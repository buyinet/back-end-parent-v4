package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.entity.BusOvoEmotionalOrientation;

import java.util.List;

/**
 * 情感取向的服务接口
 * @author 方某方
 */
public interface IBusOvoEmotionalOrientationService {

    /**
     * 根据性别和性取向获取
     * @param gender 性别
     * @param sexualOrientationCode 性取向编码
     * @return 情感取向
     */
    List<BusOvoEmotionalOrientation> getByGenderAndSexualOrientationCode(Integer gender, String sexualOrientationCode);

}
