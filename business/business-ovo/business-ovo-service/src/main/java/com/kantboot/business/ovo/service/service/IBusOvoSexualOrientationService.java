package com.kantboot.business.ovo.service.service;


import com.kantboot.business.ovo.module.entity.BusOvoSexualOrientation;

import java.util.List;
import java.util.Map;

/**
 * 性取向的服务接口
 * @author 方某方
 */
public interface IBusOvoSexualOrientationService {

    /**
     * 获取所有的性取向
     * @return 性取向
     */
    List<BusOvoSexualOrientation> getAllSexualOrientation();

    /**
     * 获取所有的性取向
     * @return 性取向
     */
    Map<String,String> getMap();

}
